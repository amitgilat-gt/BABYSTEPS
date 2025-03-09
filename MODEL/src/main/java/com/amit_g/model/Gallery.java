package com.amit_g.model;

import java.util.Objects;

public class Gallery {
    private String babyId;
    private long date;
    private String picture;


    public Gallery() {
    }

    public Gallery(String babyId, long date, String picture) {
        this.babyId = babyId;
        this.date = date;
        this.picture = picture;
    }

    public String getBabyId() {
        return babyId;
    }

    public void setBabyId(String babyId) {
        this.babyId = babyId;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Gallery)) return false;
        Gallery gallery = (Gallery) o;
        return date == gallery.date && Objects.equals(babyId, gallery.babyId) && Objects.equals(picture, gallery.picture);
    }


}
