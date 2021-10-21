package com.wumpus.wumpus;

import androidx.appcompat.app.AppCompatActivity;
import io.HighScore;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

/**
 * Program logic for the Score Activity, showing local highscores (the highest five)
 */
public class ScoreActivity extends AppCompatActivity {
    private HighScore highScore;
    private TextView[] tvs = new TextView[5];
    private TextView[] tvn = new TextView[5];
    private TextView[] tvd = new TextView[5];
    private TextView[] tvl = new TextView[5];
    private ImageButton[] ibs = new ImageButton[5];

    public static final String EXTRA_NAME = "com.example.wumpus.NAME";
    public static final String EXTRA_SCORE = "com.example.wumpus.SCORE";
    public static final String EXTRA_DATE = "com.example.wumpus.DATE";
    public static final String EXTRA_LEVEL = "com.example.wumpus.LEVEL";
    public static final String EXTRA_SHARE = "com.example.wumpus.SHARE";


    /**
     * Called automatically on creation
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

        tvs[0] = findViewById(R.id.tvs1);
        tvs[1] = findViewById(R.id.tvs2);
        tvs[2] = findViewById(R.id.tvs3);
        tvs[3] = findViewById(R.id.tvs4);
        tvs[4] = findViewById(R.id.tvs5);

        tvn[0] = findViewById(R.id.tvn1);
        tvn[1] = findViewById(R.id.tvn2);
        tvn[2] = findViewById(R.id.tvn3);
        tvn[3] = findViewById(R.id.tvn4);
        tvn[4] = findViewById(R.id.tvn5);

        tvd[0] = findViewById(R.id.tvd1);
        tvd[1] = findViewById(R.id.tvd2);
        tvd[2] = findViewById(R.id.tvd3);
        tvd[3] = findViewById(R.id.tvd4);
        tvd[4] = findViewById(R.id.tvd5);

        tvl[0] = findViewById(R.id.tvl1);
        tvl[1] = findViewById(R.id.tvl2);
        tvl[2] = findViewById(R.id.tvl3);
        tvl[3] = findViewById(R.id.tvl4);
        tvl[4] = findViewById(R.id.tvl5);

        ibs[0] = findViewById(R.id.ibs1);
        ibs[1] = findViewById(R.id.ibs2);
        ibs[2] = findViewById(R.id.ibs3);
        ibs[3] = findViewById(R.id.ibs4);
        ibs[4] = findViewById(R.id.ibs5);

        SharedPreferences pref = getSharedPreferences("WUMPUS", 0);
        highScore = new HighScore(pref);

        update();
    }

    /**
     * Go back to MainActivity
     * @param view
     */
    public void sendMessageBack (View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    /**
     * Reset the highscores and its visualisation
     * @param view
     */
    public void sendMessageClear (View view) {
        highScore.clear();
        update();
    }

    /**
     * Switch to the global highscores html view (WebActivity) without adding an entry
     * @param view
     */
    public void sendMessageView (View view) {
        Intent intent = new Intent(this, WebActivity.class);
        intent.putExtra(EXTRA_SHARE, false);
        startActivity(intent);
        finish();
    }

    /**
     * Switch to the global highscores html view (WebActivity) and add an entry
     * @param view
     */
    public void sendMessageShare (View view) {
        int index = -1;

        for (int i = 0; i < tvn.length; i++) {
            if (view.getId() == ibs[i].getId())
                index = i;
        }

        String name = tvn[index].getText().toString();
        String score = tvs[index].getText().toString();
        String date = tvd[index].getText().toString();
        String level = tvl[index].getText().toString();
        level = level.substring(4);


        Intent intent = new Intent(this, WebActivity.class);
        intent.putExtra(EXTRA_NAME, name);
        intent.putExtra(EXTRA_SCORE, score);
        intent.putExtra(EXTRA_DATE, date);
        intent.putExtra(EXTRA_LEVEL, level);
        intent.putExtra(EXTRA_SHARE, true);

        startActivity(intent);
        finish();
    }

    /**
     * called after highscores are modified to update the visualization
     */
    private void update() {
        for (int i =0; i < 5; i++) {
            tvs[i].setText("" + highScore.getScore(i));
            tvn[i].setText(highScore.getName(i));
            tvl[i].setText("lvl " + highScore.getLevel(i));
            tvd[i].setText(highScore.getDate(i));
        }
    }


}
