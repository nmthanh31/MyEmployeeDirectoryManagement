package com.example.myemployeedirectorymanagement.models;

import android.net.Uri;

public class Department {
    private String idDepartment;

    private String nameDepartment;

    private String email;

    private String website;

    private Uri logo;

    private String address;

    private String phoneNumber;

    public Department(String idDepartment, String nameDepartment, String email, String website, Uri logo, String address, String phoneNumber) {
        this.idDepartment = idDepartment;
        this.nameDepartment = nameDepartment;
        this.email = email;
        this.website = website;
        this.logo = logo;
        this.address = address;
        this.phoneNumber = phoneNumber;
    }

    public String getIdDepartment() {
        return idDepartment;
    }

    public void setIdDepartment(String idDepartment) {
        this.idDepartment = idDepartment;
    }

    public String getNameDepartment() {
        return nameDepartment;
    }

    public void setNameDepartment(String nameDepartment) {
        this.nameDepartment = nameDepartment;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public Uri getLogo() {
        return logo;
    }

    public void setLogo(Uri logo) {
        this.logo = logo;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
