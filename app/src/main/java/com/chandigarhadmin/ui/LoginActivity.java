package com.chandigarhadmin.ui;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.chandigarhadmin.R;
import com.chandigarhadmin.utils.Constant;


public class LoginActivity extends Activity {

    private TextView tvTitle, tvCountryCode;
    private EditText etEmailInput, etUserName;
    private Button btnProceed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loginscreen);

        tvTitle = (TextView) findViewById(R.id.titletv);
        // tvCountryCode = (TextView) findViewById(R.id.tvcountrycode);
        etEmailInput = (EditText) findViewById(R.id.et_email);
        etUserName = (EditText) findViewById(R.id.etusername);
        btnProceed = (Button) findViewById(R.id.proceedbtn);
        btnProceed.setTypeface(Typeface.createFromAsset(getAssets(), "stc.otf"));
        //  tvCountryCode.setTypeface(Typeface.createFromAsset(getAssets(), "stc.otf"));
        tvTitle.setTypeface(Typeface.createFromAsset(getAssets(), "stc.otf"));
        etEmailInput.setTypeface(Typeface.createFromAsset(getAssets(), "stc.otf"));
        etUserName.setTypeface(Typeface.createFromAsset(getAssets(), "stc.otf"));
        btnProceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validateInputs()) {
                    Intent it = new Intent(LoginActivity.this, OTPActivity.class);
                    it.putExtra(Constant.INPUT_USER, etUserName.getText().toString());
                    if (!TextUtils.isEmpty(etEmailInput.getText().toString().trim())) {
                        it.putExtra(Constant.INPUT_EMAIL, etEmailInput.getText());
                    }
                    startActivity(it);
                }
            }
        });

    }

    private boolean validateInputs() {

        if (!TextUtils.isEmpty(etUserName.getText().toString().trim()) && etUserName.getText().length() > 3) {

            return validateEmail();
        } else {
            etUserName.setError("Username should be more three characters");
        }
        return false;
    }

    private boolean validateEmail() {
        if (!TextUtils.isEmpty(etEmailInput.getText().toString().trim()) && etEmailInput.getText().toString().matches(Constant.EMAIL_PATTERN)) {
            return true;
        } else if (TextUtils.isEmpty(etEmailInput.getText().toString().trim())) {
            etEmailInput.setError("Enter a valid email");
            return true;
        }
        return false;
    }
}
