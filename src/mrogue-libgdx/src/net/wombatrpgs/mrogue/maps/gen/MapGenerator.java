/**
 *  MapGenerator.java
 *  Created on Oct 4, 2013 2:23:27 AM for project mrogue-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mrogue.maps.gen;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.badlogic.gdx.assets.AssetManager;

import net.wombatrpgs.mrogue.core.MGlobal;
import net.wombatrpgs.mrogue.core.Queueable;
import net.wombatrpgs.mrogue.maps.Level;
import net.wombatrpgs.mrogue.maps.Tile;
import net.wombatrpgs.mrogue.maps.gen.dec.Decorator;
import net.wombatrpgs.mrogue.maps.gen.dec.DecoratorFactory;
import net.wombatrpgs.mrogue.maps.layers.GridLayer;
import net.wombatrpgs.mrogueschema.maps.MapGeneratorMDO;
import net.wombatrpgs.mrogueschema.maps.TileMDO;
import net.wombatrpgs.mrogueschema.maps.data.TileType;
import net.wombatrpgs.mrogueschema.maps.data.WallTilesMDO;
import net.wombatrpgs.mrogueschema.maps.decorators.data.DecoratorMDO;

/**
 * The thing that y'know generates maps. Each one is created with a specific
 * map in mind.
 */
public abstract class MapGenerator implements Queueable {
	
	protected enum Halt {
		NONE, FIRST, CHANGE,
	}
	
	protected MapGeneratorMDO mdo;
	protected Random r;
	protected Level parent;
	protected Map<TileType, Tile> tileMap;
	protected List<Decorator> decorators;
	
	protected List<Queueable> assets;
	
	/**
	 * Creates a new map generator from data.
	 * @param	mdo				The data to generate from
	 * @param	parent			The level to generate for
	 */
	public MapGenerator(MapGeneratorMDO mdo, Level parent) {
		this.mdo = mdo;
		this.parent = parent;
		this.assets = new ArrayList<Queueable>();
		this.tileMap = new HashMap<TileType, Tile>();
		this.decorators = new ArrayList<Decorator>();
		addTile(mdo.floorTiles, TileType.FLOOR);
		addTile(mdo.ceilingTiles, TileType.CEILING);
		WallTilesMDO walls = MGlobal.data.getEntryFor(mdo.wallTiles, WallTilesMDO.class);
		addTile(walls.b, TileType.WALL_BOTTOM);
		addTile(walls.t, TileType.WALL_TOP);
		addTile(walls.br, TileType.WALL_BRIGHT);
		addTile(walls.bl, TileType.WALL_BLEFT);
		addTile(walls.tr, TileType.WALL_TRIGHT);
		addTile(walls.tl, TileType.WALL_TLEFT);
		addTile(walls.tm, TileType.WALL_TMID);
		addTile(walls.bm, TileType.WALL_BMID);
		for (String key : mdo.decorators) {
			DecoratorMDO decMDO = MGlobal.data.getEntryFor(key, DecoratorMDO.class);
			decorators.add(DecoratorFactory.createDecor(decMDO, this));
		}
		assets.addAll(decorators);
		
		r = new Random();
		r.setSeed(1);
	}
	
	/** @return The width of this map, in tiles */
	public int getWidth() { return parent.getWidth(); }
	
	/** @return The width of this map, in tiles */
	public int getHeight() { return parent.getHeight(); }
	
	/** @return The RNG used by this map */
	public Random rand() { return r; }
	
	/**
	 * Perform the actual construction of the level by bossing it around and
	 * making its layers.
	 */
	abstract public void generateMe();
	
	/**
	 * Fetches the default tile that this generator uses to generate.
	 * @param	type			The archetype to search for
	 * @return					The default tile used to fill that archetype
	 */
	public Tile getTile(TileType type) {
		return tileMap.get(type);
	}

	/**
	 * @see net.wombatrpgs.mrogue.core.Queueable#queueRequiredAssets
	 * (com.badlogic.gdx.assets.AssetManager)
	 */
	@Override
	public void queueRequiredAssets(AssetManager manager) {
		for (Queueable asset : assets) {
			asset.queueRequiredAssets(manager);
		}
	}

	/**
	 * @see net.wombatrpgs.mrogue.core.Queueable#postProcessing
	 * (com.badlogic.gdx.assets.AssetManager, int)
	 */
	@Override
	public void postProcessing(AssetManager manager, int pass) {
		for (Queueable asset : assets) {
			asset.postProcessing(manager, pass);
		}
		MGlobal.tiles.postProcessing(manager, pass);
	}
	
	/**
	 * Checks for passability in a passability array. This isn't just a lookup
	 * because tiles need to be checked.
	 * @param	pass			The data to check
	 * @param	x				The col to check
	 * @param	y				The row to check
	 * @return
	 */
	public boolean isPassable(TileType[][] pass, int x, int y) {
		if (x < 0 || x >= parent.getWidth() || y < 0 || y >= parent.getHeight()) {
			return false;
		} else {
			return pass[y][x].isPassable();
		}
	}
	
	/**
	 * Checks if the given type matches at that location in the array. Not a
	 * straight lookup due to bounds checking.
	 * @param	<T>				The type of the data to deal with
	 * @param	data			The data to check
	 * @param	value			The object to check equality with
	 * @param	x				The col to check
	 * @param	y				The row to check
	 * @return					True if the object at that location matches
	 */
	public <T> boolean isType(T[][] data, T value, int x, int y) {
		if (x < 0 || x >= parent.getWidth() || y < 0 || y >= parent.getHeight()) {
			return false;
		} else {
			return data[y][x] == value;
		}
	}
	
	/**
	 * Fills a tile array with tiles by type from the passed data. This assumes
	 * that the tile array has already been initialized and the type array is
	 * completely full of tile types. It also assumes the MDO has at least one
	 * tile per tile type.
	 * @param	types			The types to assign from
	 * @param	tiles			The tiles to assign to
	 */
	public void fillTiles(TileType[][] types, Tile[][] tiles) {
		for (int x = 0; x < parent.getWidth(); x += 1) {
			for (int y = 0; y < parent.getHeight(); y += 1) {
				tiles[y][x] = getTile(types[y][x]);
			}
		}
		for (Decorator d : decorators) {
			d.apply(tiles);
		}
	}
	
	/**
	 * Registers a tile to be converted later. It's a tile table thing.
	 * @param	tileMDO			The tile to set
	 * @param	type			The type of tile to replace
	 */
	protected void addTile(TileMDO tileMDO, TileType type) {
		tileMap.put(type, MGlobal.tiles.getTile(tileMDO, type));
	}
	
	/**
	 * Turns ceiling into wall based on passability of surrounding tiles.
	 * @param	types			The type array to work with
	 */
	protected void applyWalls(TileType[][] types) {
		for (int x = 0; x < parent.getWidth(); x += 1) {
			for (int y = 0; y < parent.getHeight(); y += 1) {
				if (x == 22 && y == 18) {
					System.out.println();
				}
				if (isPassable(types, x, y-1) &&
						isType(types, TileType.CEILING, x, y) &&
						isType(types, TileType.CEILING, x, y+1) &&
						isType(types, TileType.CEILING, x, y+2)) {
					if (isPassable(types, x-1, y) && isPassable(types, x+1, y)) {
						types[y+1][x] = TileType.WALL_TMID;
						types[y][x] = TileType.WALL_BMID;
					} else if (isPassable(types, x-1, y)) {
						types[y+1][x] = TileType.WALL_TLEFT;
						types[y][x] = TileType.WALL_BLEFT;
					} else if (isPassable(types, x+1, y)) {
						types[y+1][x] = TileType.WALL_TRIGHT;
						types[y][x] = TileType.WALL_BRIGHT;
					} else {
						types[y+1][x] = TileType.WALL_TOP;
						types[y][x] = TileType.WALL_BOTTOM;
					}
				}
			}
		}
	}
	
	/**
	 * Replaces all islands of ceiling with the specified value.
	 * @param	types			The data to operate on
	 * @param	value			The value to set
	 */
	protected void purgeFloatingWalls(TileType[][] types, TileType value) {
		for (int x = 0; x < parent.getWidth(); x += 1) {
			for (int y = 0; y < parent.getHeight(); y += 1) {
				if (isType(types, TileType.CEILING, x, y) && (
						(isPassable(types, x, y-1) && isPassable(types, x, y+1)) ||
						(isPassable(types, x, y-1) && isPassable(types, x, y+2)) ||
						(isPassable(types, x, y-2) && isPassable(types, x, y+1)))) {
					types[y][x] = TileType.FLOOR;
				}
			}
		}
	}
	
	/**
	 * Randomly selects a tile from a list.
	 * @param	tiles			The list of tiles to choose from
	 * @return					A random element from that list
	 */
	protected Tile getRandomTile(List<Tile> tiles) {
		return tiles.get(MGlobal.rand.nextInt(tiles.size()));
	}
	
	/**
	 * Checks if a room is unreachable. This involves checking if any paths
	 * connect to it. If no paths connect, it's considered unreachable. Note
	 * that this isn't a guarantee that a room /is/ reachable, because binary
	 * pairs of rooms could still be isolation.
	 * @param	types			The tile data to use
	 * @param	rm				The room to check
	 * @return					True if the room is unreachable (not iff)
	 */
	protected boolean roomUnreachable(TileType[][] types, Room rm) {
		for (int x = rm.x; x < rm.x+rm.rw; x += 1) {
			if (isPassable(types, x, rm.y-1)) return false;
			if (isPassable(types, x, rm.y+rm.rh+1)) return false;
		}
		for (int y = rm.y; y < rm.y+rm.rh; y += 1) {
			if (isPassable(types, rm.x-1, y)) return false;
			if (isPassable(types, rm.x+rm.rw+1, y)) return false;
		}
		return true;
	}
	
	/**
	 * Calculates the distance between the center of two rooms. Does not throw
	 * in the sqrt thing.
	 * @param	rm1				The first room
	 * @param	rm2				Any other room
	 * @return					The euclidean distance between the two
	 */
	protected float roomDistSq(Room rm1, Room rm2) {
		float dx = rm1.cx() - rm2.cx();
		float dy = rm1.cy() - rm2.cy();
		return dx*dx + dy*dy;
	}
	
	/**
	 * Adds a finished array of tiles to the map.
	 * @param	tiles			The tile data for the layer
	 * @param	z				The z-depth of the layer
	 */
	protected void addLayer(Tile[][] tiles, float z) {
		GridLayer layer = new GridLayer(parent, tiles, z);
		parent.addGridLayer(layer);
	}
	
	/**
	 * Assigns tiles to a finished array of types and adds it to the map.
	 * @param	types			The tile types for the layer
	 * @param	z				The z-depth of the layer
	 */
	protected void addLayer(TileType[][] types, float z) {
		Tile[][] tiles = new Tile[parent.getHeight()][parent.getWidth()];
		fillTiles(types, tiles);
		addLayer(tiles, z);
	}
	
	/**
	 * Determines how many rooms this map should have based on its MDO.
	 * @return					The number of rooms for this map
	 */
	protected int roomCount() {
		int area = (parent.getWidth()-2) * (parent.getHeight()-2);
		float width = (mdo.maxRoomWidth + mdo.minRoomWidth) / 2f;
		float height = (mdo.minRoomHeight + mdo.maxRoomHeight) / 2f + 2f;
		float roomArea = width * height;
		return Math.round((float) area * mdo.density / roomArea);
	}
	
	/**
	 * Carves a path through the data to get from (x1, y1) to (x2, y2). Picks
	 * a direction to cut first, creating angular L-shaped paths.
	 * @param	<T>				The type of data to operate on
	 * @param	data			The data to cut through
	 * @param	value			The value to set
	 * @param 	x1				X1, in tiles
	 * @param 	y1				Y1, in tiles
	 * @param 	x2				X2, in tiles
	 * @param	y2				Y2, in tiles
	 * @param	halt			Halt policy
	 * @return					True if halted early, false if finished complete
	 */
	protected <T> boolean carve(T[][] data, T value, int x1, int y1, int x2, int y2, Halt halt) {
		if (r.nextBoolean()) {
			if (carveX(data, value, x1, x2, y1, halt)) return true;
			return carveY(data, value, y1, y2, x2, halt);
		} else {
			if (carveY(data, value, y1, y2, x1, halt)) return true;	
			return carveX(data, value, x1, x2, y2, halt);
		}
	}
	
	/**
	 * Carves the x-coord of a chunk of data.
	 * @param	<T>				The type of data to operate on
	 * @param	data			The data to operate on
	 * @param 	value			The value to set
	 * @param	x1				The col to start at
	 * @param 	x2				The col to finish at
	 * @param	y				The row to carve at
	 * @param	halt			Halt policy
	 * @return					True if halted early, false if finished complete
	 */
	protected <T> boolean carveX(T[][] data, T value, int x1, int x2, int y, Halt halt) {
		boolean change = false;
		if (x1 > x2) {
			int temp = x1;
			x1 = x2;
			x2 = temp;
		}
		for (int i = x1; i <= x2; i += 1) {
			if (data[y][i] == value) {
				if (halt == Halt.FIRST) return true;
				if (halt == Halt.CHANGE && change) return true;
			} else {
				data[y][i] = value;
				change = true;
			}
		}
		return false;
	}
	
	/**
	 * Carves the y-coord of a chunk of data.
	 * @param	<T>				The type of data to operate on
	 * @param	data			The data to operate on
	 * @param 	value			The value to set
	 * @param	x				The col to carve at
	 * @param 	y1				The row to start at
	 * @param	y2				The row to finish at
	 * @param	halt			Halt policy
	 * @return					True if halted early, false if finished complete
	 */
	protected <T> boolean carveY(T[][] data, T value, int y1, int y2, int x, Halt halt) {
		boolean change = false;
		if (y1 > y2) {
			int temp = y1;
			y1 = y2;
			y2 = temp;
		}
		for (int i = y1; i <= y2; i+= 1) {
			if (data[i][x] == value) {
				if (halt == Halt.FIRST) return true;
				if (halt == Halt.CHANGE && change) return true;
			} else {
				data[i][x] = value;
				change = true;
			}
		}
		return false;
	}
	
	/**
	 * Carves a rectangle to fill the area (x1, y2) to (x2, y2)
	 * @param	<T>				The type of data to operate on
	 * @param	data			The data to operate on
	 * @param	value			The value to set
	 * @param	x1				The col to start at
	 * @param	y1				The row to start at
	 * @param	x2				The col to end at
	 * @param	y2				The row to end at
	 */
	protected <T> void fillRect(T[][] data, T value, int x1, int y1, int x2, int y2) {
		if (y1 > y2) {
			int temp = y1;
			y1 = y2;
			y2 = temp;
		}
		if (x1 > x2) {
			int temp = x1;
			x1 = x2;
			x2 = temp;
		}
		for (int x = x1; x <= x2; x += 1) {
			for (int y = y1; y <= y2; y += 1) {
				data[y][x] = value;
			}
		}
	}

}
