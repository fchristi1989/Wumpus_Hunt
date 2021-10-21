package model;

import java.util.ArrayList;

/**
 * This container class represents a single one of the 64 tiles
 * by containing primitive values.
 */
public class Tile {
	private boolean isWall;
	private boolean isRevealed;
	private boolean isEntered;
	private int posX;
	private int posY;
	private boolean stinks;
	private boolean stonesFall;
	private boolean batHanging;
	private boolean isSplashing;
	private boolean isWebbed;

	// This value is used temporarily during path finding.
	private int dijkstraDistance;

	// Only horizontal and vertical neighbours.
	private ArrayList<Tile> neighbours;
	
	public Tile(int i, int j) {
		isWall = Math.random() < 0.35;
		posX = i;
		posY = j;
		isRevealed = false;
		stinks = false;
		stonesFall = false;
		batHanging = false;
		isEntered = false;
		isSplashing = false;
		isWebbed = false;

		dijkstraDistance = 100;

		neighbours = new ArrayList<Tile>();
	}

	public boolean isWall() {
		return isWall;
	}

	public int getX() {
		return posX;
	}

	public int getY() {
		return posY;
	}

	public void open() {
		isWall = false;
	}

	public void reveal() {
		isRevealed = true;
	}

	public boolean isRevealed() {
		return isRevealed;
	}

	public void stink(boolean b) {
		if (!isWall)
			stinks = b;
	}

	public boolean isStinking() {
		return stinks;
	}

    public void addNeighbour(Tile neighbour) {
		neighbours.add(neighbour);
    }

	public ArrayList<Tile> getNeighbours() {
		return neighbours;
	}

	public void stonesFall() {
		stonesFall = true;
	}

	public boolean isStoneFall() {
		return stonesFall;
	}

	public void hangBat() {
		batHanging = true;
	}

	public boolean isBatHanging() {
		return batHanging;
	}

	public void enter() {
		isEntered = true;
	}

	public boolean isEntered() {
		return isEntered;
	}

	public void splash() {
		isSplashing = true;
	}

	public boolean isSplashing() {
		return isSplashing;
	}

	public void web() {
		isWebbed = true;
	}

	public boolean isWebbed() {
		return isWebbed;
	}

    public void setDijkstraDistance(int d) {
		dijkstraDistance = d;
    }

	public int getDijktraDistance() {
		return dijkstraDistance;
	}
}
