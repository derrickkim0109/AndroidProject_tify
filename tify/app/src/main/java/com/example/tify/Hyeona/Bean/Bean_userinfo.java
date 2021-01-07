package com.example.tify.Hyeona.Bean;

public class Bean_userinfo {

    //field
    int uNo, uPayPassword, uTelNo;
    String uEmail, uNickName, uImage;

    //constructor

    public Bean_userinfo() {

    }

    public Bean_userinfo(int uNo, String uEmail , String uNickName
            , int uTelNo, String uImage, int uPayPassword) {

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

    public int getuPayPassword() {
        return uPayPassword;
    }

    public void setuPayPassword(int uPayPassword) {
        this.uPayPassword = uPayPassword;
    }

    public int getuTelNo() {
        return uTelNo;
    }

    public void setuTelNo(int uTelNo) {
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
