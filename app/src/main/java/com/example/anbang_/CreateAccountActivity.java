package com.example.anbang_;

import android.content.Intent;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class CreateAccountActivity extends AppCompatActivity {

    private EditText account_id;
    private EditText account_password;
    private EditText account_name;
    private EditText account_phone;
    private EditText account_birth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        Button btn_login = (Button) findViewById(R.id.btn_login2);
        btn_login.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            }
        });

        //계좌
        Spinner accountBankSpinner = (Spinner) findViewById(R.id.account_number_bank);
        ArrayAdapter accountBankAdapter = ArrayAdapter.createFromResource(this,R.array.account_bank, android.R.layout.simple_spinner_item);
        accountBankAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        accountBankSpinner.setAdapter(accountBankAdapter);


        account_id = findViewById(R.id.account_id);
        account_password = findViewById(R.id.account_password);
        account_name = findViewById(R.id.account_name);
        account_phone = findViewById(R.id.account_phone);
        account_birth = findViewById(R.id.account_birth);
        Button btn_register = findViewById(R.id.btn_register);
        btn_register.setOnClickListener(view -> {
            // 사용자 입력 가져오기
            String userID = account_id.getText().toString();
            String userPassword = account_password.getText().toString();
            String userName = account_name.getText().toString();
            String userPhone = account_phone.getText().toString();
            String userBirth = account_birth.getText().toString();

            // AsyncTask를 사용하여 CouchDB에 데이터 저장
            new SaveUserTask().execute(userID, userPassword, userName, userPhone, userBirth);
        });
    }

    private class SaveUserTask extends AsyncTask<String, Void, Integer> {

        @Override
        protected Integer doInBackground(String... params) {
            try {
                URL url = new URL("http://10.0.2.2:5984/anbangtest"); // CouchDB URL
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");

                // 사용자 정보 생성
                String userJson = "{ \"_id\":\"" + params[0] + "\", \"password\":\"" + params[1] + "\", \"name\":\"" + params[2] + "\", \"phonenumber\":\"" + params[3] + "\", \"birth\":\"" + params[4] + "\" }";
                // 사용자 인증 정보 추가 (예: Basic 인증)
                String credentials = "admin:admin"; // 여기에 실제 CouchDB 사용자 이름과 비밀번호를 넣어야 합니다.
                String base64Credentials = Base64.encodeToString(credentials.getBytes(), Base64.DEFAULT);
                conn.setRequestProperty("Authorization", "Basic " + base64Credentials);

                conn.setDoOutput(true);

                // 데이터 전송
                OutputStream os = conn.getOutputStream();
                os.write(userJson.getBytes());
                os.flush();

                String username = account_id.getText().toString();
                String password = account_password.getText().toString();
                String name = account_name.getText().toString();
                String phone = account_phone.getText().toString();
                String birth = account_birth.getText().toString();

                if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password) || TextUtils.isEmpty(name) || TextUtils.isEmpty(phone) || TextUtils.isEmpty(birth)) {
                    // 하나라도 입력되지 않았을 때 Toast 메시지 출력
                    return null;
                }

// 응답 확인
                int responseCode = conn.getResponseCode();
                return responseCode;

            } catch (IOException e) {
                e.printStackTrace();
                // 추가: IOException 발생 시 로그에 출력
                Log.e("SaveUserTask", "IOException", e);
                return -1;
            }
        }

        @Override
        protected void onPostExecute(Integer responseCode) {
            if (responseCode != null) {
                if (responseCode == HttpURLConnection.HTTP_CREATED) {
                    // 데이터가 성공적으로 CouchDB에 저장됨


                    Toast.makeText(CreateAccountActivity.this, "회원가입 성공!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(CreateAccountActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    // 오류 처리
                    if (responseCode == HttpURLConnection.HTTP_CONFLICT) {
                        Toast.makeText(CreateAccountActivity.this, "이미 존재하는 아이디입니다.", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(CreateAccountActivity.this, "회원가입 에러" + responseCode, Toast.LENGTH_SHORT).show();
                    }
                }
            } else {
                Toast.makeText(CreateAccountActivity.this, "모든 항목을 입력해주세요.", Toast.LENGTH_SHORT).show();
            }
        }


    }
}