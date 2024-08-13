package com.example.chef_in_home;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RequestDetailActivity extends AppCompatActivity {

    private TextView textViewUserName, textViewUserContact, textViewStatus;
    private Button buttonAccept, buttonReject;
    private DatabaseReference requestRef;
    private String requestId, chefId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_detail);

        // Initialize UI elements
        textViewUserName = findViewById(R.id.textViewUserName);
        textViewUserContact = findViewById(R.id.textViewUserContact);
        textViewStatus = findViewById(R.id.textViewStatus);
        buttonAccept = findViewById(R.id.buttonAccept);
        buttonReject = findViewById(R.id.buttonReject);

        // Get requestId from Intent extras
        requestId = getIntent().getStringExtra("requestId");
        chefId = getIntent().getStringExtra("chefId");

        // Initialize Firebase Database reference
        requestRef = FirebaseDatabase.getInstance().getReference("requests").child(chefId).child(requestId);

        // Load request details
        loadRequestDetails();

        // Set onClickListener for Accept button
        buttonAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateRequestStatus("accepted");
            }
        });

        // Set onClickListener for Reject button
        buttonReject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateRequestStatus("rejected");
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
                if ("cancelled".equals(status)) {
                    buttonAccept.setEnabled(false);
                    buttonReject.setEnabled(false);
                }
            } else {
                Toast.makeText(RequestDetailActivity.this, "Request not found", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(e -> {
            Toast.makeText(RequestDetailActivity.this, "Failed to load request details: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }

    private void updateRequestStatus(String newStatus) {
        requestRef.child("status").setValue(newStatus).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(RequestDetailActivity.this, "Request " + newStatus, Toast.LENGTH_SHORT).show();
                textViewStatus.setText("Status: " + newStatus);
            } else {
                Toast.makeText(RequestDetailActivity.this, "Failed to update request status", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
