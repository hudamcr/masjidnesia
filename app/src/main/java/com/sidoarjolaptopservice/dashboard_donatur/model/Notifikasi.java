package com.sidoarjolaptopservice.dashboard_donatur.model;

public class Notifikasi {

    private String nama_donatur;
    private String nama_masjid;
    private String jenis_donasi;
    private String nominal;
    private String tanggal_donasi;

    public String getNama_donatur() {
        return nama_donatur;
    }

    public void setNama_donatur(String nama_donatur) {
        this.nama_donatur = nama_donatur;
    }

    public String getNama_masjid() {
        return nama_masjid;
    }

    public void setNama_masjid(String nama_masjid) {
        this.nama_masjid = nama_masjid;
    }

    public String getJenis_donasi() {
        return jenis_donasi;
    }

    public void setJenis_donasi(String jenis_donasi) {
        this.jenis_donasi = jenis_donasi;
    }

    public String getNominal() {
        return nominal;
    }

    public void setNominal(String nominal) {
        this.nominal = nominal;
    }

    public String getTanggal_donasi() {
        return tanggal_donasi;
    }

    public void setTanggal_donasi(String tanggal_donasi) {
        this.tanggal_donasi = tanggal_donasi;
    }

    public Notifikasi(String nama_donatur, String nama_masjid, String jenis_donasi, String nominal, String tanggal_donasi) {
        this.nama_donatur = nama_donatur;
        this.nama_masjid = nama_masjid;
        this.jenis_donasi = jenis_donasi;
        this.nominal = nominal;
        this.tanggal_donasi = tanggal_donasi;
    }


}
