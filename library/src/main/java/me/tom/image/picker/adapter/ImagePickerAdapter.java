package me.tom.image.picker.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import me.tom.image.picker.R;
import me.tom.image.picker.model.Image;

public class ImagePickerAdapter extends BaseAdapter {

    protected Context mContext;
    protected LayoutInflater mInflater;
    protected ArrayList<Image> mImages;

    protected int mItemSize = 0;
    protected int mNumColumns = 0;

    protected RelativeLayout.LayoutParams mItemLayoutParams;

    public ImagePickerAdapter(Context context) {
        mContext = context;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mImages = new ArrayList<>();
        mItemLayoutParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT
        );
    }

    public void setNumColumns(int numColumns) {
        mNumColumns = numColumns;
    }

    public int getNumColumns() {
        return mNumColumns;
    }

    public void setItemSize(int itemSize) {
        if (itemSize == mItemSize) {
            return;
        }

        mItemSize = itemSize;
        mItemLayoutParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                mItemSize
        );
        notifyDataSetChanged();
    }

    public void setImages(ArrayList<Image> images) {
        mImages = images;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mImages.size();
    }

    @Override
    public Object getItem(int position) {
        return mImages.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = getView(parent);
        }

        if (mItemSize > 0) {
            ImageView imageView = (ImageView) convertView.findViewById(R.id.image);
            Glide.with(mContext).load(mImages.get(position).path).override(mItemSize, mItemSize).into(imageView);
            if (imageView.getLayoutParams().height != mItemSize) {
                imageView.setLayoutParams(mItemLayoutParams);
            }
        }
        return convertView;
    }

    public View getView(ViewGroup parent) {
       return mInflater.inflate(R.layout.image_picker_list_item, parent, false);
    }
}
