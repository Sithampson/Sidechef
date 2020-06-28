package com.example.sidechef.Model;

public class User {
    private String Username;
    private String Fullname;
    private String Password;

    public User(String username, String fullname, String password) {
        Username = username;
        Fullname = fullname;
        Password = password;
    }

    public User() {

    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public String getFullname() {
        return Fullname;
    }

    public void setFullname(String fullname) {
        Fullname = fullname;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }
}
