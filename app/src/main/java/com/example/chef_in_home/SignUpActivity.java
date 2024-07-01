package com.example.chef_in_home;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignUpActivity extends AppCompatActivity {
    Button register_btn;
    EditText editTextEmail, editTextPassword;
    RadioGroup radioGroupRole;
    RadioButton radioUser, radioAdmin;
    private FirebaseAuth auth;
    private String role = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up);

        register_btn = findViewById(R.id.register_btn);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        radioGroupRole = findViewById(R.id.radioGroupRole);
        radioUser = findViewById(R.id.radioUser);
        radioAdmin = findViewById(R.id.radioAdmin);

        auth = FirebaseAuth.getInstance();

        register_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = editTextEmail.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();
                int selectedRoleId = radioGroupRole.getCheckedRadioButtonId();


                if (selectedRoleId == R.id.radioUser) {
                    role = "User";
                } else if (selectedRoleId == R.id.radioAdmin) {
                    role = "Admin";
                }

                if (email.isEmpty() || password.isEmpty() || role.isEmpty()) {
                    Toast.makeText(SignUpActivity.this, "Email, password, and role cannot be empty", Toast.LENGTH_SHORT).show();
                } else {
                    auth.createUserWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            Toast.makeText(SignUpActivity.this, "Sign up success as " + role, Toast.LENGTH_SHORT).show();
                            if(role == "Admin" && email == "suhas@gmail.com"){
                                Intent intent = new Intent(SignUpActivity.this, AdminActivity.class);
                                // intent.putExtra("user_role", role); // Pass the role to MainActivity
                                startActivity(intent);
                                finish();
                            }else {
                                Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                                // intent.putExtra("user_role", role); // Pass the role to MainActivity
                                startActivity(intent);
                                finish();
                            }

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(SignUpActivity.this, "Sign up failed. " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

    public void doSignIn(View view){
        Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
        startActivity(intent);
    }
}
