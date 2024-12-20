package com.example.himpuunananapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    //declare variable
    private EditText editTextUsername, editTextPassword;
    private Button buttonLogin, buttonRegister;
    private FirebaseAuth mAuth;

    @Override
    //on create method
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        editTextUsername = findViewById(R.id.editTextUsername);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonLogin = findViewById(R.id.buttonLogin);
        buttonRegister = findViewById(R.id.buttonRegister);

        buttonLogin.setOnClickListener(v -> {
            String username = editTextUsername.getText().toString().trim();
            String password = editTextPassword.getText().toString().trim();
        //validate input
            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill in both fields", Toast.LENGTH_SHORT).show();
            } else {
                mAuth.signInWithEmailAndPassword(username, password)
                        .addOnCompleteListener(this, task -> {
                            if (task.isSuccessful()) {
                                startActivity(new Intent(this, HomeActivity.class));
                                finish();
                            } else {
                                Toast.makeText(this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

        buttonRegister.setOnClickListener(v -> startActivity(new Intent(this, RegisterActivity.class)));
    }
}