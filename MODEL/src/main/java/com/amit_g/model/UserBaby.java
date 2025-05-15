package com.amit_g.model;

import com.amit_g.model.BASE.BaseEntity;

import java.util.Objects;

public class UserBaby extends BaseEntity {
    private String userId;
    private String babyId;

    public UserBaby() {
    }

    public UserBaby(String userId, String babyId) {
        this.userId = userId;
        this.babyId = babyId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getBabyId() {
        return babyId;
    }

    public void setBabyId(String babyId) {
        this.babyId = babyId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserBaby)) return false;
        UserBaby userBaby = (UserBaby) o;
        return Objects.equals(userId, userBaby.userId) && Objects.equals(babyId, userBaby.babyId);
    }

}
