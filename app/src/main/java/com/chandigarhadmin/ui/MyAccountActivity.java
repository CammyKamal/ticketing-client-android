package com.chandigarhadmin.ui;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.chandigarhadmin.R;
import com.chandigarhadmin.session.SessionManager;
import com.chandigarhadmin.utils.Constant;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MyAccountActivity extends AppCompatActivity {


    @BindView(R.id.usernametv)
    TextView tvUserName;
    @BindView(R.id.langtv)
    TextView tvLang;
    private SessionManager sessionManager;

    @OnClick(R.id.crossicon)
    public void imageClick() {
        finish();
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myaccount);
        ButterKnife.bind(this);
        sessionManager = new SessionManager(this);
        tvUserName.setText(sessionManager.getUsername());
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
    }
}
