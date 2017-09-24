package com.chandigarhadmin.adapter;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chandigarhadmin.R;
import com.chandigarhadmin.interfaces.SelectionCallbacks;
import com.chandigarhadmin.models.ChatPojoModel;
import com.chandigarhadmin.ui.ViewTicketActivity;

import java.util.List;

import static com.chandigarhadmin.utils.Constant.INPUT_TICKET_DATA;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.MyViewHolder> {

    private List<ChatPojoModel> chatPojoModelList;
    private Context context;
    private LayoutInflater layoutInflater;
    private SelectionCallbacks selectionCallbacks;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView inMessageTv, outMessageTv, tvTicketMessage;
        private RelativeLayout llInputLayout, llOutputLayout;
        private ViewPager branchesViewPager;
        private LinearLayout viewTicketll;
        private CardView ticketCreatedCardview;

        public MyViewHolder(View view) {
            super(view);
            inMessageTv = (TextView) view.findViewById(R.id.tvuserinput);
            outMessageTv = (TextView) view.findViewById(R.id.tvresponse);
            llInputLayout = (RelativeLayout) view.findViewById(R.id.inputlayoutll);
            llOutputLayout = (RelativeLayout) view.findViewById(R.id.outputlayoutll);
            branchesViewPager = (ViewPager) view.findViewById(R.id.branchesviewpager);
            ticketCreatedCardview = (CardView) view.findViewById(R.id.card_view);
            tvTicketMessage = (TextView) view.findViewById(R.id.tvticketcreated);
            viewTicketll = (LinearLayout) view.findViewById(R.id.viewll);
        }
    }


    public ChatAdapter(Context context, List<ChatPojoModel> chatPojoModelList, SelectionCallbacks selectionCallbacks) {
        this.chatPojoModelList = chatPojoModelList;
        this.context = context;
        this.selectionCallbacks = selectionCallbacks;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_chatlayout, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final ChatPojoModel chatPojoModel = chatPojoModelList.get(position);
        if (chatPojoModel.isAlignRight()) {
            holder.inMessageTv.setText(Html.fromHtml(chatPojoModel.getInput()));
            holder.llOutputLayout.setVisibility(View.GONE);
            holder.llInputLayout.setVisibility(View.VISIBLE);
            holder.branchesViewPager.setVisibility(View.GONE);
            holder.ticketCreatedCardview.setVisibility(View.GONE);
        } else {
            if (chatPojoModel.getDepartmentResponse() != null) {
                holder.branchesViewPager.setVisibility(View.VISIBLE);
                holder.branchesViewPager.setAdapter(new CustomPagerAdapter(context, chatPojoModel.getDepartmentResponse(), selectionCallbacks));
                holder.llOutputLayout.setVisibility(View.GONE);
                holder.llInputLayout.setVisibility(View.GONE);
                holder.ticketCreatedCardview.setVisibility(View.GONE);

            } else if (chatPojoModel.getInput().contains("Reference")) {
                holder.llOutputLayout.setVisibility(View.GONE);
                holder.llInputLayout.setVisibility(View.GONE);
                holder.branchesViewPager.setVisibility(View.GONE);

                holder.ticketCreatedCardview.setVisibility(View.VISIBLE);
                holder.tvTicketMessage.setText(chatPojoModel.getInput());
                holder.viewTicketll.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (null != chatPojoModel.getGetTicketResponse().get(position)) {
                            Intent intent = new Intent(context, ViewTicketActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putSerializable(INPUT_TICKET_DATA, chatPojoModel.getGetTicketResponse().get(position));
                            intent.putExtras(bundle);
                            context.startActivity(intent);
                        }
                    }
                });
            } else if (chatPojoModel.getGetTicketResponse() != null) {
                holder.branchesViewPager.setVisibility(View.VISIBLE);
                holder.branchesViewPager.setAdapter(new TicketPagerAdapter(context, chatPojoModel.getGetTicketResponse(), selectionCallbacks));
                holder.llOutputLayout.setVisibility(View.GONE);
                holder.llInputLayout.setVisibility(View.GONE);

            } else {
                holder.llOutputLayout.setVisibility(View.VISIBLE);
                holder.llInputLayout.setVisibility(View.GONE);
                holder.outMessageTv.setText(Html.fromHtml(chatPojoModel.getInput()));
                holder.branchesViewPager.setVisibility(View.GONE);
                holder.ticketCreatedCardview.setVisibility(View.GONE);
            }
        }

    }

    @Override
    public int getItemCount() {
        return chatPojoModelList.size();
    }
}