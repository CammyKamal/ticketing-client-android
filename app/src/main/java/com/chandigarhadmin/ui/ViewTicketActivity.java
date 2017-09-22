package com.chandigarhadmin.ui;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chandigarhadmin.R;
import com.chandigarhadmin.interfaces.ResponseCallback;
import com.chandigarhadmin.models.GetTicketResponse;
import com.chandigarhadmin.models.RequestParams;
import com.chandigarhadmin.models.SingleTicketResponse;
import com.chandigarhadmin.service.ApiServiceTask;
import com.chandigarhadmin.service.JSONParser;
import com.chandigarhadmin.utils.Constant;
import com.google.gson.Gson;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ViewTicketActivity extends AppCompatActivity implements ResponseCallback {
    @BindView(R.id.tvcreated_value)
    TextView textViewCreatedTime;
    @BindView(R.id.text_status)
    TextView textViewStatus;
    @BindView(R.id.crossicon)
    ImageView closeImage;
    @BindView(R.id.tvticket_refrence)
    TextView textViewTicketId;
    @BindView(R.id.tvticket_assigne)
    TextView textViewAssignee;
    @BindView(R.id.tvsubject_value)
    TextView textViewSubject;
    @BindView(R.id.tvdescription_value)
    TextView textViewDescription;
    private ProgressDialog progressDialog;
    private GetTicketResponse getTicketResponse;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_ticket);
        ButterKnife.bind(this);
        progressDialog = Constant.createDialog(this, null);
        getTicketResponse = (GetTicketResponse) getIntent().getExtras().getSerializable(Constant.INPUT_TICKET_DATA);
        Log.e("Response", getTicketResponse.toString());
    }


    private void setValues(SingleTicketResponse getTicketResponse, GetTicketResponse previous) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (null != getTicketResponse.getCreatedAt() && getTicketResponse.getCreatedAt().contains("T")) {
            String[] date_split = getTicketResponse.getCreatedAt().split("T");
            try {
                Date date = sdf.parse(date_split[0] + " " + date_split[1].split("\\+")[0]);
                CharSequence dateCreation = DateUtils.getRelativeTimeSpanString(date.getTime(), new Date().getTime(),
                        DateUtils.MINUTE_IN_MILLIS,
                        DateUtils.FORMAT_NO_NOON);
                textViewCreatedTime.setText(dateCreation);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        if (null == getTicketResponse.getStatus()) {
            getTicketResponse.setStatus("NA");
        }

        textViewStatus.setText(getTicketResponse.getStatus());
        if (null == getTicketResponse.getId()) {
            textViewTicketId.setText("Ticket Refrence: NA");
        } else {
            textViewTicketId.setText("Ticket Refrence: " + getTicketResponse.getId());
        }

        if (null == previous.getAssignee()) {
            previous.setAssignee("NA");
        }
        textViewAssignee.setText(previous.getAssignee());
        if (null == getTicketResponse.getSubject()) {
            getTicketResponse.setSubject("NA");
        }
        textViewSubject.setText(getTicketResponse.getSubject());
        if (null == getTicketResponse.getDescription()) {
            getTicketResponse.setSubject("NA");
        }
        textViewSubject.setText(getTicketResponse.getSubject());
    }

    @OnClick(R.id.crossicon)
    void submitButton(View view) {
        if (view.getId() == R.id.crossicon) {
            finish();
        }
    }

    /**
     * getting ticket by Id
     *
     * @param getTicketResponse
     */
    private void getTicketById(GetTicketResponse getTicketResponse) {
        progressDialog.show();
        ApiServiceTask apiServiceTask = new ApiServiceTask(this, this, RequestParams.TYPE_GET_TICKET_BY);
        apiServiceTask.setRequestParams(null, JSONParser.GET);
        apiServiceTask.execute(Constant.BASE + "tickets/" + getTicketResponse.getId());

    }

    @Override
    public void onResponse(String result, String type) {
        progressDialog.hide();
        if (!result.contains("error") && !result.equalsIgnoreCase("Failed")) {
            Gson gson = new Gson();
            SingleTicketResponse singleTicketResponse = gson.fromJson(result, SingleTicketResponse.class);
            setValues(singleTicketResponse, getTicketResponse);
        }
    }
}
