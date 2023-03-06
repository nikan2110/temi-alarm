package com.nikita.doroshenko.japanmeeting.models;

import android.widget.Button;

public class Customer {

    String name;
    int age;
    long phoneNumber;
    int riskLevel;
    Button details;

    public Customer(String name, int age, long phoneNumber, int riskLevel) {
        this.name = name;
        this.age = age;
        this.phoneNumber = phoneNumber;
        this.riskLevel = riskLevel;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public long getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(long phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Button getDetails() {
        return details;
    }

    public void setDetails(Button details) {
        this.details = details;
    }

    public int getRiskLevel() {
        return riskLevel;
    }

    public void setRiskLevel(int riskLevel) {
        this.riskLevel = riskLevel;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", phoneNumber=" + phoneNumber +
                ", riskLevel=" + riskLevel +
                ", details=" + details +
                '}';
    }
}
