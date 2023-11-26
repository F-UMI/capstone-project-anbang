package com.example.myapplication;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.PutObjectRequest;

import java.io.File;
import java.util.ArrayList;

public class S3ImageUploadActivity extends AppCompatActivity {

    private static final int REQUEST_PERMISSION_CODE = 123;
    private static final int REQUEST_IMAGE_PICK = 124;

    private Button selectImageButton;
    private AmazonS3 s3Client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_upload);

        selectImageButton = findViewById(R.id.selectImageButton);

        selectImageButton.setOnClickListener(v -> checkPermissionAndOpenGallery());

        // Initialize your AWS credentials and S3 client
        AWSCredentials credentials = new BasicAWSCredentials(BuildConfig.ACCESS_KEY, BuildConfig.SECRET_ACCESS_KEY);
        s3Client = new AmazonS3Client(credentials);
        // Set your S3 bucket name
        String bucketName = BuildConfig.BUCKET_NAME;
    }

    private void checkPermissionAndOpenGallery() {
            openGallery();
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        startActivityForResult(intent, REQUEST_IMAGE_PICK);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openGallery();
            } else {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_PICK && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                ArrayList<Uri> selectedImages = new ArrayList<>();
                if (data.getData() != null) {
                    // Single image selected
                    selectedImages.add(data.getData());
                } else if (data.getClipData() != null) {
                    // Multiple images selected
                    for (int i = 0; i < data.getClipData().getItemCount(); i++) {
                        selectedImages.add(data.getClipData().getItemAt(i).getUri());
                    }
                }

                // Upload selected images to S3
                new S3ImageUploadTask().execute(selectedImages);
            }
        }
    }

    private class S3ImageUploadTask extends AsyncTask<ArrayList<Uri>, Void, Void> {

        @Override
        protected Void doInBackground(ArrayList<Uri>... params) {
            ArrayList<Uri> selectedImages = params[0];

            for (Uri uri : selectedImages) {
                // Get the file path from the URI
                String filePath = getRealPathFromURI(S3ImageUploadActivity.this, uri);

                // Create a File object from the file path
                File file = new File(filePath);

                // Specify the destination folder and file name in the S3 bucket
                String folderName = "images";
                String fileName = file.getName();
                String s3ObjectKey = folderName + "/" + fileName;

                // Upload the file to S3
                s3Client.putObject(new PutObjectRequest(BuildConfig.BUCKET_NAME, s3ObjectKey, file));
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Toast.makeText(S3ImageUploadActivity.this, "Images uploaded to S3", Toast.LENGTH_SHORT).show();
        }
    }

    private String getRealPathFromURI(Context context, Uri uri) {
        ContentResolver contentResolver = context.getContentResolver();
        String[] projection = {MediaStore.Images.Media.DATA};

        Cursor cursor = contentResolver.query(uri, projection, null, null, null);
        if (cursor != null) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            String filePath = cursor.getString(column_index);
            cursor.close();
            return filePath;
        }
        return null;
    }
}