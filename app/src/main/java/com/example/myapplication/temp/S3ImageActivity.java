package com.example.myapplication.temp;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.example.myapplication.BuildConfig;
import com.example.myapplication.R;

import java.util.ArrayList;
import java.util.List;

// S3에서 Image를 가져와 리사이클러뷰에 등록
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

            ObjectListing objectListing = s3Client.listObjects(BuildConfig.BUCKET_NAME, "test1/");
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
}