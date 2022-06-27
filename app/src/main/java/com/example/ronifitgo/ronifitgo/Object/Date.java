package com.example.ronifitgo.ronifitgo.Object;

public class Date {

    private int month;
    private int year;
    private int dayOfMonth;

    public Date() {
    }

    public Date(int month, int year, int dayOfMonth) {
        this.month = month;
        this.year = year;
        this.dayOfMonth = dayOfMonth;
    }

    public int getMonth() {
        return month;
    }

    public Date setMonth(int month) {
        this.month = month;
        return this;
    }

    public int getYear() {
        return year;
    }

    public Date setYear(int year) {
        this.year = year;
        return this;
    }

    public int getDayOfMonth() {
        return dayOfMonth;
    }

    public Date setDayOfMonth(int dayOfMonth) {
        this.dayOfMonth = dayOfMonth;
        return this;

    }

    @Override
    public String toString() {
        return dayOfMonth + "/" + month + "/" + year;

    }
}
