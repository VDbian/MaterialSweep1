package com.example.fei.materialsweep.bean;

import java.util.List;

/**
 * Created by fei on 2017/7/12.
 */

public class OutBean {

    //门店名称
    private String org_name;
    //医生
    private String doc_name;
    //出库人
    private String out_username;
    //出库时间
    private String out_time;
    //子集
    private List<OutChildBean> items;

    public OutBean() {
    }

    public OutBean(String org_name, String doc_name, String out_username, String out_time, List<OutChildBean> items) {
        this.org_name = org_name;
        this.doc_name = doc_name;
        this.out_username = out_username;
        this.out_time = out_time;
        this.items = items;
    }

    public String getOrg_name() {
        return org_name;
    }

    public void setOrg_name(String org_name) {
        this.org_name = org_name;
    }

    public String getDoc_name() {
        return doc_name;
    }

    public void setDoc_name(String doc_name) {
        this.doc_name = doc_name;
    }

    public String getOut_username() {
        return out_username;
    }

    public void setOut_username(String out_username) {
        this.out_username = out_username;
    }

    public String getOut_time() {
        return out_time;
    }

    public void setOut_time(String out_time) {
        this.out_time = out_time;
    }

    public List<OutChildBean> getItems() {
        return items;
    }

    public void setItems(List<OutChildBean> outChildBeanList) {
        this.items = outChildBeanList;
    }

    @Override
    public String toString() {
        return "OutBean{" +
                "org_name='" + org_name + '\'' +
                ", doc_name='" + doc_name + '\'' +
                ", out_username='" + out_username + '\'' +
                ", out_time='" + out_time + '\'' +
                ", outChildBeanList=" + items +
                '}';
    }
}
