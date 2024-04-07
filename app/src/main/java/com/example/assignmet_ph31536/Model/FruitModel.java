package com.example.assignmet_ph31536.Model;

public class FruitModel {
    
    private String _id;

    private String ten;

    private int soLuong;

    private double gia;

    private String avatar;

    public FruitModel() {
    }

    public FruitModel(String _id, String ten, int soLuong, double gia, String avatar) {
        this._id = _id;
        this.ten = ten;
        this.soLuong = soLuong;
        this.gia = gia;
        this.avatar = avatar;
    }

    public FruitModel(String ten, int soLuong, double gia, String avatar) {
        this.ten = ten;
        this.soLuong = soLuong;
        this.gia = gia;
        this.avatar = avatar;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getTen() {
        return ten;
    }

    public void setTen(String ten) {
        this.ten = ten;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }

    public double getGia() {
        return gia;
    }

    public void setGia(double gia) {
        this.gia = gia;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    @Override
    public String toString() {
        return "Data [NAME=" + ten + ", CLASS=" + soLuong + ", THIRTY=" + gia
                + ", NINETY=" + avatar + "]";
    }
}
