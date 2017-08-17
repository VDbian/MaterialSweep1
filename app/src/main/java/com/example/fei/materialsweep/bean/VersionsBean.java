package com.example.fei.materialsweep.bean;

/**
 * Created by fei on 2017/7/26.
 */

public class VersionsBean {

    private String code;
    private String msg;
    private Versions output;

    public VersionsBean() {
    }

    public VersionsBean(String code, String msg, Versions output) {
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

    public Versions getOutput() {
        return output;
    }

    public void setOutput(Versions output) {
        this.output = output;
    }

    @Override
    public String toString() {
        return "VersionsBean{" +
                "code='" + code + '\'' +
                ", msg='" + msg + '\'' +
                ", output=" + output +
                '}';
    }

}

