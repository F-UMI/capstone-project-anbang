package com.example.anbang_;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.util.Linkify;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MyPageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_page);

        TextView textView;
        LinearLayout buy_list;
        LinearLayout sold_list;
        LinearLayout sales_manage;

        textView = findViewById(R.id.user_modify);
        buy_list = findViewById(R.id.mypage_buy_list);
        sold_list = findViewById(R.id.mypage_sold_list);
        sales_manage = findViewById(R.id.mypage_sales_manage);


    //정보 수정 텍스트 버튼
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), UserModifyActivity.class);
                startActivity(intent);
            }

        });

        //매매 내역, 매물 관리 버튼

        buy_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MyPurchaseListActivity.class);
                startActivity(intent);
            }
        });

        sold_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MySoldListActivity.class);
                startActivity(intent);
            }
        });

        sales_manage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MySalesManageActivity.class);
                startActivity(intent);
            }
        });





    }
}