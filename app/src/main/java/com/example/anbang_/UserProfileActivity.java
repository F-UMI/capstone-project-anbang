package com.example.anbang_;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.graphics.drawable.Drawable;
import android.util.Log;
import android.util.Pair;

import java.io.BufferedWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.List;
import java.util.ArrayList;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class UserProfileActivity extends AppCompatActivity {

    private String ownerId;
    private ListView currentTransactionListView;
    private ListView pastTransactionListView;
    private ListviewAdapter2 currentTransactionAdapter;
    private ListviewAdapter2 pastTransactionAdapter;
    private TextView userId, userName, userPhone, userRegistrationDate, userBirth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        String intentUserId = getIntent().getStringExtra("USER_ID");

        userId = findViewById(R.id.userId);
        userName = findViewById(R.id.userName);
        userPhone = findViewById(R.id.userPhone);
        userRegistrationDate = findViewById(R.id.userReigstrationDate);
        userBirth = findViewById(R.id.userBirth);

        // 사용자 정보 가져오기
        new GetUserTask().execute(intentUserId);

        // 현재 거래 ListView 초기화
        currentTransactionListView = findViewById(R.id.profile_current_transaction);
        currentTransactionAdapter = new ListviewAdapter2();
        currentTransactionListView.setAdapter(currentTransactionAdapter);

        // 과거 거래 ListView 초기화
        pastTransactionListView = findViewById(R.id.profile_past_transaction);
        pastTransactionAdapter = new ListviewAdapter2();
        pastTransactionListView.setAdapter(pastTransactionAdapter);

        // 속성 정보 가져오기
        new GetPropertyTask().execute(intentUserId);
    }

    private class GetUserTask extends AsyncTask<String, Void, JSONObject> {

        @Override
        protected JSONObject doInBackground(String... params) {
            try {
                URL url = new URL("http://10.0.2.2:5984/anbangtest/" + params[0]); // CouchDB에서 사용자 정보 조회
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
        protected void onPostExecute(JSONObject userJsonObject) {
            if (userJsonObject != null) {
                // 사용자 정보가 성공적으로 조회됨


                // 각 필드의 값을 가져옴
                String id = userJsonObject.optString("_id", "N/A");
                String birth = userJsonObject.optString("birth", "N/A");
                String phone = userJsonObject.optString("phonenumber", "N/A");
                String name = userJsonObject.optString("name", "N/A");
                String registerDate = userJsonObject.optString("registrationDate", "N/A");

                // TextView에 정보를 표시
                userId.setText(id);
                userBirth.setText(id);
                userPhone.setText(phone);
                userName.setText(name);
                userRegistrationDate.setText(registerDate);
                new GetPropertyTask().execute(id);


            } else {
                // 사용자 정보 조회 실패
            }
        }
    }
    private class GetPropertyTask extends AsyncTask<String, Void, List<Pair<Drawable, String>>> {

        @Override
        protected List<Pair<Drawable, String>> doInBackground(String... params) {
            List<Pair<Drawable, String>> propertyList = new ArrayList<>();
            try {
                // Mango 쿼리를 사용하여 속성 정보를 조회
                String query = "{\"selector\":{\"owner\":\"" + params[0] + "\"}}";
                URL url = new URL("http://10.0.2.2:5984/property/_find");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setDoOutput(true);

                // Basic 인증 정보 추가
                String username = "admin";
                String password = "admin";
                String credentials = username + ":" + password;
                String base64Credentials = Base64.encodeToString(credentials.getBytes(), Base64.DEFAULT);
                conn.setRequestProperty("Authorization", "Basic " + base64Credentials);

                // 쿼리 전송
                OutputStream os = conn.getOutputStream();
                os.write(query.getBytes());
                os.flush();

                // 응답 확인
                int responseCode = conn.getResponseCode();

                if (responseCode == HttpURLConnection.HTTP_OK) {
                    InputStream inputStream = conn.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                    StringBuilder stringBuilder = new StringBuilder();
                    String line;

                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuilder.append(line);
                    }

                    // 응답 데이터 파싱
                    String response = stringBuilder.toString();
                    JSONObject jsonResponse = new JSONObject(response);
                    JSONArray docs = jsonResponse.getJSONArray("docs");

                    // 파싱된 데이터를 리스트에 추가
                    for (int i = 0; i < docs.length(); i++) {
                        JSONObject property = docs.getJSONObject(i);
                        String propertyId = property.optString("_id", "N/A");
                        // 속성 정보를 Drawable과 String 형태로 변환하여 추가
                        Drawable drawable = getResources().getDrawable(R.drawable.house); // 기본 이미지 설정
                        propertyList.add(new Pair<>(drawable, propertyId));
                    }
                } else {
                    return null;
                }

            } catch (IOException | JSONException e) {
                e.printStackTrace();
                return null;
            }
            return propertyList;
        }

        @Override
        protected void onPostExecute(List<Pair<Drawable, String>> propertyList) {
            if (propertyList != null) {
                // doInBackground 메서드에서 얻은 속성 리스트를 사용하여 ListView를 업데이트합니다.
                ListviewAdapter2 adapter = new ListviewAdapter2();
                for (Pair<Drawable, String> property : propertyList) {
                    adapter.addItem(property.first, property.second);
                }
                // ListView에 어댑터 설정
                currentTransactionListView.setAdapter(adapter); // currentTransactionListView를 사용하거나 pastTransactionListView를 사용할지에 따라 설정해야 합니다.
            } else {
                // 에러 처리 로직 추가
            }
        }
    }

}