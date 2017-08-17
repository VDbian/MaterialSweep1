package com.example.fei.materialsweep.bean;

import java.util.List;

/**
 * Created by fei on 2017/7/18.
 */

public class XmlBean<T> {

    private String code;
    private String msg;
    private String check;
    private List<T> output;

    public XmlBean() {
    }

    public XmlBean(String code, String msg, List<T> output) {
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

    public List<T> getOutput() {
        return output;
    }

    public void setOutput(List<T> output) {
        this.output = output;
    }

    public String getCheck() {
        return check;
    }

    public void setCheck(String check) {
        this.check = check;
    }

    @Override
    public String toString() {
        return "XmlBean{" +
                "code='" + code + '\'' +
                ", msg='" + msg + '\'' +
                ", output=" + output +
                '}';
    }
}
