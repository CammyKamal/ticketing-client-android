package com.chandigarhadmin.ui;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chandigarhadmin.R;
import com.chandigarhadmin.session.SessionManager;

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
       // tvLang.setText(sessionManager.getLanguage());
        ((ImageView)findViewById(R.id.crossicon)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }
}
