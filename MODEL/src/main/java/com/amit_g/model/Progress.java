package com.amit_g.model;

import com.amit_g.model.BASE.BaseEntity;

import java.util.Objects;

public class Progress extends BaseEntity {
    private String babyId;
    private long date;
    private double weight;
    private double height;

    public Progress() {
    }

    public Progress(String babyId, long date, double weight, double height) {
        babyId = babyId;
        this.date = date;
        this.weight = weight;
        this.height = height;
    }

    public String getBabyId() {
        return babyId;
    }

    public void setBabyId(String babyId) {
        babyId = babyId;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Progress)) return false;
        Progress progress = (Progress) o;
        return date == progress.date && Double.compare(weight, progress.weight) == 0 && Double.compare(height, progress.height) == 0 && Objects.equals(babyId, progress.babyId);
    }

}
