package com.wumpus.wumpus;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GestureDetectorCompat;
import io.DungeonView;
import io.HighScore;
import model.Action;
import model.Game;
import model.GameState;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

/**
 * Program logic for the Game Activity where the actual game is played
 */
public class GameActivity extends AppCompatActivity implements GestureDetector.OnGestureListener {

    public static final String EXTRA_STATE = "com.example.wumpus.STATE";

    private Game game;
    private int level;

    // Drawables to visualize the game in the view
    private Drawable[] drawables;
    private DungeonView dv;

    // Plays respective sound
    private MediaPlayer mpMove;
    private MediaPlayer mpExplode;
    private MediaPlayer mpMistake;
    private MediaPlayer mpMusic;

    private boolean soundOn;
    private boolean musicOn;

    // Used to detect swipe gesture
    private GestureDetectorCompat mDetector;

    private HighScore highScore;



    /**
     * Called automatically on creation
     * @param savedInstanceState
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        Intent intent = getIntent();
        level = intent.getIntExtra(MainActivity.EXTRA_LEVEL, -1);
        int score = intent.getIntExtra(MainActivity.EXTRA_SCORE, -1);

        SharedPreferences pref = getSharedPreferences("WUMPUS", 0);
        highScore = new HighScore(pref);

        game = new Game(this, level, score);

        drawables = new Drawable[] {
                getResources().getDrawable(R.drawable.wumpus_wall, null),
                getResources().getDrawable(R.drawable.wumpus_floorrevealed, null),
                getResources().getDrawable(R.drawable.wumpus_floorentered, null),
                getResources().getDrawable(R.drawable.wumpus_wumpus, null),
                getResources().getDrawable(R.drawable.wumpus_herowithgun, null),
                getResources().getDrawable(R.drawable.wumpus_stench, null),
                getResources().getDrawable(R.drawable.wumpus_stonefall, null),
                getResources().getDrawable(R.drawable.wumpus_singlebat, null),
                getResources().getDrawable(R.drawable.wumpus_flyingbat, null),
                getResources().getDrawable(R.drawable.wumpus_water, null),
                getResources().getDrawable(R.drawable.wumpus_river, null),
                getResources().getDrawable(R.drawable.wumpus_smallweb, null),
                getResources().getDrawable(R.drawable.wumpus_bigweb, null),
                getResources().getDrawable(R.drawable.wumpus_frame, null),
                getResources().getDrawable(R.drawable.wumpus_skeleton, null),
                getResources().getDrawable(R.drawable.wumpus_wumpusdead, null),
                getResources().getDrawable(R.drawable.wumpus_stonefallhuge, null)};


        ImageView iv = findViewById(R.id.imageView);
        dv = new DungeonView(game, iv, drawables);

        soundOn = highScore.isSoundOn();
        musicOn = highScore.isMusicOn();

        mpMove = MediaPlayer.create(this, R.raw.synthie_move);
        mpExplode = MediaPlayer.create(this, R.raw.synthie_explode);
        mpMistake = MediaPlayer.create(this, R.raw.synthie_mistake);
        mpMove.setVolume(0.5f, 0.5f);

        mpMove.setVolume(0.05f, 0.05f);
        mpExplode.setVolume(0.1f, 0.1f);
        mpMistake.setVolume(0.1f, 0.1f);

        mpMusic = MediaPlayer.create(this, R.raw.wumpus_1);
        mpMusic.setLooping(true);

        // Eventually mute sound
        if (!soundOn) {
            mpMove.setVolume(0.0f, 0.0f);
            mpMistake.setVolume(0.0f, 0.0f);
            mpExplode.setVolume(0.0f, 0.0f);
        }


        // Eventually mute music
        if (musicOn)
            mpMusic.start();


        updateState();

        mDetector = new GestureDetectorCompat(this,this);


    }

    /**
     * Called initially an after each interaction to react to the new state
     */
    public void updateState() {
        Intent intent;

        // Get new game state from model layer
        GameState state = game.getState();

        if (state != GameState.Running)
            mpMusic.stop();

        switch (state) {
            // Get to WonActivity if won
            case Won:
                game.lock();
                intent = new Intent(this, WonActivity.class);
                intent.putExtra(MainActivity.EXTRA_LEVEL, level);
                intent.putExtra(MainActivity.EXTRA_SCORE, game.getScore());
                startActivity(intent);
                finish();
                break;

            case Running:
                TextView tvInfo = findViewById(R.id.tvInfo);
                String infoText = "LEVEL: " + game.getLevel() + "  SCORE: " + game.getScore() + "\n" + "LIGHT: " + game.getFuel() + "  DYNAMITE: " + game.getDynamite() + "\n";

                switch (game.getEvent()) {
                    case Burst:
                        infoText += "You bursted a wall with dynamite.";
                        infoText = addWumpusTextInfo(infoText);
                        mpExplode.seekTo(0);
                        mpExplode.start();
                        break;
                    case River:
                        infoText += "You fell into a river with a big noise.";
                        infoText = addWumpusTextInfo(infoText);
                        mpMistake.seekTo(0);
                        mpMistake.start();
                        break;
                    case FlyingBat:
                        infoText += "You were hijacked by a giant bat.";
                        mpMistake.seekTo(0);
                        mpMistake.start();
                        break;
                    case BigWeb:
                        infoText += "You got trapped in a huge web and wasted a long time to free yourself.";
                        mpMistake.seekTo(0);
                        mpMistake.start();
                        break;
                    case Water:
                        infoText += "You hear water flowing.";
                        mpMove.seekTo(0);
                        mpMove.start();
                        break;
                    case Stench:
                        infoText += "You smell the stench of the Wumpus.";
                        mpMove.seekTo(0);
                        mpMove.start();
                        break;
                    case Skeleton:
                        infoText += "You found an adventurer's corpse holding some useful resources.";
                        mpMove.seekTo(0);
                        mpMove.start();
                        break;
                    case SmallWeb:
                        infoText += "You found a spider web.";
                        mpMove.seekTo(0);
                        mpMove.start();
                        break;
                    case StoneFall:
                        infoText += "Several smaller stones fell down near you.";
                        mpMove.seekTo(0);
                        mpMove.start();
                        break;
                    case HangingBat:
                        infoText += "You hear bats.";
                        mpMove.seekTo(0);
                        mpMove.start();
                        break;
                    default:
                        mpMove.seekTo(0);
                        mpMove.start();
                        break;
                }

                tvInfo.setText(infoText);



                dv.update();
                break;

            case Locked:
                intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                finish();
                break;

            default:
                // Default case: Lost
                intent = new Intent(this, LostActivity.class);
                intent.putExtra(EXTRA_STATE, game.getState().ordinal());
                intent.putExtra(MainActivity.EXTRA_LEVEL, level);
                intent.putExtra(MainActivity.EXTRA_SCORE, game.getScore());
                game.lock();
                startActivity(intent);
                finish();
                break;
        }



    }

    /**
     * Text info is enriched with the Wumpus moving distance info
     * @param infoText The info text to view on screen
     * @return The enriched info text
     */
    private String addWumpusTextInfo(String infoText) {
        infoText += "The listening Wumpus came up to " + (int) (Math.sqrt(level) * 2);
        infoText += " tiles nearer.";

        return infoText;
    }

    /**
     * React to toggle of action buttons
     * @param view
     */
    public void sendMessage(View view) {
        ToggleButton btnChangeMove = findViewById(R.id.btnChangeMove);
        ToggleButton btnChangeBlast = findViewById(R.id.btnChangeBlast);
        ToggleButton btnChangeShoot = findViewById(R.id.btnChangeShoot);

        btnChangeMove.setChecked(false);
        btnChangeBlast.setChecked(false);
        btnChangeShoot.setChecked(false);

        ((ToggleButton) view).setChecked(true);

        switch (view.getId()) {
            case R.id.btnChangeMove:
                game.toggleAction(Action.Move);
                break;
            case R.id.btnChangeBlast:
                game.toggleAction(Action.Blast);
                break;
            case R.id.btnChangeShoot:
                game.toggleAction(Action.Shoot);
                break;
        }
    }

    @Override
    public void finish() {
        super.finish();
    }

    /**
     * Called when the app gets in background
     */
    @Override
    protected void onPause() {
        if (musicOn)
            mpMusic.pause();

        super.onPause();
    }

    /**
     * Called when the app resumes from background
     */
    @Override
    protected void onResume() {
        if (musicOn)
            mpMusic.start();

        super.onResume();
    }

    // Methods to implement OnGestureListener (only OnFling of importance)

    @Override
    public boolean onTouchEvent(MotionEvent event){
        if (this.mDetector.onTouchEvent(event)) {
            return true;
        }
        return super.onTouchEvent(event);
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    /**
     * React to fling gesture
     * @param e1 represents starting point
     * @param e2 represents end point
     * @param velocityX horizontal fling velocity
     * @param velocityY vertical fling velocity
     * @return always false
     */
    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        float distanceX = e1.getX() - e2.getX();
        float distanceY = e1.getY() - e2.getY();

        // Call validation methods on model layer (business logic) depending on fling direction
        if (Math.abs(distanceX) > Math.abs(distanceY)) {
            if (distanceX < 0)
                game.validateRight();
            else
                game.validateLeft();
        } else if (distanceY < 0)
            game.validateDown();
        else
            game.validateUp();

        return false;
    }
}
