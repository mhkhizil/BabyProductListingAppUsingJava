package com.example.assignment;


// Replace with your actual package name

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.content.Intent;

public class EntryPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.entry); // Replace with your layout's name

        Button connectButton = findViewById(R.id.entry_page_button);
        connectButton.setOnClickListener(view -> {
            Intent intent = new Intent(EntryPage.this, LoginActivity.class);
            startActivity(intent);
            // Consider adding an optional finish() here if you don't want the user to return to the entry page via the back button
        });
    }
}

