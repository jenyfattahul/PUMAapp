package com.example.himpuunananapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class AboutActivity extends AppCompatActivity {

    private ImageView imageViewAbout;
    private TextView textViewDescription;
    private Button buttonInstagram, buttonTikTok, buttonLinkedIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        // Initialize views
        imageViewAbout = findViewById(R.id.imageViewAbout);
        textViewDescription = findViewById(R.id.textViewDescription);
        buttonInstagram = findViewById(R.id.buttonInstagram);
        buttonTikTok = findViewById(R.id.buttonTikTok);
        buttonLinkedIn = findViewById(R.id.buttonLinkedIn);

        // Set content for About page
        imageViewAbout.setImageResource(R.drawable.puma); // Use your image here
        textViewDescription.setText("The President University Major Association (PUMA) Information Systems is a student-led organization dedicated to fostering collaboration, innovation, and academic excellence within the Information Systems program. It organizes events, workshops, and networking opportunities to enhance students’ knowledge, skills, and career readiness in the field of technology and systems management.");

        // Set Instagram button click listener
        buttonInstagram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.instagram.com/ispresuniv?utm_source=ig_web_button_share_sheet&igsh=ZDNlZDc0MzIxNw=="));
                startActivity(intent);
            }
        });

        // Set TikTok button click listener
        buttonTikTok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.tiktok.com/@ispresuniv"));
                startActivity(intent);
            }
        });

        // Set LinkedIn button click listener
        buttonLinkedIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.linkedin.com/company/puma-information-system/"));
                startActivity(intent);
            }
        });
    }
}
