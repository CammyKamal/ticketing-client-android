package com.chandigarhadmin.utils;


import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.widget.Toast;

public class Constant {
    public static final String USERNAME = "Harry";
    public static final String INPUT_USER = "username";
    public static final String INPUT_EMAIL = "email";
    public static final String INPUT_TICKET_DATA = "ticket_data";
    public static final String SELECTED_LOCALE = "selected_locale";
    public static final String SELECTED_LOCALE_LANGUAGE = "selected_locale_language";
    public static final String BASE = "http://95.85.55.146/api/rest/latest/desk/";
    public static final String PASSWORD = "ee47ba573ef6205fa11b0bcb5e0d959db02f36a5";
    public static final String PASSWORDH = "9ac47cec4801d91bdefba9ad89705d12a111d4c8";
    public static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    public static final String AI_CONFIGURATION_TOKEN="0882e9aa21ef450694115050513f3822";
    public static ProgressDialog createDialog(Context context, String message) {
        ProgressDialog progressDialog = new ProgressDialog(context);
        if (null != message) {
            progressDialog.setMessage(message);
        } else {
            progressDialog.setMessage("Please wait");
        }
        progressDialog.setCancelable(true);
        return progressDialog;
    }

    //checking for internet connectivity status
    public static boolean isNetworkAvailable(Context context) {
        final ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }

    public static void showToastMessage(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }
}

