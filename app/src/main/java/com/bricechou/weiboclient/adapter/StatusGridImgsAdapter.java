package com.bricechou.weiboclient.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout.LayoutParams;

import com.bricechou.weiboclient.R;
import com.bricechou.weiboclient.model.PicUrls;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

/**
 * @author BriceChou
 * @datetime 16-6-21 10:30
 * @TODO To show the grid layout image.
 */


public class StatusGridImgsAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<PicUrls> datas;
    private ImageLoader mImageLoader;

    public StatusGridImgsAdapter(Context context, ArrayList<PicUrls> datas) {
        this.context = context;
        this.datas = datas;
        mImageLoader = ImageLoader.getInstance();
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public PicUrls getItem(int position) {
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(context, R.layout.item_status_grid_image, null);
            holder.iv_image = (ImageView) convertView.findViewById(R.id.iv_image);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        GridView gv = (GridView) parent;
        int horizontalSpacing = gv.getHorizontalSpacing();
        int numColumns = gv.getNumColumns();
        int itemWidth = (gv.getWidth() - (numColumns - 1) * horizontalSpacing
                - gv.getPaddingLeft() - gv.getPaddingRight()) / numColumns;
        LayoutParams params = new LayoutParams(itemWidth, itemWidth);
        holder.iv_image.setLayoutParams(params);
        PicUrls urls = getItem(position);
        mImageLoader.displayImage(urls.getThumbnail_pic(), holder.iv_image);
        return convertView;
    }

    public static class ViewHolder {
        public ImageView iv_image;
    }

}
