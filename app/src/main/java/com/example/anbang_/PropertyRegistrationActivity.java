package com.example.anbang_;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class PropertyRegistrationActivity extends AppCompatActivity {

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



    }
}