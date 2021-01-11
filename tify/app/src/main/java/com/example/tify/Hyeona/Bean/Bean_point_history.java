package com.example.tify.Hyeona.Bean;

public class Bean_point_history {

    int rhNo;
    String rhDay;
    String rhContent;
    int rhChoice;
    String rhPointHow;

    public Bean_point_history() {
    }

    public Bean_point_history(int rhNo, String rhDay, String rhContent, int rhChoice, String rhPointHow) {
        this.rhNo = rhNo;
        this.rhDay = rhDay;
        this.rhContent = rhContent;
        this.rhChoice = rhChoice;
        this.rhPointHow = rhPointHow;
    }

    public int getRhNo() {
        return rhNo;
    }

    public void setRhNo(int rhNo) {
        this.rhNo = rhNo;
    }

    public String getRhDay() {
        return rhDay;
    }

    public void setRhDay(String rhDay) {
        this.rhDay = rhDay;
    }

    public String getRhContent() {
        return rhContent;
    }

    public void setRhContent(String rhContent) {
        this.rhContent = rhContent;
    }

    public int getRhChoice() {
        return rhChoice;
    }

    public void setRhChoice(int rhChoice) {
        this.rhChoice = rhChoice;
    }

    public String getRhPointHow() {
        return rhPointHow;
    }

    public void setRhPointHow(String rhPointHow) {
        this.rhPointHow = rhPointHow;
    }
}
