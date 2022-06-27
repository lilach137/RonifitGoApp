package com.example.ronifitgo.ronifitgo.Object;


import java.util.ArrayList;

public class User {

    private String userId;
    private String phoneNumber;
    private String profileImgUrl = "https://firebasestorage.googleapis.com/v0/b/superme-e69d5.appspot.com/o/images%2Fimg_profile_pic.JPG?alt=media&token=5970cec0-9663-4ddd-9395-ef2791ad938d"; //default pic
    private int age;
    private float firstWeight;
    private float currentWeight;
    private float height;
    private String name;
    private float goal;
    private int gender; //1 for woman 2 for man
    private ArrayList<String> myMeasures;
    private ArrayList<String> myWeights;

    public User() {
    }


    public User(String userId, String phoneNumber, int age, float firstWeight, float height, String name, int gender, float goal) {
        this.userId = userId;
        this.phoneNumber = phoneNumber;
        this.profileImgUrl = "https://firebasestorage.googleapis.com/v0/b/superme-e69d5.appspot.com/o/images%2Fimg_profile_pic.JPG?alt=media&token=5970cec0-9663-4ddd-9395-ef2791ad938d";;
        this.age = age;
        this.firstWeight = firstWeight;
        this.currentWeight = firstWeight;
        this.height = height;
        this.name = name;
        this.gender = gender;
        this.goal = goal;
        this.myWeights = new ArrayList<>();
        this.myMeasures = new ArrayList<>();
    }

    public String getUserId() {
        return userId;
    }


    public String getPhoneNumber() {
        return phoneNumber;
    }

    public User setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }

    public String getProfileImgUrl() {
        return profileImgUrl;
    }

    public User setProfileImgUrl(String profileImgUrl) {
        this.profileImgUrl = profileImgUrl;
        return this;
    }
    public int getAge() {
        return age;
    }

    public User setAge(int age) {
        this.age = age;
        return this;
    }

    public float getFirstWeight() {
        return firstWeight;
    }

    public User setFirstWeight(float firstWeight) {
        this.firstWeight = firstWeight;
        return this;
    }

    public float getCurrentWeight() {
        return currentWeight;
    }

    public User setCurrentWeight(float currentWeight) {
        this.currentWeight = currentWeight;
        return this;
    }

    public float getHeight() {
        return height;
    }

    public User setHeight(float height) {
        this.height = height;
        return this;
    }

    public int getGender() {
        return gender;
    }

    public User setGender(int gender) {
        this.gender = gender;
        return this;
    }

    public String getName() {
        return name;
    }

    public User setName(String name) {
        this.name = name;
        return this;
    }

    public ArrayList<String> getMyMeasures() {
        return myMeasures;
    }

    public User setMyMeasures(ArrayList<String> myMeasures) {
        this.myMeasures = myMeasures;
        return this;
    }

    public boolean addToMeasuresUid(String uidToAdd){
        return this.myMeasures.add(uidToAdd);
    }

    public boolean removeFromMeasuresUids(String mUid) {
        return this.myMeasures.remove(mUid);
    }

    public boolean addToWeightsUid(String uidToAdd){
        return this.myWeights.add(uidToAdd);
    }

    public boolean removeFromWeightsUids(String wUid) {
        return this.myWeights.remove(wUid);
    }

    public float getGoal() {
        return goal;
    }

    public User setGoal(float goal) {
        this.goal = goal;
        return this;
    }

    public ArrayList<String> getMyWeights() {
        return myWeights;
    }

    public User setMyWeights(ArrayList<String> myWeights) {
        this.myWeights = myWeights;
        return this;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId='" + userId + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", profileImgUrl='" + profileImgUrl + '\'' +
                ", age=" + age +
                ", weight=" + firstWeight +
                ", height=" + height +
                ", name='" + name + '\'' +
                ", gender=" + gender +
                '}';
    }
}