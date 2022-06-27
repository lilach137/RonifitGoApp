package com.example.ronifitgo.ronifitgo.utils;

import com.example.ronifitgo.ronifitgo.Object.Measure;
import com.example.ronifitgo.ronifitgo.Object.Weight;

import java.util.ArrayList;

public class MyDB {

    private ArrayList<Weight> myWeights;
    private ArrayList<Measure> myMeasures;



    public ArrayList<Weight> getMyWeights() {
        return myWeights;
    }

    public void setMyWeights(ArrayList<Weight> myWeights) {
        this.myWeights = myWeights;
    }

    public ArrayList<Measure> getMyMeasures() {
        return myMeasures;
    }

    public void setMyMeasures(ArrayList<Measure> myMeasures) {
        this.myMeasures = myMeasures;
    }
}
