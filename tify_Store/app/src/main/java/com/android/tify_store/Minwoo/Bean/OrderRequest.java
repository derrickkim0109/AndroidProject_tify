package com.android.tify_store.Minwoo.Bean;

public class OrderRequest {

    //orderlist
    int order_oNo;
    String store_sName;
    String menu_mName;
    int olSeqNo;
    int olSizeUp;
    int olAddShot;
    String olRequest;
    int olPrice;
    int olQuantity;
    String oInsertDate;
    int oStatus;

    public OrderRequest() {
    }

    public OrderRequest(int order_oNo, String store_sName, String menu_mName, int olSeqNo, int olSizeUp, int olAddShot, String olRequest, int olPrice, int olQuantity, String oInsertDate, int oStatus) {
        this.order_oNo = order_oNo;
        this.store_sName = store_sName;
        this.menu_mName = menu_mName;
        this.olSeqNo = olSeqNo;
        this.olSizeUp = olSizeUp;
        this.olAddShot = olAddShot;
        this.olRequest = olRequest;
        this.olPrice = olPrice;
        this.olQuantity = olQuantity;
        this.oInsertDate = oInsertDate;
        this.oStatus = oStatus;
    }

    public OrderRequest(int order_oNo) {
        this.order_oNo = order_oNo;
    }


    public int getOrder_oNo() {
        return order_oNo;
    }

    public void setOrder_oNo(int order_oNo) {
        this.order_oNo = order_oNo;
    }

    public String getStore_sName() {
        return store_sName;
    }

    public void setStore_sName(String store_sName) {
        this.store_sName = store_sName;
    }

    public String getMenu_mName() {
        return menu_mName;
    }

    public void setMenu_mName(String menu_mName) {
        this.menu_mName = menu_mName;
    }

    public int getOlSeqNo() {
        return olSeqNo;
    }

    public void setOlSeqNo(int olSeqNo) {
        this.olSeqNo = olSeqNo;
    }

    public int getOlSizeUp() {
        return olSizeUp;
    }

    public void setOlSizeUp(int olSizeUp) {
        this.olSizeUp = olSizeUp;
    }

    public int getOlAddShot() {
        return olAddShot;
    }

    public void setOlAddShot(int olAddShot) {
        this.olAddShot = olAddShot;
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

    public int getoStatus() {
        return oStatus;
    }

    public void setoStatus(int oStatus) {
        this.oStatus = oStatus;
    }

    public String getoInsertDate() {
        return oInsertDate;
    }

    public void setoInsertDate(String oInsertDate) {
        this.oInsertDate = oInsertDate;
    }


}
