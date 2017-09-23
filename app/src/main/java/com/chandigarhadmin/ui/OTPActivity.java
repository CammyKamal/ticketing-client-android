package com.chandigarhadmin.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
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

import static com.chandigarhadmin.R.id.timer;

/**
 * Created by harendrasinghbisht on 23/09/17.
 */

public class OTPActivity extends AppCompatActivity implements View.OnClickListener, ResponseCallback {
    private EditText etPhoneNumber, etOptRecevier;
    private Button submitBtn;
    private boolean isOtpReceived = false;
    private SessionManager sessionManager;
    private SmsVerifyCatcher smsVerifyCatcher;
    private ProgressDialog progressDialog;
    private int time=0;
    private LinearLayout llOtp;
    Timer t = new Timer();

    TimerTask task,timerTask;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);
        sessionManager = new SessionManager(this);
        progressDialog = Constant.createDialog(this, null);
        init();
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
                    submitBtn.setText("Submit");
                    //create user api call;

                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void init() {
        etPhoneNumber = (EditText) findViewById(R.id.etphonenumber);
        etOptRecevier = (EditText) findViewById(R.id.et_readotp);
        submitBtn = (Button) findViewById(R.id.submitbtn);
        submitBtn.setOnClickListener(this);
        TextView tvCountryCode = (TextView) findViewById(R.id.tvcountrycode);
        submitBtn.setTypeface(Typeface.createFromAsset(getAssets(), "stc.otf"));
        tvCountryCode.setTypeface(Typeface.createFromAsset(getAssets(), "stc.otf"));
        //  tvCountryCode.setTypeface(Typeface.createFromAsset(getAssets(), "stc.otf"));
        ((TextView) findViewById(R.id.titletv)).setTypeface(Typeface.createFromAsset(getAssets(), "stc.otf"));
        etPhoneNumber.setTypeface(Typeface.createFromAsset(getAssets(), "stc.otf"));
        etOptRecevier.setTypeface(Typeface.createFromAsset(getAssets(), "stc.otf"));
         llOtp=(LinearLayout)findViewById(R.id.ll_otp);
        llOtp.setVisibility(View.GONE);
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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.submitbtn:
                if (validatePhoneNumber()) {
                    checkClickAction();
                }
                break;

        }
    }

    private boolean validatePhoneNumber() {
        if (!TextUtils.isEmpty(etPhoneNumber.getText()) & etPhoneNumber.getText().length() == 10) {
            return true;
        } else {
            etPhoneNumber.setError("Enter correct phone number");
        }
        return false;
    }

    private void checkClickAction() {
        if (!isOtpReceived) {
            // submitBtn.setText("Resend OTP");
            getUserByEmail(etPhoneNumber.getText().toString());
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
                navigateToDashBoard();
            }
        } else if (type.equalsIgnoreCase(RequestParams.TYPE_GET_USER_BY)) {

            try {
                JSONObject response = new JSONObject(result);

                if (response.has("error") && response.getString("error").equalsIgnoreCase("User not found.")) {
                    //send otp on mobile number
                   // progressDialog.show();
                    isOtpReceived=true;
                    llOtp.setVisibility(View.VISIBLE);
                    startTimer();

                } else if (response.has(RequestParams.EMAIL) && !result.equals("Failed")) {
                    sessionManager.createLoginSession(response.getString("first_name"), response.getString("last_name"), response.getString("email"));
                    navigateToDashBoard();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

    }

    private void navigateToDashBoard() {
        Intent it = new Intent(OTPActivity.this, AdminAgentActivity.class);
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
                        tv1.setText("waiting " + time + "sec");
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


    /*public void initializeTimerTask() {

            timerTask = new TimerTask() {

                public void run() {

                    //use a handler to run a toast that shows the current timestamp

                    handler.post(new Runnable() {

                        public void run() {

                            //get the current timeStamp

                            Calendar calendar = Calendar.getInstance();

                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd:MMMM:yyyy HH:mm:ss a");

                            final String strDate = simpleDateFormat.format(calendar.getTime());



                            int duration = Toast.LENGTH_SHORT;

                            Toast toast = Toast.makeText(getApplicationContext(), strDate, duration);

                            toast.show();

                        }

                    });

                }

            };

        }*/

}
