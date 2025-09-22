package com.MongoSpring.MongoSpring.Model;

public class StudentDto {
    private String name;
    private String address;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public StudentDto(String name, String address) {
        this.name = name;
        this.address = address;
    }
}
