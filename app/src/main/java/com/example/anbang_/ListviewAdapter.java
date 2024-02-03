package com.example.anbang_;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class ListviewAdapter extends BaseAdapter {
    private ArrayList<ListviewItem> arrayList = new ArrayList<>();

    public ListviewAdapter() {

    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return arrayList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        Context context = viewGroup.getContext();

        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.item, viewGroup, false);
        }
        ImageView imageView = (ImageView) view.findViewById(R.id.property_image);
        TextView text = (TextView) view.findViewById(R.id.property_name);

        ListviewItem listviewItem = arrayList.get(i);

        imageView.setImageDrawable(listviewItem.getDrawable());
        text.setText(listviewItem.getText());

        return view;

    }

    public void addItem(Drawable drawable, String text) {
        ListviewItem listviewItem = new ListviewItem();
        listviewItem.setDrawable(drawable);
        listviewItem.setText(text);

        arrayList.add(listviewItem);
    }
}
