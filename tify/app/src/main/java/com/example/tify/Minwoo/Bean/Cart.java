package com.example.tify.Minwoo.Bean;

public class Cart {

    String sName;
    String mName;
    String cLImage;
    String addOrder1;
    String addOrder2;
    String request;
    int cLQuantity;
    int cLPrice;

    public Cart(String mName, String addOrder1, String addOrder2, String request, int cLQuantity, int cLPrice) {
        this.mName = mName;
        this.addOrder1 = addOrder1;
        this.addOrder2 = addOrder2;
        this.request = request;
        this.cLQuantity = cLQuantity;
        this.cLPrice = cLPrice;
    }

    public String getsName() {
        return sName;
    }

    public void setsName(String sName) {
        this.sName = sName;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public String getcLImage() {
        return cLImage;
    }

    public void setcLImage(String cLImage) {
        this.cLImage = cLImage;
    }

    public String getAddOrder1() {
        return addOrder1;
    }

    public void setAddOrder1(String addOrder1) {
        this.addOrder1 = addOrder1;
    }

    public String getAddOrder2() {
        return addOrder2;
    }

    public void setAddOrder2(String addOrder2) {
        this.addOrder2 = addOrder2;
    }

    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }

    public int getcLQuantity() {
        return cLQuantity;
    }

    public void setcLQuantity(int cLQuantity) {
        this.cLQuantity = cLQuantity;
    }

    public int getcLPrice() {
        return cLPrice;
    }

    public void setcLPrice(int cLPrice) {
        this.cLPrice = cLPrice;
    }
}
