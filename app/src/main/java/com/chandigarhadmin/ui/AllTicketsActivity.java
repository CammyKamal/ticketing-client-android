package com.chandigarhadmin.ui;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.chandigarhadmin.R;
import com.chandigarhadmin.adapter.AllTicketAdapter;
import com.chandigarhadmin.interfaces.ResponseCallback;
import com.chandigarhadmin.models.GetTicketResponse;
import com.chandigarhadmin.service.ApiServiceTask;
import com.chandigarhadmin.service.JSONParser;
import com.chandigarhadmin.utils.Constant;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.chandigarhadmin.models.RequestParams.TYPE_GET_ALL_TICKET;

/**
 * Created by harendrasinghbisht on 24/09/17.
 */

public class AllTicketsActivity extends AppCompatActivity implements ResponseCallback {
    @BindView(R.id.ticketrecyleview)
    RecyclerView recyclerView;
    @BindView(R.id.closebtn)
    ImageView closeBtn;
    private ProgressDialog progressDialog;
    private AllTicketAdapter allTicketAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alltickets);
        ButterKnife.bind(this);
        progressDialog = Constant.createDialog(this, null);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        //mLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        allTicketAdapter = new AllTicketAdapter(this);
        recyclerView.setAdapter(allTicketAdapter);
        if (Constant.isNetworkAvailable(this)) {
            getAlltickets();
        } else {
            Constant.showToastMessage(this, getString(R.string.no_internet));
        }

    }

    @OnClick(R.id.closebtn)
    void closeScreen(View view) {
        if (view.getId() == R.id.closebtn) {
            finish();
        }
    }

    /**
     * getting all tickets
     */
    private void getAlltickets() {
        progressDialog.show();
        ApiServiceTask apiServiceTask = new ApiServiceTask(this, this, TYPE_GET_ALL_TICKET);
        apiServiceTask.setRequestParams(null, JSONParser.GET);
        apiServiceTask.execute(Constant.BASE + "tickets");
    }

    @Override
    public void onResponse(String result, String type) {
        progressDialog.hide();
        if (!result.contains("error") && !result.equalsIgnoreCase("Failed")) {
            if (type.equalsIgnoreCase(TYPE_GET_ALL_TICKET)) {
                GsonBuilder gsonBuilder = new GsonBuilder();
                gsonBuilder.setDateFormat("M/d/yy hh:mm a");
                Gson gson = gsonBuilder.create();
                parseTickets(result, gson);
            }
        }

    }

    private void parseTickets(String result, Gson gson) {
        List<GetTicketResponse> ticketResponseList = Arrays.asList(gson.fromJson(result, GetTicketResponse[].class));
        allTicketAdapter.setData(ticketResponseList);
        allTicketAdapter.notifyDataSetChanged();
    }
}
