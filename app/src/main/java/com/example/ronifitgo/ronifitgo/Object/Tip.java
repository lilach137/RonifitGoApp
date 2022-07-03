package com.example.ronifitgo.ronifitgo.Object;

import java.util.UUID;

public class Tip {

    private String description;
    private String tipId;

    public Tip(String description) {
        this.description = description;
        this.tipId = UUID.randomUUID().toString();
    }

    public Tip() {
    }

    public String getDescription() {
        return description;
    }

    public Tip setDescription(String description) {
        this.description = description;
        return this;
    }

    public String getTipId() {
        return tipId;
    }

    public Tip setTipId(String tipId) {
        this.tipId = tipId;
        return this;
    }
}
