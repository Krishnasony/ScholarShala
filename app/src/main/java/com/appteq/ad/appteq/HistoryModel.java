package com.appteq.ad.appteq;

public class HistoryModel {
  String amount,date,exp_date;

    public HistoryModel(String amount, String date, String exp_date) {
        this.amount = amount;
        this.date = date;
        this.exp_date = exp_date;

    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public HistoryModel() {
    }

    public String getExp_date() {
        return exp_date;
    }

    public void setExp_date(String exp_date) {
        this.exp_date = exp_date;
    }
}
