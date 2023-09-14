package com.example.projectdatt.Model;

public class Users {
    private String id;
    private String name;
    private String pass;
    private String phone;
    private boolean role;

    public String getName() {
        return name;
    }

    public String getPass() {
        return pass;
    }

    public boolean isRole() {
        return role;
    }

    public Users() {
    }

    public Users(String name, String phone,String pass, boolean role) {
        this.name = name;
        this.pass = pass;
        this.role = role;
        this.phone = phone;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public void setRole(boolean role) {
        this.role = role;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
