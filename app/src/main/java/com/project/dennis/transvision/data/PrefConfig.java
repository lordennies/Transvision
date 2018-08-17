package com.project.dennis.transvision.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.project.dennis.transvision.R;

public class PrefConfig {

    private SharedPreferences preferences;
    private Context context;

    public PrefConfig(Context context) {
        this.context = context;
        preferences = context.getSharedPreferences(context.getString(R.string.pref_file),
                Context.MODE_PRIVATE);
    }

    public void writeLoginStatus(boolean status) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(context.getString(R.string.pref_login_status), status);
        editor.apply();
    }

    public boolean readLoginStatus() {
        return preferences.getBoolean(context.getString(R.string.pref_login_status), false);
    }

    public void writeEmail(String email) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(context.getString(R.string.pref_email), email);
        editor.apply();
    }

    public String readEmail() {
        return preferences.getString(context.getString(R.string.pref_email), "");
    }

    public void displayToast(String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
}
