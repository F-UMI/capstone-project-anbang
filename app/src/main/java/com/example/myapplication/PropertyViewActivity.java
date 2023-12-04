package com.example.myapplication;


import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.database.PropertyDB;
import com.example.myapplication.dto.PropertyDto;

import java.util.List;


public class PropertyViewActivity extends AppCompatActivity {
    private int position;
    private int boardId;
    private byte[] imagePath;
    private TextView viewUserName;
    private TextView viewTitle;
    private TextView viewDate;
    private TextView viewText;

    private ImageView imageView;
    private Button backBtn;
    private Button updateBtn;
    private Button deleteBtn;

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
    }
}
