package com.example.qlnv.model;

public enum Status {
    Processing("Đang làm"),
    Finish("Đã Hoàn thành"),
    Late("Quá hạn");

    public final String label;

    private Status(String label) {
        this.label = label;
    }
}
