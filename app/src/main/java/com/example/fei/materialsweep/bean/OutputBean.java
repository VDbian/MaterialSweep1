package com.example.fei.materialsweep.bean;

/**
 * Created by fei on 2017/7/19.
 */

public class OutputBean {
    //材料编码
    private String tarr_code;
    //材料批次编号
    private String tarr_id;
    //材料名称
    private String tarr_name;
    //类型
    private int tarr_type;
    //材料单位
    private String unit;
    //规格
    private String spec;
    //单价
    private double price;
    //批次
    private String batch;
    //属性  材料 1，药品 2
    private int tarr_attr;

    public OutputBean() {
    }

    public String getTarr_code() {
        return tarr_code;
    }

    public void setTarr_code(String tarr_code) {
        this.tarr_code = tarr_code;
    }

    public String getTarr_id() {
        return tarr_id;
    }

    public void setTarr_id(String tarr_id) {
        this.tarr_id = tarr_id;
    }

    public String getTarr_name() {
        return tarr_name;
    }

    public void setTarr_name(String tarr_name) {
        this.tarr_name = tarr_name;
    }

    public int getTarr_type() {
        return tarr_type;
    }

    public void setTarr_type(int tarr_type) {
        this.tarr_type = tarr_type;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getSpec() {
        return spec;
    }

    public void setSpec(String spec) {
        this.spec = spec;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getBatch() {
        return batch;
    }

    public void setBatch(String batch) {
        this.batch = batch;
    }

    public int getTarr_attr() {
        return tarr_attr;
    }

    public void setTarr_attr(int tarr_attr) {
        this.tarr_attr = tarr_attr;
    }

    @Override
    public String toString() {
        return "OutputBean{" +
                "tarr_code='" + tarr_code + '\'' +
                ", tarr_id='" + tarr_id + '\'' +
                ", tarr_name='" + tarr_name + '\'' +
                ", tarr_type=" + tarr_type +
                ", unit='" + unit + '\'' +
                ", spec='" + spec + '\'' +
                ", price=" + price +
                ", batch='" + batch + '\'' +
                ", tarr_attr=" + tarr_attr +
                '}';
    }
}
