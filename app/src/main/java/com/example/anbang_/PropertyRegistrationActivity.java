package com.example.anbang_;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class PropertyRegistrationActivity extends AppCompatActivity {

    private DatePicker datePicker;

    private RadioGroup property_type;
    private EditText property_address;
    private EditText address_building;
    private EditText address_room;
    private EditText pyeong;
    private EditText squaremeter;
    private EditText num_of_room;
    private RadioGroup transaction_info;
    private EditText price;
    private RadioGroup maintenance_cost_presence;
    private EditText maintenance_cost;
    private EditText property_detail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_property_registration);

        datePicker = findViewById(R.id.DatePicker);
        Button propertyRegistrationButton = findViewById(R.id.property_registration);
        propertyRegistrationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickPropertyRegistration();
                saveSelectedDate();
            }
        });
    }
    private String saveSelectedDate() {
        // DatePicker에서 선택한 날짜 가져오기
        int year = datePicker.getYear();
        int month = datePicker.getMonth();
        int dayOfMonth = datePicker.getDayOfMonth();

        // 선택한 날짜를 String으로 변환
        Calendar selectedDate = Calendar.getInstance();
        selectedDate.set(year, month, dayOfMonth);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String selectedDateString = dateFormat.format(selectedDate.getTime());

        return "입주가능일자 : " + selectedDateString;
    }
    private void onClickPropertyRegistration() {
        // 매물 정보 수집
        String propertyName = getPropertyName();
        String category = getCategory();
        String address = getAddress();
        String size = getSize();
        String roomcount = getRoomCount();
        String monthyear = getMonthYear();
        String price = getPrice();
        String managepay = getManagePay();
        String moveindate = saveSelectedDate();
        String aboutproperty = getAboutProperty();
        String owner = getIntent().getStringExtra("USER_ID");


        // 여기에서 CouchDB에 저장하는 AsyncTask를 실행
        new SavePropertyTask().execute(propertyName, category, address, size, roomcount, monthyear,price,managepay,moveindate,aboutproperty,owner).execute();
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

    private String getPrice() {
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

                    property_type = findViewById(R.id.property_type);
                    property_address = findViewById(R.id.property_address);
                    address_building = findViewById(R.id.address_building);
                    address_room = findViewById(R.id.address_room);
                    pyeong = findViewById(R.id.pyeong);
                    squaremeter = findViewById(R.id.squaremeter);
                    num_of_room = findViewById(R.id.num_of_room);
                    transaction_info = findViewById(R.id.transaction_info);
                    price = findViewById(R.id.price);
                    maintenance_cost_presence = findViewById(R.id.maintenance_cost_presence);
                    maintenance_cost = findViewById(R.id.maintenance_cost);
                    DatePicker vDatePicker = findViewById(R.id.vDatePicker);
                    property_detail = findViewById(R.id.property_detail);
                    Button photo_registration = findViewById(R.id.photo_registration);
                    Button property_registration = findViewById(R.id.property_registration);
                    property_registration.setOnClickListener(view -> {

                    });


                    //라디오버튼 열고닫기
                    RadioGroup rgprotype = findViewById(R.id.property_type);
                    RadioButton rboneroom = findViewById(R.id.type_oneroom);
                    RadioGroup rgprotypedetail = findViewById(R.id.property_type_detail);


                    rgprotype.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                            if (rboneroom.isChecked()){
                                rgprotypedetail.setVisibility(View.VISIBLE);
                            } else {
                                rgprotypedetail.setVisibility(View.INVISIBLE);
                            }

                        }
                    });

                    //등록 버튼
                    property_registration.setOnClickListener(new View.OnClickListener(){
                        @Override
                        public void onClick(View view){
                            Toast.makeText(getApplicationContext(),"등록 완료", Toast.LENGTH_SHORT).show();
                        }
                    });


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
