package ru.ochakovo.app;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {
    private SharedPreferences prefs;

    public SessionManager(Context context) {
        prefs = context.getSharedPreferences("OchakovoPrefs", Context.MODE_PRIVATE);
    }

    public void saveUserId(int id) {
        prefs.edit().putInt("user_id", id).apply();
    }

    public int getUserId() {
        return prefs.getInt("user_id", -1);
    }

    public void logout() {
        prefs.edit().clear().apply();
    }
}