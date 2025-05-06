package com.amit_g.model;

import com.amit_g.model.BASE.BaseEntity;

import java.io.Serializable;
import java.util.Objects;

public class Baby extends BaseEntity implements Serializable {
    private String name;
    private long birthDate;
    private Gender gender;

    public Baby() {
    }

    public Baby(String name, long birthDate) {
        this.name = name;
        this.birthDate = birthDate;
    }

    public Gender getGender() {
        return gender;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(long birthDate) {
        this.birthDate = birthDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Baby)) return false;
        if (!super.equals(o)) return false;
        Baby baby = (Baby) o;
        return birthDate == baby.birthDate && Objects.equals(name, baby.name) && gender == baby.gender;
    }


    public void setGender(Gender gender) {
        this.gender = gender;
    }
}
