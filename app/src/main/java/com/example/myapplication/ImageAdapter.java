package com.example.myapplication;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import java.util.List;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.MyViewHolder> {

    private List<Integer> imageResourceIds;
    private Context context;

    public ImageAdapter(Context context, List<Integer> imageResourceIds) {
        this.context = context;
        this.imageResourceIds = imageResourceIds;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_photo, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        // Load and display images using Glide
        Glide.with(context)
                .load(imageResourceIds.get(position))
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return imageResourceIds.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        MyViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
        }
    }
}