package com.appteq.ad.appteq.model;

import com.appteq.ad.appteq.model.AnswerModel;

import java.util.ArrayList;

public class QuestionModel {
    private int ques_id;
    private String Question;
    private ArrayList<AnswerModel> answerList;
    private boolean is_ml_ans;
    private int test_id;
    private int class_id;
    private int subject_id;
    private int passage_id;
    private boolean isFillUp = false;

    public QuestionModel(String question, ArrayList<AnswerModel> answerList, boolean is_ml_ans , boolean isFillUp) {
        Question = question;
        this.answerList = answerList;
        this.is_ml_ans = is_ml_ans;
        this.isFillUp = isFillUp;
    }

    public QuestionModel() {

    }

    public int getPassage_id() {
        return passage_id;
    }

    public void setPassage_id(int passage_id) {
        this.passage_id = passage_id;
    }

    public boolean getIsFillUp() {
        return isFillUp;
    }

    public void setIsFillUp(boolean isFillUp) {
        this.isFillUp = isFillUp;
    }

    public boolean isIsFillUp() { return isFillUp; }

    public String getQuestion() {
        return Question;
    }

    public void setQuestion(String question) {
        Question = question;
    }

    public ArrayList<AnswerModel> getAnswerList() {
        return answerList;
    }

    public void setAnswerList(ArrayList<AnswerModel> answerList) {
        this.answerList = answerList;
    }

    public int getQues_id() {
        return ques_id;
    }

    public void setQues_id(int ques_id) {
        this.ques_id = ques_id;
    }

    public boolean isIs_ml_ans() {
        return is_ml_ans;
    }

    public void setIs_ml_ans(boolean is_ml_ans) {
        this.is_ml_ans = is_ml_ans;
    }

    public int getTest_id() {
        return test_id;
    }

    public void setTest_id(int test_id) {
        this.test_id = test_id;
    }

    public int getClass_id() {
        return class_id;
    }

    public void setClass_id(int class_id) {
        this.class_id = class_id;
    }

    public int getSubject_id() {
        return subject_id;
    }

    public void setSubject_id(int subject_id) {
        this.subject_id = subject_id;
    }
}
