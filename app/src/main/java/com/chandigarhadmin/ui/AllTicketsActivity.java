package com.chandigarhadmin.ui;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;

import com.chandigarhadmin.App;
import com.chandigarhadmin.R;
import com.chandigarhadmin.adapter.AllTicketAdapter;
import com.chandigarhadmin.interfaces.ResponseCallback;
import com.chandigarhadmin.models.GetTicketResponse;
import com.chandigarhadmin.models.RequestParams;
import com.chandigarhadmin.session.SessionManager;
import com.chandigarhadmin.utils.Constant;

import org.json.JSONObject;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Response;

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
    private SessionManager sessionManager;

    @OnClick(R.id.closebtn)
    public void closeScreenClick() {
        finish();
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alltickets);
        ButterKnife.bind(this);
        sessionManager=new SessionManager(this);
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


    /**
     * getting all tickets
     */
    private void getAlltickets() {
        progressDialog.show();
        App.getApiController().getAllTickets(this, sessionManager.getKeyUserId(), RequestParams.TYPE_GET_ALL_TICKET);
    }

    @Override
    public void onResponse(Response response, String type) {
        progressDialog.dismiss();
        if (response.isSuccessful()) {
            if (type.equalsIgnoreCase(RequestParams.TYPE_GET_ALL_TICKET)) {
                List<GetTicketResponse> tickets = (List<GetTicketResponse>) response.body();
                parseTickets(tickets);
            }
        } else {
            try {
                JSONObject jObjError = new JSONObject(response.errorBody().string());
                if (jObjError.has("error")) {
                    Constant.showToastMessage(AllTicketsActivity.this, jObjError.getString("error"));
                }
            } catch (Exception e) {
                Constant.showToastMessage(AllTicketsActivity.this, "Something went wrong");
            }
        }
    }

    @Override
    public void onFailure(String message) {
        Constant.showToastMessage(AllTicketsActivity.this, message);
    }

    private void parseTickets(List<GetTicketResponse> ticketResponseList) {
        allTicketAdapter.setData(ticketResponseList);
        allTicketAdapter.notifyDataSetChanged();
    }
}
