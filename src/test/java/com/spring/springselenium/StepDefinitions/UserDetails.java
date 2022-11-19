package com.spring.springselenium.StepDefinitions;


public class UserDetails {
    protected String username;
    protected String password;

    public UserDetails(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
