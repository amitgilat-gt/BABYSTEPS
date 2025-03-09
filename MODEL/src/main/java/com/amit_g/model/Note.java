package com.amit_g.model;

import java.util.Objects;

public class Note{
    private String babyId;
    private long date;
    private String note;

    public Note() {
    }

    public Note(String babyId, long date, String note) {
        this.babyId = babyId;
        this.date = date;
        this.note = note;
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

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Note)) return false;
        Note note1 = (Note) o;
        return date == note1.date && Objects.equals(babyId, note1.babyId) && Objects.equals(note, note1.note);
    }

}
