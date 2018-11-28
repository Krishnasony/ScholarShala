package com.appteq.ad.appteq.model;

import com.google.gson.internal.bind.ArrayTypeAdapter;

import java.util.ArrayList;
import java.util.HashMap;

public class UserModel {
    private int userid;
    private String username;
    private String user_fullname;
    private String dob;
    private String passwd;
    private int user_class;
    private String user_class_name;
    private ArrayList<SubjectModel> subjects;
    private ArrayList<ChapterModel> chapters;
    private String parent_name;
    private String parent_phone_no;
    private String phone_no;
    private String login_token;
    private String user_login_current_time;
    private String user_last_login;
    private String user_class_med;
    private String userimage;

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUser_fullname() {
        return user_fullname;
    }

    public void setUser_fullname(String user_fullname) {
        this.user_fullname = user_fullname;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }

    public int getUser_class() {
        return user_class;
    }

    public void setUser_class(int user_class) {
        this.user_class = user_class;
    }

    public ArrayList<SubjectModel> getSubjects() {
        return subjects;
    }

    public void setSubjects(ArrayList<SubjectModel> subjects) {
        this.subjects = subjects;
    }

    public String getParent_name() {
        return parent_name;
    }

    public void setParent_name(String parent_name) {
        this.parent_name = parent_name;
    }

    public String getParent_phone_no() {
        return parent_phone_no;
    }

    public void setParent_phone_no(String parent_phone_no) {
        this.parent_phone_no = parent_phone_no;
    }

    public String getPhone_no() {
        return phone_no;
    }

    public void setPhone_no(String phone_no) {
        this.phone_no = phone_no;
    }

    public String getLogin_token() {
        return login_token;
    }

    public void setLogin_token(String login_token) {
        this.login_token = login_token;
    }

    public String getUser_login_current_time() {
        return user_login_current_time;
    }

    public void setUser_login_current_time(String user_login_current_time) {
        this.user_login_current_time = user_login_current_time;
    }

    public String getUser_last_login() {
        return user_last_login;
    }

    public void setUser_last_login(String user_last_login) {
        this.user_last_login = user_last_login;
    }

    public String getUser_class_med() {
        return user_class_med;
    }

    public void setUser_class_med(String user_class_med) {
        this.user_class_med = user_class_med;
    }

    public String getUser_class_name() {
        return user_class_name;
    }

    public void setUser_class_name(String user_class_name) {
        this.user_class_name = user_class_name;
    }

    public ArrayList<ChapterModel> getChapters() {
        return chapters;
    }

    public void setChapters(ArrayList<ChapterModel> chapters) {
        this.chapters = chapters;
    }

    public String getUserimage() {
        return userimage;
    }

    public void setUserimage(String userimage) {
        this.userimage = userimage;
    }
}
