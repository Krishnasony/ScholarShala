package com.appteq.ad.appteq.model;

public class PaymentModel {
  private int user_id;
  private String amount;
  private String payment_date;
  private String payment_txn;
  private String payment_bank_txn;
  private String status;
  private String currency;
  private String payment_valid_date;
  private String payment_package;
  private String payment_mode;
  private String transactionname;

    public String getTransactionname() {
        return transactionname;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getPayment_date() {
        return payment_date;
    }

    public void setPayment_date(String payment_date) {
        this.payment_date = payment_date;
    }

    public String getPayment_txn() {
        return payment_txn;
    }

    public void setPayment_txn(String payment_txn) {
        this.payment_txn = payment_txn;
    }

    public String getPayment_bank_txn() {
        return payment_bank_txn;
    }

    public void setPayment_bank_txn(String payment_bank_txn) {
        this.payment_bank_txn = payment_bank_txn;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getPayment_valid_date() {
        return payment_valid_date;
    }

    public void setPayment_valid_date(String payment_valid_date) {
        this.payment_valid_date = payment_valid_date;
    }

    public String getPayment_package() {
        return payment_package;
    }

    public void setPayment_package(String payment_package) {
        this.payment_package = payment_package;
    }

    public String getPayment_mode() {
        return payment_mode;
    }

    public void setPayment_mode(String payment_mode) {
        this.payment_mode = payment_mode;
    }

    public void setTransactionname(String transactionname) {
        this.transactionname = transactionname;
    }
}
