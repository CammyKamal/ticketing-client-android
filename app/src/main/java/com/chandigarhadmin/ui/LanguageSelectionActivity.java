package com.chandigarhadmin.ui;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RadioGroup;

import com.chandigarhadmin.R;
import com.chandigarhadmin.session.SessionManager;
import com.chandigarhadmin.utils.Constant;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LanguageSelectionActivity extends AppCompatActivity {

    @BindView(R.id.radio_group_languages)
    RadioGroup languageRadioGroup;
    private SessionManager sessionManager;

    @OnClick({ R.id.btn_continue})
    public void continueNextScreen() {
        int radioButtonID = languageRadioGroup.getCheckedRadioButtonId();
        View radioButton = languageRadioGroup.findViewById(radioButtonID);
        int idx = languageRadioGroup.indexOfChild(radioButton);
        switch (idx) {
            case 0:
                updateLocale("en");
                break;
            case 1:
                updateLocale("hi");
                break;
            case 2:
                updateLocale("pa");
                break;
            default:
                updateLocale("en");
                break;
        }
    }

    private void updateLocale(String loc) {
        Locale locale = new Locale(loc);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());

        sessionManager.saveBooleanValue(Constant.SELECTED_LOCALE, true);
        sessionManager.saveLanguage(Constant.SELECTED_LOCALE, loc);
        moveToNextScreen();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language_selection);

        sessionManager = new SessionManager(this);
        ButterKnife.bind(this);
        checkForLocale();
    }

    //Checking whether language is already selected or not
    private void checkForLocale() {
        boolean isLocaleSelected = sessionManager.getBooleanKey(Constant.SELECTED_LOCALE);
        if(isLocaleSelected) {
           moveToNextScreen();
        }
    }

    private void moveToNextScreen() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

}
