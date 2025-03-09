package com.amit_g.model;

import java.time.LocalTime;
import java.util.Objects;

public class LastActivity {
    private String babyId;
    private long date;
    private Action action;
    private LocalTime time;
    private String details;

    public LastActivity() {
    }

    public LastActivity(String babyId, long date, LocalTime time, String details) {
        this.babyId = babyId;
        this.date = date;
        this.time = time;
        this.details = details;
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

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
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
        if (!(o instanceof LastActivity)) return false;
        LastActivity that = (LastActivity) o;
        return date == that.date && Objects.equals(babyId, that.babyId) && Objects.equals(action, that.action) && Objects.equals(time, that.time) && Objects.equals(details, that.details);
    }


}
