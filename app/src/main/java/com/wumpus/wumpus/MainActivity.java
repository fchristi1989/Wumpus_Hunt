package com.wumpus.wumpus;

import androidx.appcompat.app.AppCompatActivity;
import io.HighScore;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ToggleButton;

/**
 * Program logic for the Main Activity, holding the main menu
 */
public class MainActivity extends AppCompatActivity {

    public static final String EXTRA_LEVEL = "com.example.wumpus.LEVEL";
    public static final String EXTRA_SCORE = "com.example.wumpus.SCORE";

    private ToggleButton tbMusic;
    private ToggleButton tbSound;
    private HighScore highScore;

    /**
     * Called automatically on creation
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences pref = getSharedPreferences("WUMPUS", 0);
        highScore = new HighScore(pref);

        tbMusic = findViewById(R.id.tbMusic);
        tbMusic.setChecked(highScore.isMusicOn());

        tbSound = findViewById((R.id.tbSound));
        tbSound.setChecked(highScore.isSoundOn());
    }

    /**
     * Start a new game -> switch to GameActivity
     * @param view
     */
    public void sendMessageGame (View view) {
        highScore.setMusicOn(tbMusic.isChecked());
        highScore.setSoundOn(tbSound.isChecked());

        Intent intent = new Intent(this, GameActivity.class);
        int level = 1;
        int score = 100;

        intent.putExtra(EXTRA_LEVEL, level);
        intent.putExtra(EXTRA_SCORE, score);
        startActivity(intent);
        finish();

    }

    /**
     * Switch to highscore board (ScoreActivity)
     * @param view
     */
    public void sendMessageScore (View view) {
        highScore.setMusicOn(tbMusic.isChecked());
        Intent intent = new Intent(this, ScoreActivity.class);
        startActivity(intent);
        finish();
    }

    /**
     * Switch to HelpActivity
     * @param view
     */
    public void sendMessageHelp (View view) {
        Intent intent = new Intent(this, AboutActivity.class);
        startActivity(intent);
        finish();
    }
}
