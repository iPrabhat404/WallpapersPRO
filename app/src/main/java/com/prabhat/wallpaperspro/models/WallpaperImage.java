package com.prabhat.wallpaperspro.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class WallpaperImage implements Serializable{

    private String mImageUrl;
    private long mIndex;

    public WallpaperImage(String imageUrl, long index) {
        mImageUrl = imageUrl;
        mIndex = index;
    }

    public WallpaperImage() {
    }

    public String getImageUrl() {
        return mImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.mImageUrl = imageUrl;
    }

    public long getIndex() {
        return mIndex;
    }

    public void setIndex(long index) {
        mIndex = index;
    }
}
