package com.chandigarhadmin.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import com.chandigarhadmin.App;
import com.chandigarhadmin.R;
import com.chandigarhadmin.interfaces.ResponseCallback;
import com.chandigarhadmin.models.CreateUserResponse;
import com.chandigarhadmin.models.RequestParams;
import com.chandigarhadmin.utils.Constant;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Response;

public class OTPActivity extends AppCompatActivity implements ResponseCallback {
    @BindView(R.id.etphonenumber)
    EditText etPhoneNumber;
    @BindView(R.id.submitbtn)
    Button submitBtn;
    private ProgressDialog progressDialog;

    @OnClick(R.id.submitbtn)
    public void submitClick() {
        if (validatePhoneNumber()) {
            getUserByEmail(etPhoneNumber.getText().toString());
        }
    }

    private void getUserByEmail(String email) {
        if (Constant.isNetworkAvailable(OTPActivity.this)) {
            if (!progressDialog.isShowing()) {
                progressDialog.show();
            }
            App.getApiController().getUserByEmail(this, email, RequestParams.TYPE_GET_USER_BY);
        } else {
            Constant.showToastMessage(OTPActivity.this, getString(R.string.no_internet));
        }

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);
        ButterKnife.bind(this);
        progressDialog = Constant.createDialog(this, null);
        submitBtn.setEnabled(false);
        submitBtn.setAlpha(0.4f);   //its like diming the brightness og button
        etPhoneNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() == 10) {
                    submitBtn.setAlpha(1.0f);
                    submitBtn.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private boolean validatePhoneNumber() {
        if (!TextUtils.isEmpty(etPhoneNumber.getText()) & etPhoneNumber.getText().length() == 10) {
            return true;
        } else {
            etPhoneNumber.setError(getString(R.string.phone_error));
        }
        return false;
    }

    @Override
    public void onResponse(Response result, String type) {
        progressDialog.dismiss();
        Log.i("RESPONSE FROM server=", "" + result);

        if (result.isSuccessful()) {
            CreateUserResponse response = (CreateUserResponse) result.body();

            //checking whether email returned in response matching with passed email or not
            if (null != response && Constant.checkString(response.getEmail())
                    && response.getEmail().contains(etPhoneNumber.getText().toString())) {
                Constant.showToastMessage(OTPActivity.this, getString(R.string.email_exist));
            }
        } else {
            try {
                JSONObject jObjError = new JSONObject(result.errorBody().string());
                if (jObjError.has("error")) {
                    navigateToConfirmOtp();
                }
            } catch (Exception e) {
                Constant.showToastMessage(OTPActivity.this, getString(R.string.womething_wrong));
            }
        }

    }

    private void navigateToConfirmOtp() {
        Intent confirmOtp = new Intent(OTPActivity.this, ConfirmOtpActivity.class);
        confirmOtp.putExtra("phone", etPhoneNumber.getText().toString());
        confirmOtp.putExtra(Constant.INPUT_USER, getIntent().getStringExtra(Constant.INPUT_USER));
        if (null != getIntent() && getIntent().hasExtra(Constant.INPUT_EMAIL)) {
            confirmOtp.putExtra(Constant.INPUT_EMAIL, getIntent().getStringExtra(Constant.INPUT_EMAIL));
        }
        startActivity(confirmOtp);
    }

    @Override
    public void onFailure(String message) {
        Constant.showToastMessage(OTPActivity.this, message);
    }

}
