package com.example.myapplication;

public class Student {
    String std_id,Email, tokens;
    public Student() {
    }

    public Student(String std_id, String email, String tokennum) {
        this.std_id = std_id;
        Email = email;
        this.tokens = tokennum;
    }

    public String getStd_id() {
        return std_id;
    }

    public void setStd_id(String std_id) {
        this.std_id = std_id;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getTokens() {
        return tokens;
    }

    public void setTokens(String tokennum) {
        this.tokens = tokennum;
    }
}
