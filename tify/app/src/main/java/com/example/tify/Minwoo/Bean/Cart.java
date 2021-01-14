package com.example.tify.Minwoo.Bean;

import java.io.Serializable;
import java.util.ArrayList;

public class Cart implements Serializable {

    int cLNo;
    int store_sSeqNo;
    String menu_mName;
    int cLPrice;
    int cLQuantity;
    String cLImage;
    int cLSizeUp;
    int cLAddShot;
    String cLRequest;
    ArrayList<Cart> carts;


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

    public Cart(ArrayList<Cart> carts) {
        this.carts = carts;
    }

    public Cart() {
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

    public int getcLPrice() {
        return cLPrice;
    }

    public void setcLPrice(int cLPrice) {
        this.cLPrice = cLPrice;
    }

    public int getcLQuantity() {
        return cLQuantity;
    }

    public void setcLQuantity(int cLQuantity) {
        this.cLQuantity = cLQuantity;
    }

    public String getcLImage() {
        return cLImage;
    }

    public void setcLImage(String cLImage) {
        this.cLImage = cLImage;
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

    public String getcLRequest() {
        return cLRequest;
    }

    public void setcLRequest(String cLRequest) {
        this.cLRequest = cLRequest;
    }

    public ArrayList<Cart> getCarts() {
        return carts;
    }

    public void setCarts(ArrayList<Cart> carts) {
        this.carts = carts;
    }
}
