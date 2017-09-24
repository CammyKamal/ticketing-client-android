package com.chandigarhadmin.ui;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chandigarhadmin.R;
import com.chandigarhadmin.session.SessionManager;
import com.chandigarhadmin.utils.Constant;

public class MyAccountActivity extends AppCompatActivity {


    private SessionManager sessionManager;

    
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myaccount);
        sessionManager = new SessionManager(this);
        TextView tvUserName = (TextView) findViewById(R.id.usernametv);
        tvUserName.setText(sessionManager.getUsername());
        TextView tvLang = (TextView) findViewById(R.id.langtv);
        String lang = "";
        if (sessionManager.getLanguage(Constant.SELECTED_LOCALE_LANGUAGE).equalsIgnoreCase("en")) {
            lang = "English";
        } else if (sessionManager.getLanguage(Constant.SELECTED_LOCALE_LANGUAGE).equalsIgnoreCase("pa")) {
            lang = "Punjabi";
        } else if (sessionManager.getLanguage(Constant.SELECTED_LOCALE_LANGUAGE).equalsIgnoreCase("hi")) {
            lang = "Hindi";
        } else {
            lang = "English";
        }
        tvLang.setText(lang);
        ((ImageView) findViewById(R.id.crossicon)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }
}
