package com.android.tify_store.Minwoo.Bean;

public class Complete {

    int seqno;
    String time;
    String date;
    String storeName;
    String menuName;
    String size;
    String quantity;
    String addOrder1;
    String addOrder2;
    String orderRequest;
    int price;

    public Complete(int seqno, String time, String date, String storeName, String menuName, String size, String quantity, String addOrder1, String addOrder2, String orderRequest, int price) {
        this.seqno = seqno;
        this.time = time;
        this.date = date;
        this.storeName = storeName;
        this.menuName = menuName;
        this.size = size;
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

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
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

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
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
