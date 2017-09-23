package com.chandigarhadmin.session;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.chandigarhadmin.ui.LoginActivity;

/**
 * Created by harendrasinghbisht on 23/09/17.
 */

public class SessionManager {
    // Shared Preferences
    SharedPreferences pref;

    // Editor for Shared preferences
    SharedPreferences.Editor editor;

    // Context
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Sharedpref file name
    private static final String PREF_NAME = "chdadmin";

    // All Shared Preferences Keys
    private static final String IS_LOGIN = "IsLoggedIn";
    public static final String KEY_USER_ID = "userid";
    public static final String KEY_USER_NAME = "userNAME";
    public static final String KEY_PHONE_NUMBER = "phone_no";
    public static final String KEY_USER_EMAIL = "email";
    public static final String KEY_FIRST_LOGIN = "is_first_login";

    // Constructor
    public SessionManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    /**
     * Create login session
     */
    public void createLoginSession(String username, String phone,String email ) {
        // Storing login value as TRUE
        editor.putBoolean(IS_LOGIN, true);;
        editor.putString(KEY_USER_EMAIL, email);
       // editor.putString(KEY_USER_ID, userDetail.getId());
        editor.putString(KEY_PHONE_NUMBER, phone);

        // commit changes
        editor.apply();
    }

    /**
     * Check login method wil check user login status
     * If false it will redirect user to login page
     * Else won't do anything
     */
    public void checkLogin() {
        // Check login status
        if (!this.isLoggedIn()) {
            // user is not logged in redirect him to Login Activity
            Intent i = new Intent(_context, LoginActivity.class);
            // Closing all the Activities
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            // Add new Flag to start new Activity
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            // Staring Login Activity
            _context.startActivity(i);
        }

    }


    /**
     * Clear session details
     */
    public void logoutUser() {
        // Clearing all data from Shared Preferences
        editor.clear();
        editor.apply();

    }


    /**
     * Quick check for login
     **/
    // Get Login State
    public boolean isLoggedIn() {
        return pref.getBoolean(IS_LOGIN, false);
    }



    public String getKeyUserId() {
        return pref.getString(KEY_USER_ID, null);
    }

    public void setKeyUserId(String keyUserId) {
        editor.putString(KEY_USER_ID, keyUserId);

        // commit changes
        editor.apply();
    }

}
