package com.example.anbang_;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class UserProfileActivity extends AppCompatActivity {

    ListView listView;
    ListviewAdapter2 adapter;
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

        new GetUserTask().execute(intentUserId);


        listView = (ListView) findViewById(R.id.profile_current_transaction);
        adapter = new ListviewAdapter2();

        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.house), "매물1");
        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.house), "매물2");
        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.house), "매물3");
        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.house), "매물4");
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long I) {
                ListviewItem listviewItem = (ListviewItem) adapterView.getItemAtPosition(i);
                Intent intent = new Intent(getApplicationContext(), PropertyInfoActivity.class);
                startActivity(intent);
            }
        });

        listView = (ListView) findViewById(R.id.profile_past_transaction);
        adapter = new ListviewAdapter2();

        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.house), "매물1");
        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.house), "매물2");
        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.house), "매물3");
        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.house), "매물4");
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long I) {
                ListviewItem listviewItem = (ListviewItem) adapterView.getItemAtPosition(i);
                Intent intent = new Intent(getApplicationContext(), PropertyInfoActivity.class);
                startActivity(intent);
            }
        });
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


            } else {
                // 사용자 정보 조회 실패
            }
        }
    }


}