package com.chandigarhadmin.ui;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chandigarhadmin.App;
import com.chandigarhadmin.R;
import com.chandigarhadmin.interfaces.ResponseCallback;
import com.chandigarhadmin.models.CreateUserResponse;
import com.chandigarhadmin.models.LoginUserModel;
import com.chandigarhadmin.models.RequestParams;
import com.chandigarhadmin.session.SessionManager;
import com.chandigarhadmin.utils.Constant;
import com.stfalcon.smsverifycatcher.OnSmsCatchListener;
import com.stfalcon.smsverifycatcher.SmsVerifyCatcher;

import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Response;

public class ConfirmOtpActivity extends Activity implements ResponseCallback {
    @BindView(R.id.et_readotp)
    EditText etOptRecevier;
    @BindView(R.id.ll_otp)
    LinearLayout llOtp;
    @BindView(R.id.btn_confirm_otp)
    Button submitBtn;
    @BindView(R.id.timer)
    TextView timerText;
    Timer t = new Timer();
    TimerTask task, timerTask;
    private SmsVerifyCatcher smsVerifyCatcher;
    private ProgressDialog progressDialog;
    private SessionManager sessionManager;
    private int time=0;
    private boolean isOtpRecived;
    private MyCountDownTimer myCountDownTimer;

    @OnClick(R.id.btn_confirm_otp)
    public void submitButton() {
        if (submitBtn.getText().toString().trim().equalsIgnoreCase(getResources().getString(R.string.resend_otp))) {
            myCountDownTimer = new MyCountDownTimer(15000, 1000);
            myCountDownTimer.start();
        } else if (Constant.checkString(etOptRecevier.getText().toString())) {
            saveLoginDetail();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_otp);
        ButterKnife.bind(this);

        submitBtn.setAlpha(0.4f);// diming the brightness of button
        submitBtn.setEnabled(false);
        myCountDownTimer = new MyCountDownTimer(15000, 1000);
        myCountDownTimer.start();       //starting the timer
        sessionManager = new SessionManager(this);
        progressDialog = Constant.createDialog(this, null);
        smsVerifyCatcher = new SmsVerifyCatcher(this, new OnSmsCatchListener<String>() {
            @Override
            public void onSmsCatch(String message) {

                String code = parseCode(message);//Parse verification code
                if (null != code && !code.isEmpty()) {
                    progressDialog.hide();
                    isOtpRecived = true;
                    etOptRecevier.setText(code);//set code in edit text
                    llOtp.setVisibility(View.INVISIBLE);
                    //then you can send verification code to server
                    if (myCountDownTimer != null) {
                        myCountDownTimer.onFinish();
                    }
                    saveLoginDetail();
                }
            }
        });

        etOptRecevier.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() == 4) {
                    submitBtn.setAlpha(1.0f);
                    submitBtn.setEnabled(true);
                    submitBtn.setText(getResources().getString(R.string.verify));
                    llOtp.setVisibility(View.INVISIBLE);
                } else if ((charSequence.length() == 0) && !isOtpRecived) {
                    submitBtn.setAlpha(1.0f);
                    submitBtn.setEnabled(true);
                    submitBtn.setText(getResources().getString(R.string.resend_otp));
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

    /**
     * create a user if not exist
     */
    private void saveLoginDetail() {
        if (null != progressDialog && progressDialog.isShowing()) {
            //already showing progress dialog
        } else {
            progressDialog.show();
        }
        String email = null;
            if (getIntent().hasExtra(Constant.INPUT_EMAIL)) {
                email = getIntent().getStringExtra(Constant.INPUT_EMAIL);
            } else {
                email = getIntent().getStringExtra("phone").trim() + "@gmail.com";
            }
        LoginUserModel user = new LoginUserModel(email, getIntent().getStringExtra(Constant.INPUT_USER), getIntent().getStringExtra("phone").trim());
        if(Constant.isNetworkAvailable(ConfirmOtpActivity.this)) {
            App.getApiController().confirmOtp(this, user, RequestParams.TYPE_CREATE_USER);
        } else {
            Constant.showToastMessage(ConfirmOtpActivity.this, getString(R.string.no_internet));
        }
    }

    @Override
    public void onResponse(Response result, String type) {
        progressDialog.dismiss();
        if (type.equalsIgnoreCase(RequestParams.TYPE_CREATE_USER)) {
            Log.i("RESPONSE FROM server=", "TYPE_CREATE_USER   " + result.body());
            if (result.isSuccessful()) {
                CreateUserResponse response = (CreateUserResponse) result.body();
                if (null != response && !Constant.checkString(response.getError())) {
                    sessionManager.createLoginSession(response.getFirstName(), response.getLastName(), response.getEmail());
                    sessionManager.setKeyUserId(response.getId());
                    navigateToDashBoard();
                } else if (null != response) {
                    Constant.showToastMessage(ConfirmOtpActivity.this, response.getError());
                }
            } else {
                try {
                    JSONObject jObjError = new JSONObject(result.errorBody().string());
                    if (jObjError.has("error")) {
                        Constant.showToastMessage(ConfirmOtpActivity.this, jObjError.getString("error"));
                    }
                } catch (Exception e) {
                    Constant.showToastMessage(ConfirmOtpActivity.this, "Something went wrong");
                }

            }
        }

    }

    private void navigateToDashBoard() {
        Intent it = new Intent(ConfirmOtpActivity.this, AdminAgentActivity.class);
        it.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(it);
        finish();
    }

    @Override
    public void onFailure(String message) {
        Constant.showToastMessage(ConfirmOtpActivity.this, message);
    }

    public class MyCountDownTimer extends CountDownTimer {

        public MyCountDownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
            llOtp.setVisibility(View.VISIBLE);
            submitBtn.setAlpha(0.4f);// diming the brightness of button
            submitBtn.setEnabled(false);
            submitBtn.setText(getResources().getString(R.string.verify));
        }

        @Override
        public void onTick(long millisUntilFinished) {
            int progress = (int) (millisUntilFinished / 1000);
            timerText.setText(getResources().getString(R.string.waiting) + Constant.WHITE_SPACE + progress +
                    Constant.WHITE_SPACE + getResources().getString(R.string.second));
        }

        @Override
        public void onFinish() {
            submitBtn.setAlpha(1.0f);
            submitBtn.setEnabled(true);
            llOtp.setVisibility(View.INVISIBLE);
            if (Constant.checkString(etOptRecevier.getText().toString()) || isOtpRecived) {
                submitBtn.setText(getResources().getString(R.string.verify));
            } else {
                submitBtn.setText(getResources().getString(R.string.resend_otp));
            }
        }
    }
}
