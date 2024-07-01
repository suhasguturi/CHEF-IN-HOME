package com.example.chef_in_home;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class ListViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_list_view);

        ListView listView = findViewById(R.id.listView);

        String[] items = {"SUHAS", "SANTHOSH", "SUBASH", "RAJESH", "SURESH"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, items);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedPerson = (String) parent.getItemAtPosition(position);
                Intent intent = new Intent(ListViewActivity.this, ProfileActivity.class);
                intent.putExtra("person_name", selectedPerson);
                startActivity(intent);
            }
        });
    }
}
