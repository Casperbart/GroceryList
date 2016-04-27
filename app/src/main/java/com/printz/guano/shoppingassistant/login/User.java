package com.printz.guano.shoppingassistant.login;

class User {

    private int mUserId;
    private String mUserName;

    public User(int mUserId, String userName) {
        this.mUserId = mUserId;
        this.mUserName = userName;
    }

    public int getUserId() {
        return mUserId;
    }

    public void setUserId(int userId) {
        this.mUserId = userId;
    }

    public String getUserName() {
        return mUserName;
    }

    public void setUserName(String userName) {
        this.mUserName = userName;
    }
}
