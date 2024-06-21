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

public class EditProfile extends AppCompatActivity {

    EditText editname,editemail,editpass;

    Button save,profilbtn;
    TextView editusername;

    DatabaseReference reference;

    String nameuser,emailuser,usernameuser,passuser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profie);

        reference = FirebaseDatabase.getInstance().getReference("users");
        editname = findViewById(R.id.editname);
        editemail = findViewById(R.id.editemail);
        editpass = findViewById(R.id.editpassword);

        save = findViewById(R.id.save);
        profilbtn = findViewById(R.id.profile);
        editusername = findViewById(R.id.editusername);

        showDatauser();

        profilbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EditProfile.this,MainActivity.class);
                intent.putExtra("name",editname.getText().toString().trim());
                intent.putExtra("usename",editusername.getText().toString().trim());
                intent.putExtra("password",editpass.getText().toString().trim());
                intent.putExtra("email",editemail.getText().toString().trim());
                startActivity(intent);
                finish();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isNameChanged()){
                    Toast.makeText(EditProfile.this, "name changed",
                            Toast.LENGTH_SHORT).show();
                }
                if (isEmailChanged()){
                    Toast.makeText(EditProfile.this, "Email changed",
                            Toast.LENGTH_SHORT).show();
                }
                if (isPassChanged()){
                    Toast.makeText(EditProfile.this, "pass changed",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

    }


    private boolean isNameChanged(){
        if (!nameuser.equals(editname.getText().toString())){
            reference.child(usernameuser).child("name")
                    .setValue(editname.getText().toString());
            return true;
        }else{
            return false;
        }
    }

    private boolean isEmailChanged(){
        if (!emailuser.equals(editemail.getText().toString())){
            reference.child(usernameuser).child("email")
                    .setValue(editemail.getText().toString());
            return true;
        }else{
            return false;
        }
    }
    private boolean isPassChanged(){
        if (!passuser.equals(editpass.getText().toString())){
            reference.child(usernameuser).child("password")
                    .setValue(editpass.getText().toString());
            return true;
        }else{
            return false;
        }
    }





    public void showDatauser(){
        Intent intent = getIntent();
        nameuser = intent.getStringExtra("name");
        emailuser = intent.getStringExtra("email");
        usernameuser = intent.getStringExtra("username");
        passuser = intent.getStringExtra("password");
        editemail.setText(emailuser);
        editname.setText(nameuser);
        editusername.setText(usernameuser);
        editpass.setText(passuser);
    }
}