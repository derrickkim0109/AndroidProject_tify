package com.example.tify.Hyeona.Bean;

public class Bean_review_review {

    int rNo, user_uNo, storekeeper_skSeqNo;
    String rContent, rImage, rOwnerComment, rDeletedate, rInsertDate;

    public Bean_review_review() {

    }

    public Bean_review_review(int rNo, int user_uNo, int storekeeper_skSeqNo, String rContent, String rImage, String rOwnerComment, String rDeletedate, String rInsertDate) {
        this.rNo = rNo;
        this.user_uNo = user_uNo;
        this.storekeeper_skSeqNo = storekeeper_skSeqNo;
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

    public int getStorekeeper_skSeqNo() {
        return storekeeper_skSeqNo;
    }

    public void setStorekeeper_skSeqNo(int storekeeper_skSeqNo) {
        this.storekeeper_skSeqNo = storekeeper_skSeqNo;
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