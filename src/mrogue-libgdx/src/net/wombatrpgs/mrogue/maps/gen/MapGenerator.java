/**
 *  MapGenerator.java
 *  Created on Oct 4, 2013 2:23:27 AM for project mrogue-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mrogue.maps.gen;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import com.badlogic.gdx.assets.AssetManager;

import net.wombatrpgs.mrogue.core.MGlobal;
import net.wombatrpgs.mrogue.core.Queueable;
import net.wombatrpgs.mrogue.maps.Level;
import net.wombatrpgs.mrogue.maps.Loc;
import net.wombatrpgs.mrogue.maps.Tile;
import net.wombatrpgs.mrogue.maps.events.DoorEvent;
import net.wombatrpgs.mrogue.maps.events.TeleportEvent;
import net.wombatrpgs.mrogue.maps.gen.dec.Decorator;
import net.wombatrpgs.mrogue.maps.gen.dec.DecoratorFactory;
import net.wombatrpgs.mrogue.maps.layers.GridLayer;
import net.wombatrpgs.mrogueschema.maps.CeilTilesMDO;
import net.wombatrpgs.mrogueschema.maps.MapGeneratorMDO;
import net.wombatrpgs.mrogueschema.maps.StairTilesMDO;
import net.wombatrpgs.mrogueschema.maps.WallTilesMDO;
import net.wombatrpgs.mrogueschema.maps.data.DoorSetMDO;
import net.wombatrpgs.mrogueschema.maps.data.TileMDO;
import net.wombatrpgs.mrogueschema.maps.data.TileType;
import net.wombatrpgs.mrogueschema.maps.decorators.data.DecoratorMDO;

/**
 * The thing that y'know generates maps. Each one is created with a specific
 * map in mind.
 */
public abstract class MapGenerator implements Queueable {
	
	protected static Set<TileType> walls = Collections.synchronizedSet(EnumSet.of(
			TileType.WALL_TLEFT, TileType.WALL_TMID, TileType.WALL_TRIGHT, TileType.WALL_TOP,
			TileType.WALL_BLEFT, TileType.WALL_BMID, TileType.WALL_TRIGHT, TileType.WALL_BOTTOM,
			TileType.ANY_MIDDLE_WALL, TileType.ANY_WALL));
	protected static Set<TileType> middleWalls = Collections.synchronizedSet(EnumSet.of(
			TileType.WALL_TOP, TileType.WALL_BOTTOM, TileType.ANY_MIDDLE_WALL));
	protected static Set<TileType> ceilings = Collections.synchronizedSet(EnumSet.of(
			TileType.CEILING_BOTTOM, TileType.CEILING_MIDDLE, TileType.CEILING_TOP, TileType.ANY_CEILING));
	
	protected enum Halt {
		NONE, FIRST, CHANGE,
	}
	protected enum ConversionState {
		INIT, GENERATING, WALLS, CEILING, CONVERSION, FINISHED,
	}
	
	protected MapGeneratorMDO mdo;
	protected Random r;
	protected ConversionState state;
	protected Level parent;
	protected Map<TileType, Tile> tileMap;
	protected List<Decorator> decorators, upDecorators;
	protected Map<String, TeleportEvent> stairTeles;	// mapID to tele
	
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
		this.upDecorators = new ArrayList<Decorator>();
		this.stairTeles = new HashMap<String, TeleportEvent>();
		
		CeilTilesMDO ceils = MGlobal.data.getEntryFor(mdo.ceilingTiles, CeilTilesMDO.class);
		WallTilesMDO walls = MGlobal.data.getEntryFor(mdo.wallTiles, WallTilesMDO.class);
		StairTilesMDO ustairs = MGlobal.data.getEntryFor(mdo.upstairTiles, StairTilesMDO.class);
		StairTilesMDO dstairs = MGlobal.data.getEntryFor(mdo.downstairTiles, StairTilesMDO.class);
		addTile(mdo.floorTiles, TileType.FLOOR);
		addTile(walls.b, TileType.WALL_BOTTOM);
		addTile(walls.t, TileType.WALL_TOP);
		addTile(walls.br, TileType.WALL_BRIGHT);
		addTile(walls.bl, TileType.WALL_BLEFT);
		addTile(walls.tr, TileType.WALL_TRIGHT);
		addTile(walls.tl, TileType.WALL_TLEFT);
		addTile(walls.tm, TileType.WALL_TMID);
		addTile(walls.bm, TileType.WALL_BMID);
		addTile(ceils.b, TileType.CEILING_BOTTOM);
		addTile(ceils.m, TileType.CEILING_MIDDLE);
		addTile(ceils.t, TileType.CEILING_TOP);
		addTile(ustairs.b, TileType.USTAIR_BOTTOM);
		addTile(ustairs.t, TileType.USTAIR_TOP);
		addTile(dstairs.b, TileType.DSTAIR_BOTTOM);
		addTile(dstairs.t, TileType.DSTAIR_TOP);
		
		for (String key : mdo.decorators) {
			DecoratorMDO decMDO = MGlobal.data.getEntryFor(key, DecoratorMDO.class);
			decorators.add(DecoratorFactory.createDecor(decMDO, this));
		}
		assets.addAll(decorators);
		for (String key : mdo.upDecorators) {
			DecoratorMDO decMDO = MGlobal.data.getEntryFor(key, DecoratorMDO.class);
			upDecorators.add(DecoratorFactory.createDecor(decMDO, this));
		}
		assets.addAll(upDecorators);
		
		assets.add(new DoorEvent(MGlobal.data.getEntryFor(mdo.doors, DoorSetMDO.class), parent));
		
		r = new Random();
		long seed = MGlobal.rand.nextLong();
		r.setSeed(seed);
		
		state = ConversionState.INIT;
		MGlobal.reporter.inform("Generator initialized for " + parent + ", "
				+ "using " + mdo.key + " algorithm and seed " + seed);
	}
	
	/** @return The width of this map, in tiles */
	public int getWidth() { return parent.getWidth(); }
	
	/** @return The width of this map, in tiles */
	public int getHeight() { return parent.getHeight(); }
	
	/** @return The RNG used by this map */
	public Random rand() { return r; }
	
	/**
	 * Perform the actual construction of the level by bossing it around and
	 * making its layers. This isn't directly overridden in subclasses, but
	 * instead delegated. This method wraps some info calls.
	 */
	public final void generateMe() {
		if (state != ConversionState.INIT) {
			MGlobal.reporter.warn("Reusing a map generator for " + parent);
			return;
		} else {
			state = ConversionState.GENERATING;
		}
		MGlobal.reporter.inform("Generation algorithm begin for " + parent +
				"(" + parent.getWidth() + ", " + parent.getHeight() + ")");
		long startTime = System.currentTimeMillis();
		generateInternal();
		long endTime = System.currentTimeMillis();
		float elapsed = (endTime - startTime) / 1000f;
		MGlobal.reporter.inform("Generation finished for " + parent + ", " +
				"elapsed time: " + elapsed + " seconds");
	}
	
	/**
	 * Fetches the default tile that this generator uses to generate. Returns
	 * null if given null.
	 * @param	type			The archetype to search for
	 * @return					The default tile used to fill that archetype
	 */
	public Tile getTile(TileType type) {
		if (type == null) return null;
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
	 * Checks for passability in a tile array. This isn't just a lookup because
	 * tiles need to be checked as well as bounds.
	 * @param	tiles			The data to check
	 * @param	x				The col to check
	 * @param	y				The row to check
	 * @return
	 */
	public boolean isPassable(Tile[][] tiles, int x, int y) {
		if (x < 0 || x >= parent.getWidth() || y < 0 || y >= parent.getHeight()) {
			return false;
		} else {
			return tiles[y][x] == null || tiles[y][x].isPassable();
		}
	}
	
	/**
	 * Checks if the given type matches at that location in the array. Not a
	 * straight lookup due to bounds checking. The value fetched is considered
	 * equivalent to null if out of bounds.
	 * @param	<T>				The type of the data to deal with
	 * @param	data			The data to check
	 * @param	value			The object to check equality with
	 * @param	x				The col to check
	 * @param	y				The row to check
	 * @return					True if the object at that location matches
	 */
	public <T> boolean isValue(T[][] data, T value, int x, int y) {
		return get(data, x, y) == value;
	}
	
	/**
	 * Array lookup method with bounds checking. Returns null if outside bounds.
	 * @param	<T>				The type of the data being operated on
	 * @param	data			The data to operate on
	 * @param	x				The x-coord to look up (in tiles)
	 * @param	y				The y-coord to look up (in tiles)
	 * @return					The value at that location in the array
	 */
	public <T> T get(T[][] data, int x, int y) {
		if (x < 0 || x >= parent.getWidth() || y < 0 || y >= parent.getHeight()) {
			return null;
		} else {
			return data[y][x];
		}
	}
	
	/**
	 * Performs an equality check in the array using wildcards. Makes use of
	 * bounds checking.
	 * @param	data			The data to operate on
	 * @param	value			The value to check for
	 * @param	x				The x-coord to look up (in tiles)
	 * @param	y				The y-coord to look up (in tiles)
	 * @return					True if the values are equivalent
	 */
	public boolean isType(TileType[][] data, TileType value, int x, int y) {
		if (x < 0 || x >= parent.getWidth() || y < 0 || y >= parent.getHeight()) {
			return (value == TileType.ANY_CEILING) || (value == TileType.CEILING_TOP);
		} else if (value == TileType.ANY_WALL) {
			return walls.contains(get(data, x, y));
		} else if (value == TileType.ANY_MIDDLE_WALL) {
			return middleWalls.contains(get(data, x, y));
		} else if (value == TileType.ANY_CEILING) {
			return ceilings.contains(get(data, x, y));
		} else {
			return isValue(data, value, x, y);
		}
	}
	
	/**
	 * Checks if the given type matches at that location in the array. Not a
	 * straight lookup due to bounds checking. Also has a special case for walls
	 * if the wall wildcard is given.
	 * @param	data			The data to check
	 * @param	value			The object to check equality with
	 * @param	x				The col to check
	 * @param	y				The row to check
	 * @return					True if the object at that location matches
	 */
	public boolean isTile(Tile[][] data, TileType value, int x, int y) {
		if (value == TileType.ANY_WALL) {
			for (TileType type : walls) {
				if (isValue(data, getTile(type), x, y)) return true;
			}
			return false;
		} else if (value == TileType.ANY_MIDDLE_WALL) {
			for (TileType type : middleWalls) {
				if (isValue(data, getTile(type), x, y)) return true;
			}
			return false;
		} else if (value == TileType.ANY_CEILING) {
			for (TileType type : ceilings) {
				if (isValue(data, getTile(type), x, y)) return true;
			}
			return false;
		} else {
			return isValue(data, getTile(value), x, y);
		}
	}
	
	/**
	 * Fills a tile array with tiles by type from the passed data. This assumes
	 * that the tile array has already been initialized and the type array is
	 * completely full of tile types. It also assumes the MDO has at least one
	 * tile per tile type.
	 * @param	types			The types that we should read from
	 * @param	decoratorRef	The types that decorators should read from
	 * @param	tiles			The tiles to assign to
	 */
	public void convertTiles(TileType[][] types, Tile[][] decoratorRef, Tile[][] tiles) {
		for (int x = 0; x < parent.getWidth(); x += 1) {
			for (int y = 0; y < parent.getHeight(); y += 1) {
				tiles[y][x] = getTile(types[y][x]);
			}
		}
		for (Decorator d : decorators) {
			d.apply(decoratorRef == null ? tiles : decoratorRef, tiles);
		}
	}
	
	/**
	 * Looks up the teleport event associated with a given map ID, or null if no
	 * staircase here leads to that location.
	 * @param	mapID			The ID of the map to look up for
	 * @return					The teleport event associated to travel there
	 */
	public TeleportEvent getTeleFor(String mapID) {
		return stairTeles.get(mapID);
	}
	
	/**
	 * The actual bulk of generation should occur here.
	 */
	protected abstract void generateInternal();
	
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
		if (state != ConversionState.GENERATING) {
			MGlobal.reporter.warn("Bad order wall generation for " + parent);
		} else {
			state = ConversionState.WALLS;
		}
		for (int x = 0; x < parent.getWidth(); x += 1) {
			for (int y = 0; y < parent.getHeight(); y += 1) {
				if (isPassable(types, x, y-1) &&
						isValue(types, TileType.ANY_CEILING, x, y) &&
						isValue(types, TileType.ANY_CEILING, x, y+1) &&
						isValue(types, TileType.ANY_CEILING, x, y+2)) {
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
	 * Converts from a one-ceiling to a two-ceiling system, like with walls.
	 */
	protected void applyCeilings(TileType[][] types) {
		if (state != ConversionState.WALLS) {
			MGlobal.reporter.warn("Bad order ceil generation for " + parent);
		} else {
			state = ConversionState.CEILING;
		}
		for (int x = 0; x < parent.getWidth(); x += 1) {
			for (int y = 0; y < parent.getHeight(); y += 1) {
				if (isValue(types, TileType.ANY_CEILING, x, y)) {
					if (isValue(types, TileType.CEILING_TOP, x, y-1)) {
						types[y][x] = TileType.CEILING_TOP;
					} else if (isValue(types, TileType.CEILING_MIDDLE, x, y-1)) {
						types[y][x] = TileType.CEILING_TOP;
					} else if (isValue(types, TileType.CEILING_BOTTOM, x, y-1)) {
						types[y][x] = TileType.CEILING_MIDDLE;
					} else if (y == 0) {
						types[y][x] = TileType.CEILING_TOP;
					} else {
						types[y][x] = TileType.CEILING_BOTTOM;
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
				if (isValue(types, TileType.ANY_CEILING, x, y) && (
						(isPassable(types, x, y-1) && isPassable(types, x, y+1)) ||
						(isPassable(types, x, y-1) && isPassable(types, x, y+2)) ||
						(isPassable(types, x, y-2) && isPassable(types, x, y+1)))) {
					types[y][x] = TileType.FLOOR;
				}
			}
		}
	}
	
	/**
	 * Adds some random upstaircase in a physically correct manner. Call this
	 * after walls are generated. Returns null if not possible to generate a
	 * staircase (this should never happen, statistically, but it's hard to
	 * guarantee)
	 * @param	types			The data to operate on
	 * @return					Where the staircase was placed (connecting tile)
	 */
	protected Loc addUpstairs(TileType[][] types) {
		if (state == ConversionState.GENERATING) {
			MGlobal.reporter.warn("Bad conversion state stair for " + parent);
			return null;
		}
		int off = r.nextInt(parent.getWidth());
		for (int ix = 0; ix <= parent.getWidth(); ix += 1) {
			int x = (off + ix) % parent.getWidth();
			for (int y = parent.getHeight()-3; y > 0; y -= 1) {
				if (!isType(types, TileType.FLOOR, x, y-1)) continue;
				if (!isType(types, TileType.ANY_WALL, x, y)) continue;
				if (!isType(types, TileType.ANY_WALL, x, y+1)) continue;
				if (!isType(types, TileType.ANY_CEILING, x, y+2)) continue;
				
				if (isPassable(types, x+1, y)) continue;
				if (isPassable(types, x-1, y)) continue;
				if (isPassable(types, x+1, y+1)) continue;
				if (isPassable(types, x-1, y+1)) continue;
				if (isPassable(types, x+1, y+2)) continue;
				if (isPassable(types, x-1, y+2)) continue;
				
				// cool!
				types[y][x] = TileType.USTAIR_BOTTOM;
				types[y+1][x] = TileType.USTAIR_TOP;
				return new Loc(x, y);
			}
		}
		return null;
	}
	
	/**
	 * Adds some random downstaircase in a physically correct manner. Call this
	 * after walls are generated. Returns null if not possible to generate a
	 * staircase (this should never happen, statistically, but it's hard to
	 * guarantee)
	 * @param	types			The data to operate on
	 * @return					Where the staircase was placed (connecting tile)
	 */
	protected Loc addDownstairs(TileType[][] types) {
		if (state == ConversionState.GENERATING) {
			MGlobal.reporter.warn("Bad conversion state stair for " + parent);
			return null;
		}
		while (true) {
			int x = r.nextInt(parent.getWidth()-2) + 1;
			for (int y = 2; y < parent.getHeight(); y += 1) {
				if (!isType(types, TileType.FLOOR, x, y+1)) continue;
				if (!isType(types, TileType.ANY_CEILING, x, y)) continue;
				if (!isType(types, TileType.ANY_CEILING, x, y-1)) continue;
				if (!isType(types, TileType.ANY_CEILING, x, y-2)) continue;
				
				if (isPassable(types, x+1, y)) continue;
				if (isPassable(types, x-1, y)) continue;
				if (isPassable(types, x+1, y-1)) continue;
				if (isPassable(types, x-1, y-1)) continue;
				if (isPassable(types, x+1, y-2)) continue;
				if (isPassable(types, x-1, y-2)) continue;
				if (isPassable(types, x+1, y-3)) continue;
				if (isPassable(types, x-1, y-3)) continue;
				if (isPassable(types, x+1, y-4)) continue;
				if (isPassable(types, x-1, y-4)) continue;
				
				// cool!
				types[y][x] = TileType.DSTAIR_TOP;
				types[y-1][x] = TileType.DSTAIR_BOTTOM;
				return new Loc(x, y);
			}
		}
	}
	
	/**
	 * Adds staircases based on the parent map's connections.
	 * @param	types			The tile types to operate on
	 */
	protected void addStaircases(TileType[][] types) {
		if (state == ConversionState.GENERATING) {
			MGlobal.reporter.warn("Bad conversion state stair for " + parent);
		}
		for (String upKey : parent.getUpKeys()) {
			Loc loc = addUpstairs(types);
			TeleportEvent tele = new TeleportEvent(parent, upKey);
			parent.addEvent(tele, loc.x, loc.y);
			parent.setTeleInLoc(upKey, loc);
			stairTeles.put(upKey, tele);
		}
		for (String downKey : parent.getDownKeys()) {
			Loc loc = addDownstairs(types);
			TeleportEvent tele = new TeleportEvent(parent, downKey);
			parent.addEvent(tele, loc.x, loc.y);
			parent.setTeleInLoc(downKey, loc);
			stairTeles.put(downKey, tele);
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
	 * Assigns tiles to a finished array of types and adds it to the map. If no
	 * decorator reference is given, will use own output instead.
	 * @param	types			The tile types for the layer
	 * @param	decoratorRef	The types decorators should read from
	 * @param	z				The z-depth of the layer
	 */
	protected void addLayer(TileType[][] types, Tile[][] decoratorRef, float z) {
		Tile[][] tiles = new Tile[parent.getHeight()][parent.getWidth()];
		convertTiles(types, decoratorRef, tiles);
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
	
	/**
	 * Generates a door and returns it for the subclass's placement.
	 * @return					The generated door
	 */
	protected DoorEvent genDoor() {
		DoorEvent door = new DoorEvent(MGlobal.data.getEntryFor(mdo.doors, DoorSetMDO.class), parent);
		door.queueRequiredAssets(MGlobal.assetManager);
		door.postProcessing(MGlobal.assetManager, 0);
		return door;
	}

}
