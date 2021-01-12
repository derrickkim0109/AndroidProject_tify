package com.example.tify.Taehyun.Bean;

public class Bean_Mypage_cardlist {

    //field
    private String cCardCompany ;
    private String cCardNo ;
    private String cYear ;
    private String cMM ;
    private int cNo;
    private String uName;


    //constructor

    public Bean_Mypage_cardlist(String cCardCompany, String cCardNo, String cYear, String cMM, int cNo, String uName) {
        this.cCardCompany = cCardCompany;
        this.cCardNo = cCardNo;
        this.cYear = cYear;
        this.cMM = cMM;
        this.cNo = cNo;
        this.uName = uName;
    }

    public Bean_Mypage_cardlist() {

    }

    public String getcCardCompany() {
        return cCardCompany;
    }

    public void setcCardCompany(String cCardCompany) {
        this.cCardCompany = cCardCompany;
    }

    public String getcCardNo() {
        return cCardNo;
    }

    public void setcCardNo(String cCardNo) {
        this.cCardNo = cCardNo;
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

    public int getcNo() {
        return cNo;
    }

    public void setcNo(int cNo) {
        this.cNo = cNo;
    }

    public String getuName() {
        return uName;
    }

    public void setuName(String uName) {
        this.uName = uName;
    }
}
