package com.example.tify.Jiseok.Bean;

public class Bean_MainCafeList_cjs {
    private String sName;
    private String sTelNo;
    private String sRunningTime;
    private String sAddress;
    private String sImage;
    private String sPackaging;
    private String sComment;
    private String storekeeper_skSeqNo;
    private String likeCount;
    private String reviewCount;
    private String skStatus;


    public Bean_MainCafeList_cjs() {
    }

    public Bean_MainCafeList_cjs(String sName, String sTelNo, String sRunningTime, String sAddress, String sImage, String sPackaging, String sComment, String storekeeper_skSeqNo, String likeCount, String reviewCount, String skStatus) {
        this.sName = sName;
        this.sTelNo = sTelNo;
        this.sRunningTime = sRunningTime;
        this.sAddress = sAddress;
        this.sImage = sImage;
        this.sPackaging = sPackaging;
        this.sComment = sComment;
        this.storekeeper_skSeqNo = storekeeper_skSeqNo;
        this.likeCount = likeCount;
        this.reviewCount = reviewCount;
        this.skStatus = skStatus;
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

    public String getsPackaging() {
        return sPackaging;
    }

    public void setsPackaging(String sPackaging) {
        this.sPackaging = sPackaging;
    }

    public String getsComment() {
        return sComment;
    }

    public void setsComment(String sComment) {
        this.sComment = sComment;
    }

    public String getStorekeeper_skSeqNo() {
        return storekeeper_skSeqNo;
    }

    public void setStorekeeper_skSeqNo(String storekeeper_skSeqNo) {
        this.storekeeper_skSeqNo = storekeeper_skSeqNo;
    }

    public String getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(String likeCount) {
        this.likeCount = likeCount;
    }

    public String getReviewCount() {
        return reviewCount;
    }

    public void setReviewCount(String reviewCount) {
        this.reviewCount = reviewCount;
    }

    public String getSkStatus() {
        return skStatus;
    }

    public void setSkStatus(String skStatus) {
        this.skStatus = skStatus;
    }
}
