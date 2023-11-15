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
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.S3Object;

import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {

    private ImageView imageView;
    private Button button;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = findViewById(R.id.imageView);
        button = (Button) findViewById(R.id.button);

        button.setOnClickListener(view -> {
            // 버튼 클릭 시 다른 액티비티로 전환
            Intent intent = new Intent(MainActivity.this, CameraActivity.class);
            startActivity(intent);
        });

        // AsyncTask 실행
//        new DownloadImageTask().execute();
    }

    // AsyncTask 클래스 정의
    private class DownloadImageTask extends AsyncTask<Void, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(Void... voids) {
            // S3에서 이미지 다운로드
            AmazonS3Client s3Client = new AmazonS3Client(new BasicAWSCredentials("액세스", "시크릿"));
            S3Object s3Object = s3Client.getObject("버킷", "test1/KakaoTalk_20230825_021231523_01.jpg");

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