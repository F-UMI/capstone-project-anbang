package com.example.myapplication;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferNetworkLossHandler;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferService;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ListObjectsV2Request;
import com.amazonaws.services.s3.model.ListObjectsV2Result;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_WRITE_EXTERNAL_STORAGE = 1;

    private AmazonS3 s3Client;
    private TransferUtility transferUtility;

    private TextView textView;
    private RecyclerView recyclerView;
    private ImageAdapter imageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        textView = findViewById(R.id.textView);
        recyclerView = findViewById(R.id.recyclerView);

        // Check for write external storage permission
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        REQUEST_WRITE_EXTERNAL_STORAGE);
            } else {
                initializeS3Client();
                // Retrieve and display images
                new ListObjectsTask().execute();
            }
        } else {
            initializeS3Client();
            // Retrieve and display images
            new ListObjectsTask().execute();
        }
    }

    private void initializeS3Client() {
        // Initialize AWS credentials and S3 client
        CognitoCachingCredentialsProvider credentialsProvider = new CognitoCachingCredentialsProvider(
                getApplicationContext(),
                "ap-northeast-2:1601b465-7ffb-4249-8a7f-5b4437df9462",  // Identity pool ID
                Regions.AP_NORTHEAST_2  // AWS Region
        );

        s3Client = new AmazonS3Client(credentialsProvider);

        // Initialize TransferUtility
        transferUtility = TransferUtility.builder()
                .context(getApplicationContext())
                .s3Client(s3Client)
                .awsConfiguration(AWSMobileClient.getInstance().getConfiguration())
                .build();

        // Start the TransferService
        getApplicationContext().startService(new Intent(getApplicationContext(), TransferService.class));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_WRITE_EXTERNAL_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                initializeS3Client();
                // Retrieve and display images
                new ListObjectsTask().execute();
            } else {
                // Handle permission denied
                // You might want to inform the user or take appropriate action
            }
        }
    }

    private class ListObjectsTask extends AsyncTask<Void, Void, List<String>> {
        @Override
        protected List<String> doInBackground(Void... voids) {
            // Create a request to list objects in the specified folder
            ListObjectsV2Request listObjectsRequest = new ListObjectsV2Request()
                    .withBucketName(BuildConfig.BUCKET_NAME)
                    .withPrefix("test2/");

            // Perform the request to list objects
            ListObjectsV2Result listObjectsResponse = s3Client.listObjectsV2(listObjectsRequest);

            // Extract the object keys from the response
            List<S3ObjectSummary> objectSummaries = listObjectsResponse.getObjectSummaries();
            return objectSummaries.stream().map(S3ObjectSummary::getKey).collect(Collectors.toList());
        }

        @Override
        protected void onPostExecute(List<String> objectKeys) {
            textView.setText("S3 Object Display");

            // Set up RecyclerView and ImageAdapter
            imageAdapter = new ImageAdapter(objectKeys, MainActivity.this);
            recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
            recyclerView.setAdapter(imageAdapter);

            // Download images and notify the adapter
            for (String objectKey : objectKeys) {
                File destinationDirectory = new File(getExternalFilesDir(null) + File.separator + "test2/");
                Log.d("Directory", "Destination Directory: " + destinationDirectory.getAbsolutePath());

                if (!destinationDirectory.exists()) {
                    if (!destinationDirectory.mkdirs()) {
                        Log.e("Directory", "Failed to create destination directory");
                        return;
                    }
                }
                TransferObserver observer = transferUtility.download(
                        BuildConfig.BUCKET_NAME,
                        objectKey,
                        new File(destinationDirectory, objectKey)
                );

                observer.setTransferListener(new TransferListener() {
                    @Override
                    public void onStateChanged(int id, TransferState state) {
                        if (state == TransferState.COMPLETED) {
                            runOnUiThread(() -> imageAdapter.notifyDataSetChanged());
                        }
                    }

                    @Override
                    public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {

                    }

                    @Override
                    public void onError(int id, Exception ex) {

                    }

                    // ... (other methods remain the same)
                });
            }
        }
    }
}
    /*
    private ImageView imageView;
    private S3Manager s3Manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TransferNetworkLossHandler.getInstance(getApplicationContext());
        setContentView(R.layout.activity_test);

        imageView = findViewById(R.id.imageView);
        s3Manager = new S3Manager(this);
        s3Manager.downloadAndDisplayImagesInFolder("anbang-bucket-01", "test2/", imageView);
    }
}
*/

/*    private ImageView imageView_1;
    private ImageView imageView_2;
    private ImageView imageView_3;
    private ImageView imageView_4;
    private ImageView imageView_5;
    private ImageView imageView_6;
    private ImageView imageView_7;
    private ImageView imageView_8;
    private ImageView imageView_9;

    private Button button;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView_1 = findViewById(R.id.imageView_1);
        imageView_2 = findViewById(R.id.imageView_2);
        imageView_3 = findViewById(R.id.imageView_3);
        imageView_4 = findViewById(R.id.imageView_4);
        imageView_5 = findViewById(R.id.imageView_5);
        imageView_6 = findViewById(R.id.imageView_6);
        imageView_7 = findViewById(R.id.imageView_7);
        imageView_8 = findViewById(R.id.imageView_8);
        imageView_9 = findViewById(R.id.imageView_9);

        button = (Button) findViewById(R.id.button);

        button.setOnClickListener(view -> {
            // 버튼 클릭 시 다른 액티비티로 전환
            Intent intent = new Intent(MainActivity.this, CameraActivity.class);
            startActivity(intent);
        });


        new DownloadImageTask().execute();

    }


    // AsyncTask 클래스 정의
    private class DownloadImageTask extends AsyncTask<Void, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(Void... voids) {
            // S3에서 이미지 다운로드
            AmazonS3Client s3Client = new AmazonS3Client(new BasicAWSCredentials(BuildConfig.ACCESS_KEY, BuildConfig.SECRET_ACCESS_KEY));
            S3Object s3Object = s3Client.getObject(BuildConfig.BUCKET_NAME, "test1/KakaoTalk_20230825_021231523_01.jpg");
            List<S3Object> bucketObjectList = new ArrayList<>();

            ObjectListing listing = s3Client.listObjects(BuildConfig.BUCKET_NAME, "test2/");
            List<S3ObjectSummary> summaries = listing.getObjectSummaries();

            while (listing.isTruncated()) {
                listing = s3Client.listNextBatchOfObjects(listing);
                summaries.addAll(listing.getObjectSummaries());
            }

            for (S3ObjectSummary objectSummary : summaries) {
                bucketObjectList.add(s3Client.getObject(BuildConfig.BUCKET_NAME, objectSummary.getKey()));
            }


            // InputStream에서 비트맵으로 변환
            for (int i = 0; i < bucketObjectList.size(); i++) {
                try (InputStream inputStream = bucketObjectList.get(i).getObjectContent()) {
                    Log.e("SUCCESS", i + "Complete");
                    return BitmapFactory.decodeStream(inputStream);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
*//*            try (InputStream inputStream = s3Object.getObjectContent()) {
                return BitmapFactory.decodeStream(inputStream);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }*//*
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            // 이미지 표시
            if (bitmap != null) {
                imageView_1.setImageBitmap(bitmap);
            } else {
                // 이미지 로딩 실패 시 처리
            }
        }
    }
}*/
