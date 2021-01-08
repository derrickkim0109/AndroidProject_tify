package com.example.tify.Minwoo.Bean;

public class Cart {

    int cLNo;
    int store_sSeqNo;
    String menu_mName;
    int cLPrice;
    int cLQuantity;
    String cLImage;
    int cLSizeUp;
    int cLAddShot;
    String cLRequest;

    String sName;
    String mName;
    String addOrder1;
    String addOrder2;
    String request;


    public Cart(String mName, String addOrder1, String addOrder2, String request, int cLQuantity, int cLPrice) {
        this.mName = mName;
        this.addOrder1 = addOrder1;
        this.addOrder2 = addOrder2;
        this.request = request;
        this.cLQuantity = cLQuantity;
        this.cLPrice = cLPrice;
    }

    public Cart(int cLNo, int store_sSeqNo, String menu_mName, int cLPrice, int cLQuantity, String cLImage, int cLSizeUp, int cLAddShot, String cLRequest) {
        this.cLNo = cLNo;
        this.store_sSeqNo = store_sSeqNo;
        this.menu_mName = menu_mName;
        this.cLPrice = cLPrice;
        this.cLQuantity = cLQuantity;
        this.cLImage = cLImage;
        this.cLSizeUp = cLSizeUp;
        this.cLAddShot = cLAddShot;
        this.cLRequest = cLRequest;
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

    public int getcLNo() {
        return cLNo;
    }

    public void setcLNo(int cLNo) {
        this.cLNo = cLNo;
    }

    public int getStore_sSeqNo() {
        return store_sSeqNo;
    }

    public void setStore_sSeqNo(int store_sSeqNo) {
        this.store_sSeqNo = store_sSeqNo;
    }

    public String getMenu_mName() {
        return menu_mName;
    }

    public void setMenu_mName(String menu_mName) {
        this.menu_mName = menu_mName;
    }


    public String getcLRequest() {
        return cLRequest;
    }

    public void setcLRequest(String cLRequest) {
        this.cLRequest = cLRequest;
    }

    public int getcLSizeUp() {
        return cLSizeUp;
    }

    public void setcLSizeUp(int cLSizeUp) {
        this.cLSizeUp = cLSizeUp;
    }

    public int getcLAddShot() {
        return cLAddShot;
    }

    public void setcLAddShot(int cLAddShot) {
        this.cLAddShot = cLAddShot;
    }
}
