package com.example.tify.Minwoo.Bean;

public class OrderList {

    // order
    int oNo;
    int store_sSeqno;
    int cartlist_cLNo;
    String oInsertDate;
    String oDeleteDate;
    int oSum;
    String oCardName;
    int oCardNo;
    int oReview;

    //orderlist
    String sName;
    int olSeqNo;
    int olAddOrder;
    String olRequest;
    int olPrice;
    int olQuantity;


    int oSeqno;
    String oDate;

    String oStatus;

    String mName;
    String addOrder1;
    String addOrder2;
    String request;

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

    // OrderListActivity
    public OrderList(int oNo, int store_sSeqno, int cartlist_cLNo, String oInsertDate, String oDeleteDate, int oSum, String oCardName, int oCardNo, int oReview) {
        this.oNo = oNo;
        this.store_sSeqno = store_sSeqno;
        this.cartlist_cLNo = cartlist_cLNo;
        this.oInsertDate = oInsertDate;
        this.oDeleteDate = oDeleteDate;
        this.oSum = oSum;
        this.oCardName = oCardName;
        this.oCardNo = oCardNo;
        this.oReview = oReview;
    }


    // OrderDetailActivity
    public OrderList(String oInsertDate, String sName, int olSeqNo, int olAddOrder, String olRequest, int olPrice, int olQuantity, String mName) {
        this.oInsertDate = oInsertDate;
        this.sName = sName;
        this.olSeqNo = olSeqNo;
        this.olAddOrder = olAddOrder;
        this.olRequest = olRequest;
        this.olPrice = olPrice;
        this.olQuantity = olQuantity;
        this.mName = mName;
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

    public int getoNo() {
        return oNo;
    }

    public void setoNo(int oNo) {
        this.oNo = oNo;
    }

    public int getCartlist_cLNo() {
        return cartlist_cLNo;
    }

    public void setCartlist_cLNo(int cartlist_cLNo) {
        this.cartlist_cLNo = cartlist_cLNo;
    }

    public String getoInsertDate() {
        return oInsertDate;
    }

    public void setoInsertDate(String oInsertDate) {
        this.oInsertDate = oInsertDate;
    }

    public String getoDeleteDate() {
        return oDeleteDate;
    }

    public void setoDeleteDate(String oDeleteDate) {
        this.oDeleteDate = oDeleteDate;
    }

    public int getoSum() {
        return oSum;
    }

    public void setoSum(int oSum) {
        this.oSum = oSum;
    }

    public String getoCardName() {
        return oCardName;
    }

    public void setoCardName(String oCardName) {
        this.oCardName = oCardName;
    }

    public int getoCardNo() {
        return oCardNo;
    }

    public void setoCardNo(int oCardNo) {
        this.oCardNo = oCardNo;
    }

    public int getoReview() {
        return oReview;
    }

    public void setoReview(int oReview) {
        this.oReview = oReview;
    }

    public int getOlSeqNo() {
        return olSeqNo;
    }

    public void setOlSeqNo(int olSeqNo) {
        this.olSeqNo = olSeqNo;
    }

    public int getOlAddOrder() {
        return olAddOrder;
    }

    public void setOlAddOrder(int olAddOrder) {
        this.olAddOrder = olAddOrder;
    }

    public String getOlRequest() {
        return olRequest;
    }

    public void setOlRequest(String olRequest) {
        this.olRequest = olRequest;
    }

    public int getOlPrice() {
        return olPrice;
    }

    public void setOlPrice(int olPrice) {
        this.olPrice = olPrice;
    }

    public int getOlQuantity() {
        return olQuantity;
    }

    public void setOlQuantity(int olQuantity) {
        this.olQuantity = olQuantity;
    }
}
