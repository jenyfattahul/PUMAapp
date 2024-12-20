package com.example.himpuunananapp;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class EditProfileActivity extends AppCompatActivity {

    private EditText editTextName, editTextMajor, editTextBatch, editTextStudentId, editTextDob;
    private Button buttonEdit, buttonSave;
    private FirebaseAuth mAuth;
    private FirebaseFirestore mFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        // Initialize FirebaseAuth and Firestore
        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();

        // Find views
        editTextName = findViewById(R.id.editTextName);
        editTextMajor = findViewById(R.id.editTextMajor);
        editTextBatch = findViewById(R.id.editTextBatch);
        editTextStudentId = findViewById(R.id.editTextStudentId);
        editTextDob = findViewById(R.id.editTextDob);
        buttonEdit = findViewById(R.id.buttonEdit);
        buttonSave = findViewById(R.id.buttonSave);

        // Load profile data if available
        loadProfileData();

        // Initially set the fields to be non-editable
        setFieldsEditable(false);

        buttonEdit.setOnClickListener(v -> {
            setFieldsEditable(true); // Enable editing
            buttonEdit.setEnabled(false); // Disable edit button
            buttonSave.setEnabled(true); // Enable save button
        });

        buttonSave.setOnClickListener(v -> saveProfileData());
    }

    private void loadProfileData() {
        if (mAuth.getCurrentUser() == null) {
            Toast.makeText(this, "User not authenticated. Please log in again.", Toast.LENGTH_SHORT).show();
            finish(); // Close activity
            return;
        }

        String userId = mAuth.getCurrentUser().getUid();
        DocumentReference userRef = mFirestore.collection("users").document(userId);

        userRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                if (task.getResult().exists()) {
                    UserProfile userProfile = task.getResult().toObject(UserProfile.class);
                    if (userProfile != null) {
                        editTextName.setText(userProfile.getName());
                        editTextMajor.setText(userProfile.getMajor());
                        editTextBatch.setText(userProfile.getBatch());
                        editTextStudentId.setText(userProfile.getStudentId());
                        editTextDob.setText(userProfile.getDob());
                    }
                } else {
                    Toast.makeText(this, "Profile not found. Please complete your profile.", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Failed to load profile. Try again later.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveProfileData() {
        if (mAuth.getCurrentUser() == null) {
            Toast.makeText(this, "User not authenticated. Please log in again.", Toast.LENGTH_SHORT).show();
            return;
        }

        String name = editTextName.getText().toString().trim();
        String major = editTextMajor.getText().toString().trim();
        String batch = editTextBatch.getText().toString().trim();
        String studentId = editTextStudentId.getText().toString().trim();
        String dob = editTextDob.getText().toString().trim();

        if (name.isEmpty() || major.isEmpty() || batch.isEmpty() || studentId.isEmpty() || dob.isEmpty()) {
            Toast.makeText(this, "Please fill all fields.", Toast.LENGTH_SHORT).show();
            return;
        }

        String userId = mAuth.getCurrentUser().getUid();
        UserProfile userProfile = new UserProfile(name, major, batch, studentId, dob);
        DocumentReference userRef = mFirestore.collection("users").document(userId);

        userRef.set(userProfile).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(this, "Profile saved successfully!", Toast.LENGTH_SHORT).show();
                setFieldsEditable(false);
                buttonEdit.setEnabled(true);
                buttonSave.setEnabled(false);
            } else {
                Toast.makeText(this, "Failed to save profile. Try again.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setFieldsEditable(boolean isEditable) {
        editTextName.setEnabled(isEditable);
        editTextMajor.setEnabled(isEditable);
        editTextBatch.setEnabled(isEditable);
        editTextStudentId.setEnabled(isEditable);
        editTextDob.setEnabled(isEditable);
    }

    public static class UserProfile {
        private String name;
        private String major;
        private String batch;
        private String studentId;
        private String dob;


        public UserProfile() {
        }

        public UserProfile(String name, String major, String batch, String studentId, String dob) {
            this.name = name;
            this.major = major;
            this.batch = batch;
            this.studentId = studentId;
            this.dob = dob;
        }

        public String getName() {
            return name;
        }

        public String getMajor() {
            return major;
        }

        public String getBatch() {
            return batch;
        }

        public String getStudentId() {
            return studentId;
        }

        public String getDob() {
            return dob;
        }
    }
}
