/*
 * Copyright 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.telecom.camera2photo;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.RectF;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CameraMetadata;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.CaptureResult;
import android.hardware.camera2.TotalCaptureResult;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.Image;
import android.media.ImageReader;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.Size;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class Camera2PhotoFragment extends Fragment
        implements View.OnClickListener, ActivityCompat.OnRequestPermissionsResultCallback {
    /**
     * 屏幕方向与JPEG方向的转换。
     */
    private static final SparseIntArray ORIENTATIONS = new SparseIntArray();
    private static final int REQUEST_PERMISSIONS = 1;
    private static final String FRAGMENT_DIALOG = "dialog";
    private static final String TAG = Camera2PhotoFragment.class.getSimpleName();
    /**
     * 相机状态
     * 0 图像预览.
     * 1 等待锁定焦点，拍照片前将预览锁上保证图像不再变化.
     * 2 等待预拍照，对焦曝光等操作.
     * 3 等待非预拍照状态，不是与拍照的曝光状态等.
     * 4 拍照完毕.
     */
    private static final int STATE_PREVIEW = 0;
    private static final int STATE_WAITING_LOCK = 1;
    private static final int STATE_WAITING_PRECAPTURE = 2;
    private static final int STATE_WAITING_NON_PRECAPTURE = 3;
    private static final int STATE_PICTURE_TAKEN = 4;
    /**
     * Camera2 API 支持的最大预览宽度和高度.
     */
    private static final int MAX_PREVIEW_WIDTH = 1920;
    private static final int MAX_PREVIEW_HEIGHT = 1080;
    private static String[] NEEDED_PERMISSIONS = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};

    static {
        ORIENTATIONS.append(Surface.ROTATION_0, 90);
        ORIENTATIONS.append(Surface.ROTATION_90, 0);
        ORIENTATIONS.append(Surface.ROTATION_180, 270);
        ORIENTATIONS.append(Surface.ROTATION_270, 180);
    }

    List<String> alreadyConfirmationPermissions = new ArrayList<>();
    private String mCameraId;// 正在使用的相机id
    private AutoFitTextureView mTextureView;// 预览使用的自定义TextureView控件
    private CameraCaptureSession mCaptureSession;// 预览用捕捉会话
    private CameraDevice mCameraDevice;// 正在使用的相机
    private Size mPreviewSize;// 预览照片的尺寸
    private HandlerThread mBackgroundThread;// 处理拍照等工作的子线程
    private Handler mBackgroundHandler;// 上面子线程的处理器
    private ImageReader mImageReader;// 照片的处理器
    private File mFile;// 输出的照片文件
    private final ImageReader.OnImageAvailableListener mOnImageAvailableListener
            = new ImageReader.OnImageAvailableListener() {

        @Override
        public void onImageAvailable(ImageReader reader) {
            mBackgroundHandler.post(new ImageSaver(reader.acquireNextImage(), mFile));
        }

    };
    private CaptureRequest.Builder mPreviewRequestBuilder;// 预览请求构建器, 用来构建"预览请求"(下面定义的)通过pipeline发送到Camera device
    private CaptureRequest mPreviewRequest;// 预览请求, 由上面的构建器构建出来
    private int mState = STATE_PREVIEW;// 当前的相机状态, 这里初始化为预览, 因为刚载入这个fragment时应显示预览
    // 信号量控制器, 防止相机没有关闭时退出本应用(若没有关闭就退出, 会造成其他应用无法调用相机)当某处获得这个许可时,
    // 其他需要许可才能执行的代码需要等待许可被释放才能获取
    private Semaphore mCameraOpenCloseLock = new Semaphore(1);
    private boolean mFlashSupported;//当前相机是否支持闪光.
    /**
     * 相机传感器方向.
     */
    private int mSensorOrientation;
    //捕获会话回调函数
    private CameraCaptureSession.CaptureCallback mCaptureCallback
            = new CameraCaptureSession.CaptureCallback() {

        private void process(CaptureResult result) {
            //Log.e(TAG, "mState=" + mState);
            switch (mState) {
                case STATE_PREVIEW: {
                    // 状态是预览时, 不需要做任何事情
                    break;
                }
                case STATE_WAITING_LOCK: {
                    // 等待对焦锁定的状态, 某些设备完成对焦锁定后CONTROL_AF_STATE可能为null
                    Integer afState = result.get(CaptureResult.CONTROL_AF_STATE);
                    if (afState == null) {
                        captureStillPicture();
                    } else if (CaptureResult.CONTROL_AF_STATE_FOCUSED_LOCKED == afState ||
                            CaptureResult.CONTROL_AF_STATE_NOT_FOCUSED_LOCKED == afState) {
                        // 如果焦点已经锁定(不管自动对焦是否成功), 检查AE的返回, 注意某些设备CONTROL_AE_STATE可能为空
                        Integer aeState = result.get(CaptureResult.CONTROL_AE_STATE);
                        if (aeState == null || aeState == CaptureResult.CONTROL_AE_STATE_CONVERGED) {
                            // 如果自动曝光(AE)设定良好, 将状态置为已经拍照, 执行拍照
                            mState = STATE_PICTURE_TAKEN;
                            captureStillPicture();
                        } else {
                            // 以上条件都不满足, 执行预拍照系列操作
                            runPrecaptureSequence();
                        }
                    }
                    break;
                }
                case STATE_WAITING_PRECAPTURE: {
                    // 等待预处理状态, 某些设备CONTROL_AE_STATE可能为null
                    Integer aeState = result.get(CaptureResult.CONTROL_AE_STATE);
                    if (aeState == null || aeState == CaptureResult.CONTROL_AE_STATE_PRECAPTURE || aeState == CaptureRequest.CONTROL_AE_STATE_FLASH_REQUIRED) {
                        mState = STATE_WAITING_NON_PRECAPTURE;
                    }
                    break;
                }
                case STATE_WAITING_NON_PRECAPTURE: {
                    // 某些设备上 CONTROL_AE_STATE 可能是空值
                    Integer aeState = result.get(CaptureResult.CONTROL_AE_STATE);
                    if (aeState == null || aeState != CaptureResult.CONTROL_AE_STATE_PRECAPTURE) {
                        // 如果AE做完"非等待预拍照", 将状态置为已经拍照, 并执行拍照操作
                        mState = STATE_PICTURE_TAKEN;
                        captureStillPicture();
                    }
                    break;
                }
            }
        }

        @Override
        public void onCaptureProgressed(@NonNull CameraCaptureSession session,
                                        @NonNull CaptureRequest request,
                                        @NonNull CaptureResult partialResult) {
            process(partialResult);
        }

        @Override
        public void onCaptureCompleted(@NonNull CameraCaptureSession session,
                                       @NonNull CaptureRequest request,
                                       @NonNull TotalCaptureResult result) {
            process(result);
        }

    };
    private final CameraDevice.StateCallback mStateCallback = new CameraDevice.StateCallback() {

        @Override
        public void onOpened(@NonNull CameraDevice cameraDevice) {
            // 当相机打开执行以下操作:
            // 1. 释放访问许可
            // 2. 将正在使用的相机指向此处打开的相机
            // 3. 创建相机预览会话
            mCameraOpenCloseLock.release();
            mCameraDevice = cameraDevice;
            createCameraPreviewSession();
        }

        @Override
        public void onDisconnected(@NonNull CameraDevice cameraDevice) {
            // 当相机失去连接时执行以下操作:
            // 1. 释放访问许可
            // 2. 关闭相机
            // 3. 将正在使用的相机指向null
            mCameraOpenCloseLock.release();
            cameraDevice.close();
            mCameraDevice = null;
        }

        @Override
        public void onError(@NonNull CameraDevice cameraDevice, int error) {
            // 当相机发生错误时执行以下操作:
            // 1. 释放访问许可
            // 2. 关闭相机
            // 3, 将正在使用的相机指向null
            // 4. 获取当前的活动, 并结束它
            mCameraOpenCloseLock.release();
            cameraDevice.close();
            mCameraDevice = null;
            Activity activity = getActivity();
            if (null != activity) {
                activity.finish();
            }
        }

    };
    /**
     * TextureView监听器
     */
    private final TextureView.SurfaceTextureListener mSurfaceTextureListener
            = new TextureView.SurfaceTextureListener() {

        @Override
        public void onSurfaceTextureAvailable(SurfaceTexture texture, int width, int height) {
            //TextureView 就绪后打开相机
            openCamera(width, height);
        }

        @Override
        public void onSurfaceTextureSizeChanged(SurfaceTexture texture, int width, int height) {
            //预览方向改变时，执行转换操作
            configureTransform(width, height);
        }

        @Override
        public boolean onSurfaceTextureDestroyed(SurfaceTexture texture) {
            return true;
        }

        @Override
        public void onSurfaceTextureUpdated(SurfaceTexture texture) {
        }

    };
    private boolean isNeedCheck = true;

    /**
     * 返回最合适的预览尺寸
     *
     * @param choices           相机支持的输出尺寸
     * @param textureViewWidth  相对于传感器坐标的 texture view 宽度
     * @param textureViewHeight 相对于传感器坐标的 texture view 高度
     * @param maxWidth          能够选择的最大宽度
     * @param maxHeight         能够选择的最大高度
     * @param aspectRatio       图像的比例(pictureSize, 只有当pictureSize和textureSize保持一致, 才不会失真)
     * @return 最合适的预览尺寸
     */

    private static Size chooseOptimalSize(Size[] choices, int textureViewWidth, int textureViewHeight, int maxWidth, int maxHeight, Size aspectRatio) {

        List<Size> bigEnough = new ArrayList<>();// 存放小于等于限定尺寸, 大于等于texture控件尺寸的Size
        List<Size> notBigEnough = new ArrayList<>();// 存放小于限定尺寸, 小于texture控件尺寸的Size
        int w = aspectRatio.getWidth();
        int h = aspectRatio.getHeight();
        for (Size option : choices) {
            if (option.getWidth() <= maxWidth && option.getHeight() <= maxHeight && option.getHeight() == option.getWidth() * h / w) {
                if (option.getWidth() >= textureViewWidth && option.getHeight() >= textureViewHeight) {
                    bigEnough.add(option);
                } else {
                    notBigEnough.add(option);
                }
            }
        }

        // 1. 若存在bigEnough数据, 则返回最大里面最小的
        // 2. 若不存bigEnough数据, 但是存在notBigEnough数据, 则返回在最小里面最大的
        // 3. 上述两种数据都没有时, 在日志上显示错误信息，返回相机支持的第一个尺寸
        if (bigEnough.size() > 0) {
            return Collections.min(bigEnough, new CompareSizesByArea());
        } else if (notBigEnough.size() > 0) {
            return Collections.max(notBigEnough, new CompareSizesByArea());
        } else {
            Log.e(TAG, "没有找到合适的预览尺寸");
            return choices[0];
        }
    }

    public static Camera2PhotoFragment newInstance() {
        return new Camera2PhotoFragment();
    }

    /**
     * 在 UI 线程显示一个Toast.
     *
     * @param text 要显示的信息
     */
    private void showToast(final String text) {
        final Activity activity = getActivity();
        if (activity != null) {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(activity, text, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_camera2_basic, container, false);
    }

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        view.findViewById(R.id.picture).setOnClickListener(this);
        view.findViewById(R.id.info).setOnClickListener(this);
        mTextureView = view.findViewById(R.id.texture);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        String galleryPath = Environment.getExternalStorageDirectory() + File.separator + Environment.DIRECTORY_DCIM + File.separator + "Camera" + File.separator;
        mFile = new File(galleryPath + UUID.randomUUID().toString() + ".jpg");

    }

    @Override
    public void onResume() {
        super.onResume();
        startBackgroundThread();
        if (mTextureView.isAvailable()) {
            // 在程序打开的前提下，锁屏之后再打开，SurfaceTexture已经就绪,可直接打开相机， 此时onSurfaceTextureAvailable不会被回调。
            openCamera(mTextureView.getWidth(), mTextureView.getHeight());
        } else {
            // 在视图被重建的情况下，需要重新设置监听器。
            mTextureView.setSurfaceTextureListener(mSurfaceTextureListener);
        }
    }

    @Override
    public void onPause() {
        closeCamera();
        stopBackgroundThread();
        super.onPause();
    }

    /**
     * 设置相机的输出, 包括预览和拍照
     * <p>
     * 处理流程如下:
     * 1. 获取当前的摄像头, 并将拍照输出设置为最高画质
     * 2. 判断显示方向和摄像头传感器方向是否一致, 是否需要旋转画面
     * 3. 获取当前显示尺寸和相机的输出尺寸, 选择最合适的预览尺寸
     *
     * @param width  预览宽度
     * @param height 预览高度
     */


    @SuppressWarnings("SuspiciousNameCombination")
    private void setUpCameraOutputs(int width, int height) {
        // 获取当前活动
        Activity activity = getActivity();
        // 获取CameraManager实例
        CameraManager manager = (CameraManager) activity.getSystemService(Context.CAMERA_SERVICE);
        try {
            for (String cameraId : manager.getCameraIdList()) {
                CameraCharacteristics characteristics
                        = manager.getCameraCharacteristics(cameraId);

                // 如果该摄像头是前置摄像头, 则看下一个摄像头(本应用不使用前置摄像头)
                Integer facing = characteristics.get(CameraCharacteristics.LENS_FACING);
                if (facing != null && facing == CameraCharacteristics.LENS_FACING_FRONT) {
                    continue;
                }

                StreamConfigurationMap map = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
                if (map == null) {
                    continue;
                }

                // 对于静态图像，选用最高画质
                // maxImages是ImageReader一次可以访问的最大图片数量
                Size largest = Collections.max(Arrays.asList(map.getOutputSizes(ImageFormat.JPEG)), new CompareSizesByArea());
                mImageReader = ImageReader.newInstance(largest.getWidth(), largest.getHeight(), ImageFormat.JPEG, /*maxImages*/2);
                mImageReader.setOnImageAvailableListener(mOnImageAvailableListener, mBackgroundHandler);

                // 获取手机目前的旋转方向(横屏还是竖屏, 对于"自然"状态下高度大于宽度的设备来说横屏是ROTATION_90
                // 或者ROTATION_270,竖屏是ROTATION_0或者ROTATION_180)
                int displayRotation = activity.getWindowManager().getDefaultDisplay().getRotation();
                Log.e(TAG, "手机方向：" + displayRotation);
                // 获取相机传感器的方向("自然"状态下垂直放置为0, 顺时针算起, 每次加90读)
                // 注意, 这个参数, 是由设备的生产商来决定的, 大多数情况下, 该值为90, 以下的switch这么写
                // 是为了配适某些特殊的手机

                mSensorOrientation = characteristics.get(CameraCharacteristics.SENSOR_ORIENTATION);
                Log.e(TAG, "相机传感器方向：" + mSensorOrientation);
                boolean swappedDimensions = false;
                switch (displayRotation) {
                    // ROTATION_0和ROTATION_180都是竖屏只需做同样的处理操作
                    // 显示为竖屏时, 若传感器方向为90或者270, 则需要进行交换(标志位置true)
                    case Surface.ROTATION_0:
                    case Surface.ROTATION_180:
                        if (mSensorOrientation == 90 || mSensorOrientation == 270) {
                            swappedDimensions = true;
                        }
                        break;
                    // ROTATION_90和ROTATION_270都是横屏只需做同样的处理操作
                    // 显示为横屏时, 若传感器方向为0或者180, 则需要进行交换(标志位置true)
                    case Surface.ROTATION_90:
                    case Surface.ROTATION_270:
                        if (mSensorOrientation == 0 || mSensorOrientation == 180) {
                            swappedDimensions = true;
                        }
                        break;
                    default:
                        Log.e(TAG, "Display rotation is invalid: " + displayRotation);
                }
                // 获取当前的屏幕尺寸, 放到一个点对象里
                Point displaySize = new Point();
                activity.getWindowManager().getDefaultDisplay().getSize(displaySize);
                // 旋转前的预览宽度(相机给出的), 通过传进来的参数获得
                int rotatedPreviewWidth = width;
                // 旋转前的预览高度(相机给出的), 通过传进来的参数获得
                int rotatedPreviewHeight = height;
                // 将当前的显示尺寸赋给最大的预览尺寸(能够显示的尺寸, 用来计算用的(texture可能比它小需要配适))
                int maxPreviewWidth = displaySize.x;
                int maxPreviewHeight = displaySize.y;
                // 如果需要进行画面旋转, 将宽度和高度对调
                if (swappedDimensions) {
                    rotatedPreviewWidth = height;
                    rotatedPreviewHeight = width;
                    maxPreviewWidth = displaySize.y;
                    maxPreviewHeight = displaySize.x;
                }

                // 尺寸太大时的极端处理
                if (maxPreviewWidth > MAX_PREVIEW_WIDTH) {
                    maxPreviewWidth = MAX_PREVIEW_WIDTH;
                }

                if (maxPreviewHeight > MAX_PREVIEW_HEIGHT) {
                    maxPreviewHeight = MAX_PREVIEW_HEIGHT;
                }
                // 自动计算出最适合的预览尺寸
                // 第一个参数:map.getOutputSizes(SurfaceTexture.class)表示SurfaceTexture支持的尺寸List
                // 危险，W.R.！试图使用太大的预览大小可能超过相机总线的带宽限制，
                // 导致华丽的预览但产生了大量的垃圾数据。
                mPreviewSize = chooseOptimalSize(map.getOutputSizes(SurfaceTexture.class),
                        rotatedPreviewWidth, rotatedPreviewHeight, maxPreviewWidth, maxPreviewHeight, largest);

                // 获取当前的屏幕方向
                int orientation = getResources().getConfiguration().orientation;
                if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    // 如果方向是横向(landscape)
                    mTextureView.setAspectRatio(mPreviewSize.getWidth(), mPreviewSize.getHeight());
                } else {
                    // 方向不是横向(即竖向)
                    mTextureView.setAspectRatio(mPreviewSize.getHeight(), mPreviewSize.getWidth());
                }
                // 检查是否支持闪光.
                Boolean available = characteristics.get(CameraCharacteristics.FLASH_INFO_AVAILABLE);
                mFlashSupported = available == null ? false : available;

                mCameraId = cameraId;
                return;
            }
        } catch (CameraAccessException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            // 当相机设备不支持 Camera2API 会抛出NPE异常.
            ErrorDialog.newInstance(getString(R.string.camera_error)).show(getChildFragmentManager(), FRAGMENT_DIALOG);
        }
    }

    /**
     * 以 mCameraId 打开相机.
     */
    @SuppressWarnings("MissingPermission")
    private void openCamera(int width, int height) {
        List<String> notGrantedPermissions = getNotGrantedPermissions(NEEDED_PERMISSIONS);
        if (notGrantedPermissions.size() > 0) {
            requestVideoPermissions(notGrantedPermissions);

            return;
        }
        // 设置相机输出
        setUpCameraOutputs(width, height);
        // 配置格式转换
        configureTransform(width, height);
        // 获取当前活动和CameraManager的实例
        Activity activity = getActivity();
        CameraManager manager = (CameraManager) activity.getSystemService(Context.CAMERA_SERVICE);
        try {
            // 尝试获得相机开打关闭许可, 等待2500时间仍没有获得则排除异常
            if (!mCameraOpenCloseLock.tryAcquire(2500, TimeUnit.MILLISECONDS)) {
                throw new RuntimeException("Time out waiting to lock camera opening.");
            }
            // 打开相机, 参数是: 相机id, 相机状态回调, 子线程处理器
            manager.openCamera(mCameraId, mStateCallback, mBackgroundHandler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            throw new RuntimeException("Interrupted while trying to lock camera opening.", e);
        }
    }

    /**
     * 关闭相机.
     */
    private void closeCamera() {
        try {
            // 获得相机开打关闭许可
            mCameraOpenCloseLock.acquire();
            // 关闭捕获会话
            if (null != mCaptureSession) {
                mCaptureSession.close();
                mCaptureSession = null;
            }
            // 关闭当前相机
            if (null != mCameraDevice) {
                mCameraDevice.close();
                mCameraDevice = null;
            }
            // 关闭拍照处理器
            if (null != mImageReader) {
                mImageReader.close();
                mImageReader = null;
            }
        } catch (InterruptedException e) {
            throw new RuntimeException("Interrupted while trying to lock camera closing.", e);
        } finally {
            // 释放相机开打关闭许可
            mCameraOpenCloseLock.release();
        }
    }

    /**
     * 启动后台线程和它的处理器.
     */
    private void startBackgroundThread() {
        mBackgroundThread = new HandlerThread("CameraBackground");
        mBackgroundThread.start();
        mBackgroundHandler = new Handler(mBackgroundThread.getLooper());
    }

    /**
     * 停止后台线程和它的处理器.
     */
    private void stopBackgroundThread() {
        mBackgroundThread.quitSafely();
        try {
            mBackgroundThread.join();
            mBackgroundThread = null;
            mBackgroundHandler = null;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 创建一个新的拍照预览会话.
     */
    private void createCameraPreviewSession() {
        try {
            SurfaceTexture texture = mTextureView.getSurfaceTexture();
            assert texture != null;

            // 根据预览尺寸大小设置缓存.
            texture.setDefaultBufferSize(mPreviewSize.getWidth(), mPreviewSize.getHeight());
            // 这个surface用来进行预览输出.
            Surface surface = new Surface(texture);

            // 创建预览请求构建器并将surface添加到输出.
            mPreviewRequestBuilder = mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
            mPreviewRequestBuilder.addTarget(surface);

            // 创建预览的拍照会话
            mCameraDevice.createCaptureSession(Arrays.asList(surface, mImageReader.getSurface()),
                    new CameraCaptureSession.StateCallback() {

                        @Override
                        public void onConfigured(@NonNull CameraCaptureSession cameraCaptureSession) {
                            // 如果相机已经关闭直接返回
                            if (null == mCameraDevice) {
                                return;
                            }
                            // 将捕捉会话付给成员变量
                            mCaptureSession = cameraCaptureSession;
                            try {
                                // 自动对焦
                                mPreviewRequestBuilder.set(CaptureRequest.CONTROL_AF_MODE, CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE);
                                // 自动闪光
                                setAutoFlash(mPreviewRequestBuilder);
                                // 创建相机预览请求
                                mPreviewRequest = mPreviewRequestBuilder.build();
                                // 重复进行上面构建的请求, 以便显示预览
                                mCaptureSession.setRepeatingRequest(mPreviewRequest, mCaptureCallback, mBackgroundHandler);
                            } catch (CameraAccessException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onConfigureFailed(@NonNull CameraCaptureSession cameraCaptureSession) {
                            showToast("Failed");
                        }
                    }, null
            );
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * 为`mTextureView`设置必要的转换.
     * 这个方法在使用setUpCameraOutputs确定预览尺寸或者mTextureView的尺寸固定之后被调用.
     *
     * @param viewWidth  mTextureView 的宽度
     * @param viewHeight mTextureView 的高度
     */
    private void configureTransform(int viewWidth, int viewHeight) {
        Activity activity = getActivity();
        if (null == mTextureView || null == mPreviewSize || null == activity) {
            return;
        }
        int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
        Matrix matrix = new Matrix();
        RectF viewRect = new RectF(0, 0, viewWidth, viewHeight);
        RectF bufferRect = new RectF(0, 0, mPreviewSize.getHeight(), mPreviewSize.getWidth());
        float centerX = viewRect.centerX();
        float centerY = viewRect.centerY();
        if (Surface.ROTATION_90 == rotation || Surface.ROTATION_270 == rotation) {//Surface.ROTATION_90 机头朝左  Surface.ROTATION_270 机头朝右
            bufferRect.offset(centerX - bufferRect.centerX(), centerY - bufferRect.centerY());
            matrix.setRectToRect(viewRect, bufferRect, Matrix.ScaleToFit.FILL);
            float scale = Math.max((float) viewHeight / mPreviewSize.getHeight(), (float) viewWidth / mPreviewSize.getWidth());
            matrix.postScale(scale, scale, centerX, centerY);
            matrix.postRotate(90 * (rotation - 2), centerX, centerY);// 顺时针转动进行转换,对于手机就是横屏了
        } else if (Surface.ROTATION_180 == rotation) { // Surface.ROTATION_180机头朝下
            matrix.postRotate(180, centerX, centerY);//
        }
        mTextureView.setTransform(matrix);
    }

    /**
     * 开始一个静态图像拍照.
     */
    private void takePicture() {
        lockFocus();
    }

    /**
     * 为了拍照静态图像第一步锁定焦点.
     */
    private void lockFocus() {
        try {
            // 构建自动对焦请求
            mPreviewRequestBuilder.set(CaptureRequest.CONTROL_AF_TRIGGER, CameraMetadata.CONTROL_AF_TRIGGER_START);
            // 告诉mCaptureCallback回调状态
            mState = STATE_WAITING_LOCK;
            // 提交一个捕获单一图片的请求
            mCaptureSession.capture(mPreviewRequestBuilder.build(), mCaptureCallback, mBackgroundHandler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * 为拍摄一个静态图像执行预拍照设置.
     * 这个方法在 lockFocus() 的 mCaptureCallback 获得一个回应之后被调用.
     */
    private void runPrecaptureSequence() {
        try {
            // 构建预拍照请求
            mPreviewRequestBuilder.set(CaptureRequest.CONTROL_AE_PRECAPTURE_TRIGGER, CaptureRequest.CONTROL_AE_PRECAPTURE_TRIGGER_START);
            // 告诉mCaptureCallback回调状态
            mState = STATE_WAITING_PRECAPTURE;
            // 提交一个捕获单一图片的请求
            mCaptureSession.capture(mPreviewRequestBuilder.build(), mCaptureCallback, mBackgroundHandler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * 拍摄一个静态图像. 这个方法也在 lockFocus() 的 mCaptureCallback 获得一个回应之后被调用.
     */
    private void captureStillPicture() {
        try {
            final Activity activity = getActivity();
            if (null == activity || null == mCameraDevice) {
                return;
            }
            // 这个 CaptureRequest.Builder 用来进行拍照.
            final CaptureRequest.Builder captureBuilder = mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE);
            captureBuilder.addTarget(mImageReader.getSurface());

            // 使用相同的AE(自动曝光)和AF(自动对焦)模式来预览.
            captureBuilder.set(CaptureRequest.CONTROL_AF_MODE, CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE);
            setAutoFlash(captureBuilder);

            // Orientation
            int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
            captureBuilder.set(CaptureRequest.JPEG_ORIENTATION, getOrientation(rotation));

            CameraCaptureSession.CaptureCallback captureCallback
                    = new CameraCaptureSession.CaptureCallback() {

                @Override
                public void onCaptureCompleted(@NonNull CameraCaptureSession session,
                                               @NonNull CaptureRequest request,
                                               @NonNull TotalCaptureResult result) {

                    showToast("Saved: " + mFile);
                    getActivity().sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(mFile)));
                    unlockFocus();
                }
            };

            mCaptureSession.stopRepeating();
            mCaptureSession.abortCaptures();
            mCaptureSession.capture(captureBuilder.build(), captureCallback, null);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * 通过屏幕方向获取JPEG图像的方向.
     *
     * @param rotation 屏幕方向.
     * @return JPEG 方向 (one of 0, 90, 270, and 360)
     */
    private int getOrientation(int rotation) {
        // 大部分相机传感器方向是90，少数是270(例如 Nexus 5X).
        // 我们必须考虑到这一点并适当地转动JPEG图像.
        // For devices with orientation of 90, we simply return our mapping from ORIENTATIONS.
        // For devices with orientation of 270, we need to rotate the JPEG 180 degrees.
        return (ORIENTATIONS.get(rotation) + mSensorOrientation + 270) % 360;
    }

    /**
     * 取消对焦，这个方法在图像捕捉完毕之后被调用。
     */
    private void unlockFocus() {
        try {
            // 构建取消AF的请求
            mPreviewRequestBuilder.set(CaptureRequest.CONTROL_AF_TRIGGER, CameraMetadata.CONTROL_AF_TRIGGER_CANCEL);
            // 构建自动闪光请求(之前拍照前会构建为需要或者不需要闪光灯, 这里重新设会自动)
            setAutoFlash(mPreviewRequestBuilder);
            mCaptureSession.capture(mPreviewRequestBuilder.build(), mCaptureCallback, mBackgroundHandler);
            // 在这个之后，相机将变为正常的预览状态.
            mState = STATE_PREVIEW;
            // 拍完照后, 设置成预览状态, 并重复预览请求
            mCaptureSession.setRepeatingRequest(mPreviewRequest, mCaptureCallback, mBackgroundHandler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.picture: {
                takePicture();
                break;
            }
            case R.id.info: {
                Activity activity = getActivity();
                if (null != activity) {
                    new AlertDialog.Builder(activity)
                            .setMessage(R.string.intro_message)
                            .setPositiveButton(android.R.string.ok, null)
                            .show();
                }
                break;
            }
        }
    }

    private void setAutoFlash(CaptureRequest.Builder requestBuilder) {
        if (mFlashSupported) {
            requestBuilder.set(CaptureRequest.CONTROL_AE_MODE, CaptureRequest.CONTROL_AE_MODE_ON_AUTO_FLASH);
        }
    }

    private List<String> getNotGrantedPermissions(String[] permissions) {
        List<String> list = new ArrayList<>();
        for (String permission : permissions) {
            if (ActivityCompat.checkSelfPermission(getActivity(), permission) != PackageManager.PERMISSION_GRANTED) {
                list.add(permission);
            }
        }
        return list;
    }

    private void requestVideoPermissions(List<String> notGrantedPermissions) {
        Log.e(TAG, "------------------------------------------------------");
        for (String permission : notGrantedPermissions) {
            Log.e(TAG, permission);
            if (!alreadyConfirmationPermissions.contains(permission)) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), permission)) {
                    ConfirmationDialog.newInstance(permission).show(getFragmentManager(), FRAGMENT_DIALOG);
                } else {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{permission}, REQUEST_PERMISSIONS);
                }
                alreadyConfirmationPermissions.add(permission);
            }
        }
        Log.e(TAG, "------------------------------------------------------");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        Log.d(TAG, "onRequestPermissionsResult");
        Log.e(TAG, "permissions.length=" + permissions.length);
        Log.e(TAG, "grantResults.length=" + grantResults.length);
        if (requestCode == REQUEST_PERMISSIONS) {
            if (grantResults.length == 1 && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                ErrorDialog.newInstance("错误\r\n需要权限:" + permissions[0]).show(getChildFragmentManager(), FRAGMENT_DIALOG);
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private static class ImageSaver implements Runnable {
        private final Image mImage;
        private final File mFile;

        ImageSaver(Image image, File file) {
            mImage = image;
            mFile = file;
        }

        @Override
        public void run() {
            ByteBuffer buffer = mImage.getPlanes()[0].getBuffer();
            byte[] bytes = new byte[buffer.remaining()];
            buffer.get(bytes);
            FileOutputStream output = null;
            try {
                output = new FileOutputStream(mFile);
                output.write(bytes);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                mImage.close();
                if (null != output) {
                    try {
                        output.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

    }

    static class CompareSizesByArea implements Comparator<Size> {

        @Override
        public int compare(Size lhs, Size rhs) {
            // 此处进行了类型转换防止乘法溢出
            return Long.signum((long) lhs.getWidth() * lhs.getHeight() -
                    (long) rhs.getWidth() * rhs.getHeight());
        }

    }

    /**
     * 显示错误信息对话框.
     */
    public static class ErrorDialog extends DialogFragment {

        private static final String ARG_MESSAGE = "message";

        public static ErrorDialog newInstance(String message) {
            ErrorDialog dialog = new ErrorDialog();
            Bundle args = new Bundle();
            args.putString(ARG_MESSAGE, message);
            dialog.setArguments(args);
            return dialog;
        }

        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Activity activity = getActivity();
            return new AlertDialog.Builder(activity)
                    .setMessage(getArguments().getString(ARG_MESSAGE))
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            activity.finish();
                        }
                    })
                    .create();
        }

    }


    public static class ConfirmationDialog extends DialogFragment {
        private static final String ARG_PERMISSION = "permission";

        public static ConfirmationDialog newInstance(String permission) {
            ConfirmationDialog dialog = new ConfirmationDialog();
            Bundle args = new Bundle();
            args.putString(ARG_PERMISSION, permission);
            dialog.setArguments(args);
            return dialog;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final String permission = getArguments().getString(ARG_PERMISSION);
            return new AlertDialog.Builder(getActivity())
                    .setMessage("需要 " + permission + " 权限")
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(getActivity(), new String[]{permission}, REQUEST_PERMISSIONS);

                        }
                    })
                    .setNegativeButton(android.R.string.cancel,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    getActivity().finish();
                                }
                            })
                    .create();
        }

    }

}
