package co.helpdesk.faveo;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by sumit on 2/7/2016.
 */
public class Preference {

    private static boolean crashReport;

    public static Context applicationContext;

    public Preference(Context applicationContext) {
        Preference.applicationContext = applicationContext;
    }

    public static String getToken() {
        SharedPreferences prefs = applicationContext.getSharedPreferences(Constants.PREFERENCE, 0);
        return prefs.getString("TOKEN", null);
    }

    public static String getUsername() {
        SharedPreferences prefs = applicationContext.getSharedPreferences(Constants.PREFERENCE, 0);
        return prefs.getString("USERNAME", null);
    }

    public static String getPassword() {
        SharedPreferences prefs = applicationContext.getSharedPreferences(Constants.PREFERENCE, 0);
        return prefs.getString("PASSWORD", null);
    }

    public static String getUserID() {
        SharedPreferences prefs = applicationContext.getSharedPreferences(Constants.PREFERENCE, 0);
        return prefs.getString("ID", null);
    }

    public static void setToken(String token) {
        SharedPreferences.Editor authenticationEditor = applicationContext.getSharedPreferences(Constants.PREFERENCE, 0).edit();
        authenticationEditor.putString("TOKEN", token);
        authenticationEditor.apply();
    }

    public static void setCompanyURL(String companyURL) {
        SharedPreferences.Editor authenticationEditor = applicationContext.getSharedPreferences(Constants.PREFERENCE, 0).edit();
        authenticationEditor.putString("COMPANY_URL", companyURL);
        authenticationEditor.apply();
    }

    public static String getCompanyURL() {
        SharedPreferences prefs = applicationContext.getSharedPreferences(Constants.PREFERENCE, 0);
        return prefs.getString("COMPANY_URL", null);
    }

    public static boolean isCrashReport() {
        SharedPreferences prefs = applicationContext.getSharedPreferences(Constants.PREFERENCE, 0);
        return prefs.getBoolean("CRASH_REPORT", true);
    }

}
