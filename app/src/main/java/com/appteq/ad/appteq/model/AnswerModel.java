package com.appteq.ad.appteq.model;

public class AnswerModel {

    private String answer;
    private int id;
    private boolean isSelected;
    private boolean is_right;
    private boolean is_url;
    private int ques_id;
    public AnswerModel() {

    }
    public AnswerModel(int id,String answer,boolean is_right,boolean is_url){
        this.id = id;
        this.answer = answer;
        this.is_right = is_right;
        this.is_url = is_url;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isIs_right() {
        return is_right;
    }

    public void setIs_right(boolean is_right) {
        this.is_right = is_right;
    }

    public boolean isIs_url() {
        return is_url;
    }

    public void setIs_url(boolean is_url) {
        this.is_url = is_url;
    }

    public int getQues_id() {
        return ques_id;
    }

    public void setQues_id(int ques_id) {
        this.ques_id = ques_id;
    }
}
