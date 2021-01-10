package com.android.tify_store.Minwoo.Bean;

public class Menu {

    int mNo;
    String mName;
    int mPrice;
    int mSizeUp;
    int mShut;
    String mImage;
    int mType;
    String mComment;

    public Menu(int mNo, String mName, int mPrice, int mSizeUp, int mShut, String mImage, int mType, String mComment) {
        this.mNo = mNo;
        this.mName = mName;
        this.mPrice = mPrice;
        this.mSizeUp = mSizeUp;
        this.mShut = mShut;
        this.mImage = mImage;
        this.mType = mType;
        this.mComment = mComment;
    }

    public int getmNo() {
        return mNo;
    }

    public void setmNo(int mNo) {
        this.mNo = mNo;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public int getmPrice() {
        return mPrice;
    }

    public void setmPrice(int mPrice) {
        this.mPrice = mPrice;
    }

    public int getmSizeUp() {
        return mSizeUp;
    }

    public void setmSizeUp(int mSizeUp) {
        this.mSizeUp = mSizeUp;
    }

    public int getmShut() {
        return mShut;
    }

    public void setmShut(int mShut) {
        this.mShut = mShut;
    }

    public String getmImage() {
        return mImage;
    }

    public void setmImage(String mImage) {
        this.mImage = mImage;
    }

    public int getmType() {
        return mType;
    }

    public void setmType(int mType) {
        this.mType = mType;
    }

    public String getmComment() {
        return mComment;
    }

    public void setmComment(String mComment) {
        this.mComment = mComment;
    }
}
