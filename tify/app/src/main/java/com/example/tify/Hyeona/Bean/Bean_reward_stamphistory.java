package com.example.tify.Hyeona.Bean;

public class Bean_reward_stamphistory {

    int user_uNo;
    String oInsertDate;
    String store_sName;
    int total;

    public Bean_reward_stamphistory(int user_uNo, String oInsertDate, String store_sName, int total) {
        this.user_uNo = user_uNo;
        this.oInsertDate = oInsertDate;
        this.store_sName = store_sName;
        this.total = total;
    }

    public Bean_reward_stamphistory() {

    }

    public int getUser_uNo() {
        return user_uNo;
    }

    public void setUser_uNo(int user_uNo) {
        this.user_uNo = user_uNo;
    }

    public String getoInsertDate() {
        return oInsertDate;
    }

    public void setoInsertDate(String oInsertDate) {
        this.oInsertDate = oInsertDate;
    }

    public String getStore_sName() {
        return store_sName;
    }

    public void setStore_sName(String store_sName) {
        this.store_sName = store_sName;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
