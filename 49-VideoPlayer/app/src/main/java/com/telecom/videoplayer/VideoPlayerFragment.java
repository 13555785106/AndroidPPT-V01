package com.telecom.videoplayer;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.media.MediaPlayer;
import android.media.TimedMetaData;
import android.media.TimedText;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

/**
 * A placeholder fragment containing a simple view.
 */
public class VideoPlayerFragment extends Fragment {
    private static final String TAG = VideoPlayerFragment.class.getSimpleName();
    ImageButton mButtonPlay;
    ImageButton mButtonPause;
    ImageButton mButtonStop;
    ProgressBar mProgressBarVideo;
    AutoFitTextureView mTextureViewVideo;
    MediaPlayer mMediaPlayer;
    LinearLayout mLinearLayout;
    Timer mTimer;
    String url = "http://ips.ifeng.com/video19.ifeng.com/video09/2017/05/24/4664192-102-008-1012.mp4";
    boolean mMediaPrepared = false;
    int mMediaState = -1;
    int mMediaDuration = 0;
    int mMediaCurrentPosition = 0;
    TextureView.SurfaceTextureListener mVideoTextureListener = new TextureView.SurfaceTextureListener() {
        @Override
        public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int width, int height) {
            Log.e(TAG, "onSurfaceTextureAvailable width=" + width + ",height=" + height);
            mMediaPlayer.setSurface(new Surface(mTextureViewVideo.getSurfaceTexture()));

        }

        @Override
        public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture, int width, int height) {
            Log.e(TAG, "onSurfaceTextureSizeChanged width=" + width + ",height=" + height);
        }

        @Override
        public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
            Log.e(TAG, "onSurfaceTextureDestroyed");

            if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
                mMediaPlayer.pause();
                mMediaPlayer.setSurface(null);
            }
            return true;
        }

        @Override
        public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {
            //Log.e(TAG, "onSurfaceTextureUpdated");
        }
    };

    View.OnClickListener buttonOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.button_play:
                    mMediaPlayer.start();
                    mMediaState = 0;
                    mButtonPlay.setVisibility(View.GONE);
                    mButtonPause.setVisibility(View.VISIBLE);
                    mButtonStop.setVisibility(View.VISIBLE);
                    break;
                case R.id.button_pause:
                    mMediaPlayer.pause();
                    mMediaState = 1;
                    mButtonPlay.setVisibility(View.VISIBLE);
                    mButtonPause.setVisibility(View.GONE);
                    mButtonStop.setVisibility(View.VISIBLE);
                    break;
                case R.id.button_stop:
                    mMediaPlayer.pause();
                    mMediaState = 2;
                    mMediaPlayer.seekTo(0);
                    mMediaCurrentPosition = 0;
                    mProgressBarVideo.setProgress(mMediaCurrentPosition);
                    mButtonPlay.setVisibility(View.VISIBLE);
                    mButtonPause.setVisibility(View.GONE);
                    mButtonStop.setVisibility(View.GONE);
                    break;
                default:
                    break;
            }
        }
    };


    public VideoPlayerFragment() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.e(TAG, "onAttach");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e(TAG, "onCreate");
        setRetainInstance(true);
        mMediaPlayer = new MediaPlayer();
        try {
            mMediaPlayer.setDataSource(getActivity(), Uri.parse(url));
        } catch (IOException e) {
            e.printStackTrace();
        }
        mMediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
                Log.e(TAG, "onError " + i + " " + i1);
                return false;
            }
        });
        mMediaPlayer.setOnTimedTextListener(new MediaPlayer.OnTimedTextListener() {
            @Override
            public void onTimedText(MediaPlayer mp, TimedText text) {
                Log.e(TAG,text.getText());
            }
        });
        mMediaPlayer.setOnTimedMetaDataAvailableListener(new MediaPlayer.OnTimedMetaDataAvailableListener() {
            @Override
            public void onTimedMetaDataAvailable(MediaPlayer mp, TimedMetaData data) {
                Log.e(TAG,""+data.getTimestamp());
            }
        });
        mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                Log.e(TAG, "videoWidth=" + mp.getVideoWidth() + ",videoHeight=" + mp.getVideoHeight());
                mMediaPrepared = true;
                mLinearLayout.setVisibility(View.VISIBLE);
                mTextureViewVideo.setAspectRatio(mp.getVideoWidth(), mp.getVideoHeight());
                mMediaDuration = mMediaPlayer.getDuration();
                mProgressBarVideo.setMax(mMediaDuration);
                mTimer = new Timer();
                mTimer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        if (mMediaPlayer.isPlaying()) {
                            mMediaCurrentPosition = mMediaPlayer.getCurrentPosition();
                            mProgressBarVideo.setProgress(mMediaCurrentPosition);
                        }
                    }
                }, 0, 1000);
            }
        });
        mMediaPlayer.prepareAsync();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.e(TAG, "onCreateView");
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Log.e(TAG, "onViewCreated");
        mLinearLayout = view.findViewById(R.id.linear_layout);
        mButtonPlay = view.findViewById(R.id.button_play);
        mButtonPlay.setOnClickListener(buttonOnClickListener);
        mButtonPause = view.findViewById(R.id.button_pause);
        mButtonPause.setOnClickListener(buttonOnClickListener);
        mButtonStop = view.findViewById(R.id.button_stop);
        mButtonStop.setOnClickListener(buttonOnClickListener);
        mProgressBarVideo = view.findViewById(R.id.progress_bar_video);
        mTextureViewVideo = view.findViewById(R.id.texture_view_video);

    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e(TAG, "onResume");
        if (mTextureViewVideo.isAvailable()) {
            //  锁屏之后再打开时，SurfaceTexture是可用的
        } else {
            //  视图重新构建要重新注册监听器
            mTextureViewVideo.setSurfaceTextureListener(mVideoTextureListener);
        }
        if (mMediaDuration != 0)
            mProgressBarVideo.setMax(mMediaDuration);
        if (mMediaCurrentPosition != 0)
            mProgressBarVideo.setProgress(mMediaCurrentPosition);
        if (mMediaState == 0) {
            mMediaPlayer.start();
            mButtonPlay.setVisibility(View.GONE);
            mButtonPause.setVisibility(View.VISIBLE);
            mButtonStop.setVisibility(View.VISIBLE);
        } else if (mMediaState == 1) {
            mButtonPlay.setVisibility(View.VISIBLE);
            mButtonPause.setVisibility(View.GONE);
            mButtonStop.setVisibility(View.VISIBLE);
        } else if (mMediaState == 2) {
            mButtonPlay.setVisibility(View.VISIBLE);
            mButtonPause.setVisibility(View.GONE);
            mButtonStop.setVisibility(View.GONE);
        }
        if (mMediaPrepared)
            mLinearLayout.setVisibility(View.VISIBLE);

    }

    @Override
    public void onPause() {
        Log.e(TAG, "onPause");
        if (mMediaPlayer != null && mMediaPlayer.isPlaying())
            mMediaPlayer.pause();
        super.onPause();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "onDestroy");
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
        if (mMediaPlayer != null) {
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }


    @Override
    public void onDetach() {
        Log.e(TAG, "onDetach");
        super.onDetach();
    }
}
