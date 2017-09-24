package com.chandigarhadmin.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
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
import com.chandigarhadmin.interfaces.ResponseCallback;
import com.chandigarhadmin.models.CreateUserResponse;
import com.chandigarhadmin.models.RequestParams;
import com.chandigarhadmin.service.ApiServiceTask;
import com.chandigarhadmin.service.JSONParser;
import com.chandigarhadmin.session.SessionManager;
import com.chandigarhadmin.utils.Constant;
import com.google.gson.Gson;
import com.stfalcon.smsverifycatcher.OnSmsCatchListener;
import com.stfalcon.smsverifycatcher.SmsVerifyCatcher;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import butterknife.BindView;
import butterknife.ButterKnife;

public class OTPActivity extends AppCompatActivity implements View.OnClickListener {
    @BindView(R.id.etphonenumber)
    EditText etPhoneNumber;
    @BindView(R.id.submitbtn)
    Button submitBtn;

    private MyCountDownTimer myCountDownTimer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);
        ButterKnife.bind(this);
        submitBtn.setEnabled(false);
        init();
        smsVerifyCatcher = new SmsVerifyCatcher(this, new OnSmsCatchListener<String>() {
            @Override
            public void onSmsCatch(String message) {
                isOtpReceived = true;
                progressDialog.hide();
                String code = parseCode(message);//Parse verification code
                etOptRecevier.setText(code);//set code in edit text
                //then you can send verification code to server
                if(myCountDownTimer!=null) {
                    myCountDownTimer.onFinish();
                }
                saveLoginDetail();
            }
        });
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
        etOptRecevier.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() == 4) {
                    // TODO: 23/09/17 need to change this otp receive boolean
                    isOtpReceived=true;
                    submitBtn.setText("Submit");
                    //create user api call;
                }
                else{
                    isOtpReceived=false;
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

    private void checkClickAction() {
        if (!isOtpReceived) {
            // submitBtn.setText("Resend OTP");
            if(!etPhoneNumber.getText().toString().trim().equalsIgnoreCase("Resend Otp")) {
                getUserByEmail(etPhoneNumber.getText().toString());
            }
            else{
                myCountDownTimer.start();
            }
        } else if (isOtpReceived) {
            if (!TextUtils.isEmpty(etOptRecevier.getText())) {
                saveLoginDetail();
            }
        }
    }

    /**
     * create a user if not exist
     */
    private void saveLoginDetail() {
        progressDialog.show();
        JSONObject jsonObject = new JSONObject();
        try {
            if (getIntent().hasExtra(Constant.INPUT_EMAIL)) {
                jsonObject.put(RequestParams.EMAIL, getIntent().getStringExtra(Constant.INPUT_EMAIL));
            } else {
                jsonObject.put(RequestParams.EMAIL, etPhoneNumber.getText().toString().trim() + "@gmail.com");
            }
            jsonObject.put(RequestParams.NAME, getIntent().getStringExtra(Constant.INPUT_USER));
            jsonObject.put(RequestParams.PHONE, etPhoneNumber.getText().toString().trim());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        ApiServiceTask task = new ApiServiceTask(this, this, RequestParams.TYPE_CREATE_USER);
        task.setRequestParams(jsonObject, JSONParser.POST);
        task.execute(Constant.BASE + "users");
        //  }

    }

    private void getUserByEmail(String email) {
        progressDialog.show();
        ApiServiceTask task = new ApiServiceTask(this, this, RequestParams.TYPE_GET_USER_BY);
        task.setRequestParams(null, JSONParser.GET);
        task.execute(Constant.BASE + "users" + "/" + email);
    }

    @Override
    public void onResponse(String result, String type) {
        progressDialog.dismiss();
        Log.i("RESPONSE FROM server=", "" + result);
        if (type.equalsIgnoreCase(RequestParams.TYPE_CREATE_USER)) {

            if (!result.contains("error") && !result.equals("Failed")) {
                Gson gson = new Gson();
                CreateUserResponse createUserResponse = gson.fromJson(result, CreateUserResponse.class);
                sessionManager.createLoginSession(createUserResponse.getFirstName(), createUserResponse.getLastName(), createUserResponse.getEmail());
                sessionManager.setKeyUserId(createUserResponse.getId());
                navigateToDashBoard();
            }
        } else if (type.equalsIgnoreCase(RequestParams.TYPE_GET_USER_BY)) {

            try {
                JSONObject response = new JSONObject(result);

                if (response.has("error") && response.getString("error").equalsIgnoreCase("User not found.")) {
                   // isOtpReceived = true;
                    llOtp.setVisibility(View.VISIBLE);
                    myCountDownTimer = new MyCountDownTimer(20000, 1000);
                    myCountDownTimer.start();

                } else if (response.has(RequestParams.EMAIL) && !result.equals("Failed")) {
                    sessionManager.createLoginSession(response.getString("first_name"), response.getString("last_name"), response.getString("email"));
                    sessionManager.setKeyUserId(response.getString("id"));
                    navigateToDashBoard();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

    }

                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd:MMMM:yyyy HH:mm:ss a");

    public class MyCountDownTimer extends CountDownTimer {

        public MyCountDownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {

            int progress = (int) (millisUntilFinished / 1000);

            // progressBar.setProgress(progressBar.getMax()-progress);
            TextView tv1 = (TextView) findViewById(timer);
            tv1.setTypeface(Typeface.createFromAsset(getAssets(), "stc.otf"));
            tv1.setText("waiting " + progress + "sec");
            if (progress == 0) {
                llOtp.setVisibility(View.GONE);
                submitBtn.setText("Resend OTP");
            }
        }

        @Override
        public void onFinish() {
            llOtp.setVisibility(View.GONE);
            submitBtn.setText("Resend OTP");
        }
    }

}
