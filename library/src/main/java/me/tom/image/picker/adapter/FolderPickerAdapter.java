package me.tom.image.picker.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import me.tom.image.picker.R;
import me.tom.image.picker.model.Folder;

public class FolderPickerAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater mInflater;
    private ArrayList<Folder> mFolders;

    public FolderPickerAdapter(Context context) {
        mContext = context;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mFolders = new ArrayList<>();
    }

    public void setFolders(ArrayList<Folder> folders) {
        mFolders = folders;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mFolders.size();
    }

    @Override
    public Object getItem(int position) {
        return mFolders.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.folder_picker_list_item, null, false);
        }

        Folder folder = mFolders.get(position);
        ImageView cover = (ImageView) convertView.findViewById(R.id.cover);
        Glide.with(mContext).load(folder.cover).placeholder(R.drawable.loading).into(cover);
        TextView name = (TextView) convertView.findViewById(R.id.name);
        name.setText(folder.name);
        TextView count = (TextView) convertView.findViewById(R.id.count);
        count.setText(Integer.toString(folder.imageCount));
        return convertView;
    }
}
