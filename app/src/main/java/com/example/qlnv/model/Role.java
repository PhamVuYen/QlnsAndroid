package com.example.qlnv.model;

public enum Role {

    Admin("ADMIN"),
    Manager("Trưởng phòng"),
    SubManager("Phó phòng"),
    Employee("Nhân viên");
    private String cv;

    Role(String cv) {
        this.cv = cv;
    }

    public String getRole() {
        return this.cv;
    }
}
