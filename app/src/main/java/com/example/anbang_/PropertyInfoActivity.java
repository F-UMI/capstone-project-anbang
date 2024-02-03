package com.example.anbang_;

import android.content.Intent;
import android.os.Bundle;
import android.text.util.Linkify;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PropertyInfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_property_info);

        TextView location1 = (TextView) findViewById(R.id.property_info_location);
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
                startActivity(intent);
            }
        });
    }
}