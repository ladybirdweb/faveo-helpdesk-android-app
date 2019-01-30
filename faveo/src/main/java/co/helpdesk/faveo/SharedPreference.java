package co.helpdesk.faveo;

import android.content.Context;

public class SharedPreference {
    // All Shared Preferences Keys Declare as #public
    public static final String KEY_SET_APP_RUN_FIRST_TIME = "KEY_SET_APP_RUN_FIRST_TIME";
    private static final String PREF_NAME = "testing";
    android.content.SharedPreferences pref;
    android.content.SharedPreferences.Editor editor;
    Context _context;


    public SharedPreference(Context context) // Constructor
    {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, 0);
        editor = pref.edit();

    }

    /*
     *  Set Method Generally Store Data;
     *  Get Method Generally Retrieve Data ;
     * */

    public String getApp_runFirst() {
        String App_runFirst = pref.getString(KEY_SET_APP_RUN_FIRST_TIME, "FIRST");
        return App_runFirst;
    }

    public void setApp_runFirst(String App_runFirst) {
        editor.remove(KEY_SET_APP_RUN_FIRST_TIME);
        editor.putString(KEY_SET_APP_RUN_FIRST_TIME, App_runFirst);
        editor.commit();
    }

}
