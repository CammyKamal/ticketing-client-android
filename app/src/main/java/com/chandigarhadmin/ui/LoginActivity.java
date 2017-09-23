package com.chandigarhadmin.ui;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.chandigarhadmin.R;


public class LoginActivity extends Activity {

    TextView tvTitle, tvCountryCode;
    EditText etPhoneInput;
    Button btnProceed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loginscreen);

        tvTitle = (TextView) findViewById(R.id.titletv);
        tvCountryCode = (TextView) findViewById(R.id.tvcountrycode);
        etPhoneInput = (EditText) findViewById(R.id.etphonenumber);
        btnProceed = (Button) findViewById(R.id.proceedbtn);
        btnProceed.setTypeface(Typeface.createFromAsset(getAssets(), "stc.otf"));
        tvCountryCode.setTypeface(Typeface.createFromAsset(getAssets(), "stc.otf"));
        tvTitle.setTypeface(Typeface.createFromAsset(getAssets(), "stc.otf"));
        etPhoneInput.setTypeface(Typeface.createFromAsset(getAssets(), "stc.otf"));
        btnProceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, AdminAgentActivity.class));
                finish();
            }
        });

    }
}
