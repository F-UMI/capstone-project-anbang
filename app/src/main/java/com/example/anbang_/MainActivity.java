package com.example.anbang_;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        String myId = getIntent().getStringExtra("USER_ID");

        ImageButton profile1 = findViewById(R.id.profile_button);
        profile1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(getApplicationContext(), UserProfileActivity.class);
                Intent intent = new Intent(getApplicationContext(), MyPageActivity.class);
                intent.putExtra("USER_ID", myId);

                startActivity(intent);
            }
        });

        Button buying1 = (Button) findViewById(R.id.buying_oneroom_button);
        buying1.setOnClickListener(new View.OnClickListener(){
        @Override
            public void onClick(View view){
            Intent intent = new Intent(getApplicationContext(), BuyingOneRoomActivity.class);
            startActivity(intent);
        }
        });

        Button buying2 = (Button) findViewById(R.id.buying_apt_button);
        buying2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(getApplicationContext(), BuyingAptActivity.class);
                startActivity(intent);
            }
        });

        Button buying3 = (Button) findViewById(R.id.buying_store_button);
        buying3.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(getApplicationContext(), BuyingStoreActivity.class);
                startActivity(intent);
            }
        });

        Button selling1 = (Button) findViewById(R.id.sell_property);
        selling1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(getApplicationContext(), PropertyRegistrationActivity.class);
                intent.putExtra("USER_ID", myId);
                startActivity(intent);
            }
        });
    }
}