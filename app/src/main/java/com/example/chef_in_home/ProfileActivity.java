package com.example.chef_in_home;

import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class ProfileActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile);

        String personName = getIntent().getStringExtra("person_name");

        TextView textView = findViewById(R.id.textViewProfile);
        textView.setText("Profile of " + personName);
    }
}
