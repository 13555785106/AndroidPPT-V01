package com.telecom.draganddraw;

import android.graphics.PointF;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by xiaojf on 18/1/17.
 */

public class Box implements Parcelable {
    private PointF mOrigin;
    private PointF mCurrent;
    private double mRadian;
    public Box(Parcel in){
        mOrigin = in.readParcelable(PointF.class.getClassLoader());
        mCurrent = in.readParcelable(PointF.class.getClassLoader());
        mRadian = in.readDouble();
    }
    public Box(PointF origin) {
        mOrigin = origin;
        mCurrent = origin;
    }

    public PointF getCurrent() {
        return mCurrent;
    }

    public void setCurrent(PointF current) {
        mCurrent = current;
    }

    public PointF getOrigin() {
        return mOrigin;
    }

    public double getRadian() {
        return mRadian;
    }

    public void setRadian(double radian) {
        mRadian = radian;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeParcelable(getOrigin(),flags);
        out.writeParcelable(getCurrent(),flags);
        out.writeDouble(getRadian());
    }

    public static final Parcelable.Creator<Box> CREATOR = new Creator<Box>()
    {
        @Override
        public Box[] newArray(int size)
        {
            return new Box[size];
        }

        @Override
        public Box createFromParcel(Parcel in)
        {
            return new Box(in);
        }
    };
}
