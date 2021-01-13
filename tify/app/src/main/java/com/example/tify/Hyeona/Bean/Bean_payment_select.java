package com.example.tify.Hyeona.Bean;

public class Bean_payment_select {

    int user_uNo, oNo, storekeeper_skSeqNo;
    String store_sName, oInsertDate, oDeleteDate, oSum, oCardName, oCardNo;

    public Bean_payment_select(int user_uNo, int oNo, int storekeeper_skSeqNo, String store_sName, String oInsertDate, String oDeleteDate, String oSum, String oCardName, String oCardNo) {
        this.user_uNo = user_uNo;
        this.oNo = oNo;
        this.storekeeper_skSeqNo = storekeeper_skSeqNo;
        this.store_sName = store_sName;
        this.oInsertDate = oInsertDate;
        this.oDeleteDate = oDeleteDate;
        this.oSum = oSum;
        this.oCardName = oCardName;
        this.oCardNo = oCardNo;
    }

    public Bean_payment_select() {

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

    public int getStorekeeper_skSeqNo() {
        return storekeeper_skSeqNo;
    }

    public void setStorekeeper_skSeqNo(int storekeeper_skSeqNo) {
        this.storekeeper_skSeqNo = storekeeper_skSeqNo;
    }

    public String getStore_sName() {
        return store_sName;
    }

    public void setStore_sName(String store_sName) {
        this.store_sName = store_sName;
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

    public String getoSum() {
        return oSum;
    }

    public void setoSum(String oSum) {
        this.oSum = oSum;
    }

    public String getoCardName() {
        return oCardName;
    }

    public void setoCardName(String oCardName) {
        this.oCardName = oCardName;
    }

    public String getoCardNo() {
        return oCardNo;
    }

    public void setoCardNo(String oCardNo) {
        this.oCardNo = oCardNo;
    }
}
