package com.example.chef_in_home;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ChefActivity extends AppCompatActivity {

    private static final String TAG = "ChefActivity";
    private RecyclerView recyclerViewRequests;
    private RequestAdapter requestAdapter;
    private List<Request> requestList;

    private TextView textViewChefName,
            textViewChefSpecialty,
            textViewChefAvailability,
            textViewChefContact,
            textViewChefExperience,
            textViewChefRating,
            textViewChefBio;
    private Button button_edit;
    private String chefId;

    // Firebase
    private DatabaseReference databaseReference;
    private FirebaseAuth auth;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chef);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        auth = FirebaseAuth.getInstance();
        if(auth.getCurrentUser() != null){
            chefId = auth.getCurrentUser().getUid();
        }

        textViewChefName = findViewById(R.id.textViewChefName);
        textViewChefSpecialty = findViewById(R.id.textViewChefSpecialty);
        textViewChefAvailability = findViewById(R.id.textViewChefAvailability);
        textViewChefContact = findViewById(R.id.textViewChefContact);
        textViewChefExperience = findViewById(R.id.textViewChefExperience);
        textViewChefRating = findViewById(R.id.textViewChefRating);
        textViewChefBio = findViewById(R.id.textViewChefBio);
        recyclerViewRequests = findViewById(R.id.recyclerViewRequests);

        // Set up RecyclerView
        recyclerViewRequests.setLayoutManager(new LinearLayoutManager(this));
        requestList = new ArrayList<>();
        requestAdapter = new RequestAdapter(requestList);
        recyclerViewRequests.setAdapter(requestAdapter);

        // Load requests from Firebase
        loadRequests();

        button_edit = findViewById(R.id.btn_edit);
        button_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChefActivity.this, EditChefProfileActivity.class);
                intent.putExtra("chefId", chefId);
                startActivity(intent);
            }
        });

        databaseReference = FirebaseDatabase.getInstance().getReference().child("chefs");
        databaseReference.child(chefId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Chef chef = dataSnapshot.getValue(Chef.class);
                    if (chef != null) {
                        textViewChefName.setText(chef.getName());
                        textViewChefSpecialty.setText(chef.getSpecialty());
                        textViewChefAvailability.setText(chef.isAvailable() ? "Available" : "Not Available");
                        textViewChefContact.setText(chef.getContact());
                        textViewChefExperience.setText(chef.getExperience());
                        textViewChefRating.setText(String.valueOf(chef.getRating()));
                        textViewChefBio.setText(chef.getBio());
                    } else {
                        Log.d(TAG, "Chef data is null");
                    }
                } else {
                    Log.d(TAG, "Chef data does not exist");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, "Failed to read chef details", databaseError.toException());
                Toast.makeText(ChefActivity.this, "Failed to load chef details", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadRequests() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("requests").child(chefId);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                requestList.clear();
                for (DataSnapshot requestSnapshot : snapshot.getChildren()) {
                    Request request = requestSnapshot.getValue(Request.class);
                    requestList.add(request);
                }
                requestAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ChefActivity.this, "Failed to load requests.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_user, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_profile) {
            Intent profileIntent = new Intent(ChefActivity.this, ProfileActivity.class);
            startActivity(profileIntent);
            return true;
        } else if (item.getItemId() == R.id.action_sign_out) {
            Intent signOutIntent = new Intent(ChefActivity.this, SignOutActivity.class);
            startActivity(signOutIntent);
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }

    }
}
