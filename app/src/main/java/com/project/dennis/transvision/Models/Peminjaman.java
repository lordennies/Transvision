package com.project.dennis.transvision.Models;

public class Peminjaman {

    private String tujuan;
    private String keperluan;
    private String jumPenumpang;
    private String tglPemakaian;

    public Peminjaman(String tujuan, String keperluan, String jumPenumpang, String tglPemakaian) {
        this.tujuan = tujuan;
        this.keperluan = keperluan;
        this.jumPenumpang = jumPenumpang;
        this.tglPemakaian = tglPemakaian;
    }

    private void setTujuan(String tujuan) { this.tujuan = tujuan; }

    public String getTujuan() { return tujuan; }

    private void setKeperluan(String keperluan) { this.keperluan = keperluan; }

    public String getKeperluan() { return keperluan; }

    private void setJumPenumpang(String jumPenumpang) { this.jumPenumpang = jumPenumpang; }

    public String getJumPenumpang() { return jumPenumpang; }

    private void setTglPemakaian(String tglPemakaian) { this.tglPemakaian = tglPemakaian; }

    public String getTglPemakaian() { return tglPemakaian; }

}
