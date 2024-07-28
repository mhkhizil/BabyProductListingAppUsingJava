package com.example.assignment;// Package Declaration




// Import necessary Android classes and libraries

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.content.Intent;
// Class Definition - EntryPage is an Activity (a screen in the app)
public class EntryPage extends AppCompatActivity {

    @Override
    // onCreate() is the main lifecycle method of an Activity, called when it starts
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); // Call the parent class's onCreate() (necessary for setup)
        setContentView(R.layout.entry); // Set the layout file for this Activity (defines the UI elements)

        // Find a Button in the layout with the ID "entry_page_button"
        Button connectButton = findViewById(R.id.entry_page_button);
        // Set an onClickListener for the button to handle clicks
        connectButton.setOnClickListener(view -> {
            // Create an Intent to start a new Activity called SignupActivity
            Intent intent = new Intent(EntryPage.this, SignupActivity.class);
            // Start the SignupActivity using the Intent
            startActivity(intent);

        });
    }
}

