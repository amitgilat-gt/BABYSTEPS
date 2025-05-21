package com.amit_g.model;

import com.amit_g.model.BASE.BaseEntity;

import java.io.Serializable;
import java.util.Objects;

public class Baby extends BaseEntity implements Serializable {
    private String name;
    private String idB;
    private long birthDate;
    private Gender gender;
    private String Password;

    public Baby() {
    }

    public Baby(String name, String id, long birthDate, String password) {
        this.name = name;
        this.idB = id;
        this.birthDate = birthDate;
        Password = password;
    }

    public Baby(String name, long birthDate) {
        this.name = name;
        this.birthDate = birthDate;
    }

    public void setId(String id) {
        this.idB = id;
    }

    public Baby(String name, long birthDate, String password) {
        this.name = name;
        this.birthDate = birthDate;
        Password = password;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
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

    public String getIdB() {
        return idB;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Baby baby = (Baby) o;
        return birthDate == baby.birthDate && Objects.equals(name, baby.name) && Objects.equals(id, baby.id) && gender == baby.gender && Objects.equals(Password, baby.Password);
    }


    public void setGender(Gender gender) {
        this.gender = gender;
    }
}
