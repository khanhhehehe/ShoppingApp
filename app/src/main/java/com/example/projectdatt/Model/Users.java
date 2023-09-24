package com.example.projectdatt.Model;

public class Users {
    private String id;
    private String name;
    private String pass;
    private String phone;
    private boolean role;
    private boolean isBan;
    private String image;

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

    public Users(String name, String phone,String pass, boolean role, boolean isBan) {
        this.name = name;
        this.pass = pass;
        this.role = role;
        this.phone = phone;
        this.isBan = isBan;
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

    public boolean isBan() {
        return isBan;
    }

    public void setBan(boolean ban) {
        isBan = ban;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
