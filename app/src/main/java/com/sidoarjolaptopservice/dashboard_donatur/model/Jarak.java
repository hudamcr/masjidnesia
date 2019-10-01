package com.sidoarjolaptopservice.dashboard_donatur.model;

public class Jarak {

    private String nama, jarak,alamat,nama_takmir;
    private boolean isSelected;
    private int id;
    private String gambar;

    public Jarak() {
    }

    public Jarak(int id,String gambar,String nama, String jarak,String alamat,String nama_takmir) {
        this.nama = nama;
        this.gambar =gambar;
        this.jarak = jarak;
        this.id=id;
        this.nama_takmir=nama_takmir;
        this.alamat=alamat;
    }
    public boolean getSelected() {
        return isSelected;
    }
    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public int getId() {

        return id;
    }

    public String getGambar() {
        return gambar;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }


    public String getNama_takmir() {
        return nama_takmir;
    }

    public void setNama_takmir(String nama_takmir) {
        this.nama_takmir = nama_takmir;
    }


    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getJarak() {
        return jarak;
    }

    public void setJarak(String jarak) {
        this.jarak = jarak;
    }
}
