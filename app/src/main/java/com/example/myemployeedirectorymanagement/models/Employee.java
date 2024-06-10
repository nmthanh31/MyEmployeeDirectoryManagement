package com.example.myemployeedirectorymanagement.models;

import android.net.Uri;

public class Employee {
    private String idEmployee;

    private String nameEmployee;

    private String position;

    private String email;

    private String phoneNumber;

    private Uri Avatar;

    private String idDepartment;

    public Employee(String idEmployee, String nameEmployee, String position, String email, String phoneNumber, Uri avatar, String idDepartment) {
        this.idEmployee = idEmployee;
        this.nameEmployee = nameEmployee;
        this.position = position;
        this.email = email;
        this.phoneNumber = phoneNumber;
        Avatar = avatar;
        this.idDepartment = idDepartment;
    }

    public String getIdEmployee() {
        return idEmployee;
    }

    public void setIdEmployee(String idEmployee) {
        this.idEmployee = idEmployee;
    }

    public String getNameEmployee() {
        return nameEmployee;
    }

    public void setNameEmployee(String nameEmployee) {
        this.nameEmployee = nameEmployee;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Uri getAvatar() {
        return Avatar;
    }

    public void setAvatar(Uri avatar) {
        Avatar = avatar;
    }

    public String getIdDepartment() {
        return idDepartment;
    }

    public void setIdDepartment(String idDepartment) {
        this.idDepartment = idDepartment;
    }
}
