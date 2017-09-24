package com.chandigarhadmin.ui;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.chandigarhadmin.R;
import com.chandigarhadmin.utils.Constant;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class LoginActivity extends Activity {

    @BindView(R.id.titletv)
    TextView tvTitle;
    @BindView(R.id.proceedbtn)
    Button btnProceed;

    @BindView(R.id.et_email)
    EditText etEmailInput;
    @BindView(R.id.etusername)
    EditText etUserName;
    @OnClick(R.id.proceedbtn)
    public void clickProceedButton() {
        if (validateInputs()) {
            Intent it = new Intent(LoginActivity.this, OTPActivity.class);
            it.putExtra(Constant.INPUT_USER, etUserName.getText().toString());
            if (!TextUtils.isEmpty(etEmailInput.getText().toString().trim())) {
                it.putExtra(Constant.INPUT_EMAIL, etEmailInput.getText());
            }
            startActivity(it);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loginscreen);

        ButterKnife.bind(this);
        btnProceed.setTypeface(Typeface.createFromAsset(getAssets(), "stc.otf"));
        tvTitle.setTypeface(Typeface.createFromAsset(getAssets(), "stc.otf"));
        etEmailInput.setTypeface(Typeface.createFromAsset(getAssets(), "stc.otf"));
        etUserName.setTypeface(Typeface.createFromAsset(getAssets(), "stc.otf"));
    }

    private boolean validateInputs() {

        if (!TextUtils.isEmpty(etUserName.getText().toString().trim()) && etUserName.getText().length() > 3) {

            return validateEmail();
        } else {
            etUserName.setError(getString(R.string.username_error));
        }
        return false;
    }

    private boolean validateEmail() {
        if (!TextUtils.isEmpty(etEmailInput.getText().toString().trim()) && etEmailInput.getText().toString().matches(Constant.EMAIL_PATTERN)) {
            return true;
        } else if (TextUtils.isEmpty(etEmailInput.getText().toString().trim())) {
            etEmailInput.setError(getString(R.string.email_error));
            return true;
        }
        return false;
    }
}
