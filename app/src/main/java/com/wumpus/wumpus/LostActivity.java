package com.wumpus.wumpus;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import io.DungeonView;
import io.HighScore;
import model.GameState;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Date;

import static model.GameState.*;

/**
 * Program logic for the Lost Activity, starting after a game is lost
 */
public class LostActivity extends AppCompatActivity {
    private int level;
    private int score;
    private HighScore highScore;
    private DungeonView dv;


    /**
     * Called automatically on creation
     * @param savedInstanceState
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lost);

        Intent intent = getIntent();
        int iState = intent.getIntExtra(GameActivity.EXTRA_STATE, -1);
        GameState state = values()[iState];
        level = intent.getIntExtra(MainActivity.EXTRA_LEVEL, - 1);
        score = intent.getIntExtra(MainActivity.EXTRA_SCORE, -1);

        TextView tv = findViewById(R.id.tvLost);

        dv = DungeonView.getLastInstance();
        dv.setImageView((ImageView) findViewById(R.id.ivLost));
        dv.updateLost();

        String deathMessage = "";

        switch (state) {
            case Eaten:
                deathMessage = "You were eaten alive by the Wumpus.";
                break;
            case Missed:
                deathMessage = "Your only bullet missed the Wumpus.";
                break;
            case OutOfFuel:
                deathMessage = "Your light ran out of fuel and you got lost in the dark.";
                break;
            case Stoned:
                deathMessage = "A massive stone fell down on your head and crushed you to death.";
                break;
            default:
                break;
        }

        deathMessage += "\n You reached level " + level + ".";
        deathMessage += "\n Your Score: " + score;
        tv.setText(deathMessage);

        SharedPreferences pref = getSharedPreferences("WUMPUS", 0);
        highScore = new HighScore(pref);
        int lastScore = highScore.getScore(4);

        if (score > lastScore)
            findViewById(R.id.etName).setVisibility(View.VISIBLE);

        MediaPlayer mp = MediaPlayer.create(this, R.raw.wilhelm);

        if (highScore.isSoundOn())
            mp.start();

    }

    /**
     * Get from LostActivity to MainActivity after button click
     * Add highScore entry in case of reached highscore
     * @param view
     */
    public void sendMessage(View view) {
        EditText etName = findViewById(R.id.etName);

        if (etName.getVisibility() == View.VISIBLE) {
            String name = etName.getText().toString();
            highScore.addScore(score, level, name);
        }

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
