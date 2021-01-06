package com.example.tify.Minwoo.Bean;

public class OrderList {

    int oSeqno;
    String oDate;
    int store_sSeqno;
    String oStatus;

    String mName;
    String addOrder1;
    String addOrder2;
    String request;
    String sName;
    int subTotalPrice;

    public OrderList(int oSeqno, String oDate, int store_sSeqno, String oStatus) { // OrderListActivity
        this.oSeqno = oSeqno;
        this.oDate = oDate;
        this.store_sSeqno = store_sSeqno;
        this.oStatus = oStatus;
    }

    public OrderList(int oSeqno, String oDate, String mName, String addOrder1, String addOrder2, String request, String sName, int subTotalPrice) { // OrderDetailActivity
        this.oSeqno = oSeqno;
        this.oDate = oDate;
        this.mName = mName;
        this.addOrder1 = addOrder1;
        this.addOrder2 = addOrder2;
        this.request = request;
        this.sName = sName;
        this.subTotalPrice = subTotalPrice;
    }

    public int getoSeqno() {
        return oSeqno;
    }

    public void setoSeqno(int oSeqno) {
        this.oSeqno = oSeqno;
    }

    public String getoDate() {
        return oDate;
    }

    public void setoDate(String oDate) {
        this.oDate = oDate;
    }

    public int getStore_sSeqno() {
        return store_sSeqno;
    }

    public void setStore_sSeqno(int store_sSeqno) {
        this.store_sSeqno = store_sSeqno;
    }

    public String getoStatus() {
        return oStatus;
    }

    public void setoStatus(String oStatus) {
        this.oStatus = oStatus;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
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

    public String getsName() {
        return sName;
    }

    public void setsName(String sName) {
        this.sName = sName;
    }

    public int getSubTotalPrice() {
        return subTotalPrice;
    }

    public void setSubTotalPrice(int subTotalPrice) {
        this.subTotalPrice = subTotalPrice;
    }
}
