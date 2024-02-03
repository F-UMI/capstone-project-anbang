package com.example.myapplication.temp;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;
import com.example.myapplication.database.PropertyDB;
import com.example.myapplication.dto.PropertyDto;

import java.util.List;

public class PropertyEditActivity extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;

    byte[] imagePath = new byte[0];
    private PropertyDB propertyDB;
    private List<PropertyDto> propertyDtoList;
    private PropertyAdapter propertyAdapter;

    private EditText editBoardUserName;
    private EditText editBoardTitle;

    private EditText editBoardPassword;
    private EditText editBoardText;
    private ImageView editBoardImage;
    private Button imageEditBtn;
    private Button updateBoardBtn;


    private int position;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_property_edit);
        propertyDB = PropertyDB.getInstance(this);
        propertyDtoList = propertyDB.propertyDao().getAll();
/*//
        propertyAdapter = new PropertyAdapter(this, propertyDtoList);

        editBoardTitle = findViewById(R.id.editBoardTitle);
        editBoardUserName = findViewById(R.id.editBoardUserName);
        editBoardPassword = findViewById(R.id.editBoardPassword);
        editBoardText = findViewById(R.id.editBoardText);
        editBoardImage = findViewById(R.id.editBoardImage);

        imageEditBtn = findViewById(R.id.imageEditBtn);
        updateBoardBtn = findViewById(R.id.updateBoardBtn);

        Intent intent = getIntent();
        position = intent.getIntExtra("position", 0);
        editBoardTitle.setText(intent.getStringExtra("title"));
        editBoardUserName.setText(intent.getStringExtra("userName"));
        editBoardPassword.setText(intent.getStringExtra("password"));
        editBoardText.setText(intent.getStringExtra("text"));
        if (propertyDtoList.get(position).getImagePath() != null) {
            editBoardImage.setImageBitmap(byteArrayToBitmap(propertyDtoList.get(position).getImagePath()));
        } else {
            editBoardImage.setVisibility(ImageView.GONE);
        }

        imageEditBtn.setOnClickListener(e -> {
            openGallery();
        });

        updateBoardBtn.setOnClickListener(e -> {
            new Thread(() -> {
                String title = editBoardTitle.getText().toString();
                String userName = editBoardUserName.getText().toString();
                String text = editBoardText.getText().toString();
                String password = editBoardPassword.getText().toString();
                String date = String.valueOf(LocalDateTime.now(ZoneId.of("Asia/Seoul")).format(DateTimeFormatter.ofPattern("MM월 dd일 HH시 mm분")));
                PropertyDto propertyDto = new PropertyDto(propertyDtoList.get(position).getId(), password, title, userName, text, date, imagePath);
                Log.e("editID", propertyDtoList.get(position).getId().toString());
                propertyDB.propertyDao().update(propertyDto);
                propertyAdapter.notifyDataSetChanged();
            }).start();

            Intent i = new Intent(this, MainActivity.class);
            i.putExtra("FRAGMENT_TO_LOAD", "BoardFragment");
            startActivity(i);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.putExtra("id", propertyDtoList.get(position).getId());
            i.putExtra("title", propertyDtoList.get(position).getTitle());
            i.putExtra("userName", propertyDtoList.get(position).getUserName());
            i.putExtra("text", propertyDtoList.get(position).getText());
            i.putExtra("date", propertyDtoList.get(position).getDate());
            i.putExtra("position", position);
            this.startActivity(i);
        });

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false); // 기존 title 지우기
        actionBar.setDisplayHomeAsUpEnabled(true); // 뒤로가기 버튼 만들기
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
        editBoardImage.setImageBitmap(imageBitmap);
        editBoardImage.setVisibility(ImageView.VISIBLE);
    }

    private void setImagePath(Bitmap imageBitmap) {
        String image = "";
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        imageBitmap.compress(Bitmap.CompressFormat.JPEG, 50, stream);
        this.imagePath = stream.toByteArray();
    }

    public Bitmap byteArrayToBitmap(byte[] byteArray) {
        Bitmap bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        return bitmap;
    }*/
    }
}
