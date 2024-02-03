package com.example.myapplication.temp;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.TextView;

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
import com.example.myapplication.database.PropertyDB;
import com.example.myapplication.dto.PropertyDto;

import java.util.ArrayList;
import java.util.List;


public class PropertyViewActivity extends AppCompatActivity {
    private int position;

    private RecyclerView recyclerView;
    private PhotoAdapter photoAdapter;

    private PropertyDB propertyDB;
    private List<PropertyDto> propertyDtoList;
    private PropertyAdapter propertyAdapter;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_property_view);
        propertyDB = PropertyDB.getInstance(this);
        propertyDtoList = propertyDB.propertyDao().getAll();
        propertyAdapter = new PropertyAdapter(this, propertyDtoList);
        TextView propertyTextView = findViewById(R.id.propertyTextView);
        Intent intent = getIntent();
        position = intent.getIntExtra("position", 0);
        propertyTextView.setText(propertyDtoList.get(position).toString());

        recyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);

        new PropertyViewActivity.S3ObjectRetrievalTask().execute();
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

            ObjectListing objectListing = s3Client.listObjects(BuildConfig.BUCKET_NAME, "test3/");
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
       /* propertyDB = PropertyDB.getInstance(this);
        propertyDtoList = propertyDB.propertyDao().getAll();
        propertyAdapter = new PropertyAdapter(this, propertyDtoList);


        viewTitle = findViewById(R.id.editBoardTitle);
        viewUserName = findViewById(R.id.editBoardUserName);
        viewDate = findViewById(R.id.editBoardPassword);
        viewText = findViewById(R.id.editBoardText);
        imageView = findViewById(R.id.editBoardImage);
*//*        updateBtn = findViewById(R.id.updateBtn);
        deleteBtn = findViewById(R.id.deleteBtn);*//*

        Intent intent = getIntent();

        viewUserName.setText(intent.getStringExtra("userName"));
        viewTitle.setText(intent.getStringExtra("title"));
        viewText.setText(intent.getStringExtra("text"));
        viewDate.setText(intent.getStringExtra("date"));
        boardId = (int) intent.getLongExtra("id", 0);
        position = intent.getIntExtra("position", 0);
        imagePath = intent.getByteArrayExtra("imagePath");
        if (imagePath != null) {
            imageView.setImageBitmap(BitmapFactory.decodeByteArray(imagePath, 0, imagePath.length));
        } else {
            imageView.setVisibility(ImageView.GONE);
        }


*//*        updateBtn.setOnClickListener(e -> {
            showPasswordInputDialog("update");
        });

        deleteBtn.setOnClickListener(e -> {
            showPasswordInputDialog("delete");
        });*//*
        Log.e("viewID", boardId + "");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false); // 기존 title 지우기
        actionBar.setDisplayHomeAsUpEnabled(true); // 뒤로가기 버튼 만들기
    }

    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home: { // 뒤로가기 버튼 눌렀을 때

                Intent intent = new Intent(this, MainActivity.class);
                intent.putExtra("FRAGMENT_TO_LOAD", "BoardFragment");
                startActivity(intent);

                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

*//*    private void showPasswordInputDialog(String query) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter Password");

        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.password_input_dialog, null);
        builder.setView(dialogView);

        final EditText passwordInput = dialogView.findViewById(R.id.passwordInput);

        builder.setPositiveButton("Update", (dialog, which) -> {
            // Check if the entered password is correct (you should replace "your_password" with the actual correct password)
            String enteredPassword = passwordInput.getText().toString();
            if (enteredPassword.equals(propertyDtoList.get(position).getPassword())) {
                if (query.equals("update")) {
                    boardUpdate();
                } else {
                    boardDelete();
                }
            } else {
                showToast("잘못된 비밀번호");
            }
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> {
            // User canceled the dialog
            dialog.dismiss();
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }*//*

    private void boardUpdate() {
*//*        Intent updateIntent = new Intent(PropertyViewActivity.this, BoardEditActivity.class);
        updateIntent.putExtra("id", propertyDtoList.get(position).getId());
        updateIntent.putExtra("title", propertyDtoList.get(position).getTitle());
        updateIntent.putExtra("userName", propertyDtoList.get(position).getUserName());
        updateIntent.putExtra("text", propertyDtoList.get(position).getText());
        updateIntent.putExtra("date", propertyDtoList.get(position).getDate());
        updateIntent.putExtra("position", position);
        updateIntent.putExtra("imagePath", propertyDtoList.get(position).getImagePath());
        Log.e("position", String.valueOf(position));
        startActivity(updateIntent);
        showToast("비밀번호 인증 완료");
        finish();*//*
    }

    private void boardDelete() {
        propertyDtoList.remove(position);
        Log.e("position", String.valueOf(position));
        propertyDB.propertyDao().delete(new BoardDto(boardId));
        propertyAdapter.notifyDataSetChanged();
        propertyAdapter.notifyItemRangeChanged(position, propertyDtoList.size());
        Intent i = new Intent(this, MainActivity.class);
        i.putExtra("FRAGMENT_TO_LOAD", "BoardFragment");
        startActivity(i);
        Log.e("Delete Complete", String.valueOf(propertyDtoList.size()));
        showToast("삭제 완료");
        finish();
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }*/
