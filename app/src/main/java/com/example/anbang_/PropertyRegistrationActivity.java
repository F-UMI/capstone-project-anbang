package com.example.anbang_;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.example.anbang_.key.APIConfig;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class PropertyRegistrationActivity extends AppCompatActivity {

    private static final int REQUEST_PERMISSION_CODE = 123;
    private static final int REQUEST_IMAGE_PICK = 124;
    private DatePicker vDatePicker;
    private EditText et_PropertyAddress;
    private EditText et_AddressBuilding;
    private EditText et_AddressRoom;
    private EditText et_Pyeong;
    private EditText et_Squaremeter;
    private EditText et_NumOfRoom;
    private EditText et_MaintenanceCost;
    private EditText et_PropertyDetail;
    private EditText et_Price;
    private RadioGroup rg_PropertyType;
    private RadioGroup rg_TransactionInfo;
    private RadioGroup rg_MaintenanceCostPresence;
    private RadioGroup rg_PropertyTypeDetail;
    private RadioButton rb_BoneRoom;
    private Button btn_PhotoRegistration;
    private Button btn_PropertyRegistration;
    private ImageView iv_PropertyPhoto;
    private AmazonS3 s3Client;
    private ArrayList<Uri> selectedImageUris;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_property_registration);

        et_PropertyAddress = findViewById(R.id.property_address);
        et_AddressBuilding = findViewById(R.id.address_building);
        et_AddressRoom = findViewById(R.id.address_room);
        et_Pyeong = findViewById(R.id.pyeong);
        et_Squaremeter = findViewById(R.id.squaremeter);
        et_NumOfRoom = findViewById(R.id.num_of_room);
        et_Price = findViewById(R.id.price);
        et_MaintenanceCost = findViewById(R.id.maintenance_cost);
        et_PropertyDetail = findViewById(R.id.property_detail);

        rg_MaintenanceCostPresence = findViewById(R.id.maintenance_cost_presence);
        rg_TransactionInfo = findViewById(R.id.transaction_info);
        rg_PropertyType = findViewById(R.id.property_type);

        iv_PropertyPhoto = findViewById(R.id.property_photo);

        btn_PhotoRegistration = findViewById(R.id.photo_registration_btn);
        btn_PropertyRegistration = findViewById(R.id.property_registration_btn);

        vDatePicker = findViewById(R.id.vDatePicker);

        //라디오버튼 열고닫기
        rb_BoneRoom = findViewById(R.id.type_oneroom);
        rg_PropertyTypeDetail = findViewById(R.id.property_type_detail);

        rg_PropertyType.setOnCheckedChangeListener((radioGroup, checkedId) -> {
            if (rb_BoneRoom.isChecked()) {
                rg_PropertyTypeDetail.setVisibility(View.VISIBLE);
            } else {
                rg_PropertyTypeDetail.setVisibility(View.INVISIBLE);
            }
        });

        //이미지 등록
        btn_PhotoRegistration.setOnClickListener(v -> openGallery());
        //등록 버튼
        btn_PropertyRegistration.setOnClickListener(v -> {
            if (!selectedImageUris.isEmpty()) {
                // Upload the selected images to S3
                new PropertyRegistrationActivity.S3ImageUploadTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, selectedImageUris);
                Log.e("S3", "업로드 성공");
            } else {
                Toast.makeText(PropertyRegistrationActivity.this, "Please select at least one image", Toast.LENGTH_SHORT).show();
            }
            Toast.makeText(getApplicationContext(), "등록 완료", Toast.LENGTH_SHORT).show();
            onClickPropertyRegistration();
            saveSelectedDate();
        });
        AWSCredentials credentials = new BasicAWSCredentials(APIConfig.ACCESS_KEY, APIConfig.SECRET_ACCESS_KEY);
        s3Client = new AmazonS3Client(credentials);
        // Set your S3 bucket name
        String bucketName = APIConfig.BUCKET_NAME;
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
                iv_PropertyPhoto.setImageURI(selectedImageUris.get(0));
                // Show the upload button
                /*uploadButton.setVisibility(View.VISIBLE);*/
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
                String filePath = getRealPathFromURI(PropertyRegistrationActivity.this, selectedImageUri);
                // Create a File object from the file path
                File file = new File(filePath);
                // Specify the destination folder and file name in the S3 bucket
                String folderName = "test4";
                String fileName = file.getName();
                String s3ObjectKey = folderName + "/" + fileName;
                // Upload the file to S3
                s3Client.putObject(new PutObjectRequest(APIConfig.BUCKET_NAME, s3ObjectKey, file));
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Toast.makeText(PropertyRegistrationActivity.this, "Images uploaded to S3", Toast.LENGTH_SHORT).show();
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

    private String saveSelectedDate() {
        // DatePicker에서 선택한 날짜 가져오기
        int year = vDatePicker.getYear();
        int month = vDatePicker.getMonth();
        int dayOfMonth = vDatePicker.getDayOfMonth();

        // 선택한 날짜를 String으로 변환
        Calendar selectedDate = Calendar.getInstance();
        selectedDate.set(year, month, dayOfMonth);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String selectedDateString = dateFormat.format(selectedDate.getTime());

        return "입주가능일자 : " + selectedDateString;
    }

    private void onClickPropertyRegistration() {
        SavePropertyTask SavePropertyTask = new SavePropertyTask();

        // 매물 정보 수집
        String propertyName = getPropertyName();
        String category = getCategory();
        String address = getAddress();
        String size = getSize();
        String roomcount = getRoomCount();
        String monthyear = getMonthYear();
        String price = getEt_Price();
        String managepay = getManagePay();
        String moveindate = saveSelectedDate();
        String aboutproperty = getAboutProperty();
        String owner = getIntent().getStringExtra("USER_ID");


        // 여기에서 CouchDB에 저장하는 AsyncTask를 실행
        SavePropertyTask.execute(propertyName, category, address, size, roomcount, monthyear, price, managepay, moveindate, aboutproperty, owner).execute();
    }

    private String getPropertyName() {
        EditText propertyNameEditText = findViewById(R.id.property_name);

        String propertyName = propertyNameEditText.getText().toString();

        return propertyName;
    }

    private String getCategory() {
        RadioGroup propertyTypeRadioGroup = findViewById(R.id.property_type);
        int selectedId = propertyTypeRadioGroup.getCheckedRadioButtonId();

        RadioButton selectedRadioButton = findViewById(selectedId);
        if (selectedRadioButton != null) {
            return "유형 : " + selectedRadioButton.getText().toString();
        } else {
            return "N/A";
        }
    }

    private String getMonthYear() {
        RadioGroup monthyearRadioGroup = findViewById(R.id.transaction_info);
        int selectedId = monthyearRadioGroup.getCheckedRadioButtonId();

        RadioButton selectedRadioButton = findViewById(selectedId);
        if (selectedRadioButton != null) {
            return "월세/전세 : " + selectedRadioButton.getText().toString();
        } else {
            return "N/A";
        }
    }

    private String getAddress() {
        EditText addressEditText = findViewById(R.id.property_address);
        EditText buildingEditText = findViewById(R.id.address_building);
        EditText roomEditText = findViewById(R.id.address_room);

        // 각각의 EditText에서 문자열을 가져옴
        String address = addressEditText.getText().toString();
        String building = buildingEditText.getText().toString();
        String room = roomEditText.getText().toString();

        // 주소, 동, 호를 합쳐서 반환
        return "주소 : " + address + " " + building + "동 " + room + "호";
    }

    private String getSize() {
        EditText pyeongEditText = findViewById(R.id.pyeong);
        EditText squaremeterEditText = findViewById(R.id.squaremeter);

        String pyeong = pyeongEditText.getText().toString();
        String squaremeter = squaremeterEditText.getText().toString();

        return "방 크기 : " + pyeong + "평, " + squaremeter + "m²";
    }

    private String getRoomCount() {
        EditText roomcountEditText = findViewById(R.id.num_of_room);

        String roomcount = roomcountEditText.getText().toString();

        return "방 개수 : " + roomcount + "개";
    }

    private String getEt_Price() {
        EditText priceEditText = findViewById(R.id.price);

        String price = priceEditText.getText().toString();

        return "가격 : " + price + "만원";
    }

    private String getManagePay() {
        RadioGroup costPresenceRadioGroup = findViewById(R.id.maintenance_cost_presence);
        EditText maintenanceCostEditText = findViewById(R.id.maintenance_cost);

        int selectedId = costPresenceRadioGroup.getCheckedRadioButtonId();
        String maintenanceCost = maintenanceCostEditText.getText().toString();

        if (selectedId == R.id.cost_yes) {
            return "관리비 : " + maintenanceCost + "원";
        } else {
            return "관리비 : 없음";
        }
    }

    private String getAboutProperty() {
        EditText aboutpropertyEditText = findViewById(R.id.property_detail);

        String aboutproperty = aboutpropertyEditText.getText().toString();


        return "상세 정보 : " + aboutproperty;
    }


    private class SavePropertyTask extends AsyncTask<String, Void, Integer> {

        @Override
        protected Integer doInBackground(String... params) {

            try {
                URL url = new URL("http://10.0.2.2:5984/property"); // CouchDB URL
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");

                // 사용자 정보 생성
                String userJson = "{ \"_id\":\"" + params[0] + "\", \"category\":\"" + params[1] + "\", \"address\":\"" + params[2] +
                        "\", \"size\":\"" + params[3] + "\", \"roomcount\":\"" + params[4] + "\", \"monthyear\":\"" + params[5] +
                        "\", \"price\":\"" + params[6] + "\", \"managepay\":\"" + params[7] + "\", \"moveindate\":\"" + params[8] +
                        "\", \"aboutproperty\":\"" + params[9] + "\", \"owner\":\"" + params[10] + "\"}";


// 사용자 인증 정보 추가 (예: Basic 인증)
                String credentials = "admin:admin"; // 여기에 실제 CouchDB 사용자 이름과 비밀번호를 넣어야 합니다.
                String base64Credentials = Base64.encodeToString(credentials.getBytes(), Base64.DEFAULT);
                conn.setRequestProperty("Authorization", "Basic " + base64Credentials);

                conn.setDoOutput(true);

                // 데이터 전송
                OutputStream os = conn.getOutputStream();
                os.write(userJson.getBytes());
                os.flush();

// 응답 확인
                int responseCode = conn.getResponseCode();
                return responseCode;

            } catch (IOException e) {
                e.printStackTrace();
                // 추가: IOException 발생 시 로그에 출력
                Log.e("SavePropertyTask", "IOException", e);
                return -1;
            }
        }

        @Override
        protected void onPostExecute(Integer responseCode) {
            if (responseCode != null) {
                if (responseCode == HttpURLConnection.HTTP_CREATED) {
                    // 데이터가 성공적으로 CouchDB에 저장됨


                    Toast.makeText(PropertyRegistrationActivity.this, "매물 등록 성공!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(PropertyRegistrationActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    // 오류 처리
                    if (responseCode == HttpURLConnection.HTTP_CONFLICT) {
                        Toast.makeText(PropertyRegistrationActivity.this, "매물 등록에 실패하셨습니다.", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(PropertyRegistrationActivity.this, "매물 등록에 실패하셨습니다." + responseCode, Toast.LENGTH_SHORT).show();
                    }
                }
            } else {
                Toast.makeText(PropertyRegistrationActivity.this, "모든 항목을 입력해주세요.", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
