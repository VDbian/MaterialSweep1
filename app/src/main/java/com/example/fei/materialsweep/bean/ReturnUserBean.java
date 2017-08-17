package com.example.fei.materialsweep.bean;

/**
 * Created by fei on 2017/7/19.
 */

public class ReturnUserBean {

    private String code;
    private String msg;
    private UserBean output;

    public ReturnUserBean() {
    }

    public ReturnUserBean(String code, String msg, UserBean output) {
        this.code = code;
        this.msg = msg;
        this.output = output;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public UserBean getOutput() {
        return output;
    }

    public void setOutput(UserBean output) {
        this.output = output;
    }

    @Override
    public String toString() {
        return "ReturnUserBean{" +
                "code='" + code + '\'' +
                ", msg='" + msg + '\'' +
                ", output='" + output + '\'' +
                '}';
    }
}
