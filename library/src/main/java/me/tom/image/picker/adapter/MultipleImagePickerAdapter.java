package me.tom.image.picker.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import me.tom.image.picker.R;

public class MultipleImagePickerAdapter extends ImagePickerAdapter {

    public MultipleImagePickerAdapter(Context context) {
        super(context);
    }

    @Override
    public View getView(ViewGroup parent) {
        return mInflater.inflate(R.layout.multiple_image_picker_list_item, parent, false);
    }
}
