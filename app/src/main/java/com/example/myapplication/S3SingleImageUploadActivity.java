package com.example.myapplication;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.S3Object;

import java.io.IOException;
import java.io.InputStream;


public class S3SingleImageUploadActivity extends AppCompatActivity {

    private ImageView imageView;
    private Button button;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = findViewById(R.id.imageView);
        button = findViewById(R.id.button);

        CognitoCachingCredentialsProvider credentialsProvider = new CognitoCachingCredentialsProvider(
                getApplicationContext(),
                "ap-northeast-2:1601b465-7ffb-4249-8a7f-5b4437df9462",  // Replace with your Cognito identity pool ID
                Regions.AP_NORTHEAST_2  // Replace with the appropriate AWS region
        );

        button.setOnClickListener(view -> {
            // 버튼 클릭 시 다른 액티비티로 전환
            Intent intent = new Intent(S3SingleImageUploadActivity.this, CameraActivity.class);
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

            // InputStream에서 비트맵으로 변환
            try (InputStream inputStream = s3Object.getObjectContent()) {
                return BitmapFactory.decodeStream(inputStream);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            // 이미지 표시
            if (bitmap != null) {
                imageView.setImageBitmap(bitmap);
            } else {
                // 이미지 로딩 실패 시 처리
            }
        }
    }

}