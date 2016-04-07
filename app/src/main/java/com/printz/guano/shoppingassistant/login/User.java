package com.printz.guano.shoppingassistant.login;

public class User {

    private int uId;
    private String mUserName;
    private String mPassword;

    public User(int uId, String userName, String password) {
        this.uId = uId;
        this.mUserName = userName;
        this.mPassword = password;
    }

    public int getuId() {
        return uId;
    }

    public void setuId(int uId) {
        this.uId = uId;
    }

    public String getUserName() {
        return mUserName;
    }

    public void setUserName(String userName) {
        this.mUserName = userName;
    }

    public String getPassword() {
        return mPassword;
    }

    public void setPassword(String password) {
        this.mPassword = password;
    }
}
