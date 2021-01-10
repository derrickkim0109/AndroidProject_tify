package com.example.tify.Hyeona.Bean;

public class Bean_review_store {

    int storekeeper_skSeqNo;
    String sName, sTelNo, sAddress, sImage;


    public Bean_review_store() {

    }
    public Bean_review_store(int storekeeper_skSeqNo, String sName, String sTelNo, String sAddress, String sImage) {
        this.storekeeper_skSeqNo = storekeeper_skSeqNo;
        this.sName = sName;
        this.sTelNo = sTelNo;
        this.sAddress = sAddress;
        this.sImage = sImage;
    }



    public int getStorekeeper_skSeqNo() {
        return storekeeper_skSeqNo;
    }

    public void setStorekeeper_skSeqNo(int storekeeper_skSeqNo) {
        this.storekeeper_skSeqNo = storekeeper_skSeqNo;
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
}
