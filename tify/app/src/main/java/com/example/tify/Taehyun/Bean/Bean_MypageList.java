package com.example.tify.Taehyun.Bean;

public class Bean_MypageList {
   // Date.2021.01.06 - 태현
    //field
    private String terms;
    private int into;

    public Bean_MypageList(String terms, int into) {
        this.terms = terms;
        this.into = into;
    }

    public String getTerms() {
        return terms;
    }

    public void setTerms(String terms) {
        this.terms = terms;
    }

    public int getInto() {
        return into;
    }

    public void setInto(int into) {
        this.into = into;
    }
}
