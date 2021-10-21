package com.wumpus.wumpus;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

/**
 * Program logic for the About Activity
 */
public class AboutActivity extends AppCompatActivity {

    /**
     * Called automatically on creation
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
    }

    /**
     * Go back to Main activity after clicking the "Back" button
     * @param view
     */
    public void sendMessageBack (View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }


}
