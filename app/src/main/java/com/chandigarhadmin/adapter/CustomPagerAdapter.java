package com.chandigarhadmin.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chandigarhadmin.R;
import com.chandigarhadmin.interfaces.SelectionCallbacks;
import com.chandigarhadmin.models.BranchesModel;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class CustomPagerAdapter extends PagerAdapter {
    private Context context;
    private List<BranchesModel> dataObjectList;
    private LayoutInflater layoutInflater;
    private ImageLoader imageLoader;
    private SelectionCallbacks selectionCallbacks;

    public CustomPagerAdapter(Context context, List<BranchesModel> dataObjectList, SelectionCallbacks selectionCallbacks) {
        this.context = context;
        this.selectionCallbacks = selectionCallbacks;
        this.layoutInflater = (LayoutInflater) this.context.getSystemService(this.context.LAYOUT_INFLATER_SERVICE);
        //creating new arraylist from available list of departments
        ArrayList<BranchesModel> branchesModelArrayList = new ArrayList<>();
        branchesModelArrayList.addAll(dataObjectList);
        //removing default branch from list of departments
        branchesModelArrayList.remove(0);
        this.dataObjectList = branchesModelArrayList;
        imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(context));

    }

    @Override
    public float getPageWidth(final int position) {
        return 0.50f;
    }

    @Override
    public int getCount() {
        return dataObjectList.size();
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
        tvBranchName.setText(dataObjectList.get(position).getName());
        if (dataObjectList.get(position).getLogo() != null && dataObjectList.get(position).getLogo().getPathname() != null)
            imageLoader.displayImage("http://95.85.55.146" + dataObjectList.get(position).getLogo().getPathname(), circleImageView);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectionCallbacks.onResultSelection(dataObjectList.get(position).getId(), dataObjectList.get(position).getName());
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