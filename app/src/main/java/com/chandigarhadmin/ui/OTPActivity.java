package com.chandigarhadmin.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.chandigarhadmin.R;
import com.chandigarhadmin.utils.Constant;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OTPActivity extends AppCompatActivity implements View.OnClickListener {
    @BindView(R.id.etphonenumber)
    EditText etPhoneNumber;
    @BindView(R.id.submitbtn)
    Button submitBtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);
        ButterKnife.bind(this);
        submitBtn.setEnabled(false);
        init();

        etPhoneNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() == 10) {
                    submitBtn.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void init() {
        submitBtn.setOnClickListener(this);
        TextView tvCountryCode = (TextView) findViewById(R.id.tvcountrycode);
//        submitBtn.setTypeface(Typeface.createFromAsset(getAssets(), "stc.otf"));
//        tvCountryCode.setTypeface(Typeface.createFromAsset(getAssets(), "stc.otf"));
        //  tvCountryCode.setTypeface(Typeface.createFromAsset(getAssets(), "stc.otf"));
//        ((TextView) findViewById(R.id.titletv)).setTypeface(Typeface.createFromAsset(getAssets(), "stc.otf"));
//        etPhoneNumber.setTypeface(Typeface.createFromAsset(getAssets(), "stc.otf"));

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.submitbtn:
                if (validatePhoneNumber()) {
                    Intent confirmOtp = new Intent(OTPActivity.this, ConfirmOtpActivity.class);
                    confirmOtp.putExtra("phone", etPhoneNumber.getText().toString());
                    confirmOtp.putExtra(Constant.INPUT_USER, getIntent().getStringExtra(Constant.INPUT_USER));
                    if (null != getIntent() && getIntent().hasExtra(Constant.INPUT_EMAIL)) {
                        confirmOtp.putExtra(Constant.INPUT_EMAIL, getIntent().getStringExtra(Constant.INPUT_EMAIL));
                    }
                    startActivity(confirmOtp);
                }
                break;

        }
    }

    private boolean validatePhoneNumber() {
        if (!TextUtils.isEmpty(etPhoneNumber.getText()) & etPhoneNumber.getText().length() == 10) {
            return true;
        } else {
            etPhoneNumber.setError(getString(R.string.phone_error));
        }
        return false;
    }
}
