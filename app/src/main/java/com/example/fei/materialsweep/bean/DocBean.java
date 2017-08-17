package com.example.fei.materialsweep.bean;

/**
 * Created by fei on 2017/7/19.
 */

public class DocBean {
    private String doc_id;
    private  String doc_name ;

    public DocBean() {
    }

    public String getDoc_id() {
        return doc_id;
    }

    public void setDoc_id(String doc_id) {
        this.doc_id = doc_id;
    }

    public String getDoc_name() {
        return doc_name;
    }

    public void setDoc_name(String doc_name) {
        this.doc_name = doc_name;
    }

    @Override
    public String toString() {
        return doc_name;
    }
}
