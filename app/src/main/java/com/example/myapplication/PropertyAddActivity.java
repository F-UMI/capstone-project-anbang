package com.example.myapplication;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.myapplication.database.PropertyDB;
import com.example.myapplication.dto.PropertyDto;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import database.BoardDB;
import dto.BoardDto;

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

    private byte[] imagePath;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_property);
        propertyDB = PropertyDB.getInstance(this);
        date = String.valueOf(LocalDateTime.now(ZoneId.of("Asia/Seoul")).format(DateTimeFormatter.ofPattern("MM월 dd일 HH시 mm분")));

        propertyType = findViewById(R.id.property_type);
        propertyAddress = findViewById(R.id.property_address);
        detailedAddress = findViewById(R.id.address_building);
        propertySize = findViewById(R.id.address_room);
        numberOfRooms = findViewById(R.id.num_of_room);
        typeOfPropertyTransaction = findViewById(R.id.transaction_info);
        propertyPrice = findViewById(R.id.price);
        maintenanceCost = findViewById(R.id.maintenance_cost);
        availableMoveInDate = String.valueOf(findViewById(R.id.vDatePicker));
        btnGetImage = findViewById(R.id.photo_registration);

        btnGetImage.setOnClickListener(v -> {
            openGallery();
        });


        final Runnable addRunnable = () -> {
            PropertyDto newPropertyDto = new PropertyDto();
            newPropertyDto.setPropertyType(propertyType.getText().toString());
            newPropertyDto.setPropertyAddress(propertyAddress.getText().toString());
            newPropertyDto.setDetailedAddress(detailedAddress.getText().toString());
            newPropertyDto.setPropertySize(propertySize.getText().toString());
            newPropertyDto.setNumberOfRooms(numberOfRooms.getText().toString());
            newPropertyDto.setTypeOfPropertyTransaction(typeOfPropertyTransaction.getText().toString());
            newPropertyDto.setPropertyPrice(propertyPrice.getText().toString());
            newPropertyDto.setMaintenanceCost(maintenanceCost.getText().toString());
            newPropertyDto.setAvailableMoveInDate(availableMoveInDate.getText().toString());
            propertyDB.boardDao().insert(newPropertyDto);
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
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {

            Uri selectedImageUri = data.getData();
            Bitmap imageBitmap = convertUriToBitmap(selectedImageUri);
            displaySelectedImage(imageBitmap);
            setImagePath(imageBitmap);
        }
    }

    private Bitmap convertUriToBitmap(Uri uri) {
        // Convert the URI to a bitmap (you may want to use a library like Glide or Picasso)
        // For simplicity, you can use BitmapFactory
        try {
            InputStream inputStream = getContentResolver().openInputStream(uri);
            return BitmapFactory.decodeStream(inputStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void displaySelectedImage(Bitmap imageBitmap) {
        // Set the selected image to the ImageView
        addImage.setImageBitmap(imageBitmap);
    }

    private void setImagePath(Bitmap imageBitmap) {
        String image = "";
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        imageBitmap.compress(Bitmap.CompressFormat.JPEG, 50, stream);
        this.imagePath = stream.toByteArray();
    }
}