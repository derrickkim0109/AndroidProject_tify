package com.example.tify.Taehyun.Bean;

public class Bean_Mypage_userinfo {

    //field
    int uNo;
    String uEmail, uNickName, uImage,uPayPassword, uTelNo;

    //constructor

    public Bean_Mypage_userinfo() {

    }

    public Bean_Mypage_userinfo(int uNo, String uEmail , String uNickName
            , String uTelNo, String uImage, String uPayPassword) {

        this.uNo = uNo;
        this.uPayPassword = uPayPassword;
        this.uTelNo = uTelNo;
        this.uEmail = uEmail;
        this.uNickName = uNickName;
        this.uImage = uImage;
    }

    public int getuNo() {
        return uNo;
    }

    public void setuNo(int uNo) {
        this.uNo = uNo;
    }

    public String getuPayPassword() {
        return uPayPassword;
    }

    public void setuPayPassword(String uPayPassword) {
        this.uPayPassword = uPayPassword;
    }

    public String getuTelNo() {
        return uTelNo;
    }

    public void setuTelNo(String uTelNo) {
        this.uTelNo = uTelNo;
    }

    public String getuEmail() {
        return uEmail;
    }

    public void setuEmail(String uEmail) {
        this.uEmail = uEmail;
    }

    public String getuNickName() {
        return uNickName;
    }

    public void setuNickName(String uNickName) {
        this.uNickName = uNickName;
    }

    public String getuImage() {
        return uImage;
    }

    public void setuImage(String uImage) {
        this.uImage = uImage;
    }
}
