package com.amit_g.model;

import java.util.Objects;

public class User {
    private String userName;
    private String Password;
    private String Email;
    private boolean NotificationEnabled;

    public User() {
    }

    public User(String userName, String password, String email, boolean notificationEnabled) {
        this.userName = userName;
        Password = password;
        Email = email;
        NotificationEnabled = notificationEnabled;
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

    public boolean isNotificationEnabled() {
        return NotificationEnabled;
    }

    public void setNotificationEnabled(boolean notificationEnabled) {
        NotificationEnabled = notificationEnabled;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return NotificationEnabled == user.NotificationEnabled && Objects.equals(userName, user.userName) && Objects.equals(Password, user.Password) && Objects.equals(Email, user.Email);
    }

}
