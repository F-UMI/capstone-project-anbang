package com.example.anbang_;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public class UserProfileActivity extends AppCompatActivity {

    ListView listView;
    ListviewAdapter2 adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        listView = (ListView) findViewById(R.id.profile_current_transaction);
        adapter = new ListviewAdapter2();

        adapter.addItem(ContextCompat.getDrawable(this,R.drawable.house),"매물1");
        adapter.addItem(ContextCompat.getDrawable(this,R.drawable.house),"매물2");
        adapter.addItem(ContextCompat.getDrawable(this,R.drawable.house),"매물3");
        adapter.addItem(ContextCompat.getDrawable(this,R.drawable.house),"매물4");
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long I) {
                ListviewItem listviewItem = (ListviewItem)adapterView.getItemAtPosition(i);
                Intent intent = new Intent(getApplicationContext(), PropertyInfoActivity.class);
                startActivity(intent);
            }
        });

        listView = (ListView) findViewById(R.id.profile_past_transaction);
        adapter = new ListviewAdapter2();

        adapter.addItem(ContextCompat.getDrawable(this,R.drawable.house),"매물1");
        adapter.addItem(ContextCompat.getDrawable(this,R.drawable.house),"매물2");
        adapter.addItem(ContextCompat.getDrawable(this,R.drawable.house),"매물3");
        adapter.addItem(ContextCompat.getDrawable(this,R.drawable.house),"매물4");
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long I) {
                ListviewItem listviewItem = (ListviewItem)adapterView.getItemAtPosition(i);
                Intent intent = new Intent(getApplicationContext(), PropertyInfoActivity.class);
                startActivity(intent);
            }
        });
    }
}