package me.tom.image.picker.activity;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.support.v4.content.Loader;
import android.view.Gravity;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.jakewharton.rxbinding.view.RxView;

import java.util.ArrayList;

import me.tom.image.picker.R;
import me.tom.image.picker.adapter.MultipleImagePickerAdapter;
import me.tom.image.picker.model.Image;

public class MultipleImagePickerActivity extends ImagePickerActivity {

    private ArrayList<CharSequence> mSelectedImages;
    private TextView mChosenCountView;

    @Override
    protected void initialize() {
        super.initialize();

        mSelectedImages = new ArrayList<>();
        mGridView.setOnItemClickListener((parent, view, position, id) -> {
            Image image = (Image) mImagePickerAdapter.getItem(position);
            CheckBox checkBox = (CheckBox) view.findViewById(R.id.check);
            if (checkBox.isChecked()) {
                checkBox.setChecked(false);
                if (mSelectedImages.contains(image.path)) {
                    mSelectedImages.remove(image.path);
                }
            } else {
                checkBox.setChecked(true);
                mSelectedImages.add(image.path);
            }
            mChosenCountView.setText(mSelectedImages.size() + "/" + mImagePickerAdapter.getCount());
        });

        mChosenCountView = (TextView) findViewById(R.id.chosenCount);
        RxView.clicks(findViewById(R.id.choose)).subscribe(aVoid -> {
            if (mSelectedImages.size() > 0) {
                Intent intent = new Intent();
                intent.putCharSequenceArrayListExtra("images", mSelectedImages);
                setResult(Activity.RESULT_OK, intent);
                finish();
            } else {
                Toast toast = Toast.makeText(this, R.string.choose_warn, Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
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
        mImagePickerAdapter = new MultipleImagePickerAdapter(this);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        super.onLoadFinished(loader, data);
        mChosenCountView.setText("0/" + mImagePickerAdapter.getCount());
    }
}
