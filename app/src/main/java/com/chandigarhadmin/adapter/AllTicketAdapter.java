package com.chandigarhadmin.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chandigarhadmin.R;
import com.chandigarhadmin.interfaces.SelectionCallbacks;
import com.chandigarhadmin.models.GetTicketResponse;
import com.chandigarhadmin.ui.ViewTicketActivity;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.chandigarhadmin.utils.Constant.INPUT_TICKET_DATA;

/**
 * Created by harendrasinghbisht on 24/09/17.
 */

public class AllTicketAdapter extends RecyclerView.Adapter<AllTicketAdapter.MyViewHolder> {

    private Context context;
    private List<GetTicketResponse> getTicketResponseList;
    private LayoutInflater layoutInflater;
    private SelectionCallbacks selectionCallbacks;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvBranchName, tvTicketId;
        CircleImageView circleImageView;
        LinearLayout linearLayoutMainRow;

        public MyViewHolder(View view) {
            super(view);
            tvBranchName = (TextView) view.findViewById(R.id.branchnametv);
            circleImageView = (CircleImageView) view.findViewById(R.id.departmentiv);
            tvTicketId = (TextView) view.findViewById(R.id.tvticketId);
            linearLayoutMainRow = (LinearLayout) view.findViewById(R.id.ll_mainrow);
        }
    }


    public AllTicketAdapter(Context context) {

        this.context = context;
    }

    public void setData(List<GetTicketResponse> getTicketResponseList) {
        this.getTicketResponseList = getTicketResponseList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_ticket, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final GetTicketResponse getTicketResponse = getTicketResponseList.get(position);
        holder.tvBranchName.setText(getTicketResponse.getSubject());
        holder.tvTicketId.setText(getTicketResponse.getId());

        holder.linearLayoutMainRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ViewTicketActivity.class);
                if (null != getTicketResponse) {

                    intent.putExtra(INPUT_TICKET_DATA, getTicketResponse);

                }
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        if (null != getTicketResponseList) {
            return getTicketResponseList.size();
        }
        return 0;
    }
}