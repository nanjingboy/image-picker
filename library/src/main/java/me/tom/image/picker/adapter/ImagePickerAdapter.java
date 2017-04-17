package me.tom.image.picker.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import me.tom.image.picker.R;
import me.tom.image.picker.model.Image;

public class ImagePickerAdapter extends RecyclerView.Adapter<ImagePickerAdapter.ViewHolder> {

    protected Context mContext;
    protected LayoutInflater mInflater;
    protected ArrayList<Image> mImages;
    protected int mImageSize;

    protected IItemClickListener mItemClickListener;

    public ImagePickerAdapter(Context context) {
        this(context, context.getResources().getDimensionPixelSize(R.dimen.image_picker_image_size));
    }

    public ImagePickerAdapter(Context context, int imageSize) {
        mContext = context;
        mImages = new ArrayList<>();
        mImageSize = imageSize;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public Image getItem(int position) {
        return mImages.get(position);
    }

    @Override
    public int getItemCount() {
        return mImages.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder holder = new ViewHolder(mInflater.inflate(R.layout.image_picker_list_item, parent, false));
        holder.itemView.setOnClickListener(view -> {
            if (mItemClickListener != null) {
                mItemClickListener.onItemClick(view);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Glide.with(mContext).load(mImages.get(position).path)
                .placeholder(R.drawable.loading)
                .override(mImageSize, mImageSize)
                .into(holder.imageView);
    }


    public void setImages(ArrayList<Image> images) {
        mImages = images;
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(IItemClickListener listener) {
        mItemClickListener = listener;
    }

    public interface IItemClickListener {
        void onItemClick(View view);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView imageView;

        public ViewHolder(View view) {
            super(view);
            imageView = (ImageView) view.findViewById(R.id.imageView);
        }
    }
}
