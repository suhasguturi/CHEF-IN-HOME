package com.example.chef_in_home;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class RequestActivity extends AppCompatActivity {

    private RecyclerView recyclerViewRequests;
    private UserRequestAdapter requestAdapter;
    private List<Request> requestList;
    private  String userId;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request);
        auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();
        if(currentUser != null){
            userId = currentUser.getUid();
        }

        recyclerViewRequests = findViewById(R.id.recyclerViewRequests);
        recyclerViewRequests.setLayoutManager(new LinearLayoutManager(this));

        requestList = new ArrayList<>();
        requestAdapter = new UserRequestAdapter(requestList);
        recyclerViewRequests.setAdapter(requestAdapter);

        loadRequestsFromFirebase();
    }

    private void loadRequestsFromFirebase() {
        DatabaseReference requestsRef = FirebaseDatabase.getInstance().getReference("requests");
        requestsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                requestList.clear();
                for (DataSnapshot chefSnapshot : dataSnapshot.getChildren()) {
                    for (DataSnapshot requestSnapshot : chefSnapshot.getChildren()) {
                        Request request = requestSnapshot.getValue(Request.class);
                        if (request != null && userId.equals(request.getUserId())) {
                            requestList.add(request);
                        }
                    }
                }
                requestAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle database error
            }
        });
    }


}
