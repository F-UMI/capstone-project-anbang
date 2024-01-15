package com.example.anbang_;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageButton profile1 = findViewById(R.id.profile1);
        profile1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
                startActivity(intent);
            }
        });

        Button buying1 = (Button) findViewById(R.id.buying1);
        buying1.setOnClickListener(new View.OnClickListener(){
        @Override
            public void onClick(View view){
            Intent intent = new Intent(getApplicationContext(), Buying1Activity.class);
            startActivity(intent);
        }
        });

        Button buying2 = (Button) findViewById(R.id.buying2);
        buying2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(getApplicationContext(), Buying2Activity.class);
                startActivity(intent);
            }
        });

        Button buying3 = (Button) findViewById(R.id.buying3);
        buying3.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(getApplicationContext(), Buying3Activity.class);
                startActivity(intent);
            }
        });

        Button selling1 = (Button) findViewById(R.id.selling1);
        selling1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(getApplicationContext(), RegistrationActivity.class);
                startActivity(intent);
            }
        });
    }
}