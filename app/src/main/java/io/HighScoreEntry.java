package io;

/**
 * This is a container class to hold primitive values representing an highscore entry.
 * After creation, it is Read-only
 */
public class HighScoreEntry {
    private int score;
    private int level;
    private String name;
    private String date;

    public HighScoreEntry(int score, int level, String name, String date) {
        this.score = score;
        this.level = level;
        this.name = name;
        this.date = date;

    }

    public int getScore() {
        return score;
    }

    public String getName() {
        return name;
    }

    public String getDate() {
        return date;
    }

    public int getLevel() {
        return level;
    }
}
