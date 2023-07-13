package com.example.tkshop.Model;

public class Users {

    private String name;
    private String pass;
    private String phone;
    private String address;
    private int role; // 0 admin , 1 customer
    private String avatar;

    public Users() {
    }

    public Users(String name, String pass, String address, int role, String avatar) {
        this.name = name;
        this.pass = pass;
        this.address = address;
        this.role = role;
        this.avatar = avatar;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
