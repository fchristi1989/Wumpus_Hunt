package model;

import com.wumpus.wumpus.GameActivity;

import java.util.LinkedList;

/**
 * This class manages business logic of the game and hols model data.
 */
public class Game {
	private static final int NUMTILES = 8;
	
	private GameActivity activity;
	
	private Action currentAction;
	private GameState state;
	private GameEvent event;
	private int level;
	private int score;
	private int fuel;
	private int dynamite;
	
	private Tile [][] tiles;
	private CaveElement hero;
	private CaveElement beast;
	private CaveElement stoneFall;
	private CaveElement bat;
	private CaveElement river;
	private CaveElement spider;
	private CaveElement skeleton;


	/**
	 * The Constructor, called by the GameActivity
	 * @param ga The calling GameActivity
	 * @param l The current level reached
	 * @param score The current score reached
	 */
	public Game(GameActivity ga, int l, int score) {
		activity = ga;
		level = l;
		this.score = score;
		fuel = 50;
		dynamite = 3;
		hero = null;
		start();
	}

	/**
	 * Find a random open , i.e. non-wall and non-hero tile to return
	 * and later place a CaveElement on it.
	 * @return The found random open tile.
	 */
	private Tile openTile() {
		Tile t = null;
		boolean retry;

		// Get a random tile until it is "open".
		do {
			retry = false;
			t = tiles [(int) (Math.random() * (double) NUMTILES)][(int) (Math.random() * (double) NUMTILES)];

			if (t.isWall())
				retry = true;

			if (hero != null) {
				if (hero.getTile() == t)
					retry = true;
			}
		}
		while (retry);
		
		return t;
	}

	/**
	 * Called, after the hero is moved.
	 * The method investigates, what the newly entered tile looks like.
	 */
	private void reveal() {
		Tile tile = hero.getTile();

		tile.enter();
		tile.reveal();

		for (Tile t: tile.getNeighbours()) {
			t.reveal();
		}

		for (Tile t : hero.getTile().getNeighbours()) {
			if (!t.isWall()) {
				for (Tile t2 : t.getNeighbours()) {
					if (t2 == beast.getTile()) {
						hero.getTile().stink(true);

						if (event == GameEvent.None)
							event = GameEvent.Stench;
					}
				}
			}

			if (t == beast.getTile()) {
				hero.getTile().stink(true);

				if (event == GameEvent.None)
					event = GameEvent.Stench;
			}

			if (t == stoneFall.getTile()) {
				hero.getTile().stonesFall();

				if (event == GameEvent.None)
					event = GameEvent.StoneFall;
			}

			if (t == bat.getTile()) {
				hero.getTile().hangBat();

				if (event == GameEvent.None)
					event = GameEvent.HangingBat;
			}

			if (t == river.getTile()) {
				hero.getTile().splash();

				if (event == GameEvent.None)
					event = GameEvent.Water;
			}

			if (t == spider.getTile()) {
				hero.getTile().web();

				if (event == GameEvent.None)
					event = GameEvent.SmallWeb;
			}
		}

	}

	/**
	 * Try to move the hero down.
	 */
	public void validateDown() {
		Tile source = hero.getTile();
		
		if (source.getY() < NUMTILES - 1) {
			Tile target = tiles[source.getX()][source.getY() + 1];
			
			validateAction(source, target);
		}

		activity.updateState();
	}

	/**
	 * Try to move the hero left.
	 */
	public void validateLeft() {
		Tile source = hero.getTile();
		
		if (source.getX() > 0) {
			Tile target = tiles[source.getX() - 1][source.getY()];

			validateAction(source, target);
		}

		activity.updateState();

	}

	/**
	 * Try to move the hero right.
	 */
	public void validateRight() {
		Tile source = hero.getTile();
		
		if (source.getX() < NUMTILES - 1) {
			Tile target = tiles[source.getX() + 1][source.getY()];
			
			validateAction(source, target);
		}

		activity.updateState();

	}

	/**
	 * Try to move the hero up.
	 */
	public void validateUp() {
		Tile source = hero.getTile();

		if (source.getY() > 0) {
			Tile target = tiles[source.getX()][source.getY() - 1];
			validateAction(source, target);
		}

		activity.updateState();
	}

	/**
	 * Change the next kind action to perform.
	 * @param action The kind of action to perform next.
	 */
	public void toggleAction(Action action) {
		currentAction = action;
	}


	/**
	 * Try to move, shoot or blast from a specific tile (the hero's position)
	 * to a target tile.
	 * @param source Ideally the the hero's position.
	 * @param target The tile to act on.
	 */
	private void validateAction(Tile source, Tile target) {
		if (state == GameState.Running) {
			event = GameEvent.None;

			if (currentAction == Action.Move && !target.isWall()) {
				fuel--;
				score--;
				hero.setTile(target);
			}

			else if (currentAction == Action.Blast && target.isWall()) {
				if (dynamite > 0) {
					fuel--;
					score-= 5;

					moveBeastDijkstra();

					dynamite--;

					target.open();

					event = GameEvent.Burst;
				}
			}

			else if (currentAction == Action.Shoot) {
				if (target == beast.getTile())
					state = GameState.Won;
				else
					state = GameState.Missed;
			}


			if (hero.getTile() == skeleton.getTile() && !skeleton.getTile().isEntered()) {
				dynamite++;
				fuel += 10;
				score += 25;
				event = GameEvent.Skeleton;
			}

			if (hero.getTile() == river.getTile()) {
				moveBeastDijkstra();
				event = GameEvent.River;
			}

			reveal();

			if (hero.getTile() == beast.getTile())
				state = GameState.Eaten;
			if (hero.getTile() == stoneFall.getTile())
				state = GameState.Stoned;
			if (fuel <= 0)
				state = GameState.OutOfFuel;

			if (hero.getTile() == spider.getTile()) {
				fuel -= 10;
				event = GameEvent.BigWeb;
			}

			if (hero.getTile() == bat.getTile()) {
				validateAction(hero.getTile(), openTile());
				event = GameEvent.FlyingBat;
			}
		}

	}

	/**
	 * A Dijkstra algorithm is used to move the beast towards the hero on the shortest way.
	 * This is called after the hero makes any noise.
	 */
	private void moveBeastDijkstra() {
		Tile source = beast.getTile();
		Tile target = hero.getTile();

		LinkedList<Tile> unvisited = new LinkedList<Tile>();

		for (int i = 0; i < NUMTILES; i++) {
			for (int j = 0; j < NUMTILES; j++) {
				if (!tiles[i][j].isWall()) {
					tiles[i][j].setDijkstraDistance(100);
					unvisited.add(tiles[i][j]);
				}
			}
		}

		target.setDijkstraDistance(0);

		while (!unvisited.isEmpty()) {
			Tile next = unvisited.getFirst();

			for (Tile t : unvisited) {
				if (t.getDijktraDistance() < next.getDijktraDistance())
					next = t;
			}

			unvisited.remove(next);

			for (Tile t: next.getNeighbours()) {
				if (next.getDijktraDistance() + 1 < t.getDijktraDistance() && !t.isWall())
					t.setDijkstraDistance(next.getDijktraDistance() + 1);
			}
		}

		double distance = Math.random() * 2.0 * Math.sqrt((double) level);

		for (int i = 0; i < distance; i++) {
			Tile next = source.getNeighbours().get(0);

			for (Tile t: source.getNeighbours()) {
				if (t.getDijktraDistance() < next.getDijktraDistance())
					next = t;
			}

			if (next.getDijktraDistance() < source.getDijktraDistance() && !next.isWall()) {
				beast.setTile(next);
				source = next;
			}
		}
	}

	/**
	 * Initialize the game and all its elements.
	 */
	private void start() {
		currentAction = Action.Move;
		state = GameState.Running;
		event = GameEvent.None;

		tiles = new Tile[NUMTILES][NUMTILES];

		for (int i = 0; i < NUMTILES; i++) {
			for (int j = 0; j < NUMTILES; j++) {
				tiles[i][j] = new Tile(i, j);
			}
		}

		for (int i = 0; i < NUMTILES; i++) {
			for (int j = 0; j < NUMTILES; j++) {
				Tile t1 = tiles[i][j];

				if (i < NUMTILES - 1) {
					Tile t2 = tiles[i + 1][j];
					t1.addNeighbour(t2);
					t2.addNeighbour(t1);
				}

				if (j < NUMTILES - 1) {
					Tile t3 = tiles[i][j + 1];
					t1.addNeighbour(t3);
					t3.addNeighbour(t1);
				}
			}
		}

		hero = new CaveElement(openTile());
		beast = new CaveElement(openTile());
		stoneFall = new CaveElement(openTile());
		bat = new CaveElement(openTile());
		river = new CaveElement(openTile());
		spider = new CaveElement(openTile());
		skeleton = new CaveElement(openTile());

		reveal();
	}

	// Getters

	/**
	 * Get the 8x8 matrix of Tiles
	 * @return The Tile matrix.
	 */
	public Tile[][] getTiles() {
		return tiles;
	}


	public CaveElement getHero() {
		return hero;
	}


	public CaveElement getBeast() {
		return beast;
	}


	public GameState getState() {
		return state;
	}

	public int getLevel() {
		return level;
	}


	public CaveElement getBat() {
		return bat;
	}


	public CaveElement getRiver() {
		return river;
	}


	public int getFuel() {
		return fuel;
	}


	public CaveElement getSpider() {
		return spider;
	}


	public int getDynamite() {
		return dynamite;
	}


	public CaveElement getSkeleton() {
		return skeleton;
	}


	public GameEvent getEvent() {
		return event;
	}


	public void lock() {
		state = GameState.Locked;
	}


	public CaveElement getStoneFall() {
		return stoneFall;
	}

	public int getScore() {
		return score;
	}
}
