package com.example.fei.materialsweep.bean;

/**
 * Created by fei on 2017/7/19.
 */

public class ReturnTarrBean {
    private String code;
    private String msg;
    private String check;
    private TarrBean output;

    public ReturnTarrBean() {
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

    public String getCheck() {
        return check;
    }

    public void setCheck(String check) {
        this.check = check;
    }

    public TarrBean getOutput() {
        return output;
    }

    public void setOutput(TarrBean output) {
        this.output = output;
    }

    @Override
    public String toString() {
        return "ReturnTarrBean{" +
                "code='" + code + '\'' +
                ", msg='" + msg + '\'' +
                ", check='" + check + '\'' +
                ", output=" + output +
                '}';
    }
}
