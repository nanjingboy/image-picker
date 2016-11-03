package me.tom.image.picker.widgets;

import android.content.Context;
import android.graphics.Point;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.Display;
import android.view.WindowManager;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.android.flexbox.FlexboxLayout;

import java.util.ArrayList;

import me.tom.image.picker.R;
import me.tom.image.picker.model.Image;

public class ImageGroupView extends FlexboxLayout {

    protected ArrayList<Image> mImages;

    protected Context mContext;

    protected int mColumnCount;
    protected int mSpaceSize;
    protected int mScreenWidth;

    protected IItemClickedListener mItemClickedListener;

    public interface IItemClickedListener {
        void omItemClicked(String path, boolean isSelected);
    }

    public ImageGroupView(Context context) {
        this(context, null);
    }

    public ImageGroupView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ImageGroupView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
        mImages = new ArrayList<>();
        mColumnCount = context.getResources().getInteger(R.integer.image_picker_column_count);
        mSpaceSize = context.getResources().getDimensionPixelSize(R.dimen.image_picker_image_space);
        WindowManager windowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        mScreenWidth = size.x;

        setDividerDrawable(ContextCompat.getDrawable(mContext, R.drawable.image_picker_image_group_view_divider));
        setFlexWrap(FLEX_WRAP_WRAP);
        setShowDivider(SHOW_DIVIDER_MIDDLE);
    }

    public void setItemClickedListener(IItemClickedListener listener) {
        mItemClickedListener = listener;
    }

    public void setImages(ArrayList<Image> images) {
        removeAllViews();
        mImages = images;
        int imageSize = (int) Math.floor((mScreenWidth - getPaddingLeft() - getPaddingRight() + mSpaceSize) / (float) mColumnCount) - mSpaceSize;
        for (Image image: images) {
            addImageView(image, imageSize);
        }
    }

    protected void addImageView(Image image, int imageSize) {
        ImageView imageView = new ImageView(mContext);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setOnClickListener(v -> {
            if (mItemClickedListener != null) {
                mItemClickedListener.omItemClicked(mImages.get(indexOfChild(v)).path, true);
            }
        });
        addView(imageView, imageSize, imageSize);
        Glide.with(mContext).load(image.path)
                .placeholder(R.drawable.loading)
                .override(imageSize, imageSize)
                .into(imageView);
    }
}
