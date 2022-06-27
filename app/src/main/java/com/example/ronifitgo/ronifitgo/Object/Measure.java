package com.example.ronifitgo.ronifitgo.Object;

import java.util.UUID;

public class Measure {
    private String measureId;
    private String ImgUrl =" ";
    private float armCirc;
    private float bustCirc;
    private float waistCirc;
    private float navelCirc;
    private float buttockCirc;
    private float hipCirc;
    private Date date;
    private String image_cover = "https://firebasestorage.googleapis.com/v0/b/superme-e69d5.appspot.com/o/images%2Fimg_default_list_cover.jpg?alt=media&token=60b8db0a-91fd-4a10-9bc3-47418f158da1";

    public Measure(float armCirc, float bustCirc, float waistCirc, float navelCirc, float buttockCirc, float hipCirc, Date date) {
        this.measureId = UUID.randomUUID().toString();
        this.armCirc = armCirc;
        this.bustCirc = bustCirc;
        this.waistCirc = waistCirc;
        this.navelCirc = navelCirc;
        this.buttockCirc = buttockCirc;
        this.hipCirc = hipCirc;
        this.date = date;
        this.image_cover = "https://firebasestorage.googleapis.com/v0/b/superme-e69d5.appspot.com/o/images%2Fimg_default_list_cover.jpg?alt=media&token=60b8db0a-91fd-4a10-9bc3-47418f158da1";
    }

    public Measure() {
    }


    public float getArmCirc() {
        return armCirc;
    }

    public Measure setArmCirc(float armCirc) {
        this.armCirc = armCirc;
        return this;
    }

    public float getBustCirc() {
        return bustCirc;
    }

    public Measure setBustCirc(float bustCirc) {
        this.bustCirc = bustCirc;
        return this;
    }

    public float getWaistCirc() {
        return waistCirc;
    }

    public Measure setWaistCirc(float waistCirc) {
        this.waistCirc = waistCirc;
        return this;
    }

    public float getNavelCirc() {
        return navelCirc;
    }

    public Measure setNavelCirc(float navelCirc) {
        this.navelCirc = navelCirc;
        return this;
    }

    public float getButtockCirc() {
        return buttockCirc;
    }

    public Measure setButtockCirc(float buttockCirc) {
        this.buttockCirc = buttockCirc;
        return this;
    }

    public float getHipCirc() {
        return hipCirc;
    }

    public Measure setHipCirc(float hipCirc) {
        this.hipCirc = hipCirc;
        return this;
    }

    public Date getDate() {
        return date;
    }

    public Measure setDate(Date date) {
        this.date = date;
        return this;
    }

    public String getMeasureId() {
        return measureId;
    }

    public Measure setMeasureId(String measureId) {
        this.measureId = measureId;
        return this;
    }

    public String getImage_cover() {
        return image_cover;
    }

    public Measure setImage_cover(String image_cover) {
        this.image_cover = image_cover;
        return this;
    }
}
