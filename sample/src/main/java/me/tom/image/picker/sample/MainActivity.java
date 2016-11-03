package me.tom.image.picker.sample;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.RadioButton;

import com.jakewharton.rxbinding.view.RxView;

import java.util.ArrayList;

import me.tom.image.picker.activity.FolderPickerActivity;
import me.tom.image.picker.model.Image;
import me.tom.image.picker.widgets.ImageGroupView;
import rx.functions.Action1;

public class MainActivity extends AppCompatActivity {

    private ImageGroupView mImageGroupView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RxView.clicks(findViewById(R.id.tryNow)).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                Intent intent = new Intent(MainActivity.this, FolderPickerActivity.class);
                RadioButton multiple = (RadioButton) findViewById(R.id.multiple);
                intent.putExtra("multiple", multiple.isChecked());
                startActivityForResult(intent, FolderPickerActivity.REQUEST_IMAGE_PICKER);
            }
        });
        mImageGroupView = (ImageGroupView) findViewById(R.id.imageGroupView);
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
        mImageGroupView.setImages(images);
    }
}
