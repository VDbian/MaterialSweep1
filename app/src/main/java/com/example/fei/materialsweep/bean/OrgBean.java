package com.example.fei.materialsweep.bean;

/**
 * Created by fei on 2017/7/19.
 */

public class OrgBean {
    private String org_id;
    private String org_name ;

    public OrgBean(String org_id, String org_name) {
        this.org_id = org_id;
        this.org_name = org_name;
    }

    public OrgBean() {
    }

    public String getOrg_name() {
        return org_name;
    }

    public void setOrg_name(String org_name) {
        this.org_name = org_name;
    }

    public String getOrg_id() {
        return org_id;
    }

    public void setOrg_id(String org_id) {
        this.org_id = org_id;
    }

    @Override
    public String toString() {
        return org_name;
    }
}
