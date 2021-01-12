package com.example.tify.Taehyun.Bean;

public class Bean_Mypage_CardInfo {

    //field
    private int cNo ;
    private String cImage;
    private String cInfo;
    private String cCardNo;


    public Bean_Mypage_CardInfo(String cImage, String cCardNo, int cNo, String cInfo) {
        this.cImage = cImage;
        this.cCardNo = cCardNo;
        this.cInfo = cInfo;
        this.cNo = cNo;
    }

    public Bean_Mypage_CardInfo() {

    }

    public int getcNo() {
        return cNo;
    }

    public void setcNo(int cNo) {
        this.cNo = cNo;
    }

    public String getcImage() {
        return cImage;
    }

    public void setcImage(String cImage) {
        this.cImage = cImage;
    }

    public String getcInfo() {
        return cInfo;
    }

    public void setcInfo(String cInfo) {
        this.cInfo = cInfo;
    }

    public String getcCardNo() {
        return cCardNo;
    }

    public void setcCardNo(String cCardNo) {
        this.cCardNo = cCardNo;
    }


}
