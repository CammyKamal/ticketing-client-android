package com.chandigarhadmin.ui;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
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

import static com.chandigarhadmin.R.id.timer;
import static com.chandigarhadmin.R.string.resend_otp;

public class ConfirmOtpActivity extends Activity implements ResponseCallback {
    @BindView(R.id.et_readotp)
    EditText etOptRecevier;
    @BindView(R.id.ll_otp)
    LinearLayout llOtp;
    @BindView(R.id.btn_confirm_otp)
    Button submitBtn;
    Timer t = new Timer();
    TimerTask task, timerTask;
    private boolean isOtpReceived;
    private SmsVerifyCatcher smsVerifyCatcher;
    private ProgressDialog progressDialog;
    private SessionManager sessionManager;
    private int time=0;
    private MyCountDownTimer myCountDownTimer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_otp);
        ButterKnife.bind(this);
        navigateToDashBoard();
        submitBtn.setEnabled(false);
//        etOptRecevier.setTypeface(Typeface.createFromAsset(getAssets(), "stc.otf"));
        sessionManager = new SessionManager(this);
        progressDialog = Constant.createDialog(this, null);
        checkClickAction();
        smsVerifyCatcher = new SmsVerifyCatcher(this, new OnSmsCatchListener<String>() {
            @Override
            public void onSmsCatch(String message) {
                isOtpReceived = true;
                progressDialog.hide();
                String code = parseCode(message);//Parse verification code
                etOptRecevier.setText(code);//set code in edit text
                //then you can send verification code to server
                saveLoginDetail();
            }
        });
        smsVerifyCatcher = new SmsVerifyCatcher(this, new OnSmsCatchListener<String>() {
            @Override
            public void onSmsCatch(String message) {
                isOtpReceived = true;
                progressDialog.hide();
                String code = parseCode(message);//Parse verification code
                etOptRecevier.setText(code);//set code in edit text
                //then you can send verification code to server
                if (myCountDownTimer != null) {
                    myCountDownTimer.onFinish();
                }
                saveLoginDetail();
            }
        });

        etOptRecevier.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() == 4) {
                    isOtpReceived = true;
                    submitBtn.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }


    /**
     * Parse verification code
     *
     * @param message sms message
     * @return only four numbers from massage string
     */
    private String parseCode(String message) {
        Pattern p = Pattern.compile("\\b\\d{4}\\b");
        Matcher m = p.matcher(message);
        String code = "";
        while (m.find()) {
            code = m.group(0);
        }
        return code;
    }


    @Override
    protected void onStart() {
        super.onStart();
        smsVerifyCatcher.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        smsVerifyCatcher.onStop();
    }

    /**
     * need for Android 6 real time permissions
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        smsVerifyCatcher.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void checkClickAction() {
        if (!isOtpReceived) {
            if (!getIntent().getStringExtra("phone").trim().equalsIgnoreCase(getResources().getString(R.string.resend_otp))) {
                getUserByEmail(getIntent().getStringExtra("phone"));
            } else {
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
                jsonObject.put(RequestParams.EMAIL, getIntent().getStringExtra("phone").trim() + "@gmail.com");
            }
            jsonObject.put(RequestParams.NAME, getIntent().getStringExtra(Constant.INPUT_USER));
            jsonObject.put(RequestParams.PHONE, getIntent().getStringExtra("phone").trim());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if(Constant.isNetworkAvailable(ConfirmOtpActivity.this)) {
            ApiServiceTask task = new ApiServiceTask(this, this, RequestParams.TYPE_CREATE_USER);
            task.setRequestParams(jsonObject, JSONParser.POST);
            task.execute(Constant.BASE + "users");
        } else {
            Constant.showToastMessage(ConfirmOtpActivity.this, getString(R.string.no_internet));
        }
    }

    private void getUserByEmail(String email) {
        if(Constant.isNetworkAvailable(ConfirmOtpActivity.this)) {
            progressDialog.show();
            ApiServiceTask task = new ApiServiceTask(this, this, RequestParams.TYPE_GET_USER_BY);
            task.setRequestParams(null, JSONParser.GET);
            task.execute(Constant.BASE + "users" + "/" + email);
        } else {
            Constant.showToastMessage(ConfirmOtpActivity.this, getString(R.string.no_internet));
        }

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
                    //send otp on mobile number
                    // progressDialog.show();
//                    isOtpReceived=true;
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

    private void navigateToDashBoard() {
        Intent it = new Intent(ConfirmOtpActivity.this, AdminAgentActivity.class);
        it.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(it);
        finish();
    }

    public void startTimer() {
        t = new Timer();
        task = new TimerTask() {

            @Override
            public void run() {
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        TextView tv1 = (TextView) findViewById(timer);
                        tv1.setTypeface(Typeface.createFromAsset(getAssets(), "stc.otf"));
                        tv1.setText(getString(R.string.waiting) + time + getString(R.string.second));
                        if (time > 0)
                            time -= 1;
                        else if (time == 0) {

                            //llOtp.setVisibility(View.GONE);
                            //progressDialog.hide();
                        }
                    }
                });
            }
        };
        t.scheduleAtFixedRate(task, 0, 60 * 1000);
    }

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
                submitBtn.setText(resend_otp);
            }
        }

        @Override
        public void onFinish() {
            llOtp.setVisibility(View.GONE);
            submitBtn.setText(resend_otp);
        }
    }
}
