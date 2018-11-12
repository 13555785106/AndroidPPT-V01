package com.telecom.photogallery;

import com.google.gson.annotations.SerializedName;

/**
 * Created by xiaojf on 18/1/7.
 */

public class PhotoItem {
    @SerializedName("id")
    private long mId;
    @SerializedName("name")
    private String mName;
    @SerializedName("url")
    private String mUrl;

    public long getId() {
        return mId;
    }

    public void setId(long id) {
        mId = id;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String url) {
        mUrl = url;
    }

    @Override
    public String toString() {
        return "PhotoItem{" +
                "mId=" + mId +
                ", mName='" + mName + '\'' +
                ", mUrl='" + mUrl + '\'' +
                '}';
    }
}
