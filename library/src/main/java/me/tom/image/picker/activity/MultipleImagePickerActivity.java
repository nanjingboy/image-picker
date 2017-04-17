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
import me.tom.image.picker.adapter.MultipleImagePickerAdapter;

public class MultipleImagePickerActivity extends ImagePickerActivity {

    private TextView mChosenCountView;

    @Override
    protected void initialize() {
        super.initialize();
        mImagePickerAdapter.setOnItemClickListener(view -> {
            Integer position = mRecyclerView.getChildAdapterPosition(view);
            MultipleImagePickerAdapter adapter = (MultipleImagePickerAdapter) mImagePickerAdapter;
            if (adapter.selectedPositions.contains(position)) {
                adapter.selectedPositions.remove(position);
            } else {
                adapter.selectedPositions.add(position);
            }
            adapter.notifyItemChanged(position);
            mChosenCountView.setText(adapter.selectedPositions.size() + "/" + adapter.getItemCount());
        });
        mChosenCountView = (TextView) findViewById(R.id.chosenCount);
        RxView.clicks(findViewById(R.id.choose)).subscribe(aVoid -> {
            MultipleImagePickerAdapter adapter = (MultipleImagePickerAdapter) mImagePickerAdapter;
            if (adapter.selectedPositions.size() > 0) {
                ArrayList<CharSequence> images = new ArrayList<>();
                for (Integer position: adapter.selectedPositions) {
                    images.add(adapter.getItem(position).path);
                }
                Intent intent = new Intent();
                intent.putCharSequenceArrayListExtra("images", images);
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
        mImagePickerAdapter = new MultipleImagePickerAdapter(this, mImageSize);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        super.onLoadFinished(loader, data);
        mChosenCountView.setText("0/" + mImagePickerAdapter.getItemCount());
    }
}
