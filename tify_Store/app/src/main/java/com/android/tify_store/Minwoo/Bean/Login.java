package com.android.tify_store.Minwoo.Bean;

public class Login {

    int skSeqNo;
    int cnt;
    int skStatus;

    public Login(int skSeqNo, int cnt, int skStatus) {
        this.skSeqNo = skSeqNo;
        this.cnt = cnt;
        this.skStatus = skStatus;
    }

    public int getSkSeqNo() {
        return skSeqNo;
    }

    public void setSkSeqNo(int skSeqNo) {
        this.skSeqNo = skSeqNo;
    }

    public int getCnt() {
        return cnt;
    }

    public void setCnt(int cnt) {
        this.cnt = cnt;
    }

    public int getSkStatus() {
        return skStatus;
    }

    public void setSkStatus(int skStatus) {
        this.skStatus = skStatus;
    }
}
