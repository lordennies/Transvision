package com.project.dennis.transvision.Models;

public class Peminjaman {

    private String tujuan;
    private String tgl_pemakaian;

    public Peminjaman(String tujuan, String tgl_pemakaian) {
        this.tujuan = tujuan;
        this.tgl_pemakaian = tgl_pemakaian;
    }

    public String getTujuan() { return tujuan; }

    public String getTglPemakaian() { return tgl_pemakaian; }

}
