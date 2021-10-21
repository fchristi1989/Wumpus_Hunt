package com.wumpus.wumpus;

import androidx.appcompat.app.AppCompatActivity;
import io.DungeonView;
import io.HighScore;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

/**
 * Program logic for the Won Activity, starting after the game is won on a level,
 * followed by restarting the game on the next level
 */
public class WonActivity extends AppCompatActivity {

    private int level;
    private int score;
    private DungeonView dv;

    private HighScore highScore;

    /**
     * Called automatically on creation
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_won);
        level = getIntent().getIntExtra(MainActivity.EXTRA_LEVEL, -1);
        score = getIntent().getIntExtra(MainActivity.EXTRA_SCORE, -1);
        dv = DungeonView.getLastInstance();
        dv.setImageView((ImageView) findViewById(R.id.ivWon));
        dv.updateWon();


        SharedPreferences pref = getSharedPreferences("WUMPUS", 0);
        highScore = new HighScore(pref);

        MediaPlayer mp = MediaPlayer.create(this, R.raw.tada);

        if (highScore.isSoundOn())
            mp.start();
    }

    /**
     * Restart the game on the next level
     * @param view
     */
    public void sendMessage (View view) {
        Intent intent = new Intent(this, GameActivity.class);
        intent.putExtra(MainActivity.EXTRA_LEVEL, level + 1);
        intent.putExtra(MainActivity.EXTRA_SCORE, score + 100);
        startActivity(intent);
        finish();

    }


}
