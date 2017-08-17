package com.example.fei.materialsweep.bean;

/**
 * Created by fei on 2017/7/12.
 */

public class OutChildBean {

    //材料名称
    private String tarr_name;
    //规格
    private String spec;
    //单位
    private String unit;
    //批次
    private String batch;
    //出库数量
    private String out_count;

    public OutChildBean() {
    }

    public OutChildBean(String tarr_name, String spec, String unit, String batch, String out_count) {
        this.tarr_name = tarr_name;
        this.spec = spec;
        this.unit = unit;
        this.batch = batch;
        this.out_count = out_count;
    }

    public String getTarr_name() {
        return tarr_name;
    }

    public void setTarr_name(String tarr_name) {
        this.tarr_name = tarr_name;
    }

    public String getSpec() {
        return spec;
    }

    public void setSpec(String spec) {
        this.spec = spec;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getBatch() {
        return batch;
    }

    public void setBatch(String batch) {
        this.batch = batch;
    }

    public String getOut_count() {
        return out_count;
    }

    public void setOut_count(String out_count) {
        this.out_count = out_count;
    }

    @Override
    public String toString() {
        return "OutChildBean{" +
                "tarr_name='" + tarr_name + '\'' +
                ", spec='" + spec + '\'' +
                ", unit='" + unit + '\'' +
                ", batch='" + batch + '\'' +
                ", out_count='" + out_count + '\'' +
                '}';
    }
}
