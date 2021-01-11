package com.example.tify.Jiseok.Bean;

public class Bean_Login_cjs {
    private int count;
    private int uNo;
    private String uEmail;
    private String uNickName;

    public Bean_Login_cjs() {
    }

    public Bean_Login_cjs(int count, int uNo, String uEmail, String uNickName) {
        this.count = count;
        this.uNo = uNo;
        this.uEmail = uEmail;
        this.uNickName = uNickName;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getuNo() {
        return uNo;
    }

    public void setuNo(int uNo) {
        this.uNo = uNo;
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
}


