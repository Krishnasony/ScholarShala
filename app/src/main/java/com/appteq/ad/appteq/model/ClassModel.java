package com.appteq.ad.appteq.model;

import java.util.HashMap;

public class ClassModel {
    private int class_id;
    private String class_name;
    private HashMap<Integer,String> subjects;
    public int getClass_id() {
        return class_id;
    }

    public void setClass_id(int class_id) {
        this.class_id = class_id;
    }

    public String getClass_name() {
        return class_name;
    }

    public void setClass_name(String class_name) {
        this.class_name = class_name;
    }

    public HashMap<Integer, String> getSubjects() {
        return subjects;
    }

    public void setSubjects(HashMap<Integer, String> subjects) {
        this.subjects = subjects;
    }
}
