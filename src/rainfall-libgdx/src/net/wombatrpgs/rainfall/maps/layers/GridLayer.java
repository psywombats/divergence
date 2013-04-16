/**
 *  TileLayer.java
 *  Created on Nov 29, 2012 3:51:55 PM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.rainfall.maps.layers;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;

import net.wombatrpgs.rainfall.core.RGlobal;
import net.wombatrpgs.rainfall.maps.Level;
import net.wombatrpgs.rainfall.maps.Positionable;
import net.wombatrpgs.rainfall.maps.events.MapEvent;
import net.wombatrpgs.rainfall.physics.CollisionResult;
import net.wombatrpgs.rainfall.physics.FallResult;
import net.wombatrpgs.rainfall.physics.Hitbox;
import net.wombatrpgs.rainfall.physics.RectHitbox;

/**
 * A layer of tiles that is part of a level. It's named "grid" so as to not
 * conflict with the stubby libgdx idea of a TiledLayer which isn't a layer at
 * all, really.
 */
public class GridLayer extends Layer {
	
	public static final String PROPERTY_PASSABLE = "o";
	public static final String PROPERTY_IMPASSABLE = "x";
	public static final String PROPERTY_CLIFFTOP = "top";
	public static final String PROPERTY_ABYSS = "hole";
	
	protected TiledMap map;
	protected Level parent;
	protected TiledMapTileLayer layer;
	protected List<Hitbox> passOverrides;
	protected boolean passability[][];
	protected int layerID;
	
	/**
	 * Creates a new object layer with a parent level and group of objects.
	 * @param 	parent			The parent level of the layer
	 * @param 	layer			The underlying layer of this abstraction
	 * @param	layerID			The numeric identifier of the underlying layer
	 */
	public GridLayer(Level parent, TiledMapTileLayer layer) {
		this.parent = parent;
		this.layer = layer;
		this.map = parent.getMap();
		this.passOverrides = new ArrayList<Hitbox>();
	}
	
	/**
	 * @see net.wombatrpgs.rainfall.maps.layers.Layer#getZ()
	 */
	@Override
	public float getZ() {
		if (getProperty(PROPERTY_Z) != null) {
			return Float.valueOf(getProperty(PROPERTY_Z));
		} else {
			RGlobal.reporter.warn("Layer with no z-value on " + parent);
			return 0;
		}
	}

	/**
	 * @see net.wombatrpgs.rainfall.maps.layers.Layer#render
	 * (com.badlogic.gdx.graphics.OrthographicCamera, float)
	 */
	@Override
	public void render(OrthographicCamera camera, float z) {
		if (getZ() == z) {
			camera.update();
			parent.getRenderer().setView(camera);
			parent.getRenderer().getSpriteBatch().begin();
			parent.getRenderer().renderTileLayer(layer);
			parent.getRenderer().getSpriteBatch().end();
		}
	}

	/**
	 * @see net.wombatrpgs.rainfall.graphics.Renderable#queueRequiredAssets
	 * (com.badlogic.gdx.assets.AssetManager)
	 */
	@Override
	public void queueRequiredAssets(AssetManager manager) {
		// this is handled in the parent
	}

	/**
	 * @see net.wombatrpgs.rainfall.graphics.Renderable#postProcessing
	 * (com.badlogic.gdx.assets.AssetManager, int)
	 */
	@Override
	public void postProcessing(AssetManager manager, int pass) {
		// this is handled in the parent
	}

	/**
	 * @see net.wombatrpgs.rainfall.maps.layers.Layer#applyPhysicalCorrections
	 * (net.wombatrpgs.rainfall.maps.events.MapEvent)
	 */
	@Override
	public void applyPhysicalCorrections(MapEvent event) {
		RectHitbox safeRect = null;
		if (event.isCollisionEnabled()) {
			for (Hitbox box : passOverrides) {
				if (event.getHitbox().isColliding(box).isColliding) {
					safeRect = (RectHitbox) box; // t(=_=) casting
				}
			}
		}
		Hitbox box = event.getHitbox();
		if (box == null || (box.getX()==0 && box.getY() == 0)) return;
		int atX1 = (int) Math.floor((float) box.getX() / parent.getTileWidth());
		int atX2 = (int) Math.floor((float) (box.getX() +
				box.getWidth()) / parent.getTileWidth());
		int atY1 = (int) Math.floor((float) box.getY() / (float) parent.getTileHeight());
		int atY2 = (int) Math.floor((float) (box.getY() + 
				box.getHeight()) / (float) parent.getTileHeight());
		for (int atX = atX1; atX <= atX2; atX++) {
			for (int atY = atY1; atY <= atY2; atY++) {
				applyCorrectionsByTile(event, atX, atY, safeRect);
			}
		}
		checkForHoles(event,
				(int) Math.round(((float) event.getX()) / parent.getTileWidth()),
				(int) Math.round(((float) event.getY()) / parent.getTileHeight()));
	}
	
	/**
	 * @see net.wombatrpgs.rainfall.maps.layers.Layer#isLowerChip()
	 */
	@Override
	public boolean isLowerChip() {
		float z = Float.valueOf(getProperty(PROPERTY_Z));
		return Math.floor(z) == z;
	}
	
	/**
	 * @see net.wombatrpgs.rainfall.maps.layers.Layer#finalizePassability()
	 */
	@Override
	public void finalizePassability() {
		passability = new boolean[parent.getHeight()][parent.getWidth()];
		for (int x = 0; x < parent.getWidth(); x++) {
			for (int y = 0; y < parent.getHeight(); y++) {
				if (getTileID(x, y) == 0) {
					// there is no tile at this location
					if (isLowerChip()) {
						// there is no tile and we are the bottom
						passability[y][x] = passableByUpper(x, y);
					} else {
						passability[y][x] = true;
					}
				} else if (getTileProperty(x, y, PROPERTY_IMPASSABLE) != null ||
						getTileProperty(x, y, PROPERTY_CLIFFTOP) != null) {
					// the tile at this location is impassable
					passability[y][x] = passableByUpper(x, y);
				} else {
					passability[y][x] = true;
				}
			}
		}
	}

	/**
	 * @see net.wombatrpgs.rainfall.maps.layers.Layer#dropObject
	 * (net.wombatrpgs.rainfall.physics.Hitbox)
	 */
	@Override
	public FallResult dropObject(Hitbox box) {
		FallResult result = new FallResult();
		result.finished = false;
		result.cleanLanding = true;
		float resX = parent.getTileWidth();
		float resY = parent.getTileHeight();
		int loX = (int) Math.round(box.getX() / resX);
		int hiX = loX;
		int loY = (int) Math.round(box.getY() / resY);
		int hiY = loY;
		if (loX < 0) loX = 0;
		if (loY < 0) loY = 0;
		if (hiX >= parent.getWidth()) hiX = parent.getWidth() - 1;
		if (hiY >= parent.getHeight()) hiY = parent.getHeight() - 1;
		for (int y = loY; y <= hiY; y++) {
			for (int x = loX; x <= hiX; x++) {
				if (getTileID(x, y) == 0) continue;
				result.finished = true;
				result.z = (int) Math.floor(getZ());
				if (getTileProperty(x, y, PROPERTY_IMPASSABLE) != null ||
						getTileProperty(x, y, PROPERTY_CLIFFTOP) != null) {
					result.cleanLanding = false;
				}
				break;
			}
		}
		return result;
	}

	/**
	 * @see net.wombatrpgs.rainfall.maps.layers.Layer#isPassable(MapEvent, int, int)
	 */
	@Override
	public boolean isPassable(MapEvent actor, final int x, final int y) {
		if (passability[y][x]) {
			return getTileProperty(x, y, PROPERTY_ABYSS) == null;
		}
		Hitbox subBox = new RectHitbox(new Positionable() {
			@Override public float getX() {return x * parent.getTileWidth(); }
			@Override public float getY() {return  y * parent.getTileHeight(); }
		},
				parent.getTileWidth()*1/4,
				parent.getTileHeight()*1/4,
				parent.getTileWidth()*3/4,
				parent.getTileHeight()*3/4);
		for (Hitbox box : passOverrides) {
			if (subBox.isColliding(box).isColliding) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Adds an override to the normal passability of this grid layer. If an
	 * event is in contact with this hitbox, it will be considered legal
	 * regardless of any other state it's in. This is useful for events that
	 * move around that perform the same function as upper chip. However, they
	 * need to be registered here. Note that the collision indicates that the
	 * colliding event must be >50% inside the box, not just touching like
	 * normal.
	 * @param 	box				The box that will override passability
	 */
	public void addPassabilityOverride(Hitbox box) {
		passOverrides.add(box);
	}
	
	/**
	 * Removes a previously added passability override
	 * @param 	box				The box of the override to remove
	 */
	public void removePassabilityOverride(Hitbox box) {
		if (passOverrides.contains(box)) {
			passOverrides.remove(box);
		} else {
			RGlobal.reporter.warn("Tried to remove a pass that wasn't here: " + box);
		}
	}


	/**
	 * Applies physical corrections from a single tile. Essentially this
	 * determines if the hero needs to be bumped, and if it does, delegates it.
	 * @param 	event			The event to correct
	 * @param 	tileX			The x-coord of the tile (in tiles)
	 * @param 	tileY			The y-coord of the tile (in tiles)
	 * @param	safe			Rect to subtract from all tile boxes
	 */
	private void applyCorrectionsByTile(MapEvent event, int tileX, int tileY, RectHitbox safe) {
		if (tileX < 0 || tileX >= parent.getWidth() || tileY < 0 || tileY >= parent.getHeight()) {
			// the hero has stepped outside the map
			bump(event, tileX, tileY, null);
			return;
		}
		if (passability[tileY][tileX] == false) {
			bump(event, tileX, tileY, safe);
		}
	}
	
	/**
	 * Bumps the event off the tile.
	 * @param 	event			The event that needs bumping
	 * @param 	tileX			The x-coord of the bump tile (in tiles)
	 * @param 	tileY			The y-coord of the bump tile (in tiles)
	 * @param	safe			A rect hitbox to subtract from tile boxes
	 */
	private void bump(MapEvent event, final int tileX, final int tileY, RectHitbox safe) {
		// TODO: optimize this, remove the new
		RectHitbox tileBox;
		final boolean cliff;
		if (tileX < 0 || tileX >= parent.getWidth() || tileY < 0 || tileY >= parent.getHeight()) {
			cliff = false;
		} else {
			cliff = getProperty(PROPERTY_CLIFFTOP) != null;
		}
		Positionable loc = new Positionable() {
			@Override
			public float getX() { return tileX * parent.getTileWidth();}
			@Override
			public float getY() { 
				if (cliff) return (tileY - .5f) * parent.getTileHeight();
				else return tileY * parent.getTileHeight(); 
			}
		};
		if (cliff) {
			tileBox = new RectHitbox(loc, 
					0,
					parent.getTileHeight()/2,
					parent.getTileWidth(),
					parent.getTileHeight());
		} else {
			tileBox = new RectHitbox(loc, 
					0,
					0,
					parent.getTileWidth(),
					parent.getTileHeight());
		}
		if (safe == null) {
			CollisionResult result = tileBox.isColliding(event.getHitbox());
			if (result.isColliding) {
				if (event.getHitbox() == result.collide2) {
					result.mtvX *= -1;
					result.mtvY *= -1;
				}
				event.resolveWallCollision(result);
			}
		} else {
			List<RectHitbox> boxes = tileBox.subtract(safe);
			for (RectHitbox box : boxes) {
				CollisionResult result = box.isColliding(event.getHitbox());
				if (result.isColliding) {
					if (event.getHitbox() == result.collide2) {
						result.mtvX *= -1;
						result.mtvY *= -1;
					}
					event.resolveWallCollision(result);
				}
			}
		}
	}
	
	/**
	 * Calculate if a tile is passable because of a special upper chip brdige.
	 * @param 	tileX			The x-coord of the tile to check (in tiles)
	 * @param 	tileY			The y-coord of the tile to check (in tiles)
	 * @return					True if passable, false if not
	 */
	private boolean passableByUpper(int tileX, int tileY) {
		for (GridLayer otherLayer : parent.getGridLayers()) {
			if (Math.floor(otherLayer.getZ()) == this.getZ() && otherLayer != this) {
				if (otherLayer.getTileProperty(tileX, tileY, PROPERTY_PASSABLE) != null) {
					return true;
				}
			}
		}
		// TODO: move this somewhere else
		for (EventLayer otherLayer : parent.getEventLayers()) {
			if (otherLayer.isSpecialPassable(tileX, tileY)) {
				return true;
			}
		}
		// no layers contained a passable upper chip
		return false;
	}
	
	/**
	 * Makes sure a map event didn't fall into any holes.
	 * @param 	event			The event to check
	 * @param 	tileX			The x-coord to check (in tiles)
	 * @param 	tileY			The y-coord to check (in tiles)
	 */
	private void checkForHoles(MapEvent event, int tileX, int tileY) {
		if (tileX < 0 || tileX >= parent.getWidth() || tileY < 0 || tileY >= parent.getHeight()) {
			return;
		}
		if (getTileProperty(tileX, tileY, PROPERTY_ABYSS) != null &&
				!passableByUpper(tileX, tileY)) {
			event.fallIntoHole(tileX, tileY);
		}
	}
	
	/**
	 * An easy way to keep track of properties.
	 * @param 	key				The key of the desired property
	 * @return					The value of that property
	 */
	protected String getProperty(String key) {
		Object val = layer.getProperties().get(key);
		return (val == null) ? null : val.toString();
	}
	
	/**
	 * Extracts the property from a tile at a given cell.
	 * @param 	tileX			The x-coord of the tile to get from (in tiles)
	 * @param 	tileY			The y-coord of the tile to get from (in tiles)
	 * @param 	key				The key of the property to extract
	 * @return					The tile's property at that cell
	 */
	protected String getTileProperty(int tileX, int tileY, String key) {
		Cell cell = layer.getCell(tileX, tileY);
		if (cell == null) return null;
		TiledMapTile tile = cell.getTile();
		Object val = tile.getProperties().get(key);
		return (val == null) ? null : val.toString();
	}
	
	/**
	 * Extracts the tile ID of a given cell.
	 * @param 	tileX			The x-coord of the tile to get from (in tiles)
	 * @param 	tileY			The y-coord of the tile to get from (in tiles)
	 * @return					The tile's ID at that cell
	 */
	protected int getTileID(int tileX, int tileY) {
		Cell cell =  layer.getCell(tileX, tileY);
		return (cell == null) ? 0 : cell.getTile().getId();
	}
}
