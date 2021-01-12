package com.example.tify.Taehyun.Bean;

public class Bean_Mypage_cardlist {

    //field
    private String cYear;
    private String cMM;
    private String uName;
    private String cCardNo;


    //constructor
    public Bean_Mypage_cardlist(String cYear, String cMM, String uName, String cCardNo) {
        this.cYear = cYear;
        this.cMM = cMM;
        this.uName = uName;
        this.cCardNo = cCardNo;
    }

    public Bean_Mypage_cardlist() {

    }

    public String getcYear() {
        return cYear;
    }

    public void setcYear(String cYear) {
        this.cYear = cYear;
    }

    public String getcMM() {
        return cMM;
    }

    public void setcMM(String cMM) {
        this.cMM = cMM;
    }

    public String getuName() {
        return uName;
    }

    public void setuName(String uName) {
        this.uName = uName;
    }

    public String getcCardNo() {
        return cCardNo;
    }

    public void setcCardNo(String cCardNo) {
        this.cCardNo = cCardNo;
    }

}
