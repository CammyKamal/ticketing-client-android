package com.chandigarhadmin.ui;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chandigarhadmin.R;
import com.chandigarhadmin.models.CreateTicketResponse;
import com.chandigarhadmin.models.GetTicketResponse;
import com.chandigarhadmin.models.SingleTicketResponse;
import com.chandigarhadmin.utils.Constant;
import com.google.gson.Gson;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.chandigarhadmin.utils.Constant.INPUT_CTICKET_DATA;
import static com.chandigarhadmin.utils.Constant.INPUT_TICKET_DATA;

public class ViewTicketActivity extends AppCompatActivity {
    //        implements ResponseCallback {
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
    private CreateTicketResponse createTicketResponse;
    private String ticketId, ticketAssignee;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_ticket);
        ButterKnife.bind(this);
        progressDialog = Constant.createDialog(this, null);
        if (getIntent().hasExtra(INPUT_TICKET_DATA)) {
            getTicketResponse = getIntent().getExtras().getParcelable(INPUT_TICKET_DATA);
            ticketAssignee = getTicketResponse.getAssignee();
            ticketId = getTicketResponse.getId();
        } else if (getIntent().hasExtra(INPUT_CTICKET_DATA)) {
            createTicketResponse = getIntent().getExtras().getParcelable(INPUT_CTICKET_DATA);
            ticketAssignee = createTicketResponse.getAsignee();
            ticketId = createTicketResponse.getId();
        }
        getTicketById(ticketId);

    }


    private void setValues(SingleTicketResponse getTicketResponse, String ticketAssignee) {
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

        textViewStatus.setText(" Ticket Status :" + getTicketResponse.getStatus());
        if (null == getTicketResponse.getId()) {
            textViewTicketId.setText("Ticket Refrence: NA");
        } else {
            textViewTicketId.setText("Ticket Refrence: " + getTicketResponse.getId());
        }

        if (null == ticketAssignee) {
            ticketAssignee = "NA";
        }
        //  textViewAssignee.setText(ticketAssignee);
        if (null == getTicketResponse.getSubject()) {
            getTicketResponse.setSubject("NA");
        }
        textViewSubject.setText(getTicketResponse.getSubject());
        if (null == getTicketResponse.getDescription()) {
            getTicketResponse.setDescription("NA");
        }
        textViewDescription.setText(Html.fromHtml(getTicketResponse.getDescription()));
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
     * @param ticketId
     */
    private void getTicketById(String ticketId) {
        progressDialog.show();
//        ApiServiceTask apiServiceTask = new ApiServiceTask(this, this, RequestParams.TYPE_GET_TICKET_BY);
//        apiServiceTask.setRequestParams(null, JSONParser.GET);
//        apiServiceTask.execute(Constant.BASE + "tickets/" + ticketId);

    }

    //    @Override
    public void onResponse(String result, String type) {
        progressDialog.hide();
        if (!result.contains("error") && !result.equalsIgnoreCase("Failed")) {
            Gson gson = new Gson();
            SingleTicketResponse singleTicketResponse = gson.fromJson(result, SingleTicketResponse.class);
            setValues(singleTicketResponse, ticketAssignee);
        }
    }
}
