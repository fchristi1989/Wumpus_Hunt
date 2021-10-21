package io;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;


import model.Game;
import model.Tile;

/**
 * The class manages the visualization of the game board
 */
public class DungeonView {
    //Singleton pattern: There is one single static DungeonView for all Activities
    private static DungeonView dv;

    private Game game;
    private ImageView iv;

    private Drawable dWall;
    private Drawable dFloorEntered;
    private Drawable dFloorRevealed;
    private Drawable dBeast;
    private Drawable dHero;
    private Drawable dStench;
    private Drawable dStoneFall;
    private Drawable dBatHanging;
    private Drawable dBatFlying;
    private Drawable dWater;
    private Drawable dRiver;
    private Drawable dWeb;
    private Drawable dSpider;
    private Drawable dFrame;
    private Drawable dSkeleton;
    private Drawable dWumpusDead;
    private Drawable dStoneFallHuge;

    private Bitmap bm;
    private Canvas c;

    /**
     * Constructor
     * @param game holds model data concerning the game state
     * @param iv visualizes the game board on screen
     * @param drawables graphics visualized have to be loaded in an Activity and then be passed to this class
     */
    public DungeonView(Game game, ImageView iv, Drawable[] drawables) {
        dv = this;
        this.game = game;
        this.iv = iv;

        dWall = drawables[0];
        dFloorRevealed = drawables[1];
        dFloorEntered = drawables[2];
        dBeast = drawables[3];
        dHero = drawables[4];
        dStench = drawables[5];
        dStoneFall = drawables[6];
        dBatHanging = drawables[7];
        dBatFlying = drawables[8];
        dWater = drawables[9];
        dRiver = drawables[10];
        dWeb = drawables[11];
        dSpider = drawables[12];
        dFrame = drawables[13];
        dSkeleton = drawables[14];
        dWumpusDead = drawables[15];
        dStoneFallHuge = drawables[16];


    }

    /**
     * Get the Singleton
     * @return
     */
    public static DungeonView getLastInstance() {
        return dv;
    }

    /**
     * Redraw the canvas, because game data has changed.
     * The game board consists of 8x8 tiles.
     */
    public void update() {
        bm = Bitmap.createBitmap(400, 400, Bitmap.Config.ARGB_8888);
        c = new Canvas(bm);

        Tile[][] tiles = game.getTiles();

        for (int i = 0; i < 8; i ++) {
            for (int j = 0; j < 8; j++) {
                if (tiles[i][j].isRevealed()) {
                    if (tiles[i][j].isWall()) {
                        dWall.setBounds(i * 50, j * 50, (i + 1) * 50, (j + 1) * 50);
                        dWall.draw(c);
                    }
                    else {
                        if (tiles[i][j].isEntered()) {
                            dFloorEntered.setBounds(i * 50, j * 50, (i + 1) * 50, (j + 1) * 50);
                            dFloorEntered.draw(c);
                        }
                        else {
                            dFloorRevealed.setBounds(i * 50, j * 50, (i + 1) * 50, (j + 1) * 50);
                            dFloorRevealed.draw(c);
                        }
                    }

                    if (tiles[i][j].isStinking()) {
                        dStench.setBounds(i * 50, j * 50, (i + 1) * 50, (j + 1) * 50);
                        dStench.draw(c);
                    }

                    if (tiles[i][j].isStoneFall()) {
                        dStoneFall.setBounds(i * 50, j * 50, (i + 1) * 50, (j + 1) * 50);
                        dStoneFall.draw(c);
                    }

                    if (tiles[i][j].isBatHanging()) {
                        dBatHanging.setBounds(i * 50, j * 50, (i + 1) * 50, (j + 1) * 50);
                        dBatHanging.draw(c);
                    }

                    if (tiles[i][j].isSplashing()) {
                        dWater.setBounds(i * 50, j * 50, (i + 1) * 50, (j + 1) * 50);
                        dWater.draw(c);
                    }

                    if (tiles[i][j].isWebbed()) {
                        dWeb.setBounds(i * 50, j * 50, (i + 1) * 50, (j + 1) * 50);
                        dWeb.draw(c);
                    }

                }

                else {
                    dFrame.setBounds(i * 50, j * 50, (i + 1) * 50, (j + 1) * 50);
                    dFrame.draw(c);
                }

            }
        }

        Tile bt = game.getRiver().getTile();

        if (bt.isEntered()) {
            dRiver.setBounds(bt.getX() * 50, bt.getY() * 50, (bt.getX() + 1) * 50, (bt.getY() + 1) * 50);
            dRiver.draw(c);
        }

        bt = game.getSpider().getTile();

        if (bt.isEntered()) {
            dSpider.setBounds(bt.getX() * 50, bt.getY() * 50, (bt.getX() + 1) * 50, (bt.getY() + 1) * 50);
            dSpider.draw(c);
        }

        bt = game.getBat().getTile();

        if (bt.isEntered()) {
            dBatFlying.setBounds(bt.getX() * 50, bt.getY() * 50, (bt.getX() + 1) * 50, (bt.getY() + 1) * 50);
            dBatFlying.draw(c);
        }

        bt = game.getSkeleton().getTile();

        if (bt.isEntered()) {
            dSkeleton.setBounds(bt.getX() * 50, bt.getY() * 50, (bt.getX() + 1) * 50, (bt.getY() + 1) * 50);
            dSkeleton.draw(c);
        }


        bt = game.getHero().getTile();
        dHero.setBounds(bt.getX() * 50, bt.getY() * 50, (bt.getX() + 1) * 50, (bt.getY() + 1) * 50);
        dHero.draw(c);

        bt = game.getStoneFall().getTile();
        if (bt.isEntered()) {
            dStoneFallHuge.setBounds(bt.getX() * 50, bt.getY() * 50, (bt.getX() + 1) * 50, (bt.getY() + 1) * 50);
            dStoneFallHuge.draw(c);
        }

        iv.setImageBitmap(bm);
    }

    /**
     * Connect this singleton to a new ImageView
     * @param iv The visualization on screen
     */
    public void setImageView(ImageView iv) {
        this.iv = iv;
    }

    /**
     * If the game is won, the dead wumpus is drawn on the board
     */
    public void updateWon() {
        update();
        Tile bt = game.getBeast().getTile();
        dWumpusDead.setBounds(bt.getX() * 50, bt.getY() * 50, (bt.getX() + 1) * 50, (bt.getY() + 1) * 50);
        dWumpusDead.draw(c);
    }

    /**
     * If the game is won, the living wumpus is drawn on the board
     */
    public void updateLost() {
        update();
        Tile bt = game.getBeast().getTile();
        dBeast.setBounds(bt.getX() * 50, bt.getY() * 50, (bt.getX() + 1) * 50, (bt.getY() + 1) * 50);
        dBeast.draw(c);
    }
}
