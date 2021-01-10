package com.android.tify_store.Minwoo.Bean;

public class Store {

    String sName;
    String sTelNo;
    String sRunningTime;
    String sAddress;
    String sImage;
    int sPackaging;
    String sComment;

    public Store(String sName, String sTelNo, String sRunningTime, String sAddress, String sImage, int sPackaging, String sComment) {
        this.sName = sName;
        this.sTelNo = sTelNo;
        this.sRunningTime = sRunningTime;
        this.sAddress = sAddress;
        this.sImage = sImage;
        this.sPackaging = sPackaging;
        this.sComment = sComment;
    }

    public String getsName() {
        return sName;
    }

    public void setsName(String sName) {
        this.sName = sName;
    }

    public String getsTelNo() {
        return sTelNo;
    }

    public void setsTelNo(String sTelNo) {
        this.sTelNo = sTelNo;
    }

    public String getsRunningTime() {
        return sRunningTime;
    }

    public void setsRunningTime(String sRunningTime) {
        this.sRunningTime = sRunningTime;
    }

    public String getsAddress() {
        return sAddress;
    }

    public void setsAddress(String sAddress) {
        this.sAddress = sAddress;
    }

    public String getsImage() {
        return sImage;
    }

    public void setsImage(String sImage) {
        this.sImage = sImage;
    }

    public int getsPackaging() {
        return sPackaging;
    }

    public void setsPackaging(int sPackaging) {
        this.sPackaging = sPackaging;
    }

    public String getsComment() {
        return sComment;
    }

    public void setsComment(String sComment) {
        this.sComment = sComment;
    }
}
