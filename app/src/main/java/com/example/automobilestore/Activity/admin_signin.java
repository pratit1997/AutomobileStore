package com.example.automobilestore.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.automobilestore.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;

public class admin_signin extends AppCompatActivity {

    public TextInputLayout Email, Password;
    Button login;
    private FirebaseAuth auth;
    FirebaseFirestore fStore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_signin);

        Email = findViewById(R.id.admin_email);
        Password = findViewById(R.id.admin_password);
        login = findViewById(R.id.adminlogin);

        auth = FirebaseAuth.getInstance();

        fStore = FirebaseFirestore.getInstance();

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = Email.getEditText().getText().toString();
                String pwd = Password.getEditText().getText().toString();


                Task<QuerySnapshot> documentReference = fStore.collection("Admin").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for(QueryDocumentSnapshot doc:task.getResult()){
                                String e = doc.getString("email");
                                String p = doc.getString("password");
                                if (e.equalsIgnoreCase(email) && p.equalsIgnoreCase(pwd)){
                                    startActivity(new Intent(admin_signin.this,AdminHome.class));
                                }else if(!e.equalsIgnoreCase(email)){
                                    Email.requestFocus();
                                    Email.setError("User Does not Exists with this email");
                                }else if(!p.equalsIgnoreCase(pwd)){
                                    Password.requestFocus();
                                    Password.setError("Incorrect Password");
                                }
                            }
                        }
                    }
                });

            }
        });
    }
}