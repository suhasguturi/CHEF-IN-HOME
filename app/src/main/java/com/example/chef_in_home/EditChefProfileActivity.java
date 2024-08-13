package com.example.chef_in_home;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EditChefProfileActivity extends AppCompatActivity {

    private EditText editTextName, editTextSpecialty, editTextContact, editTextExperience, editTextRating, editTextBio;
    private CheckBox checkBoxAvailable;
    private Button buttonSave;

    private DatabaseReference databaseReference;
    private String chefId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_chef_profile);

        editTextName = findViewById(R.id.editTextName);
        editTextSpecialty = findViewById(R.id.editTextSpecialty);
        checkBoxAvailable = findViewById(R.id.checkBoxAvailable);
        editTextContact = findViewById(R.id.editTextContact);
        editTextExperience = findViewById(R.id.editTextExperience);
        editTextRating = findViewById(R.id.editTextRating);
        editTextBio = findViewById(R.id.editTextBio);
        buttonSave = findViewById(R.id.buttonSave);

        chefId = getIntent().getStringExtra("chefId");
        databaseReference = FirebaseDatabase.getInstance().getReference("chefs").child(chefId);

        loadChefDetails();
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveChefDetails();
            }
        });
    }

    private void loadChefDetails() {
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Chef chef = snapshot.getValue(Chef.class);
                if (chef != null) {
                    editTextName.setText(chef.getName());
                    editTextSpecialty.setText(chef.getSpecialty());
                    checkBoxAvailable.setChecked(chef.isAvailable());
                    editTextContact.setText(chef.getContact());
                    editTextExperience.setText(chef.getExperience());
                    editTextRating.setText(String.valueOf(chef.getRating()));
                    editTextBio.setText(chef.getBio());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(EditChefProfileActivity.this, "Failed to load chef details: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveChefDetails() {
        String name = editTextName.getText().toString().trim();
        String specialty = editTextSpecialty.getText().toString().trim();
        boolean available = checkBoxAvailable.isChecked();
        String contact = editTextContact.getText().toString().trim();
        String experience = editTextExperience.getText().toString().trim();
        String ratingStr = editTextRating.getText().toString().trim();
        String bio = editTextBio.getText().toString().trim();

        if (name.isEmpty() || specialty.isEmpty() || contact.isEmpty() || experience.isEmpty() || ratingStr.isEmpty() || bio.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        double rating;
        try {
            rating = Double.parseDouble(ratingStr);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Please enter a valid rating", Toast.LENGTH_SHORT).show();
            return;
        }

        Chef chef = new Chef(chefId, name, specialty, available, contact, experience, rating, bio);

        databaseReference.setValue(chef).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                // Save name to the users node
                DatabaseReference usersReference = FirebaseDatabase.getInstance().getReference("users").child(chefId);
                usersReference.child("contact").setValue(contact);
                usersReference.child("name").setValue(name).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(EditChefProfileActivity.this, "Chef details saved", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(EditChefProfileActivity.this, "Failed to save chef details: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(EditChefProfileActivity.this, "Failed to save chef details: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}
