package com.example.anbang_;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.anbang_.client.RetrofitClient;
import com.example.anbang_.service.PropertyRetrofitService;

import java.util.Objects;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BuyingPropertyActivity extends AppCompatActivity {
    Call<ResponseBody> call;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buying_property);
        Button BuyingPropertyBtn = (Button) findViewById(R.id.buy_property_button);
        PropertyRetrofitService propertyRetrofitService = RetrofitClient.getInstance().create(PropertyRetrofitService.class);
        Call<ResponseBody> responseBodyCall = propertyRetrofitService.buildChannelCA();
        BuyingPropertyBtn.setOnClickListener(view -> {
            responseBodyCall.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    Toast.makeText(getApplicationContext(), "통신 성공", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Log.d("HTTP", "로그인 연결 실패 ");
                    Log.e("연결실패", Objects.requireNonNull(t.getMessage()));
                }
            });

            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            Toast.makeText(getApplicationContext(), "구매 완료", Toast.LENGTH_SHORT).show();
        });
    }
}