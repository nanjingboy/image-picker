package me.tom.image.picker.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;

import me.tom.image.picker.R;

public class MultipleImagePickerAdapter extends ImagePickerAdapter {

    public ArrayList<Integer> selectedPositions = new ArrayList<>();

    public MultipleImagePickerAdapter(Context context, int imageSize) {
        super(context, imageSize);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder holder = new ViewHolder(mInflater.inflate(R.layout.multiple_image_picker_list_item, parent, false));
        holder.itemView.setOnClickListener(view -> {
            if (mItemClickListener != null) {
                mItemClickListener.onItemClick(view);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ImagePickerAdapter.ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        if (selectedPositions.contains(position)) {
            ((ViewHolder) holder).selectView.setImageResource(R.drawable.image_picker_selected);
        } else {
            ((ViewHolder) holder).selectView.setImageResource(R.drawable.image_picker_unselected);
        }
    }

    public static class ViewHolder extends ImagePickerAdapter.ViewHolder {

        public ImageView selectView;

        public ViewHolder(View view) {
            super(view);
            selectView = (ImageView) view.findViewById(R.id.selectView);
        }
    }
}
