package com.example.anbang_;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.util.Linkify;
import android.util.Base64;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PropertyInfoActivity extends AppCompatActivity {

    private TextView owner, roomname, category, address, size, roomcount, monthyear, managepay, moveindate, aboutproperty;
    private String propertyname;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_property_info);

        owner = findViewById(R.id.seller_profile_name);
        roomname = findViewById(R.id.property_info_name);
        category = findViewById(R.id.category);
        address = findViewById(R.id.address);
        size = findViewById(R.id.size);
        roomcount = findViewById(R.id.roomcount);
        monthyear = findViewById(R.id.monthyear);
        managepay = findViewById(R.id.managepay);
        moveindate = findViewById(R.id.moveindate);
        aboutproperty = findViewById(R.id.aboutproperty);

        new GetPropertyTask().execute();




        TextView location1 = (TextView) findViewById(R.id.property_info_location);
        TextView sellerProfileNameTextView = findViewById(R.id.seller_profile_name);

        String sellerProfileName = sellerProfileNameTextView.getText().toString();

        String text= "부동산 위치";
        location1.setText(text);

        Linkify.TransformFilter linktest = new Linkify.TransformFilter() {
            @Override
            public String transformUrl(Matcher match, String url) {
                return "";
            }
        };
        Pattern pattern = Pattern.compile("부동산 위치");
        Linkify.addLinks(location1, pattern, "https://www.google.co.kr/maps/place/%EA%B2%BD%EA%B8%B0%EB%8F%84+%ED%99%94%EC%84%B1%EC%8B%9C+%EC%A7%84%EC%95%88%EB%8F%99/data=!3m1!4b1!4m6!3m5!1s0x357b4389e602f6e1:0x95b69f6557611b2d!8m2!3d37.2153132!4d127.0338029!16s%2Fm%2F0nbh4l8?hl=ko&entry=ttu", null, linktest);

        ImageButton profile_img1 = (ImageButton) findViewById(R.id.seller_profile_img);
        profile_img1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(getApplicationContext(), UserProfileActivity.class);
                intent.putExtra("USER_ID", sellerProfileName);
                startActivity(intent);
            }
        });
    }
    private class GetPropertyTask extends AsyncTask<String, Void, JSONObject> {

        @Override
        protected JSONObject doInBackground(String... params) { // CouchDB에서 사용자 정보 조회
            try {
                URL url = new URL("http://10.0.2.2:5984/property/7942c79a2c1a6b854243fd31ce001dea"); // CouchDB에서 사용자 정보 조회
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");

                // 사용자 인증 정보 추가 (예: Basic 인증)
                String credentials = "admin:admin"; // 여기에 실제 CouchDB 사용자 이름과 비밀번호를 넣어야 합니다.
                String base64Credentials = Base64.encodeToString(credentials.getBytes(), Base64.DEFAULT);
                conn.setRequestProperty("Authorization", "Basic " + base64Credentials);

                // 응답 확인
                int responseCode = conn.getResponseCode();

                // responseCode가 HTTP_OK인 경우에 실행됨
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    // 데이터가 성공적으로 조회됨
                    InputStream inputStream = conn.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                    StringBuilder stringBuilder = new StringBuilder();
                    String line;

                    // 응답 데이터를 읽어서 StringBuilder에 추가
                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuilder.append(line);
                    }

                    // JSON 데이터 확인
                    String userJson = stringBuilder.toString();
                    try {
                        // userJson을 JSONObject로 변환하여 반환
                        return new JSONObject(userJson);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        // JSON 파싱 오류
                        return null;
                    }

                } else {
                    // HTTP_OK가 아닌 경우
                    return null;
                }

            } catch (IOException e) {
                e.printStackTrace();
                // 추가: IOException 발생 시 로그에 출력
                return null;
            }
        }
        @Override
        protected void onPostExecute(JSONObject userJsonObject) { // CouchDB에서 정보를 제대로 조회하면 텍스트를 띄워줌
            if (userJsonObject != null) {
                // 사용자 정보가 성공적으로 조회됨


                // 각 필드의 값을 가져옴
                String getowner = userJsonObject.optString("owner", "N/A");
                String getname = userJsonObject.optString("name", "N/A");
                String getcategory = userJsonObject.optString("category", "N/A");
                String getaddress = userJsonObject.optString("address", "N/A");
                String getsize = userJsonObject.optString("size", "N/A");
                String getroomcount = userJsonObject.optString("roomcount", "N/A");
                String getmonthyear = userJsonObject.optString("monthyear", "N/A");
                String getmanagepay = userJsonObject.optString("managepay", "N/A");
                String getmoveindate = userJsonObject.optString("moveindate", "N/A");
                String getaboutproperty = userJsonObject.optString("aboutproperty", "N/A");

                // TextView에 정보를 표시
                owner.setText(getowner);
                roomname.setText(getname);
                category.setText(getcategory);
                address.setText(getaddress);
                size.setText(getsize);
                roomcount.setText(getroomcount);
                monthyear.setText(getmonthyear);
                managepay.setText(getmanagepay);
                moveindate.setText(getmoveindate);
                aboutproperty.setText(getaboutproperty);

            } else {
                // 사용자 정보 조회 실패
            }
        }
    }
}