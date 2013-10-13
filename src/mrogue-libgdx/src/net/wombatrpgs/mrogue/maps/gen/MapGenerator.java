/**
 *  MapGenerator.java
 *  Created on Oct 4, 2013 2:23:27 AM for project mrogue-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mrogue.maps.gen;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.badlogic.gdx.assets.AssetManager;

import net.wombatrpgs.mrogue.core.MGlobal;
import net.wombatrpgs.mrogue.core.Queueable;
import net.wombatrpgs.mrogue.maps.Level;
import net.wombatrpgs.mrogue.maps.Tile;
import net.wombatrpgs.mrogue.maps.TileType;
import net.wombatrpgs.mrogue.maps.layers.GridLayer;
import net.wombatrpgs.mrogueschema.maps.MapGeneratorMDO;
import net.wombatrpgs.mrogueschema.maps.TileMDO;
import net.wombatrpgs.mrogueschema.maps.data.WallTilesMDO;

/**
 * The thing that y'know generates maps. Each one is created with a specific
 * map in mind.
 */
public abstract class MapGenerator implements Queueable {
	
	protected MapGeneratorMDO mdo;
	protected Random r;
	protected Level parent;
	protected List<Tile> floorTiles;
	protected List<Tile> ceilTiles;
	protected List<Tile> uwallTiles;
	protected List<Tile> lwallTiles;
	
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
		this.floorTiles = new ArrayList<Tile>();
		this.ceilTiles = new ArrayList<Tile>();
		this.uwallTiles = new ArrayList<Tile>();
		this.lwallTiles = new ArrayList<Tile>();
		for (TileMDO tile : mdo.floorTiles) {
			floorTiles.add(MGlobal.tiles.getTile(tile, TileType.FLOOR));
		}
		for (TileMDO ceil : mdo.ceilingTiles) {
			ceilTiles.add(MGlobal.tiles.getTile(ceil, TileType.CEILING));
		}
		for (WallTilesMDO wall : mdo.wallTiles) {
			uwallTiles.add(MGlobal.tiles.getTile(wall.upper, TileType.WALL_UPPER));
			lwallTiles.add(MGlobal.tiles.getTile(wall.lower, TileType.WALL_LOWER));
		}
		r = new Random();
		r.setSeed(MGlobal.rand.nextLong());
	}
	
	/**
	 * Perform the actual construction of the level by bossing it around and
	 * making its layers.
	 */
	abstract public void generateMe();

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
	 * Fills a tile array with tiles by type from the passed data. This assumes
	 * that the tile array has already been initialized and the type array is
	 * completely full of tile types. It also assumes the MDO has at least one
	 * tile per tile type.
	 * @param	types			The types to assign from
	 * @param	tiles			The tiles to assign to
	 */
	protected void fillTiles(TileType[][] types, Tile[][] tiles) {
		for (int x = 0; x < parent.getWidth(); x += 1) {
			for (int y = 0; y < parent.getHeight(); y += 1) {
				switch (types[y][x]) {
				case CEILING: tiles[y][x] = getRandomTile(ceilTiles); break;
				case FLOOR: tiles[y][x] = getRandomTile(floorTiles); break;
				case WALL_UPPER: tiles[y][x] = getRandomTile(uwallTiles); break;
				case WALL_LOWER: tiles[y][x] = getRandomTile(lwallTiles); break;
				}
			}
		}
	}
	
	/**
	 * Turns ceiling into wall based on passability of surrounding tiles.
	 * @param	types			The type array to work with
	 */
	protected void applyWalls(TileType[][] types) {
		for (int x = 0; x < parent.getWidth(); x += 1) {
			for (int y = 0; y < parent.getHeight(); y += 1) {
				if (isPassable(types, x, y-1) &&
						isType(types, TileType.CEILING, x, y+1) &&
						isType(types, TileType.CEILING, x, y+2)) {
					types[y][x] = TileType.WALL_LOWER;
				}
			}
		}
		for (int x = 0; x < parent.getWidth(); x += 1) {
			for (int y = 0; y < parent.getHeight(); y += 1) {
				if (isType(types, TileType.WALL_LOWER, x, y-1)) {
					types[y][x] = TileType.WALL_UPPER;
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
	 * Checks for passability in a passability array. This isn't just a lookup
	 * because tiles need to be checked.
	 * @param	pass			The data to check
	 * @param	x				The col to check
	 * @param	y				The row to check
	 * @return
	 */
	protected boolean isPassable(TileType[][] pass, int x, int y) {
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
	protected <T> boolean isType(T[][] data, T value, int x, int y) {
		if (x < 0 || x >= parent.getWidth() || y < 0 || y >= parent.getHeight()) {
			return false;
		} else {
			return data[y][x] == value;
		}
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
	 * Carves a path through the data to get from (x1, y1) to (x2, y2). Picks
	 * a direction to cut first, creating angular L-shaped paths.
	 * @param	<T>				The type of data to operate on
	 * @param	data			The data to cut through
	 * @param	value			The value to set
	 * @param 	x1			X1, in tiles
	 * @param 	y1			Y1, in tiles
	 * @param 	x2				X2, in tiles
	 * @param	y2				Y2, in tiles
	 */
	protected <T> void carve(T[][] data, T value, int x1, int y1, int x2, int y2) {
		if (r.nextBoolean()) {
			carveX(data, value, x1, x2, y1);
			carveY(data, value, y1, y2, x2);
		} else {
			carveY(data, value, y1, y2, x1);	
			carveX(data, value, x1, x2, y2);
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
	 */
	protected <T> void carveX(T[][] data, T value, int x1, int x2, int y) {
		if (x1 > x2) {
			int temp = x1;
			x1 = x2;
			x2 = temp;
		}
		for (int i = x1; i <= x2; i += 1) {
			data[y][i] = value;
		}
	}
	
	/**
	 * Carves the y-coord of a chunk of data.
	 * @param	<T>				The type of data to operate on
	 * @param	data			The data to operate on
	 * @param 	value			The value to set
	 * @param	x				The col to carve at
	 * @param 	y1				The row to start at
	 * @param	y2				The row to finish at
	 */
	protected <T> void carveY(T[][] data, T value, int y1, int y2, int x) {
		if (y1 > y2) {
			int temp = y1;
			y1 = y2;
			y2 = temp;
		}
		for (int i = y1; i <= y2; i+= 1) {
			data[i][x] = value;
		}
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
