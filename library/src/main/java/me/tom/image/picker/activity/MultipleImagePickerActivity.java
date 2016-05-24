package me.tom.image.picker.activity;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.support.v4.content.Loader;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;

import java.util.ArrayList;

import me.tom.image.picker.R;
import me.tom.image.picker.adapter.MultipleImagePickerAdapter;

public class MultipleImagePickerActivity extends ImagePickerActivity {

    private ArrayList<CharSequence> mSelectedImages;
    private TextView mChosenCountView;

    @Override
    protected void initialize() {
        super.initialize();
        mGridView.setOnItemClickListener(null);

        mChosenCountView = (TextView) findViewById(R.id.chosenCount);
        RxView.clicks(findViewById(R.id.choose)).subscribe(aVoid -> {
            if (mSelectedImages.size() > 0) {
                Intent intent = new Intent();
                intent.putCharSequenceArrayListExtra("images", mSelectedImages);
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        });
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_multiple_image_picker;
    }

    @Override
    protected void setImagePickerAdapter() {
        mSelectedImages = new ArrayList<>();
        mImagePickerAdapter = new MultipleImagePickerAdapter(this, ((isChecked, image) -> {
            if (isChecked) {
                mSelectedImages.add(image.path);
            } else if (mSelectedImages.contains(image.path)) {
                mSelectedImages.remove(image.path);
            }
            mChosenCountView.setText(mSelectedImages.size() + "/" + mImagePickerAdapter.getCount());
        }));
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        super.onLoadFinished(loader, data);
        mChosenCountView.setText("0/" + mImagePickerAdapter.getCount());
    }
}
