package com.example.chef_in_home;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Signup extends AppCompatActivity {

    EditText name,username,password,email;
    TextView loginlink;
    Button signupbtn;
    FirebaseDatabase database;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        name = findViewById(R.id.name);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        email = findViewById(R.id.email);
        loginlink = findViewById(R.id.loginlink);
        signupbtn = findViewById(R.id.signup);

        loginlink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Signup.this,LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        signupbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                database = FirebaseDatabase.getInstance();
                reference = database.getReference("users");
                String nameuser = name.getText().toString().trim();
                String emailuser = email.getText().toString().trim();
                String userusername = username.getText().toString().trim();
                String passUser = password.getText().toString().trim();

                if (nameuser.isEmpty()|| emailuser.isEmpty()
                        ||userusername.isEmpty() || passUser.isEmpty() ){
                    Toast.makeText(Signup.this, "Please fill all the field",
                            Toast.LENGTH_SHORT).show();
                    return;
                }else{
                    Model user = new Model(nameuser,emailuser,userusername,passUser);
                    reference.child(userusername).setValue(user);
                    Toast.makeText(Signup.this, "Signup successfully",
                            Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Signup.this,MainActivity.class);
                    intent.putExtra("name",nameuser);
                    intent.putExtra("email",emailuser);
                    intent.putExtra("password",passUser);
                    intent.putExtra("username",userusername);
                    startActivity(intent);
                    finish();
                }

            }
        });

    }
}