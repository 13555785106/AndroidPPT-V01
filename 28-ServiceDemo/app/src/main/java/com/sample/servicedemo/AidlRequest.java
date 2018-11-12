package com.sample.servicedemo;

import android.os.Parcel;
import android.os.Parcelable;

public class AidlRequest implements Parcelable {
    private String message="默认信息";
    private int code = 0;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.message);
        dest.writeInt(this.code);
    }

    public AidlRequest() {
    }

    protected AidlRequest(Parcel in) {
        this.message = in.readString();
        this.code = in.readInt();
    }

    public static final Creator<AidlRequest> CREATOR = new Creator<AidlRequest>() {
        public AidlRequest createFromParcel(Parcel source) {
            return new AidlRequest(source);
        }

        public AidlRequest[] newArray(int size) {
            return new AidlRequest[size];
        }
    };

    @Override
    public String toString() {
        return "AidlRequest{" +
                "message='" + message + '\'' +
                ", code=" + code +
                '}';
    }
}