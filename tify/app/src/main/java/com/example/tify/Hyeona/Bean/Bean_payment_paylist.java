package com.example.tify.Hyeona.Bean;

public class Bean_payment_paylist {
    String store_sName, oInsertDate;
    int oSum;

    public Bean_payment_paylist(String store_sName, String oInsertDate, int oSum) {
        this.store_sName = store_sName;
        this.oInsertDate = oInsertDate;
        this.oSum = oSum;
    }

    public Bean_payment_paylist() {

    }

    public String getStore_sName() {
        return store_sName;
    }

    public void setStore_sName(String store_sName) {
        this.store_sName = store_sName;
    }

    public String getoInsertDate() {
        return oInsertDate;
    }

    public void setoInsertDate(String oInsertDate) {
        this.oInsertDate = oInsertDate;
    }

    public int getoSum() {
        return oSum;
    }

    public void setoSum(int oSum) {
        this.oSum = oSum;
    }
}
