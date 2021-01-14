package com.example.tify.Minwoo.Bean;

public class Order {

    // order
    int user_uNo;
    int oNo;
    int store_sSeqno;
    String store_sName;
    String oInsertDate;
    String oDeleteDate;
    int oSum;
    String oCardName;
    String oCardNo;
    int oReview;
    int oStatus;
    int max;

    public Order(int user_uNo, int oNo, int store_sSeqno, String store_sName, String oInsertDate, String oDeleteDate, int oSum, String oCardName, String oCardNo, int oReview, int oStatus) {
        this.user_uNo = user_uNo;
        this.oNo = oNo;
        this.store_sSeqno = store_sSeqno;
        this.store_sName = store_sName;
        this.oInsertDate = oInsertDate;
        this.oDeleteDate = oDeleteDate;
        this.oSum = oSum;
        this.oCardName = oCardName;
        this.oCardNo = oCardNo;
        this.oReview = oReview;
        this.oStatus = oStatus;
    }



    public Order(int oNo, int store_sSeqno, String store_sName) {
        this.oNo = oNo;
        this.store_sName = store_sName;
        this.store_sSeqno = store_sSeqno;
    }

    public Order(int max) {
        this.max = max;
    }

    public int getUser_uNo() {
        return user_uNo;
    }

    public void setUser_uNo(int user_uNo) {
        this.user_uNo = user_uNo;
    }

    public int getoNo() {
        return oNo;
    }

    public void setoNo(int oNo) {
        this.oNo = oNo;
    }

    public int getStore_sSeqno() {
        return store_sSeqno;
    }

    public void setStore_sSeqno(int store_sSeqno) {
        this.store_sSeqno = store_sSeqno;
    }

    public String getoInsertDate() {
        return oInsertDate;
    }

    public void setoInsertDate(String oInsertDate) {
        this.oInsertDate = oInsertDate;
    }

    public String getoDeleteDate() {
        return oDeleteDate;
    }

    public void setoDeleteDate(String oDeleteDate) {
        this.oDeleteDate = oDeleteDate;
    }

    public int getoSum() {
        return oSum;
    }

    public void setoSum(int oSum) {
        this.oSum = oSum;
    }

    public String getoCardName() {
        return oCardName;
    }

    public void setoCardName(String oCardName) {
        this.oCardName = oCardName;
    }

    public int getoReview() {
        return oReview;
    }

    public void setoReview(int oReview) {
        this.oReview = oReview;
    }

    public String getStore_sName() {
        return store_sName;
    }

    public void setStore_sName(String store_sName) {
        this.store_sName = store_sName;
    }

    public int getoStatus() {
        return oStatus;
    }

    public void setoStatus(int oStatus) {
        this.oStatus = oStatus;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public String getoCardNo() {
        return oCardNo;
    }

    public void setoCardNo(String oCardNo) {
        this.oCardNo = oCardNo;
    }
}
