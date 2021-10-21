package io;

import android.content.SharedPreferences;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * This class manages the highscores, that are saved in the preferences.
 * The five highest entries are considered.
 * Preferences for sound and music are also read and written.
 */
public class HighScore {
    // The pattern for dates
    private final String PATTERN = "yyyy-MM-dd";

    private HighScoreEntry [] entries;

    private boolean musicOn;
    private boolean soundOn;

    SharedPreferences pref;

    /**
     * Constructor
     * @param p SharedPreferences have to be loaded in an activity and can then
     *          be passed via constructor.
     */
    public HighScore(SharedPreferences p) {
        entries = new HighScoreEntry[5];
        pref = p;
        read();
    }

    /**
     * Read values from the preferences
     */
    private void read() {
        for (int i = 0; i < 5; i++) {
            int score = pref.getInt("SCORE" + i, 0);
            int level = pref.getInt("LEVEL" + i, 0 );
            String name = pref.getString("NAME" + i, "");
            String date = pref.getString("DATE" + i, "");

            entries[i] = new HighScoreEntry(score, level, name, date);
        }

        musicOn = pref.getBoolean("MUSICON", false);
        soundOn = pref.getBoolean("SOUNDON", true);

        if (entries[0].getScore() == 0)
            clear();
    }

    /**
     * Overwrite old values in preferences
     */
    private void write() {
        SharedPreferences.Editor editor = pref.edit();

        for (int i = 0; i < 5; i++) {
            editor.putInt("SCORE" + i, entries[i].getScore());
            editor.putInt("LEVEL" + i, entries[i].getLevel());
            editor.putString("NAME" + i, entries[i].getName());
            editor.putString("DATE" + i, entries[i].getDate());
        }

        editor.putBoolean("MUSICON", musicOn);
        editor.putBoolean("SOUNDON", soundOn);

        editor.commit();
    }

    /**
     * Get the score from a specific rank
     * @param i The rank
     * @return The score number, -1 (Error) if incorrect i
     */
    public int getScore(int i) {
        if (i < 5)
            return entries[i].getScore();
        else
            return -1;
    }

    /**
     * Clearing the highscores means overwriting all values with default highscores
     */
    public void clear() {
        SharedPreferences.Editor editor = pref.edit();

        for (int i = 0; i < 5; i++) {
            editor.putInt("SCORE" + i, 500 - i * 100);
            editor.putInt("LEVEL" + i, 5 - i);
            editor.putString("NAME" + i, "CHRISTIAN");
            editor.putString("DATE" + i, new SimpleDateFormat(PATTERN).format(new Date()));

        }

        editor.commit();
        read();
    }

    /**
     * Called from WonActivity, if a new highscore is to be added.
     * @param score The score number
     * @param level The level reached
     * @param name The name put in the edit field
     */
    public void addScore(int score, int level,  String name) {
        // The date is automatically discovered here
        String date = new SimpleDateFormat(PATTERN).format(new Date());

        HighScoreEntry entry = new HighScoreEntry(score, level, name, date);
        HighScoreEntry buffer = null;

        for (int i = 0; i < 5; i++) {
            if (entry.getScore() > entries[i].getScore()) {


                buffer = entries[i];
                entries[i] = entry;
                entry = buffer;
            }
        }

        // Now, that the HighscoreEntries have changed,
        // they need to be written back into preferences.
        write();
    }

    /**
     * Get the name from a specific rank
     * @param i The rank
     * @return The score number, "" (Error) if incorrect i
     */
    public String getName(int i) {
        if (i < 5)
            return entries[i].getName();
        else
            return "";
    }

    /**
     * Get the date from a specific rank
     * @param i The rank
     * @return The score number, "" (Error) if incorrect i
     */
    public String getDate(int i) {
        if (i < 5)
            return entries[i].getDate();
        else
            return "";
    }

    // Read and Write if music and sound are on or off.

    public void setMusicOn(boolean m) {
        musicOn = m;
        write();
    }

    public boolean isMusicOn() {
        return musicOn;
    }

    public void setSoundOn(boolean s) {
        soundOn = s;
        write();
    }

    public boolean isSoundOn() {
        return soundOn;
    }

    /**
     * Get the level from a specific rank
     * @param i The rank
     * @return The score number, -1 (Error) if incorrect i
     */
    public int getLevel(int i) {
        if (i < 5)
            return entries[i].getLevel();
        else
            return -1;
    }
}
