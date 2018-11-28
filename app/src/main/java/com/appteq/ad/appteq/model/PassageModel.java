package com.appteq.ad.appteq.model;

public class PassageModel {

    private int passageId;
    private String passageText;
    private int testId;

    public PassageModel(int passageId, String passageText, int testId) {
        this.passageId = passageId;
        this.passageText = passageText;
        this.testId = testId;
    }

    public PassageModel(){

    }

    public int getTestId() {
        return testId;
    }

    public void setTestId(int testId) {
        this.testId = testId;
    }

    public int getPassageId() {
        return passageId;
    }

    public void setPassageId(int passageId) {
        this.passageId = passageId;
    }

    public String getPassageText() {
        return passageText;
    }

    public void setPassageText(String passageText) {
        this.passageText = passageText;
    }

}
