package com.example.anbang_;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class BuyingOneRoomActivity extends AppCompatActivity {
    ListView listView;
    ListviewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buying_one_room);

        listView = (ListView) findViewById(R.id.listview);
        adapter = new ListviewAdapter();

        adapter.addItem(ContextCompat.getDrawable(this,R.drawable.house),"매물1");
        adapter.addItem(ContextCompat.getDrawable(this,R.drawable.house),"매물2");
        adapter.addItem(ContextCompat.getDrawable(this,R.drawable.house),"매물3");
        adapter.addItem(ContextCompat.getDrawable(this,R.drawable.house),"매물4");
        adapter.addItem(ContextCompat.getDrawable(this,R.drawable.house),"매물5");
        adapter.addItem(ContextCompat.getDrawable(this,R.drawable.house),"매물6");
        adapter.addItem(ContextCompat.getDrawable(this,R.drawable.house),"매물7");
        listView.setAdapter(adapter);

        listView.setOnItemClickListener((adapterView, view, i, I) -> {
            ListviewItem listviewItem = (ListviewItem)adapterView.getItemAtPosition(i);
            Intent intent = new Intent(getApplicationContext(), PropertyInfoActivity.class);
            startActivity(intent);
        });
/*

        final String[] property = {"매물1", "매물2", "매물3", "매물4", "매물5"};

        ListView list = (ListView) findViewById(R.id.listview1);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,property);
        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                Intent intent = new Intent(getApplicationContext(), Info1Activity.class);
                startActivity(intent);
            }
        });
*/

    }
}