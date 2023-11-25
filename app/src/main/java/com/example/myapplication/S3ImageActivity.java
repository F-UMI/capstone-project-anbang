package com.example.myapplication;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class S3ImageActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private PhotoAdapter photoAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_s3_images);

        recyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);

        new S3ObjectRetrievalTask().execute();
    }

    private class S3ObjectRetrievalTask extends AsyncTask<Void, Void, List<String>> {

        @Override
        protected List<String> doInBackground(Void... voids) {
            List<String> s3ObjectUrls = new ArrayList<>();

            AmazonS3Client s3Client = new AmazonS3Client(new BasicAWSCredentials(BuildConfig.ACCESS_KEY, BuildConfig.SECRET_ACCESS_KEY));
            TransferUtility transferUtility = TransferUtility.builder()
                    .context(getApplicationContext())
                    .s3Client(s3Client)
                    .build();

            ObjectListing objectListing = s3Client.listObjects(BuildConfig.BUCKET_NAME);
            List<S3ObjectSummary> objectSummaries = objectListing.getObjectSummaries();

            for (S3ObjectSummary objectSummary : objectSummaries) {
                String objectKey = objectSummary.getKey();
                String objectUrl = s3Client.getUrl(BuildConfig.BUCKET_NAME, objectKey).toString();
                s3ObjectUrls.add(objectUrl);
            }

            return s3ObjectUrls;
        }

        @Override
        protected void onPostExecute(List<String> s3ObjectUrls) {
            super.onPostExecute(s3ObjectUrls);

            photoAdapter = new PhotoAdapter(getApplicationContext(), s3ObjectUrls);
            recyclerView.setAdapter(photoAdapter);
        }
    }

    private static class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.ViewHolder> {
        private List<String> photoUrls;
        private Context context;

        public PhotoAdapter(Context context, List<String> photoUrls) {
            this.context = context;
            this.photoUrls = photoUrls;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_photo, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            String photoUrl = photoUrls.get(position);
            Picasso.get().load(Uri.parse(photoUrl)).into(holder.imageView);
        }

        @Override
        public int getItemCount() {
            return photoUrls.size();
        }

        public static class ViewHolder extends RecyclerView.ViewHolder {
            ImageView imageView;

            public ViewHolder(View itemView) {
                super(itemView);
                imageView = itemView.findViewById(R.id.imageView);
            }
        }
    }
}