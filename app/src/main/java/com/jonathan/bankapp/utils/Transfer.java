package com.jonathan.bankapp.utils;

public class Transfer {

    public int id;
    public String type, amount, description, date;
    public boolean estate;

    public Transfer() {}

    public Transfer(int id, String type, String amount, String description, String date, boolean estate) {
        this.id = id;
        this.type = type;
        this.amount = amount;
        this.description = description;
        this.date = date;
        this.estate = estate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public boolean isState() {
        return estate;
    }

    public void setEstate(boolean estate) {
        this.estate = estate;
    }
}
