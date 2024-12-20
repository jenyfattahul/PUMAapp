package com.example.himpuunananapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class HomeActivity extends AppCompatActivity {

    private TextView textViewGreeting;
    private Button buttonFeedback;
    private Button buttonLogout;
    private Button buttonProfile;
    private Button buttonAbout;
    private Button buttonVoting;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Initialize the views after setContentView
        textViewGreeting = findViewById(R.id.textViewGreeting);
        buttonFeedback = findViewById(R.id.buttonFeedback);
        buttonLogout = findViewById(R.id.buttonLogout);
        buttonProfile = findViewById(R.id.buttonProfile);
        buttonAbout = findViewById(R.id.buttonAbout);
        buttonVoting = findViewById(R.id.buttonVoting);

        // Load the user's name from SharedPreferences
        //SharedPreferences sharedPreferences = getSharedPreferences("UserProfile", MODE_PRIVATE);
        //String userName = sharedPreferences.getString("name", "User");

        // Set the greeting message
        //textViewGreeting.setText("Hi, " + userName);

        // Set listeners for buttons
        buttonProfile.setOnClickListener(v -> {
            // Go to the EditProfileActivity
            Intent intent = new Intent(HomeActivity.this, EditProfileActivity.class);
            startActivity(intent);
        });



        buttonFeedback.setOnClickListener(v -> {
            // Go to the FeedbackActivity
            Intent intent = new Intent(HomeActivity.this, FeedbackActivity.class);
            startActivity(intent);
        });

        buttonVoting.setOnClickListener(v -> {
            // Go to the FeedbackActivity
            Intent intent = new Intent(HomeActivity.this, VotingActivity.class);
            startActivity(intent);
        });


        buttonAbout.setOnClickListener(v -> {
            // Go to the FeedbackActivity
            Intent intent = new Intent(HomeActivity.this, AboutActivity.class);
            startActivity(intent);
        });
        // Logout button functionality
        buttonLogout.setOnClickListener(v -> {
            // Clear shared preferences and go to login screen
            //SharedPreferences.Editor editor = sharedPreferences.edit();
            //editor.clear();  // Clears saved data
            //editor.apply();

            // Redirect to login screen
            Intent intent = new Intent(HomeActivity.this, MainActivity.class);
            startActivity(intent);
            finish();  // End this activity so the user can't go back to it
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Reload the greeting when returning to this activity
        //SharedPreferences sharedPreferences = getSharedPreferences("UserProfile", MODE_PRIVATE);
       // String userName = sharedPreferences.getString("name", "User");
        textViewGreeting.setText("Welcome");
    }
}
