package com.example.himpuunananapp;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class VotingActivity extends AppCompatActivity {

    private Button voteButton1, voteButton2, viewResultsButton;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voting);

        // Initialize Firebase
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Get reference to buttons
        voteButton1 = findViewById(R.id.voteButton1);
        voteButton2 = findViewById(R.id.voteButton2);
        viewResultsButton = findViewById(R.id.viewResultsButton);

        // Disable buttons if user already voted
        checkUserVotingStatus();

        // Button click listeners for voting
        voteButton1.setOnClickListener(v -> showVoteConfirmationDialog("Candidate 1"));
        voteButton2.setOnClickListener(v -> showVoteConfirmationDialog("Candidate 2"));

        // Button click listener for viewing results
        viewResultsButton.setOnClickListener(v -> showVotingResults());
    }

    private void showVoteConfirmationDialog(String candidateName) {
        new AlertDialog.Builder(this)
                .setTitle("Confirm Vote")
                .setMessage("Are you sure you want to vote for " + candidateName + "?")
                .setPositiveButton("Yes", (dialog, which) -> submitVote(candidateName))
                .setNegativeButton("No", null)
                .show();
    }

    private void submitVote(String candidateName) {
        String userId = mAuth.getCurrentUser().getUid();

        db.collection("votes")
                .document(userId)
                .set(new Vote(userId, candidateName))
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Vote submitted successfully!", Toast.LENGTH_SHORT).show();
                    disableVoting();
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Failed to submit vote. Please try again.", Toast.LENGTH_SHORT).show());
    }

    private void checkUserVotingStatus() {
        String userId = mAuth.getCurrentUser().getUid();

        db.collection("votes")
                .document(userId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        Toast.makeText(this, "You have already voted!", Toast.LENGTH_SHORT).show();
                        disableVoting();
                    }
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Failed to check voting status.", Toast.LENGTH_SHORT).show());
    }

    private void disableVoting() {
        voteButton1.setEnabled(false);
        voteButton2.setEnabled(false);
    }

    private void showVotingResults() {
        db.collection("votes")
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    Map<String, Integer> voteCounts = new HashMap<>();
                    voteCounts.put("Candidate 1", 0);
                    voteCounts.put("Candidate 2", 0);

                    querySnapshot.forEach(document -> {
                        String candidateName = document.getString("candidateName");
                        if (voteCounts.containsKey(candidateName)) {
                            voteCounts.put(candidateName, voteCounts.get(candidateName) + 1);
                        }
                    });

                    String resultsMessage = "Voting Results:\n\n" +
                            "Candidate 1: " + voteCounts.get("Candidate 1") + " votes\n" +
                            "Candidate 2: " + voteCounts.get("Candidate 2") + " votes";

                    new AlertDialog.Builder(this)
                            .setTitle("Voting Results for Now")
                            .setMessage(resultsMessage)
                            .setPositiveButton("OK", null)
                            .show();
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Failed to fetch voting results.", Toast.LENGTH_SHORT).show());
    }

    static class Vote {
        private String userId;
        private String candidateName;

        public Vote(String userId, String candidateName) {
            this.userId = userId;
            this.candidateName = candidateName;
        }

        public String getUserId() {
            return userId;
        }

        public String getCandidateName() {
            return candidateName;
        }
    }
}
