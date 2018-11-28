package com.appteq.ad.appteq.model;

import java.util.ArrayList;
import java.util.HashMap;

public class TestModel {
    private int class_id;
    private String test_name;
    private int test_id;
    private int subject_id;
    private String start_time;
    private String end_time;
    private HashMap<Integer,QuestionModel> ques;
    private String test_ins;
    private ArrayList<PassageModel> passageModels;
    private HashMap<Integer, PassageModel> pmap;
    private int score;
    private int is_active;
    private boolean haspassage;
    private int chapter_id;

    public TestModel() {
    }
    public ArrayList<PassageModel> getPassageModels() {
        return passageModels;
    }

    public void setPassageModels(ArrayList<PassageModel> passageModels) {
        this.passageModels = passageModels;
    }
    public int getScore(){
        return score;
    }
    public String getTest_name() {
        return test_name;
    }
    public void setTest_name(String test_name) {
        this.test_name = test_name;
    }

    public int getClass_id() {
        return class_id;
    }

    public void setClass_id(int class_id) {
        this.class_id = class_id;
    }

    public int getTest_id() {
        return test_id;
    }

    public void setTest_id(int test_id) {
        this.test_id = test_id;
    }

    public int getSubject_id() {
        return subject_id;
    }

    public void setSubject_id(int subject_id) {
        this.subject_id = subject_id;
    }

    public HashMap<Integer,QuestionModel> getQues() {
        return ques;
    }

    public void setQues(HashMap<Integer,QuestionModel> ques) {
        this.ques = ques;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getTest_ins() {
        return test_ins;
    }

    public void setTest_ins(String test_ins) {
        this.test_ins = test_ins;
    }

    public int getIs_active() {
        return is_active;
    }

    public void setIs_active(int is_active) {
        this.is_active = is_active;
    }

    public HashMap<Integer, PassageModel> getPmap() {
        return pmap;
    }

    public void setPmap(HashMap<Integer, PassageModel> pmap) {
        this.pmap = pmap;
    }

    public boolean isHaspassage() {
        return haspassage;
    }

    public int getChapter_id() {
        return chapter_id;
    }

    public void setChapter_id(int chapter_id) {
        this.chapter_id = chapter_id;
    }

    public void setHaspassage(boolean haspassage) {
        this.haspassage = haspassage;
    }

}
