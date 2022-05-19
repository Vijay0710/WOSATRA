package com.example.iot_project;

public class User {
    private String email;
    private String Password;
    public User(){}
    public User(String email,String Password) {
        this.email = email;
        this.Password = Password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }
}
