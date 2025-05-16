package com.amit_g.model;

import com.amit_g.model.BASE.BaseEntity;

import java.time.LocalTime;
import java.util.Objects;

public class LastActivity extends BaseEntity {
    private String babyId;
    private long date;
    private Action action;
    private long time;
    private String details;

    public LastActivity() {
    }

    public LastActivity(String babyId, long date, long time, String details) {
        this.babyId = babyId;
        this.date = date;
        this.time = time;
        this.details = details;
    }

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
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

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LastActivity that = (LastActivity) o;
        return date == that.date && Objects.equals(babyId, that.babyId) && action == that.action && Objects.equals(time, that.time) && Objects.equals(details, that.details);
    }

}
