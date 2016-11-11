package me.tom.image.picker.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import me.tom.image.picker.R;

public class MultipleImagePickerAdapter extends ImagePickerAdapter {

    public MultipleImagePickerAdapter(Context context) {
        super(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.multiple_image_picker_list_item, null, false);
            holder.image = (ImageView) convertView.findViewById(R.id.image);
            holder.checkBox = (CheckBox) convertView.findViewById(R.id.check);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (mItemSize > 0) {
            Glide.with(mContext).load(mImages.get(position).path)
                    .placeholder(R.drawable.loading)
                    .override(mItemSize, mItemSize)
                    .into(holder.image);
            if (holder.image.getLayoutParams().height != mItemSize) {
                holder.image.setLayoutParams(mItemLayoutParams);
            }
            holder.checkBox.setChecked(mImages.get(position).checked);
        }
        return convertView;
    }


}
