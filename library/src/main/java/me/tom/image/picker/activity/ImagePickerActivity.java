package me.tom.image.picker.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.tbruyelle.rxpermissions.RxPermissions;

import java.util.ArrayList;

import me.tom.image.picker.R;
import me.tom.image.picker.model.Image;
import me.tom.image.picker.widgets.ImageGroupView;

public class ImagePickerActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    protected String mFolderName;
    protected ImageGroupView mImageGroupView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        RxPermissions.getInstance(this)
                .request(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe(granted -> {
                    if (granted) {
                        initialize();
                    } else {
                        finish();
                    }
                });
    }

    protected void initialize() {
        setContentView(getContentView());

        RxView.clicks(findViewById(R.id.goBack)).subscribe(aVoid -> finish());
        RxView.clicks(findViewById(R.id.cancel)).subscribe(aVoid -> finish());

        mFolderName = getIntent().getStringExtra("folderName");
        ((TextView) findViewById(R.id.name)).setText(mFolderName);
        mImageGroupView = (ImageGroupView) findViewById(R.id.imageGroupView);
        mImageGroupView.setItemClickedListener((path, isSelected) -> {
            ArrayList<CharSequence> images = new ArrayList<>();
            images.add(path);
            Intent intent = new Intent();
            intent.putCharSequenceArrayListExtra("images", images);
            setResult(Activity.RESULT_OK, intent);
            finish();
        });

        getSupportLoaderManager().restartLoader(0, null, this);
    }

    protected int getContentView() {
        return R.layout.activity_image_picker;
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String selection = null;
        String[] selectionArgs = null;

        if (!mFolderName.equals(getString(R.string.all))) {
            selection = MediaStore.Images.Media.BUCKET_DISPLAY_NAME + "=?";
            selectionArgs = new String[] {mFolderName};
        }

        return new CursorLoader(
                this,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[] {
                        MediaStore.Images.Media.DATA
                },
                selection,
                selectionArgs,
                MediaStore.Images.Media.DATE_ADDED + " DESC"
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data == null || data.getCount() <= 0) {
            return;
        }
        data.moveToFirst();
        ArrayList<Image> images = new ArrayList<>();
        int pathColumnIndex = data.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        do {
            Image image = new Image();
            image.path = data.getString(pathColumnIndex);
            images.add(image);
        } while (data.moveToNext());
        mImageGroupView.setImages(images);
    }
}
