package com.chandigarhadmin.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chandigarhadmin.R;
import com.chandigarhadmin.interfaces.SelectionCallbacks;
import com.chandigarhadmin.models.GetTicketResponse;
import com.chandigarhadmin.ui.ViewTicketActivity;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.chandigarhadmin.utils.Constant.INPUT_TICKET_DATA;

/**
 * Created by harendrasinghbisht on 23/09/17.
 */

public class TicketPagerAdapter extends PagerAdapter {
    private Context context;
    private List<GetTicketResponse> getTicketResponseList;
    private LayoutInflater layoutInflater;
    private SelectionCallbacks selectionCallbacks;

    public TicketPagerAdapter(Context context, List<GetTicketResponse> getTicketResponseList, SelectionCallbacks selectionCallbacks) {
        this.context = context;
        this.selectionCallbacks = selectionCallbacks;
        this.layoutInflater = (LayoutInflater) this.context.getSystemService(this.context.LAYOUT_INFLATER_SERVICE);
        this.getTicketResponseList = getTicketResponseList;
        //imageLoader = ImageLoader.getInstance();
        //imageLoader.init(ImageLoaderConfiguration.createDefault(context));

    }

    @Override
    public float getPageWidth(final int position) {
        return 0.50f;
    }

    @Override
    public int getCount() {
        return getTicketResponseList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((View) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        View view = this.layoutInflater.inflate(R.layout.row_departments, container, false);
        TextView tvBranchName = (TextView) view.findViewById(R.id.branchnametv);
        CircleImageView circleImageView = (CircleImageView) view.findViewById(R.id.departmentiv);
        tvBranchName.setText(getTicketResponseList.get(position).getSubject());
        //if (dataObjectList.get(position).getLogo() != null && dataObjectList.get(position).getLogo().getPathname() != null)
        // imageLoader.displayImage("http://95.85.55.146" + dataObjectList.get(position).getLogo().getPathname(), circleImageView);
        circleImageView.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ticket_img));
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ViewTicketActivity.class);
                if (null != getTicketResponseList && null != getTicketResponseList.get(position)) {
                    intent.putExtra(INPUT_TICKET_DATA, getTicketResponseList.get(position));
                }
                context.startActivity(intent);
            }
        });
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}

