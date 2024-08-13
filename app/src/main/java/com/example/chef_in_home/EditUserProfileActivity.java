package com.example.chef_in_home;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EditUserProfileActivity extends AppCompatActivity {
    private DatabaseReference usersRef;
    private String userId;
    private EditText editTextUsername, editTextUserContact;
    private Button buttonSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user_profile);


        editTextUsername = findViewById(R.id.editTextUsername);
        editTextUserContact = findViewById(R.id.editTextUserContact);
        buttonSave = findViewById(R.id.buttonSave);


        userId = getIntent().getStringExtra("userId");


        usersRef = FirebaseDatabase.getInstance().getReference("users").child(userId);


        loadUserDetails();


        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveUserDetails();
            }
        });
    }

    private void loadUserDetails() {
        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String username = dataSnapshot.child("name").getValue(String.class);
                    String userContact = dataSnapshot.child("contact").getValue(String.class);

                    if (username != null) {
                        editTextUsername.setText(username);
                    } else {
                        Toast.makeText(EditUserProfileActivity.this, "Username not found", Toast.LENGTH_SHORT).show();
                    }

                    if (userContact != null) {
                        editTextUserContact.setText(userContact);
                    } else {
                        Toast.makeText(EditUserProfileActivity.this, "Contact not found", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(EditUserProfileActivity.this, "User data not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(EditUserProfileActivity.this, "Failed to load user details: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveUserDetails() {
        String newUsername = editTextUsername.getText().toString().trim();
        String newUserContact = editTextUserContact.getText().toString().trim();

        if (newUsername.isEmpty()) {
            Toast.makeText(EditUserProfileActivity.this, "Please enter a username", Toast.LENGTH_SHORT).show();
            editTextUsername.setError("Username can't be empty");
            return;
        }

        if (newUserContact.isEmpty()) {
            editTextUserContact.setError("Contact can't be empty");
            Toast.makeText(EditUserProfileActivity.this, "Please enter contact details", Toast.LENGTH_SHORT).show();
            return;
        }


        if (!newUserContact.matches("\\d{10}")) {
            editTextUserContact.setError("Invalid contact");
            Toast.makeText(EditUserProfileActivity.this, "Please enter a valid 10-digit contact number", Toast.LENGTH_SHORT).show();
            return;
        }


        usersRef.child("name").setValue(newUsername);
        usersRef.child("contact").setValue(newUserContact).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(EditUserProfileActivity.this, "User details updated successfully", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(EditUserProfileActivity.this, "Failed to update user details", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
