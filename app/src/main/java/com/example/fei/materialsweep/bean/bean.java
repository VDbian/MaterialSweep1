package com.example.fei.materialsweep.bean;

/**
 * Created by fei on 2017/7/27.
 */

public class bean {
    private String code;
    private String msg;
    private String output;

    public bean() {
    }

    public bean(String code, String msg, String output) {
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

    public String getOutput() {
        return output;
    }

    public void setOutput(String output) {
        this.output = output;
    }

    @Override
    public String toString() {
        return "bean{" +
                "code='" + code + '\'' +
                ", msg='" + msg + '\'' +
                ", output='" + output + '\'' +
                '}';
    }
}
