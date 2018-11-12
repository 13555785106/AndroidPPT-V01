package com.telecom.imagemask;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader.TileMode;
import android.util.AttributeSet;
import android.view.View;

public class RoundedCornerImageView extends View {

    private Bitmap mImage;
    private Paint mBitmapPaint;

    private RectF mBounds;
    private float mRadius = 25.0f;

    public RoundedCornerImageView(Context context) {
        super(context);
        init();
    }

    public RoundedCornerImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RoundedCornerImageView(Context context, AttributeSet attrs,
                                  int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        //创建图片的涂绘
        mBitmapPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        //创建作为绘图边界的矩形
        mBounds = new RectF();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int height=0, width=0;
        //所请求的大小是图片内容的大小
        int imageHeight, imageWidth;
        if (mImage == null) {
            imageHeight = imageWidth = 0;
        } else {
            imageHeight = mImage.getHeight();
            imageWidth = mImage.getWidth();
        }
        //获取最佳测量值并在视图上设置该值
        width = getMeasurement(widthMeasureSpec, imageWidth);
        height = getMeasurement(heightMeasureSpec, imageHeight);

        setMeasuredDimension(width, height);
    }

    /*
     * 用于测量宽度和高度的辅助方法
     */
    private int getMeasurement(int measureSpec, int contentSize) {
        int specSize = MeasureSpec.getSize(measureSpec);
        switch (MeasureSpec.getMode(measureSpec)) {
            case MeasureSpec.AT_MOST:
                return Math.min(specSize, contentSize);
            case MeasureSpec.UNSPECIFIED:
                return contentSize;
            case MeasureSpec.EXACTLY:
                return specSize;
            default:
                return 0;
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        if (w != oldw || h != oldh) {
            //为了居中显示图片，在视图大小改变时需要移动图片
            int imageWidth, imageHeight;
            if (mImage == null) {
                imageWidth = imageHeight = 0;
            } else {
                imageWidth = mImage.getWidth();
                imageHeight = mImage.getHeight();
            }
            int left = (w - imageWidth) / 2;
            int top = (h - imageHeight) / 2;
            //设置边界以便宜圆角矩形
            mBounds.set(left, top, left + imageWidth, top + imageHeight);
            //偏移着色器以在矩形内部绘制位图
            //如果没有此步骤，位图将在视图的(0,0)处
            if (mBitmapPaint.getShader() != null) {
                Matrix m = new Matrix();
                m.setTranslate(left, top);
                mBitmapPaint.getShader().setLocalMatrix(m);
            }
        }
    }

    public void setImage(Bitmap bitmap) {
        if (mImage != bitmap) {
            mImage = bitmap;
            if (mImage != null) {
                BitmapShader shader = new BitmapShader(mImage, TileMode.CLAMP, TileMode.CLAMP);
                mBitmapPaint.setShader(shader);
            } else {
                mBitmapPaint.setShader(null);
            }
            requestLayout();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //让视图绘制背景等对象
        super.onDraw(canvas);
        //使用计算出来的值绘制位图
        if (mBitmapPaint != null) {
            canvas.drawRoundRect(mBounds, mRadius, mRadius, mBitmapPaint);
        }
    }
}
