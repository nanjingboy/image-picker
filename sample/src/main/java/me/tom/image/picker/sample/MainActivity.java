package me.tom.image.picker.sample;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewTreeObserver;
import android.widget.GridView;

import com.jakewharton.rxbinding.view.RxView;

import java.util.ArrayList;

import me.tom.image.picker.activity.FolderPickerActivity;
import me.tom.image.picker.adapter.ImagePickerAdapter;
import me.tom.image.picker.model.Image;

public class MainActivity extends AppCompatActivity {

    private ImagePickerAdapter mImagePickerAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RxView.clicks(findViewById(R.id.tryNow)).subscribe(aVoid -> {
            Intent intent = new Intent(this, FolderPickerActivity.class);
            startActivityForResult(intent, FolderPickerActivity.REQUEST_IMAGE_PICKER);
        });

        mImagePickerAdapter = new ImagePickerAdapter(this);
        GridView gridView = (GridView) findViewById(R.id.gridView);
        gridView.setAdapter(mImagePickerAdapter);
        gridView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (mImagePickerAdapter.getNumColumns() == 0) {
                    int imageSize = getResources().getDimensionPixelSize(me.tom.image.picker.R.dimen.image_size);
                    int imageSpace = getResources().getDimensionPixelOffset(me.tom.image.picker.R.dimen.image_space);
                    int numColumns = (int) Math.floor(gridView.getWidth() / (imageSize + imageSpace));
                    if (numColumns > 0) {
                        int columnWidth = (gridView.getWidth() / numColumns) - imageSpace;
                        mImagePickerAdapter.setNumColumns(numColumns);
                        mImagePickerAdapter.setItemSize(columnWidth);
                    }
                }
                gridView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        ArrayList<Image> images = new ArrayList<>();
        if (requestCode == FolderPickerActivity.REQUEST_IMAGE_PICKER && resultCode == Activity.RESULT_OK) {
            ArrayList<CharSequence> imageArrayList = data.getCharSequenceArrayListExtra("images");
            for (int index = 0; index < imageArrayList.size(); index++) {
                Image image = new Image();
                image.path = imageArrayList.get(index).toString();
                images.add(image);
            }
        }
        mImagePickerAdapter.setImages(images);
    }
}
