package model;

/**
 * This class represents an element on the game board.
 * This can be moving figure, as well as a immobile object.
 */
public class CaveElement {
	// Each CaveElement is located on a single Tile.
	private Tile tile;

	public CaveElement(Tile t) {
		tile = t;
	}

	public Tile getTile() {
		return tile;
	}

	public void setTile(Tile tile2) {
		tile = tile2;
	}
	
}
