package com.example.anbang_;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class LoginActivity extends AppCompatActivity {

    private EditText userId, userPassword;
    private Button btn_login, btn_register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userId = findViewById(R.id.login_user_id);
        userPassword = findViewById(R.id.login_user_password);
        btn_login = findViewById(R.id.btn_login);
        btn_register = findViewById(R.id.btn_create_account);

        //회원가입 버튼
        btn_register.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, CreateAccountActivity.class);
            startActivity(intent);


        });

        btn_login.setOnClickListener(view -> {
            // 사용자 입력 가져오기
            String inputId = userId.getText().toString();
            String inputPassword = userPassword.getText().toString();

            // AsyncTask를 사용하여 CouchDB에서 데이터 확인
            new CheckUserTask().execute(inputId, inputPassword);
        });
    }


    private class CheckUserTask extends AsyncTask<String, Void, Boolean> {

        @Override
        protected Boolean doInBackground(String... params) {
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
                    Log.d("UserJson", userJson);

                    try {
                        // userJson을 JSONObject로 변환
                        JSONObject jsonObject = new JSONObject(userJson);

                        // "password" 필드의 값을 가져옴
                        String savedPassword = jsonObject.getString("password");

                        // 사용자가 입력한 비밀번호
                        String enteredPassword = params[1];

                        // 비밀번호 확인
                        if (savedPassword.equals(enteredPassword)) {
                            // 비밀번호 일치
                            Log.d("PasswordCheck", "Password matched!");
                            return true;
                        } else {
                            // 비밀번호 불일치
                            Log.d("PasswordCheck", "Password mismatch!");
                            return false;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        // JSON 파싱 오류
                        Log.e("JsonParsingError", "Error parsing JSON", e);
                        return false;
                    }

                } else {
                    // HTTP_OK가 아닌 경우
                    return false;
                }

            } catch (IOException e) {
                e.printStackTrace();
                Log.e("ERR", e.toString());
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if (result) {
                // 로그인 성공
                Toast.makeText(LoginActivity.this, "로그인 성공!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            } else {
                // 로그인 실패
                Toast.makeText(LoginActivity.this, "아이디 또는 비밀번호가 일치하지않습니다.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
