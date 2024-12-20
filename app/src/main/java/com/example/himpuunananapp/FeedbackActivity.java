package com.example.himpuunananapp;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.himpuunananapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class FeedbackActivity extends AppCompatActivity {

    private EditText editTextFeedback;
    private Button buttonSubmitFeedback;
    private LinearLayout feedbackContainer;
    private Spinner spinnerTopic;
    private List<Feedback> feedbackList;
    private boolean isEditing = false;
    private Feedback feedbackToEdit;
    private TextView textViewToEdit;

    // Firebase variables
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private FirebaseFirestore db;
    private CollectionReference feedbackRef;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        // Initialize views
        editTextFeedback = findViewById(R.id.editTextFeedback);
        buttonSubmitFeedback = findViewById(R.id.buttonSubmitFeedback);
        feedbackContainer = findViewById(R.id.feedbackContainer);
        spinnerTopic = findViewById(R.id.spinnerTopic);

        // Initialize Firebase
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();
        feedbackRef = db.collection("feedback");

        // Set up the spinner
        String[] topics = {
                "Events", "Competitions", "Social Media", "Seminars and Guest Lectures",
                "Company Visits", "UI/UX Design", "General Suggestions", "Others"
        };
        ArrayAdapter<String> topicAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, topics);
        topicAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTopic.setAdapter(topicAdapter);

        // Load feedback
        if (currentUser == null) {
            promptUserToSignIn();
        } else {
            loadFeedbackFromFirebase();
        }

        buttonSubmitFeedback.setOnClickListener(v -> {
            if (currentUser == null) {
                promptUserToSignIn();
            } else {
                String feedbackText = editTextFeedback.getText().toString().trim();
                String selectedTopic = spinnerTopic.getSelectedItem().toString();
                if (!feedbackText.isEmpty()) {
                    if (isEditing) {
                        updateFeedback(feedbackText, selectedTopic);
                    } else {
                        addFeedback(feedbackText, selectedTopic);
                    }
                    editTextFeedback.setText("");
                } else {
                    Toast.makeText(FeedbackActivity.this, "Feedback cannot be empty", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void addFeedback(String feedbackText, String topic) {
        Feedback feedback = new Feedback(feedbackText, topic, currentUser.getUid());
        feedbackRef.add(feedback)
                .addOnSuccessListener(documentReference -> {
                    feedback.setId(documentReference.getId());
                    LinearLayout feedbackLayout = createFeedbackView(feedback);
                    feedbackContainer.addView(feedbackLayout, 0); // Add at the top
                })
                .addOnFailureListener(e -> Toast.makeText(FeedbackActivity.this, "Error adding feedback", Toast.LENGTH_SHORT).show());
    }

    private LinearLayout createFeedbackView(Feedback feedback) {
        LinearLayout feedbackLayout = new LinearLayout(this);
        feedbackLayout.setOrientation(LinearLayout.VERTICAL);
        feedbackLayout.setPadding(16, 16, 16, 16);
        feedbackLayout.setBackgroundResource(android.R.color.darker_gray);

        // TextView for topic
        TextView textViewTopic = new TextView(this);
        textViewTopic.setText("Topic: " + feedback.getTopic());
        textViewTopic.setTextSize(14);
        feedbackLayout.addView(textViewTopic);

        // TextView for feedback
        TextView textViewFeedback = new TextView(this);
        textViewFeedback.setText(feedback.getText());
        textViewFeedback.setTextSize(16);
        feedbackLayout.addView(textViewFeedback);

        // Buttons for edit and delete
        LinearLayout buttonLayout = new LinearLayout(this);
        buttonLayout.setOrientation(LinearLayout.HORIZONTAL);

        Button buttonEdit = new Button(this);
        buttonEdit.setText("Edit");
        buttonEdit.setOnClickListener(v -> {
            if (feedback.getUserId().equals(currentUser.getUid())) {
                editFeedback(feedback, textViewFeedback);
            } else {
                Toast.makeText(this, "You can only edit your feedback", Toast.LENGTH_SHORT).show();
            }
        });

        Button buttonDelete = new Button(this);
        buttonDelete.setText("Delete");
        buttonDelete.setOnClickListener(v -> {
            if (feedback.getUserId().equals(currentUser.getUid())) {
                deleteFeedback(feedback, feedbackLayout);
            } else {
                Toast.makeText(this, "You can only delete your feedback", Toast.LENGTH_SHORT).show();
            }
        });

        buttonLayout.addView(buttonEdit);
        buttonLayout.addView(buttonDelete);
        feedbackLayout.addView(buttonLayout);

        return feedbackLayout;
    }

    private void editFeedback(Feedback feedback, TextView textView) {
        // Set the activity state to editing mode
        isEditing = true;

        // Store the feedback and the TextView being edited
        feedbackToEdit = feedback;
        textViewToEdit = textView;

        // Populate the EditText and Spinner with the current feedback details
        editTextFeedback.setText(feedback.getText());
        ArrayAdapter<String> adapter = (ArrayAdapter<String>) spinnerTopic.getAdapter();
        int position = adapter.getPosition(feedback.getTopic());
        spinnerTopic.setSelection(position);

        Toast.makeText(this, "Edit the feedback and click Submit to save changes", Toast.LENGTH_SHORT).show();
    }

    private void updateFeedback(String newFeedbackText, String topic) {
        feedbackToEdit.setText(newFeedbackText);
        feedbackToEdit.setTopic(topic);
        textViewToEdit.setText(newFeedbackText);

        feedbackRef.document(feedbackToEdit.getId()).update("text", newFeedbackText, "topic", topic)
                .addOnSuccessListener(aVoid -> Toast.makeText(this, "Feedback updated", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(this, "Error updating feedback", Toast.LENGTH_SHORT).show());

        isEditing = false;
        feedbackToEdit = null;
        textViewToEdit = null;
    }

    private void deleteFeedback(Feedback feedback, LinearLayout feedbackLayout) {
        feedbackContainer.removeView(feedbackLayout);
        feedbackRef.document(feedback.getId()).delete()
                .addOnSuccessListener(aVoid -> Toast.makeText(this, "Feedback deleted", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(this, "Error deleting feedback", Toast.LENGTH_SHORT).show());
    }

    private void loadFeedbackFromFirebase() {
        feedbackList = new ArrayList<>();
        feedbackRef.whereEqualTo("userId", currentUser.getUid()).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot querySnapshot = task.getResult();
                        for (QueryDocumentSnapshot document : querySnapshot) {
                            String text = document.getString("text");
                            String topic = document.getString("topic");
                            if (text != null && topic != null) {
                                Feedback feedback = new Feedback(text, topic, currentUser.getUid());
                                feedback.setId(document.getId());
                                feedbackList.add(feedback);

                                LinearLayout feedbackLayout = createFeedbackView(feedback);
                                feedbackContainer.addView(feedbackLayout);
                            }
                        }
                    } else {
                        Toast.makeText(this, "Error loading feedback", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void promptUserToSignIn() {
        Toast.makeText(this, "Please sign in to submit feedback", Toast.LENGTH_LONG).show();
    }

    private static class Feedback {
        private String id;    // Feedback ID (Firestore document ID)
        private String text;  // Feedback text
        private String topic; // Feedback topic
        private String userId; // User ID of the submitter

        public Feedback(String text, String topic, String userId) {
            this.text = text;
            this.topic = topic;
            this.userId = userId;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getText() {
            return text;
        }

        public void setText(String id) {
            this.text = text;
        }

        public String getTopic() {
            return topic;
        }

        public void setTopic(String topic) {
            this.topic = topic;
        }

        // Getter for user ID
        public String getUserId() {
            return userId;
        }
    }
}