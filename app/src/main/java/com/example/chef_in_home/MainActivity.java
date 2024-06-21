package com.example.chef_in_home;



import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DownloadManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    TextView profileusername,profilename,profilepass,profileemail;

    TextView titlename,titleusername;

    Button signout,editprofile,deleteprofile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        profileusername = findViewById(R.id.profileusername);
        profilename = findViewById(R.id.profilename);
        profilepass= findViewById(R.id.profilepass);
        profileemail = findViewById(R.id.profileemail);

        titlename = findViewById(R.id.titlename);
        titleusername = findViewById(R.id.titleusername);

        signout = findViewById(R.id.SignOut);
        editprofile = findViewById(R.id.editprofile);
        deleteprofile = findViewById(R.id.deleteprofile);
        showDatauser();

        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,
                        LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        deleteprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String maninusername = profileusername.getText().toString().trim();
                DatabaseReference reference = FirebaseDatabase.getInstance()
                        .getReference("users");

                Query checkuserData = reference.orderByChild("username")
                        .equalTo(maninusername);
                checkuserData.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            snapshot.child(maninusername).getRef().removeValue();
                            Intent intent = new Intent(MainActivity.this,
                                    Signup.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });

        editprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                passDataUser();
            }
        });
    }


    public void passDataUser(){
        String maninusername = profileusername.getText().toString().trim();
        DatabaseReference reference = FirebaseDatabase.getInstance()
                .getReference("users");

        Query checkuserData = reference.orderByChild("username")
                .equalTo(maninusername);

        checkuserData.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    String usernameDB = snapshot.child(maninusername).child("username")
                            .getValue(String.class);
                    String passDB = snapshot.child(maninusername).child("password")
                            .getValue(String.class);
                    String nameDB = snapshot.child(maninusername).child("name")
                            .getValue(String.class);
                    String emailDB =snapshot.child(maninusername).child("email")
                            .getValue(String.class);
                    Intent intent = new Intent(MainActivity.this,
                            EditProfile.class);
                    intent.putExtra("name",nameDB);
                    intent.putExtra("email",emailDB);
                    intent.putExtra("password",passDB);
                    intent.putExtra("username",usernameDB);
                    startActivity(intent);
                    finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }






    public void showDatauser(){
        Intent intent = getIntent();
        titlename.setText("Welcome " + intent.getStringExtra("name"));
        titleusername.setText(intent.getStringExtra("username"));
        profileemail.setText(intent.getStringExtra("email"));
        profilename.setText(intent.getStringExtra("name"));
        profileusername.setText(intent.getStringExtra("username"));
        profilepass.setText(intent.getStringExtra("password"));
    }
}