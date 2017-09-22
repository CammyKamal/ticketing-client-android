package com.chandigarhadmin.ui;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.chandigarhadmin.R;
import com.chandigarhadmin.models.ChatPojoModel;
import com.chandigarhadmin.models.CreateTicketResponse;

public class ViewTicketActivity extends AppCompatActivity{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_ticket);
        ChatPojoModel chatPojoModel= (ChatPojoModel) getIntent().getExtras().getSerializable("createTicketResponse");
        CreateTicketResponse createTicketResponse=chatPojoModel.getCreateTicketResponse();
        Log.e("Response",createTicketResponse.toString());
    }
}
