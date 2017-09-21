package com.chandigarhadmin.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chandigarhadmin.R;
import com.chandigarhadmin.interfaces.SelectionCallbacks;
import com.chandigarhadmin.models.GetTicketResponse;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

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
        this.selectionCallbacks=selectionCallbacks;
        this.layoutInflater = (LayoutInflater) this.context.getSystemService(this.context.LAYOUT_INFLATER_SERVICE);
        this.getTicketResponseList = getTicketResponseList;
        //imageLoader = ImageLoader.getInstance();
        //imageLoader.init(ImageLoaderConfiguration.createDefault(context));

    }
    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return false;
    }
    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        View view = this.layoutInflater.inflate(R.layout.row_departments, container, false);
        TextView tvBranchName = (TextView) view.findViewById(R.id.branchnametv);
        CircleImageView circleImageView = (CircleImageView) view.findViewById(R.id.departmentiv);
        /*tvBranchName.setText(dataObjectList.get(position).getName());
        if (dataObjectList.get(position).getLogo() != null && dataObjectList.get(position).getLogo().getPathname() != null)
            imageLoader.displayImage("http://95.85.55.146" + dataObjectList.get(position).getLogo().getPathname(), circleImageView);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectionCallbacks.onResultSelection(dataObjectList.get(position).getId(),dataObjectList.get(position).getName());
            }
        });*/
        container.addView(view);
        return view;
    }
}

