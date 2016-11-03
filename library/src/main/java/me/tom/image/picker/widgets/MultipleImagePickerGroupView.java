package me.tom.image.picker.widgets;

import android.content.Context;
import android.graphics.drawable.StateListDrawable;
import android.util.AttributeSet;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;

import me.tom.image.picker.R;
import me.tom.image.picker.model.Image;

public class MultipleImagePickerGroupView extends ImageGroupView {

    private int mCheckBoxSize;

    public MultipleImagePickerGroupView(Context context) {
        this(context, null);
    }

    public MultipleImagePickerGroupView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MultipleImagePickerGroupView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mCheckBoxSize = context.getResources().getDimensionPixelSize(R.dimen.image_picker_check_box_size);
    }

    @Override
    protected void addImageView(Image image, int imageSize) {
        RelativeLayout relativeLayout = new RelativeLayout(mContext);
        ImageView imageView = new ImageView(mContext);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        Glide.with(mContext).load(image.path)
                .placeholder(R.drawable.loading)
                .override(imageSize, imageSize)
                .into(imageView);
        RelativeLayout.LayoutParams imageViewLayoutParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT
        );
        imageView.setLayoutParams(imageViewLayoutParams);
        relativeLayout.addView(imageView);

        CheckBox checkBoxView = new CheckBox(mContext);
        checkBoxView.setClickable(false);
        checkBoxView.setFocusable(false);
        checkBoxView.setFocusableInTouchMode(false);
        checkBoxView.setBackgroundResource(R.drawable.image_picker_checkbox);
        checkBoxView.setButtonDrawable(new StateListDrawable());
        RelativeLayout.LayoutParams checkBoxViewLayoutParams = new RelativeLayout.LayoutParams(mCheckBoxSize, mCheckBoxSize);
        checkBoxViewLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
        checkBoxView.setLayoutParams(checkBoxViewLayoutParams);
        relativeLayout.addView(checkBoxView);

        relativeLayout.setOnClickListener(v -> {
            boolean isSelected;
            CheckBox checkBox = (CheckBox) ((RelativeLayout) v).getChildAt(1);
            if (checkBox.isChecked()) {
                isSelected = false;
            } else {
                isSelected = true;
            }
            checkBox.setChecked(isSelected);
            if (mItemClickedListener != null) {
                mItemClickedListener.omItemClicked(mImages.get(indexOfChild(v)).path, isSelected);
            }
        });

        addView(relativeLayout, imageSize, imageSize);
    }
}
