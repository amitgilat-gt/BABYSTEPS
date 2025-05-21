package com.amit_g.model;

import com.amit_g.model.BASE.BaseEntity;

import java.io.Serializable;
import java.util.Objects;

public class User extends BaseEntity implements Serializable {
    private String userName;
    private String Password;
    private String Email;


    public User() {
    }

    public User(String userName, String password, String email) {
        this.userName = userName;
        Password = password;
        Email = email;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        User user = (User) o;
        return Objects.equals(userName, user.userName) && Objects.equals(Password, user.Password) && Objects.equals(Email, user.Email);
    }

}
