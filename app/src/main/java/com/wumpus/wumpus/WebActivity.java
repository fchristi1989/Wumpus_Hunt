package com.wumpus.wumpus;

import androidx.appcompat.app.AppCompatActivity;
import io.HighScore;
import io.HighScoreEntry;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
/**
 * Program logic for the Web Activity showing global highscores saved in an xml file on a web server
 */
public class WebActivity extends AppCompatActivity {

    // URL to web service managing global highscores
    private  final String URLSTUMP = "http://wumpushunt.bplaced.net/leaderboard08/leaderboard.php";

    /**
     * Called automatically on creation
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);

        Intent intent = getIntent();
        String name = intent.getStringExtra(ScoreActivity.EXTRA_NAME);
        String score = intent.getStringExtra(ScoreActivity.EXTRA_SCORE);
        String date = intent.getStringExtra(ScoreActivity.EXTRA_DATE);
        String level = intent.getStringExtra(ScoreActivity.EXTRA_LEVEL);
        boolean share = intent.getBooleanExtra(ScoreActivity.EXTRA_SHARE, false);

        WebView browser = (WebView) findViewById(R.id.webView);

        // In case of sharing an additional entry, the URL has to be extended
        if (share) {
            try {
                name = URLEncoder.encode(name, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            browser.loadUrl(URLSTUMP + "?name=" + name + "&score=" + score + "&date=" + date + "&level=" + level);
        }
        else {
            browser.loadUrl(URLSTUMP);
        }

    }

    /**
     * Go back to ScoreActivity
     * @param view
     */
    public void sendMessageBack (View view) {
        Intent intent = new Intent(this, ScoreActivity.class);
        startActivity(intent);
        finish();
    }


}