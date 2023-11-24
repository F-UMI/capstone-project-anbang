package com.example.myapplication;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ImageActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ImageAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);

        recyclerView = findViewById(R.id.recyclerView);

        // Create a LinearLayoutManager with horizontal orientation
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);

        // Sample data
        List<String> data = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            data.add("Item " + (i + 1));
        }

        // Set up the adapter
        List<Integer> imageResourceIds = Arrays.asList(
                R.drawable.ic_launcher_background,
                R.drawable.ic_launcher_background
                // Add more resource IDs as needed
        );

// Set up the adapter
        adapter = new ImageAdapter(this, imageResourceIds);
        recyclerView.setAdapter(adapter);
    }
}

