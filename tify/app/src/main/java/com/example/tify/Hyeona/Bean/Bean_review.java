package com.example.tify.Hyeona.Bean;

public class Bean_review {

    int rNo, user_uNo, store_sSeqNo;
    String rContent, rImage, rOwnerComment, rDeletedate, rInsertDate;

    public Bean_review(){

    };

    public Bean_review(int rNo, int user_uNo, int store_sSeqNo, String rContent, String rImage, String rOwnerComment, String rDeletedate, String rInsertDate) {
        this.rNo = rNo;
        this.user_uNo = user_uNo;
        this.store_sSeqNo = store_sSeqNo;
        this.rContent = rContent;
        this.rImage = rImage;
        this.rOwnerComment = rOwnerComment;
        this.rDeletedate = rDeletedate;
        this.rInsertDate = rInsertDate;
    }

    public int getrNo() {
        return rNo;
    }

    public void setrNo(int rNo) {
        this.rNo = rNo;
    }

    public int getUser_uNo() {
        return user_uNo;
    }

    public void setUser_uNo(int user_uNo) {
        this.user_uNo = user_uNo;
    }

    public int getStore_sSeqNo() {
        return store_sSeqNo;
    }

    public void setStore_sSeqNo(int store_sSeqNo) {
        this.store_sSeqNo = store_sSeqNo;
    }

    public String getrContent() {
        return rContent;
    }

    public void setrContent(String rContent) {
        this.rContent = rContent;
    }

    public String getrImage() {
        return rImage;
    }

    public void setrImage(String rImage) {
        this.rImage = rImage;
    }

    public String getrOwnerComment() {
        return rOwnerComment;
    }

    public void setrOwnerComment(String rOwnerComment) {
        this.rOwnerComment = rOwnerComment;
    }

    public String getrDeletedate() {
        return rDeletedate;
    }

    public void setrDeletedate(String rDeletedate) {
        this.rDeletedate = rDeletedate;
    }

    public String getrInsertDate() {
        return rInsertDate;
    }

    public void setrInsertDate(String rInsertDate) {
        this.rInsertDate = rInsertDate;
    }
}
