package com.example.tify.Jiseok.Bean;

public class Bean_Login_cjs {
    private String uEmail;
    private String uPayPassword;

    public Bean_Login_cjs() {
    }

    public Bean_Login_cjs(String uEmail, String uPayPassword) {
        this.uEmail = uEmail;
        this.uPayPassword = uPayPassword;
    }

    public String getuEmail() {
        return uEmail;
    }

    public void setuEmail(String uEmail) {
        this.uEmail = uEmail;
    }

    public String getuPayPassword() {
        return uPayPassword;
    }

    public void setuPayPassword(String uPayPassword) {
        this.uPayPassword = uPayPassword;
    }
}


