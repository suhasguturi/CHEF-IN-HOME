package com.example.chef_in_home;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UserRequestDetailActivity extends AppCompatActivity {

    private TextView textViewUserName, textViewUserContact, textViewStatus;
    private Button buttonCancel, buttonPay;
    private DatabaseReference requestRef;
    private String requestId, chefId;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_request_detail);

        // Initialize UI elements
        textViewUserName = findViewById(R.id.textViewUserName);
        textViewUserContact = findViewById(R.id.textViewUserContact);
        textViewStatus = findViewById(R.id.textViewStatus);
        buttonCancel = findViewById(R.id.buttonCancel);
        buttonPay = findViewById(R.id.buttonPay);

        // Get requestId from Intent extras
        requestId = getIntent().getStringExtra("requestId");
        chefId = getIntent().getStringExtra("chefId");

        // Initialize Firebase Database reference
        requestRef = FirebaseDatabase.getInstance().getReference("requests").child(chefId).child(requestId);

        // Load request details
        loadRequestDetails();

        // Set onClickListener for Accept button
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateRequestStatus("cancelled");
            }
        });

        // Set onClickListener for Reject button
        buttonPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateRequestStatus("paid");
            }
        });
    }

    private void loadRequestDetails() {
        requestRef.get().addOnSuccessListener(dataSnapshot -> {
            if (dataSnapshot.exists()) {
                String userName = dataSnapshot.child("userName").getValue(String.class);
                String userContact = dataSnapshot.child("userContact").getValue(String.class);
                String status = dataSnapshot.child("status").getValue(String.class);

                textViewUserName.setText("Customer: " + userName);
                textViewUserContact.setText("Contact: " + userContact);
                textViewStatus.setText("Status: " + status);

                if ("paid".equals(status)) {
                    buttonCancel.setEnabled(false);
                    buttonPay.setEnabled(false);
                }

                if ("cancelled".equals(status)) {
                    buttonCancel.setEnabled(false);
                    buttonPay.setEnabled(false);
                }
            } else {
                Toast.makeText(UserRequestDetailActivity.this, "Request not found", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(e -> {
            Toast.makeText(UserRequestDetailActivity.this, "Failed to load request details: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }

    private void updateRequestStatus(String newStatus) {
        requestRef.child("status").setValue(newStatus).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(UserRequestDetailActivity.this, "Request " + newStatus, Toast.LENGTH_SHORT).show();
                textViewStatus.setText("Status: " + newStatus);
            } else {
                Toast.makeText(UserRequestDetailActivity.this, "Failed to update request status", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
