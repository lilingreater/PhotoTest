package com.lilin.test.phototest;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.ArrayList;

/**
 * Created by lilin on 2016/12/7.
 * func : 图片显示的gridview的adapter
 */
public class GridAdapter extends BaseAdapter {
    private final Context context;
    private ArrayList<ImageItem> image_list;
    private LayoutInflater inflater;

    public GridAdapter(Context context, ArrayList<ImageItem> image_list) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.image_list = image_list;
    }

    public int getCount() {
        return (image_list.size() + 1);
    }

    public void update(ArrayList<ImageItem> image_list) {
        this.image_list = image_list;
        notifyDataSetChanged();
    }


    public Object getItem(int arg0) {
        return null;
    }

    public long getItemId(int arg0) {
        return 0;
    }

    public View getView(final int position, View convertView, final ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_gv,
                    parent, false);
            holder = new ViewHolder();
            holder.image = (ImageView) convertView
                    .findViewById(R.id.item_grida_image);
            holder.iv_item_delete = (ImageView) convertView.findViewById(R.id.iv_item_delete);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.iv_item_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemDeleteClick != null) {
                    onItemDeleteClick.onItemDelete(position);
                }
            }
        });
        holder.iv_item_delete.setVisibility(View.VISIBLE);
        Log.e("TAG", "image_ist.size==" + image_list.size());
        if (position == image_list.size()) {
            holder.image.setImageBitmap(BitmapFactory.decodeResource(
                    context.getResources(), R.drawable.icon_add));
            holder.iv_item_delete.setVisibility(View.GONE);
            if (position == 5) {
                holder.image.setVisibility(View.GONE);
            }
        } else {
            holder.image.setImageBitmap(image_list.get(position).getBitmap());

        }

        return convertView;
    }

    public class ViewHolder {
        public ImageView image, iv_item_delete;
    }

    public interface onItemDeleteClick {
        void onItemDelete(int position);
    }

    private onItemDeleteClick onItemDeleteClick;

    public void setOnItemDeleteClick(GridAdapter.onItemDeleteClick onItemDeleteClick) {
        this.onItemDeleteClick = onItemDeleteClick;
    }
}