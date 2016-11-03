package me.tom.image.picker.activity;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.support.v4.content.Loader;
import android.view.Gravity;
import android.widget.TextView;
import android.widget.Toast;

import com.jakewharton.rxbinding.view.RxView;

import java.util.ArrayList;

import me.tom.image.picker.R;

public class MultipleImagePickerActivity extends ImagePickerActivity {

    private ArrayList<CharSequence> mSelectedImages;
    private TextView mChosenCountView;

    @Override
    protected void initialize() {
        super.initialize();

        mSelectedImages = new ArrayList<>();
        mChosenCountView = (TextView) findViewById(R.id.chosenCount);
        mImageGroupView.setItemClickedListener((path, isSelected) -> {
            if (isSelected) {
                mSelectedImages.add(path);
            } else if (mSelectedImages.contains(path)) {
                mSelectedImages.remove(path);
            }
           mChosenCountView.setText(mSelectedImages.size() + "/" + mImageGroupView.getChildCount());
        });

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
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        super.onLoadFinished(loader, data);
        mChosenCountView.setText("0/" + mImageGroupView.getChildCount());
    }
}
