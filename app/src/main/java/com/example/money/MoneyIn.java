package com.example.money;

import java.util.ArrayList;
import java.util.List;

public class MoneyIn {
    private String name;
    private String date;
    private int money;
    private String category;
    private String note;
    private String month;
    private String time;

    public MoneyIn() {

    }

    public MoneyIn(String name, String date, int money, String category, String note, String month, String time) {
        this.name = name;
        this.date = date;
        this.money = money;
        this.category = category;
        this.note = note;
        this.month = month;
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}