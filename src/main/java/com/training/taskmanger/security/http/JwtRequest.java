package com.training.taskmanger.security.http;

import java.io.Serializable;

public class JwtRequest implements Serializable {

    private String password;
    private String username;

    public JwtRequest() {
    }

    public JwtRequest(String username,String password) {
        this.password = password;
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
