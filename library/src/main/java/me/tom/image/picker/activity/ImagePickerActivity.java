package me.tom.image.picker.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Point;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.tbruyelle.rxpermissions.RxPermissions;

import java.util.ArrayList;

import me.tom.image.picker.R;
import me.tom.image.picker.adapter.ImagePickerAdapter;
import me.tom.image.picker.common.widgets.GridSpacingItemDecoration;
import me.tom.image.picker.model.Image;

public class ImagePickerActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    protected String mFolderName;
    protected ImagePickerAdapter mImagePickerAdapter;
    protected RecyclerView mRecyclerView;

    protected int mImageSize;
    protected int mImageSpace;
    protected int mColumnCount;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mImageSize = getResources().getDimensionPixelSize(R.dimen.image_picker_image_size);
        mImageSpace = getResources().getDimensionPixelOffset(R.dimen.image_picker_image_space);
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        mColumnCount = (int) Math.floor((size.x - mImageSpace) / (mImageSize + mImageSpace));
        RxPermissions rxPermissions = new RxPermissions(this);
        rxPermissions.request(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
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
        TextView name = (TextView) findViewById(R.id.name);
        name.setText(mFolderName);
        setImagePickerAdapter();
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mRecyclerView.setAdapter(mImagePickerAdapter);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, mColumnCount));
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.image_picker_image_space);
        mRecyclerView.addItemDecoration(new GridSpacingItemDecoration(mColumnCount, spacingInPixels, true, 0));
        mImagePickerAdapter.setOnItemClickListener(view -> {
            int position = mRecyclerView.getChildAdapterPosition(view);
            Image image = mImagePickerAdapter.getItem(position);
            ArrayList<CharSequence> images = new ArrayList<>();
            images.add(image.path);
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

    protected void setImagePickerAdapter() {
        mImagePickerAdapter = new ImagePickerAdapter(this, mImageSize);
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
        mImagePickerAdapter.setImages(images);
    }
}
