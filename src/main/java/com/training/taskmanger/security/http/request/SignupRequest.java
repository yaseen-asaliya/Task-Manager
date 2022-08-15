package com.training.taskmanger.security.http.request;

 
public class SignupRequest {
    private String name;
    private String password;
    private String email;
    private String username;

    public SignupRequest(String name, String password, String email, String username) {
        this.name = name;
        this.password = password;
        this.email = email;
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

}
