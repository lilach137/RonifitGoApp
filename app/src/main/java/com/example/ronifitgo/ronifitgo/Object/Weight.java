package com.example.ronifitgo.ronifitgo.Object;

import java.util.UUID;

public class Weight {

    private float pFat;
    private float pMuscle;
    private float weight;
    private Date date;
    private String weightId;

    public Weight(float pFat, float pMuscle, float weight, Date date) {
        this.pFat = pFat;
        this.pMuscle = pMuscle;
        this.weight = weight;
        this.date = date;
        this.weightId = UUID.randomUUID().toString();
    }

    public Weight() {
    }

    public float getpFat() {
        return pFat;
    }

    public Weight setpFat(float pFat) {
        this.pFat = pFat;
        return this;
    }

    public float getpMuscle() {
        return pMuscle;
    }

    public Weight setpMuscle(float pMuscle) {
        this.pMuscle = pMuscle;
        return this;
    }

    public float getWeight() {
        return weight;
    }

    public Weight setWeight(float weight) {
        this.weight = weight;
        return this;
    }

    public Date getDate() {
        return date;
    }

    public Weight setDate(Date date) {
        this.date = date;
        return this;
    }

    public String getWeightId() {
        return weightId;
    }

    public Weight setWeightId(String weightId) {
        this.weightId = weightId;
        return this;
    }
}
