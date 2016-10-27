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
import android.widget.ListView;

import com.jakewharton.rxbinding.view.RxView;
import com.tbruyelle.rxpermissions.RxPermissions;

import java.util.ArrayList;
import java.util.HashMap;

import me.tom.image.picker.R;
import me.tom.image.picker.adapter.FolderPickerAdapter;
import me.tom.image.picker.model.Folder;

public class FolderPickerActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    public static final int REQUEST_IMAGE_PICKER = 32;

    private FolderPickerAdapter mFolderPickerAdapter;

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

    private void initialize() {
        setContentView(R.layout.activity_folder_picker);
        RxView.clicks(findViewById(R.id.cancel)).subscribe(aVoid -> finish());

        mFolderPickerAdapter = new FolderPickerAdapter(this);
        ListView listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(mFolderPickerAdapter);
        listView.setOnItemClickListener((parent, view, position, id) -> {
            Folder folder = (Folder) mFolderPickerAdapter.getItem(position);
            Intent intent = null;
            if (getIntent().getBooleanExtra("multiple", false)) {
                intent = new Intent(this, MultipleImagePickerActivity.class);
            } else {
                intent = new Intent(this, ImagePickerActivity.class);
            }
            intent.putExtra("folderName", folder.name);
            startActivityForResult(intent, REQUEST_IMAGE_PICKER);
        });
        getSupportLoaderManager().restartLoader(0, null, this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_PICKER && resultCode == Activity.RESULT_OK) {
            setResult(Activity.RESULT_OK, data);
            finish();
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(
                this,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[] {
                        MediaStore.Images.Media.DATA,
                        MediaStore.Images.Media.BUCKET_DISPLAY_NAME
                },
                null,
                null,
                MediaStore.Images.Media.DATE_ADDED + " DESC"
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data == null || data.getCount() <= 0) {
            return;
        }

        data.moveToFirst();
        Folder defaultFolder = new Folder();
        defaultFolder.name = getString(R.string.all);
        defaultFolder.imageCount = data.getCount();
        HashMap<String, ArrayList<String>> imageBuckets = new HashMap<>();
        int pathColumnIndex = data.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        int bucketColumnIndex = data.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);
        do {
            String path = data.getString(pathColumnIndex);
            if (defaultFolder.cover == null) {
                defaultFolder.cover = path;
            }
            String bucket = data.getString(bucketColumnIndex);
            if (!imageBuckets.containsKey(bucket)) {
                imageBuckets.put(bucket, new ArrayList<>());
            }
            imageBuckets.get(bucket).add(path);
        } while (data.moveToNext());

        ArrayList<Folder> folders = new ArrayList<>();
        folders.add(defaultFolder);
        for (String key: imageBuckets.keySet()) {
            ArrayList<String> images = imageBuckets.get(key);
            Folder folder = new Folder();
            folder.name = key;
            folder.cover = images.get(0);
            folder.imageCount = images.size();
            folders.add(folder);
        }
        mFolderPickerAdapter.setFolders(folders);
    }
}
