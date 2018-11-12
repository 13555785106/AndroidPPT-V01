package com.telecom.draganddraw;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;


public class BoxDrawingView extends View {
    private static final String TAG = "BoxDrawingView";
    private Box mCurrentBox;
    private ArrayList<Box> mBoxes = new ArrayList<>();
    private Paint mRotatingPaint;
    private Paint mBoxPaint;
    private Paint mBackgroundPaint;
    private boolean isRotating = false;


    private int rotatingEndPointId;
    private PointF startLineStartPoint = new PointF();
    private PointF startLineEndPoint = new PointF();
    private int rotatingStartPointId;
    private PointF endLineStartPoint = new PointF();
    private PointF endLineEndPoint = new PointF();

    public BoxDrawingView(Context context) {
        this(context, null);
    }

    public BoxDrawingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mRotatingPaint = new Paint();
        mRotatingPaint.setStrokeWidth(16);
        mRotatingPaint.setColor(0xff0000ff);
        mRotatingPaint.setTextSize(64);
        mRotatingPaint.setFakeBoldText(true);


        mBoxPaint = new Paint();
        mBoxPaint.setColor(0x22ff0000);
        mBackgroundPaint = new Paint();
        mBackgroundPaint.setColor(0xfff8efe0);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        PointF current = new PointF(event.getX(), event.getY());
        String action = "";
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                action = "ACTION_DOWN";
                mCurrentBox = new Box(current);
                mBoxes.add(mCurrentBox);
                if (event.getPointerCount() == 1) {
                    rotatingStartPointId = event.getPointerId(0);
                    startLineStartPoint.x = event.getX(0);
                    startLineStartPoint.y = event.getY(0);
                }
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                action = "ACTION_POINTER_DOWN";
                isRotating = true;
                mBoxes.remove(mCurrentBox);
                if (event.getPointerCount() == 2) {
                    for (int i = 0; i < 2; i++) {
                        int pointId = event.getPointerId(i);
                        if (pointId != rotatingStartPointId) {
                            rotatingEndPointId = pointId;
                            startLineEndPoint.x = event.getX(i);
                            startLineEndPoint.y = event.getY(i);
                        }
                    }
                }
                break;
            case MotionEvent.ACTION_MOVE:
                action = "ACTION_MOVE";
                if (mCurrentBox != null) {
                    mCurrentBox.setCurrent(current);
                }
                if (event.getPointerCount() == 2) {
                    int startIndex = event.findPointerIndex(rotatingStartPointId);
                    endLineStartPoint.x = event.getX(startIndex);
                    endLineStartPoint.y = event.getY(startIndex);
                    int endIndex = event.findPointerIndex(rotatingEndPointId);
                    endLineEndPoint.x = event.getX(endIndex);
                    endLineEndPoint.y = event.getY(endIndex);
                }
                invalidate();
                break;
            case MotionEvent.ACTION_POINTER_UP:
                action = "ACTION_POINTER_UP";
                mCurrentBox = null;
                isRotating = false;
                double startRadian = radian(startLineStartPoint, startLineEndPoint);
                double endRadian = radian(endLineStartPoint, endLineEndPoint);
                double radianDelta = endRadian - startRadian;
                if (radianDelta != 0) {
                    for (Box box : mBoxes)
                        box.setRadian(box.getRadian() + radianDelta);
                }
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                action = "ACTION_UP";
                mCurrentBox = null;
                break;
            case MotionEvent.ACTION_CANCEL:
                action = "ACTION_CANCEL";
                mCurrentBox = null;
                break;
        }
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawPaint(mBackgroundPaint);
        if (isRotating) {
            double startRadian = radian(startLineStartPoint, startLineEndPoint);
            double endRadian = radian(endLineStartPoint, endLineEndPoint);
            int angleDelta = (int) ((endRadian - startRadian) * 180 / Math.PI);
            canvas.drawText("角度差:" + angleDelta, endLineEndPoint.x + 32, endLineEndPoint.y + 32, mRotatingPaint);
            canvas.drawCircle(startLineStartPoint.x, startLineStartPoint.y, 16, mRotatingPaint);
            canvas.drawCircle(startLineEndPoint.x, startLineEndPoint.y, 32, mRotatingPaint);
            canvas.drawLine(startLineStartPoint.x, startLineStartPoint.y, startLineEndPoint.x, startLineEndPoint.y, mRotatingPaint);
            canvas.drawCircle(endLineStartPoint.x, endLineStartPoint.y, 16, mRotatingPaint);
            canvas.drawCircle(endLineEndPoint.x, endLineEndPoint.y, 32, mRotatingPaint);
            canvas.drawLine(endLineStartPoint.x, endLineStartPoint.y, endLineEndPoint.x, endLineEndPoint.y, mRotatingPaint);
        }
        for (Box box : mBoxes) {
            canvas.save();
            float xCenter = (box.getOrigin().x + box.getCurrent().x)/2;
            float yCenter = (box.getOrigin().y + box.getCurrent().y)/2;
            canvas.translate(xCenter,yCenter);
            canvas.rotate(0-(float) (box.getRadian()*180/Math.PI));

            float left = Math.min(box.getOrigin().x-xCenter, box.getCurrent().x-xCenter);
            float right = Math.max(box.getOrigin().x-xCenter, box.getCurrent().x-xCenter);
            float top = Math.min(box.getOrigin().y-yCenter, box.getCurrent().y-yCenter);
            float bottom = Math.max(box.getOrigin().y-yCenter, box.getCurrent().y-yCenter);

            canvas.drawRect(left, top, right, bottom, mBoxPaint);
            canvas.restore();

        }
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable("super_data", super.onSaveInstanceState());
        bundle.putParcelableArrayList("boxes", mBoxes);
        return bundle;
    }

    private double radian(PointF start, PointF end) {
        double result = 0;
        float xDelta = end.x - start.x;
        float yDelta = end.y - start.y;
        if (xDelta != 0) {
            result = Math.abs(Math.atan(yDelta / xDelta));
            if (xDelta > 0) {
                if (yDelta < 0) {//第一象限

                }
                if (yDelta > 0) //第四象限
                    result = 2 * Math.PI - result;
            }
            if (xDelta < 0) {
                if (yDelta == 0)
                    result = Math.PI + result;
                if (yDelta < 0) //第二象限
                    result = Math.PI - result;

                if (yDelta > 0) //第三象限
                    result = Math.PI + result;
            }
        } else {
            if (yDelta < 0)
                result = 0.5 * Math.PI;
            if (yDelta > 0)
                result = 1.5 * Math.PI;
        }

        return result;
    }


    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        Bundle bundle = (Bundle) state;
        Parcelable superData = bundle.getParcelable("super_data");
        mBoxes = bundle.getParcelableArrayList("boxes");
        super.onRestoreInstanceState(superData);
    }
}
