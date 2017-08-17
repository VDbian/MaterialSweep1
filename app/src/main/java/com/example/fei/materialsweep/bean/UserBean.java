package com.example.fei.materialsweep.bean;

/**
 * Created by fei on 2017/7/20.
 */

public class UserBean  {
    private String userid;
    private String username;

    public UserBean() {
    }

    public UserBean(String username, String userid) {
        this.username = username;
        this.userid = userid;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return "UserBean{" +
                "userid='" + userid + '\'' +
                ", username='" + username + '\'' +
                '}';
    }
}
