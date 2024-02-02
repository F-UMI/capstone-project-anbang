package com.example.anbang_;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class BuyingAptActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buying_apt);

//        listView = (ListView) findViewById(R.id.listview2);
//        adapter = new ListviewAdapter();
//
//        adapter.addItem(ContextCompat.getDrawable(this,R.drawable.house),"매물1");
//        adapter.addItem(ContextCompat.getDrawable(this,R.drawable.house),"매물2");
//        adapter.addItem(ContextCompat.getDrawable(this,R.drawable.house),"매물3");
//        adapter.addItem(ContextCompat.getDrawable(this,R.drawable.house),"매물4");
//        adapter.addItem(ContextCompat.getDrawable(this,R.drawable.house),"매물5");
//        adapter.addItem(ContextCompat.getDrawable(this,R.drawable.house),"매물6");
//        adapter.addItem(ContextCompat.getDrawable(this,R.drawable.house),"매물7");
//        listView.setAdapter(adapter);
//
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long I) {
//                ListviewItem listviewItem = (ListviewItem)adapterView.getItemAtPosition(i);
//                Intent intent = new Intent(getApplicationContext(), Info1Activity.class);
//                startActivity(intent);
//            }
//        });
    }
}