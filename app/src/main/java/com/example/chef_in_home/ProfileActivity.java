package com.example.chef_in_home;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileActivity extends AppCompatActivity {

    private TextView textViewUsername, textViewEmail, textViewContact, textViewRole;
    private Button buttonEditProfile;

    private FirebaseAuth auth;
    private DatabaseReference usersRef;
    private String userId;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        textViewUsername = findViewById(R.id.textViewUsername);
        textViewEmail = findViewById(R.id.textViewEmail);
        textViewContact = findViewById(R.id.textViewContact);
        textViewRole = findViewById(R.id.textViewRole);
        buttonEditProfile = findViewById(R.id.btn_edit);

        auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();
        if(currentUser != null){
            userId = currentUser.getUid();
        }
        usersRef = FirebaseDatabase.getInstance().getReference().child("users").child(auth.getCurrentUser().getUid());


        usersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String username = dataSnapshot.child("name").getValue(String.class);
                    String email = dataSnapshot.child("email").getValue(String.class);
                    String role = dataSnapshot.child("role").getValue(String.class);
                    String contact = dataSnapshot.child("contact").getValue(String.class);

                    if (username != null && email != null && role != null) {
                        textViewUsername.setText("Name: " + username);
                        textViewEmail.setText("Email: " + email);
                        textViewRole.setText("Role: " + role);
                    }
                    if(contact != null){
                        textViewContact.setText("Contact: " + contact);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle database error
            }
        });

        buttonEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileActivity.this, EditUserProfileActivity.class);
                intent.putExtra("userId", userId);
                startActivity(intent);
            }

        });



    }
}
