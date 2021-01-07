package com.android.tify_store.Minwoo.Bean;

public class Progressing {

    int seqno;
    String date;
    String storeName;
    String menuName;
    int quantity;
    String addOrder1;
    String addOrder2;
    String orderRequest;
    int price;

    public Progressing(int seqno, String date, String storeName, String menuName, int quantity, String addOrder1, String addOrder2, String orderRequest, int price) {
        this.seqno = seqno;
        this.date = date;
        this.storeName = storeName;
        this.menuName = menuName;
        this.quantity = quantity;
        this.addOrder1 = addOrder1;
        this.addOrder2 = addOrder2;
        this.orderRequest = orderRequest;
        this.price = price;
    }

    public int getSeqno() {
        return seqno;
    }

    public void setSeqno(int seqno) {
        this.seqno = seqno;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
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

    public String getOrderRequest() {
        return orderRequest;
    }

    public void setOrderRequest(String orderRequest) {
        this.orderRequest = orderRequest;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
