package com.chandigarhadmin.adapter;


import android.content.Context;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chandigarhadmin.R;
import com.chandigarhadmin.interfaces.SelectionCallbacks;
import com.chandigarhadmin.models.ChatPojoModel;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.MyViewHolder> {

    private List<ChatPojoModel> chatPojoModelList;
    private Context context;
    private LayoutInflater layoutInflater;
private SelectionCallbacks selectionCallbacks;
    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView inMessageTv, outMessageTv;
        private RelativeLayout llInputLayout, llOutputLayout;
        private ViewPager branchesViewPager;

        public MyViewHolder(View view) {
            super(view);
            inMessageTv = (TextView) view.findViewById(R.id.tvuserinput);
            outMessageTv = (TextView) view.findViewById(R.id.tvresponse);
            llInputLayout = (RelativeLayout) view.findViewById(R.id.inputlayoutll);
            llOutputLayout = (RelativeLayout) view.findViewById(R.id.outputlayoutll);
            branchesViewPager = (ViewPager) view.findViewById(R.id.branchesviewpager);
        }
    }


    public ChatAdapter(Context context, List<ChatPojoModel> chatPojoModelList, SelectionCallbacks selectionCallbacks) {
        this.chatPojoModelList = chatPojoModelList;
        this.context = context;
        this.selectionCallbacks=selectionCallbacks;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_chatlayout, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        ChatPojoModel chatPojoModel = chatPojoModelList.get(position);
        if (chatPojoModel.isAlignRight()) {
            holder.inMessageTv.setText(Html.fromHtml(chatPojoModel.getInput()));
            holder.llOutputLayout.setVisibility(View.GONE);
            holder.llInputLayout.setVisibility(View.VISIBLE);
            holder.branchesViewPager.setVisibility(View.GONE);
        } else {
            if (chatPojoModel.getDepartmentResponse() != null) {
                holder.branchesViewPager.setVisibility(View.VISIBLE);
                holder.branchesViewPager.setAdapter(new CustomPagerAdapter(context, chatPojoModel.getDepartmentResponse(),selectionCallbacks));
                holder.llOutputLayout.setVisibility(View.GONE);
                holder.llInputLayout.setVisibility(View.GONE);

            } else {
                holder.llOutputLayout.setVisibility(View.VISIBLE);
                holder.llInputLayout.setVisibility(View.GONE);
                holder.outMessageTv.setText(Html.fromHtml(chatPojoModel.getInput()));
                holder.branchesViewPager.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public int getItemCount() {
        return chatPojoModelList.size();
    }
}