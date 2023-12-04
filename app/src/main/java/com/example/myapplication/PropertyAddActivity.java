package com.example.myapplication;


import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.example.myapplication.database.PropertyDB;
import com.example.myapplication.dto.PropertyDto;

import java.io.File;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;


public class PropertyAddActivity extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;
    private PropertyDB propertyDB;
    private String date;

    private RadioButton propertyType;
    private EditText propertyAddress;
    private EditText detailedAddress;
    private EditText propertySize;
    private EditText numberOfRooms;
    private RadioButton typeOfPropertyTransaction;
    private EditText propertyPrice;
    private EditText maintenanceCost;
    private String availableMoveInDate;

    /*
    *         this.propertyId = propertyId;
        this.propertyName = propertyName;
        this.bargainerName = bargainerName;
        this.listingCreationDate = listingCreationDate;
    * */
    private Button btnGetImage;

    private static final int REQUEST_PERMISSION_CODE = 123;
    private static final int REQUEST_IMAGE_PICK = 124;

    private ImageView selectedImageView;
    private Button selectImageButton;
    private Button uploadButton;
    private AmazonS3 s3Client;
    private ArrayList<Uri> selectedImageUris;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_property_add);
        propertyDB = PropertyDB.getInstance(this);
        date = String.valueOf(LocalDateTime.now(ZoneId.of("Asia/Seoul")).format(DateTimeFormatter.ofPattern("MM월 dd일 HH시 mm분")));
        propertyAddress = findViewById(R.id.property_address);
        detailedAddress = findViewById(R.id.address_building);
        propertySize = findViewById(R.id.address_room);
        numberOfRooms = findViewById(R.id.num_of_room);
        propertyPrice = findViewById(R.id.price);
        maintenanceCost = findViewById(R.id.maintenance_cost);
        availableMoveInDate = String.valueOf(findViewById(R.id.vDatePicker));
        btnGetImage = findViewById(R.id.photo_registration);
        selectedImageView = findViewById(R.id.addPropertyImage);
        getPropertyTypeValue(findViewById(R.id.type_oneroom), findViewById(R.id.type_mall), findViewById(R.id.type_apart), propertyType);
        getTypeOfPropertyTransactionValue(findViewById(R.id.transaction_lease), findViewById(R.id.transaction_monthly_rent), typeOfPropertyTransaction);

        btnGetImage.setOnClickListener(v -> {
            openGallery();
        });
        // Initialize your AWS credentials and S3 client
        AWSCredentials credentials = new BasicAWSCredentials(BuildConfig.ACCESS_KEY, BuildConfig.SECRET_ACCESS_KEY);
        s3Client = new AmazonS3Client(credentials);
        // Set your S3 bucket name
        String bucketName = BuildConfig.BUCKET_NAME;


        final Runnable addRunnable = () -> {
            PropertyDto newPropertyDto = new PropertyDto();
            newPropertyDto.setPropertyName("test");
            newPropertyDto.setBargainerName("매도자");
            newPropertyDto.setListingCreationDate(date);
//            newPropertyDto.setPropertyType(propertyType.getText().toString());
            newPropertyDto.setPropertyType("원룸");
            newPropertyDto.setPropertyAddress(propertyAddress.getText().toString());
            newPropertyDto.setDetailedAddress(detailedAddress.getText().toString());
            newPropertyDto.setPropertySize(propertySize.getText().toString());
            newPropertyDto.setNumberOfRooms(numberOfRooms.getText().toString());
//            newPropertyDto.setTypeOfPropertyTransaction(typeOfPropertyTransaction.getText().toString());
            newPropertyDto.setTypeOfPropertyTransaction("전세");
            newPropertyDto.setPropertyPrice(propertyPrice.getText().toString());
            newPropertyDto.setMaintenanceCost(maintenanceCost.getText().toString());
            newPropertyDto.setAvailableMoveInDate(availableMoveInDate);
            propertyDB.propertyDao().insert(newPropertyDto);
            if (!selectedImageUris.isEmpty()) {
                // Upload the selected images to S3
                new PropertyAddActivity.S3ImageUploadTask().execute(selectedImageUris);
            } else {
                Toast.makeText(PropertyAddActivity.this, "Please select at least one image", Toast.LENGTH_SHORT).show();
            }

        };

        Button propertyRegistration = findViewById(R.id.property_registration);
        propertyRegistration.setOnClickListener(v -> {
            Thread addThread = new Thread(addRunnable);
            addThread.start();
            Intent i = new Intent(this, PropertyListActivity.class);
            startActivity(i);
            finish();
        });
    }

    private void getTypeOfPropertyTransactionValue(RadioButton lease, RadioButton monthlyRent, RadioButton typeOfPropertyTransaction) {
        if (lease.isChecked()) {
            typeOfPropertyTransaction = findViewById(R.id.transaction_lease);
        } else {
            typeOfPropertyTransaction = findViewById(R.id.transaction_monthly_rent);
        }
    }

    private void getPropertyTypeValue(RadioButton type_Oneroom, RadioButton type_Mall, RadioButton type_Apart, RadioButton propertyType) {
        if (type_Oneroom.isChecked()) {
            propertyType = findViewById(R.id.type_oneroom);
        } else if (type_Apart.isChecked()) {
            propertyType = findViewById(R.id.type_apart);
        } else {
            propertyType = findViewById(R.id.type_mall);
        }
    }

    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home: { // 뒤로가기 버튼 눌렀을 때

                Intent intent = new Intent(this, PropertyListActivity.class);
                intent.putExtra("FRAGMENT_TO_LOAD", "BoardFragment");
                startActivity(intent);
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        PropertyDB.destroyInstance();
        super.onDestroy();
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

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_PICK && resultCode == RESULT_OK && data != null) {
            selectedImageUris = new ArrayList<>();

            if (data.getData() != null) {
                // Single image selected
                selectedImageUris.add(data.getData());
            } else if (data.getClipData() != null) {
                // Multiple images selected
                int count = data.getClipData().getItemCount();
                for (int i = 0; i < count; i++) {
                    selectedImageUris.add(data.getClipData().getItemAt(i).getUri());
                }
            }

            if (!selectedImageUris.isEmpty()) {
                // Display the first selected image
                selectedImageView.setImageURI(selectedImageUris.get(0));
                // Show the upload button
            } else {
                Toast.makeText(this, "Error retrieving image URIs", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class S3ImageUploadTask extends AsyncTask<ArrayList<Uri>, Void, Void> {

        @Override
        protected Void doInBackground(ArrayList<Uri>... uriLists) {
            ArrayList<Uri> uriList = uriLists[0];

            for (Uri selectedImageUri : uriList) {
                // Get the file path from the URI
                String filePath = getRealPathFromURI(PropertyAddActivity.this, selectedImageUri);

                // Create a File object from the file path
                File file = new File(filePath);

                // Specify the destination folder and file name in the S3 bucket
                String folderName = "test2";
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
            Toast.makeText(PropertyAddActivity.this, "Images uploaded to S3", Toast.LENGTH_SHORT).show();
        }
    }

// ...


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