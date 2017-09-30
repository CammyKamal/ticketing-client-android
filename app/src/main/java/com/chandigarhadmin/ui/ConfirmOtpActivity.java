package com.chandigarhadmin.ui;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
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
import com.chandigarhadmin.controller.ApiController;
import com.chandigarhadmin.interfaces.ResponseCallback;
import com.chandigarhadmin.models.CreateUserResponse;
import com.chandigarhadmin.models.LoginUser;
import com.chandigarhadmin.models.RequestParams;
import com.chandigarhadmin.session.SessionManager;
import com.chandigarhadmin.utils.Constant;

import java.util.List;
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
    private boolean isOtpReceived;
    //    private SmsVerifyCatcher smsVerifyCatcher;
    private ProgressDialog progressDialog;
    private SessionManager sessionManager;
    private int time=0;
    private MyCountDownTimer myCountDownTimer;
    private ApiController apiController;

    @OnClick(R.id.btn_confirm_otp)
    public void submitButton() {
        if (submitBtn.getText().toString().trim().equalsIgnoreCase("resend_otp")) {
            myCountDownTimer.start();
        } else {
            if (!TextUtils.isEmpty(etOptRecevier.getText())) {
//                saveLoginDetail();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_otp);
        ButterKnife.bind(this);

        apiController = new ApiController();
        submitBtn.setAlpha(0.7f);// diming the brightness of button
        submitBtn.setEnabled(false);
        sessionManager = new SessionManager(this);
        progressDialog = Constant.createDialog(this, null);
        getallUsers();
//        if (getIntent().hasExtra("phone")) {
//            getUserByEmail(getIntent().getStringExtra("phone"));
//            saveLoginDetail();
//        }

//        smsVerifyCatcher = new SmsVerifyCatcher(this, new OnSmsCatchListener<String>() {
//            @Override
//            public void onSmsCatch(String message) {
//
//                String code = parseCode(message);//Parse verification code
//                if(null!=code&&!code.isEmpty()) {
//                    isOtpReceived = true;
//                    progressDialog.hide();
//                    etOptRecevier.setText(code);//set code in edit text
//                    //then you can send verification code to server
//                    if (myCountDownTimer != null) {
//                        myCountDownTimer.onFinish();
//                    }
//                    saveLoginDetail();
//                }
//            }
//        });

        etOptRecevier.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() == 4) {
                    isOtpReceived = true;
                    submitBtn.setAlpha(1.0f);
                    submitBtn.setEnabled(true);
                    submitBtn.setText(R.string.submit);
                    llOtp.setVisibility(View.GONE);
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
//        smsVerifyCatcher.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
//        smsVerifyCatcher.onStop();
    }

    /**
     * need for Android 6 real time permissions
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        smsVerifyCatcher.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void checkClickAction() {
        if (!isOtpReceived) {
            if (getIntent().hasExtra("phone")) {
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
        LoginUser user = new LoginUser(email, getIntent().getStringExtra(Constant.INPUT_USER), getIntent().getStringExtra("phone").trim());
        if(Constant.isNetworkAvailable(ConfirmOtpActivity.this)) {
//            apiController.confirmOtp(this, user, RequestParams.TYPE_CREATE_USER);
        } else {
            Constant.showToastMessage(ConfirmOtpActivity.this, getString(R.string.no_internet));
        }
    }

    private void getUserByEmail(String email) {
        if(Constant.isNetworkAvailable(ConfirmOtpActivity.this)) {
            if (null == progressDialog) {
                progressDialog.show();
            }
            apiController.getUserByEmail(this, email, RequestParams.TYPE_GET_USER_BY);
        } else {
            Constant.showToastMessage(ConfirmOtpActivity.this, getString(R.string.no_internet));
        }

    }

    @Override
    public void onResponse(Response result, String type) {
        progressDialog.dismiss();
        Log.i("RESPONSE FROM server=", "" + result);
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
                Constant.showToastMessage(ConfirmOtpActivity.this, "Something went wrong");
            }
        } else if (type.equalsIgnoreCase(RequestParams.TYPE_GET_USER_BY)) {
            if (result.isSuccessful()) {
                List<CreateUserResponse> response = (List<CreateUserResponse>) result.body();
                if (null != response && !response.isEmpty()) {
                    for (CreateUserResponse user : response) {
                        if (user.getEmail().equalsIgnoreCase(getIntent().getStringExtra("phone").trim() + "@gmail.com")) {
                            Constant.showToastMessage(ConfirmOtpActivity.this, "found");
                            break;
                        }
                    }
//                    sessionManager.createLoginSession(response.getFirstName(), response.getLastName(), response.getEmail());
//                    sessionManager.setKeyUserId(response.getId());
//                    navigateToDashBoard();
                }
            } else {
                Constant.showToastMessage(ConfirmOtpActivity.this, "Something went wrong");
            }
//            try {
//                JSONObject response = new JSONObject(result);
//
//                if (response.has("error") && response.getString("error").equalsIgnoreCase("User not found.")) {
//                    //send otp on mobile number
//                    llOtp.setVisibility(View.VISIBLE);
//                    //isOtpReceived=true;
//                    myCountDownTimer = new MyCountDownTimer(20000, 1000);
//                    myCountDownTimer.start();
//
//                } else if (response.has(RequestParams.EMAIL) && !result.equals("Failed")) {
//                    sessionManager.createLoginSession(response.getString("first_name"), response.getString("last_name"), response.getString("email"));
//                    sessionManager.setKeyUserId(response.getString("id"));
//                    navigateToDashBoard();
//                }
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }

        }

    }

    private void navigateToDashBoard() {
        Intent it = new Intent(ConfirmOtpActivity.this, AdminAgentActivity.class);
        it.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(it);
        finish();
    }

    private void getallUsers() {
        if (Constant.isNetworkAvailable(ConfirmOtpActivity.this)) {
            if (null == progressDialog) {
                progressDialog.show();
            }
            apiController.getAllUsers(this, RequestParams.TYPE_GET_USER_BY);
//            ApiServiceTask task = new ApiServiceTask(this, this, RequestParams.TYPE_GET_USER_BY);
//            task.setRequestParams(null, JSONParser.GET);
//            task.execute(Constant.BASE + "users");
        } else {
            Constant.showToastMessage(ConfirmOtpActivity.this, getString(R.string.no_internet));
        }
    }

    @Override
    public void onFailure(String message) {
        Constant.showToastMessage(ConfirmOtpActivity.this, message);
    }

    public class MyCountDownTimer extends CountDownTimer {

        public MyCountDownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            int progress = (int) (millisUntilFinished / 1000);
            timerText.setText("waiting " + progress + "sec");
            if (progress == 0) {
                llOtp.setVisibility(View.GONE);
                submitBtn.setText(getResources().getString(R.string.resend_otp));
            }
        }

        @Override
        public void onFinish() {
            llOtp.setVisibility(View.GONE);
            submitBtn.setText(getResources().getString(R.string.resend_otp));
        }
    }
}
