package com.example.tify.Minwoo.Bean;

public class Menu {

    String mName;
    int mPrice;

    public Menu(String mName, int mPrice) {
        this.mName = mName;
        this.mPrice = mPrice;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public int getmPrice() {
        return mPrice;
    }

    public void setmPrice(int mPrice) {
        this.mPrice = mPrice;
    }
}
