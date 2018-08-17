package com.project.dennis.transvision.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Peminjaman implements Parcelable {

    @SerializedName("tujuan")
    private String tujuan;

    @SerializedName("keperluan")
    private String keperluan;

    @SerializedName("jum_penumpang")
    private String jumPenumpang;

    @SerializedName("tgl_pemakaian")
    private String tglPemakaian;

    public Peminjaman(String tujuan, String keperluan, String jumPenumpang, String tglPemakaian) {
        this.tujuan = tujuan;
        this.keperluan = keperluan;
        this.jumPenumpang = jumPenumpang;
        this.tglPemakaian = tglPemakaian;
    }

    public Peminjaman(Parcel in) {
        tujuan = in.readString();
        keperluan = in.readString();
        jumPenumpang = in.readString();
        tglPemakaian = in.readString();
    }

    public String getTujuan() { return tujuan; }

    public String getKeperluan() { return keperluan; }

    public String getJumPenumpang() { return jumPenumpang; }

    public String getTglPemakaian() { return tglPemakaian; }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(tujuan);
        dest.writeString(keperluan);
        dest.writeString(jumPenumpang);
        dest.writeString(tglPemakaian);
    }

    public static final Creator<Peminjaman> CREATOR = new Creator<Peminjaman>() {
        @Override
        public Peminjaman createFromParcel(Parcel in) {
            return new Peminjaman(in);
        }

        @Override
        public Peminjaman[] newArray(int size) {
            return new Peminjaman[size];
        }
    };
}
