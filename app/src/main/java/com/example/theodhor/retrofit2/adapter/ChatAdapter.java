package com.example.theodhor.retrofit2.adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.theodhor.retrofit2.R;
import com.example.theodhor.retrofit2.net.ChatPojoModel;

import java.util.List;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.MyViewHolder> {

    private List<ChatPojoModel> chatPojoModelList;
    private Context context;
    private LayoutInflater layoutInflater;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView inMessageTv, outMessageTv;
        private LinearLayout productListContainer;

        public MyViewHolder(View view) {
            super(view);
            productListContainer = (LinearLayout) view.findViewById(R.id.productcontainer);
            inMessageTv = (TextView) view.findViewById(R.id.inmessahetv);
            outMessageTv = (TextView) view.findViewById(R.id.outmessahetv);
        }
    }


    public ChatAdapter(Context context, List<ChatPojoModel> chatPojoModelList) {
        this.chatPojoModelList = chatPojoModelList;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.chat_layout, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        ChatPojoModel chatPojoModel = chatPojoModelList.get(position);
        if (chatPojoModel.getType().equalsIgnoreCase("userinput")) {
            holder.inMessageTv.setText(chatPojoModel.getUserInput());
            holder.outMessageTv.setVisibility(View.GONE);
            holder.productListContainer.setVisibility(View.GONE);
        } else if (chatPojoModel.getType().equalsIgnoreCase("showCategories")) {
            holder.inMessageTv.setVisibility(View.GONE);
            holder.productListContainer.setVisibility(View.GONE);
            String outMessage = "";
            for (int i = 0; i < chatPojoModel.getResponse().body().getValue().size(); i++) {
                if (i == 0)
                    outMessage = chatPojoModel.getResponse().body().getValue().get(i).getTitle() + ", ";
                else if (i != chatPojoModel.getResponse().body().getValue().size() - 1)
                    outMessage = outMessage + chatPojoModel.getResponse().body().getValue().get(i).getTitle() + ", ";
                else
                    outMessage = outMessage + chatPojoModel.getResponse().body().getValue().get(i).getTitle();
            }
            holder.outMessageTv.setText("We have " + outMessage);
        } else if (chatPojoModel.getType().equalsIgnoreCase("productsTitles")) {
            holder.inMessageTv.setVisibility(View.GONE);
            holder.productListContainer.setVisibility(View.GONE);
            String outMessage = "";
            for (int i = 0; i < chatPojoModel.getResponse().body().getValue().size(); i++) {
                if (i == 0) {
                    if (chatPojoModel.getResponse().body().getValue().get(i).getParent() != null &&
                            !chatPojoModel.getResponse().body().getValue().get(i).getParent().equals("null")) {
                        outMessage = chatPojoModel.getResponse().body().getValue().get(i).getTitle() + ", ";
                    }
                } else if (i != chatPojoModel.getResponse().body().getValue().size() - 1) {
                    if (chatPojoModel.getResponse().body().getValue().get(i).getParent() != null &&
                            !chatPojoModel.getResponse().body().getValue().get(i).getParent().equals("null")) {
                        outMessage = outMessage + chatPojoModel.getResponse().body().getValue().get(i).getTitle() + ", ";
                    }
                } else {
                    if (chatPojoModel.getResponse().body().getValue().get(i).getParent() != null &&
                            !chatPojoModel.getResponse().body().getValue().get(i).getParent().equals("null")) {
                        outMessage = outMessage + chatPojoModel.getResponse().body().getValue().get(i).getTitle();
                    }
                }
            }
            holder.outMessageTv.setText("We have " + outMessage + ". See anything you like? Just ask for it.");
        } else if (chatPojoModel.getType().equalsIgnoreCase("productSearch")) {
            holder.inMessageTv.setVisibility(View.GONE);
            holder.productListContainer.setVisibility(View.VISIBLE);
            holder.outMessageTv.setText("I found " + chatPojoModel.getProductSearchResponse().body().getValue().size() + " products and here are the best matches. Tap on the image to take a closer look.");
            layoutInflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);

            for (int i = 0; i < chatPojoModel.getProductSearchResponse().body().getValue().size(); i++) {
                View view = layoutInflater.inflate(R.layout.row_products, null);
                TextView mTitle=(TextView)view.findViewById(R.id.productnametv);
                TextView mPrice=(TextView)view.findViewById(R.id.productpricetv);
                TextView productDesc=(TextView)view.findViewById(R.id.productdesctv);

                mTitle.setText(chatPojoModel.getProductSearchResponse().body().getValue().get(i).getTitle());
                mPrice.setText(chatPojoModel.getProductSearchResponse().body().getValue().get(i).getPrice()+"");
                productDesc.setText(chatPojoModel.getProductSearchResponse().body().getValue().get(i).getDescription());
                holder.productListContainer.addView(view);
            }
        }
    }

    @Override
    public int getItemCount() {
        return chatPojoModelList.size();
    }
}