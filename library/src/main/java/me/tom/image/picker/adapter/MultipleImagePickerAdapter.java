package me.tom.image.picker.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import me.tom.image.picker.R;
import me.tom.image.picker.model.Image;

public class MultipleImagePickerAdapter extends ImagePickerAdapter {

    private ItemCheckedChangeListener mItemCheckedChangeListener;

    public interface ItemCheckedChangeListener {
        void onItemCheckedChange(boolean isChecked, Image image);
    }

    public MultipleImagePickerAdapter(Context context, ItemCheckedChangeListener itemCheckedChangeListener) {
        super(context);
        mItemCheckedChangeListener = itemCheckedChangeListener;
    }

    @Override
    public View getView(int position, ViewGroup parent) {
        View view = mInflater.inflate(R.layout.multiple_image_picker_list_item, parent, false);
        if (mItemCheckedChangeListener != null) {
            CheckBox checkBox = (CheckBox) view.findViewById(R.id.check);
            checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                mItemCheckedChangeListener.onItemCheckedChange(isChecked, (Image) getItem(position));
            });
        }
        return view;
    }
}
