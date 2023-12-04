package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.database.PropertyDB;
import com.example.myapplication.dto.PropertyDto;

import java.util.ArrayList;
import java.util.List;

public class PropertyListActivity extends AppCompatActivity {
    private PropertyDB propertyDB;
    private List<PropertyDto> propertyDtoList;
    private PropertyAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private Button mAddBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_property_list);
        propertyDB = PropertyDB.getInstance(this);
        propertyDtoList = new ArrayList<>();
        mAdapter = new PropertyAdapter(this, propertyDtoList);
        mRecyclerView = findViewById(R.id.mRecyclerView);
        mAddBtn = findViewById(R.id.writingBtn);
        Runnable r = () -> {
            try {
                propertyDtoList = propertyDB.propertyDao().getAll();
                mAdapter = new PropertyAdapter(PropertyListActivity.this, propertyDtoList);
                mAdapter.notifyDataSetChanged();
                mRecyclerView.setAdapter(mAdapter);
                mRecyclerView.setLayoutManager(new LinearLayoutManager(PropertyListActivity.this));
                mRecyclerView.setHasFixedSize(true);
            } catch (Exception e) {
                Log.d("tag", "Error - " + e);
            }
        };
        Thread thread = new Thread(r);
        thread.start();

        mAddBtn.setOnClickListener(v -> {
            Intent i = new Intent(PropertyListActivity.this, PropertyAddActivity.class);
            startActivity(i);
            finish();
        });
    }

    @Override
    protected void onDestroy() {
        PropertyDB.destroyInstance();
        propertyDB = null;
        super.onDestroy();
    }
}
