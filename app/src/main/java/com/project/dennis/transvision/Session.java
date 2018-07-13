package com.project.dennis.transvision;

import android.content.Context;
import android.content.SharedPreferences;

public class Session {
    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;
    private Context context;

    public Session(Context context) {
        this.context = context;
        prefs = context.getSharedPreferences("user", Context.MODE_PRIVATE);
        editor = prefs.edit();
    }

    public void setLoggedIn(boolean loggedIn) {
        editor.putBoolean("loggedInMode", loggedIn);
        editor.commit();
    }

    public boolean loggedIn() {
        return prefs.getBoolean("loggedInMode", false);
    }
}