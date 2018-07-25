package com.project.dennis.transvision.Data;

public final class ConfigLink {

    // Mencegah terjadinya instansiasi kelas secara tidak sengaja
    private ConfigLink() {}

    public static final String URL_MAIN = "http://192.168.105/lordennies/transvision-cls/api/";
    public static final String LOGIN = URL_MAIN + "login";
    public static final String PEMINJAMAN = URL_MAIN + "pinjam";

    public static final String LOGIN_PREF = "login";

    public final static String ACTION = "action";
    public final static String USER_ID = "user_id";
    public final static String TUJUAN = "tujuan";
    public final static String KEPERLUAN = "keperluan";
    public final static String JUM_PENUMPANG = "jum_penumpang";
    public final static String TGL_PEMAKAIAN = "tgl_pemakaian";

}
