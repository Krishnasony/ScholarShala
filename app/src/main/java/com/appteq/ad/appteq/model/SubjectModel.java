package com.appteq.ad.appteq.model;

public class SubjectModel {
    private int subject_id;
    private String subject_name;
    private int user_id;
    private String subject_desc;
    private int subjectimge;
    private int subjectback;
    private String subtype;
    public int getSubject_id() {
        return subject_id;
    }

    public void setSubject_id(int subject_id) {
        this.subject_id = subject_id;
    }

    public String getSubject_name() {
        return subject_name;
    }

    public void setSubject_name(String subject_name) {
        this.subject_name = subject_name;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getSubject_desc() {
        return subject_desc;
    }

    public void setSubject_desc(String subject_desc) {
        this.subject_desc = subject_desc;
    }

    public int getSubjectimge() {
        return subjectimge;
    }

    public void setSubjectimge(int subjectimge) {
        this.subjectimge = subjectimge;
    }

    public int getSubjectback() {
        return subjectback;
    }

    public void setSubjectback(int subjectback) {
        this.subjectback = subjectback;
    }

    public String getSubtype() {
        return subtype;
    }

    public void setSubtype(String subtype) {
        this.subtype = subtype;
    }
}
