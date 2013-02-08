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
import com.badlogic.gdx.graphics.g2d.tiled.TiledLayer;
import com.badlogic.gdx.graphics.g2d.tiled.TiledMap;

import net.wombatrpgs.rainfall.collisions.CollisionResult;
import net.wombatrpgs.rainfall.collisions.FallResult;
import net.wombatrpgs.rainfall.collisions.Hitbox;
import net.wombatrpgs.rainfall.collisions.RectHitbox;
import net.wombatrpgs.rainfall.core.RGlobal;
import net.wombatrpgs.rainfall.maps.Level;
import net.wombatrpgs.rainfall.maps.Positionable;
import net.wombatrpgs.rainfall.maps.events.MapEvent;

/**
 * A layer of tiles that is part of a level. It's named "grid" so as to not
 * conflict with the stubby libgdx idea of a TiledLayer which isn't a layer at
 * all, really.
 */
public class GridLayer extends Layer {
	
	public static final String PROPERTY_IMPASSABLE = "x";
	public static final String PROPERTY_CLIFFTOP = "top";
	public static final String PROPERTY_ABYSS = "hole";
	
	protected TiledMap map;
	protected Level parent;
	protected TiledLayer layer;
	protected List<Hitbox> passOverrides;
	protected boolean passability[][];
	protected int layerID;
	
	/**
	 * Creates a new object layer with a parent level and group of objects.
	 * @param 	parent		The parent level of the layer
	 * @param 	layer		The underlying layer of this abstraction
	 * @param	layerID		The numeric identifier of the underlying layer
	 */
	public GridLayer(Level parent, TiledLayer layer, int layerID) {
		this.parent = parent;
		this.layer = layer;
		this.layerID = layerID;
		this.map = parent.getMap();
		this.passOverrides = new ArrayList<Hitbox>();
	}
	
	/**
	 * Gets the underlying tiles of the tiled layer.
	 * @return				The underlying tiles
	 */
	public int[][] getTiles() {
		return layer.tiles;
	}
	
	/**
	 * @see net.wombatrpgs.rainfall.maps.layers.Layer#getZ()
	 */
	@Override
	public float getZ() {
		if (layer.properties.containsKey("z")) {
			return Float.valueOf(layer.properties.get("z"));
		} else {
			RGlobal.reporter.warn("Layer with no z-value on " + parent);
			return 0;
		}
	}

	/**
	 * @see net.wombatrpgs.rainfall.maps.layers.Layer#render
	 * (com.badlogic.gdx.graphics.OrthographicCamera, int)
	 */
	@Override
	public void render(OrthographicCamera camera, int z) {
		if ((int) Math.floor(getZ()) == z) {
			parent.getRenderer().render(camera, new int[] {layerID});
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
		int atX1 = (int) Math.floor((float) box.getX() / (float) map.tileWidth);
		int atX2 = (int) Math.floor((float) (box.getX() + box.getWidth()) / (float) map.tileWidth);
		int atY1 = (int) Math.floor((float) box.getY() / (float) map.tileHeight);
		int atY2 = (int) Math.floor((float) (box.getY() + box.getHeight()) / (float) map.tileHeight);
		for (int atX = atX1; atX <= atX2; atX++) {
			for (int atY = atY1; atY <= atY2; atY++) {
				applyCorrectionsByTile(event, atX, atY, safeRect);
			}
		}
		checkForHoles(event,
				(int) Math.round(((float) event.getX()) / map.tileWidth),
				(int) Math.round(((float) event.getY()) / map.tileHeight));
	}
	
	/**
	 * @see net.wombatrpgs.rainfall.maps.layers.Layer#isLowerChip()
	 */
	@Override
	public boolean isLowerChip() {
		float z = Float.valueOf(layer.properties.get("z"));
		return Math.floor(z) == z;
	}
	
	/**
	 * @see net.wombatrpgs.rainfall.maps.layers.Layer#finalizePassability()
	 */
	@Override
	public void finalizePassability() {
		passability = new boolean[map.height][map.width];
		for (int x = 0; x < map.width; x++) {
			for (int y = 0; y < map.height; y++) {
				int tileID = layer.tiles[map.height-y-1][x];
				if (tileID == 0) {
					// there is no tile at this location
					if (isLowerChip()) {
						// there is no tile and we are the bottom
						passability[y][x] = passableByUpper(x, y);
					} else {
						passability[y][x] = true;
					}
				} else if (map.getTileProperty(tileID, PROPERTY_IMPASSABLE) != null ||
						map.getTileProperty(tileID, PROPERTY_CLIFFTOP) != null) {
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
	 * (net.wombatrpgs.rainfall.collisions.Hitbox)
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
		if (hiX >= map.width) hiX = map.width - 1;
		if (hiY >= map.height) hiY = map.height - 1;
		for (int y = loY; y <= hiY; y++) {
			for (int x = loX; x <= hiX; x++) {
				int tileID = layer.tiles[map.height-y-1][x];
				if (tileID == 0) continue;
				result.finished = true;
				result.z = (int) Math.floor(getZ());
				if (map.getTileProperty(tileID, PROPERTY_IMPASSABLE) != null ||
					map.getTileProperty(tileID, PROPERTY_CLIFFTOP) != null) {
					result.cleanLanding = false;
				}
				break;
			}
		}
		return result;
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
		if (tileX < 0 || tileX >= map.width || tileY < 0 || tileY >= map.height) {
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
		if (tileX < 0 || tileX >= map.width || tileY < 0 || tileY >= map.height) {
			cliff = false;
		} else {
			int tileID = layer.tiles[map.height-tileY-1][tileX];
			cliff = map.getTileProperty(tileID, PROPERTY_CLIFFTOP) != null;
		}
		Positionable loc = new Positionable() {
			@Override
			public int getX() { return tileX * map.tileWidth;}
			@Override
			public int getY() { 
				if (cliff) return tileY * map.tileHeight - map.tileHeight/2;
				else return tileY * map.tileHeight; 
			}
		};
		if (cliff) {
			tileBox = new RectHitbox(loc, 0, map.tileHeight/2, map.tileWidth, map.tileHeight);
		} else {
			tileBox = new RectHitbox(loc, 0, 0, map.tileWidth, map.tileHeight);
		}
		if (safe == null) {
			CollisionResult result = tileBox.isColliding(event.getHitbox());
			if (result.isColliding) {
				if (event.getHitbox() == result.collide2) {
					result.mtvX *= -1;
					result.mtvY *= -1;
				}
				if (event == RGlobal.hero) {
					System.out.println();
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
					if (event == RGlobal.hero) {
						System.out.println();
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
				int tileID = otherLayer.getTiles()[map.height-tileY-1][tileX];
				if (map.getTileProperty(tileID, "passable") != null) {
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
		if (tileX < 0 || tileX >= map.width || tileY < 0 || tileY >= map.height) {
			return;
		}
		int tileID = layer.tiles[map.height-tileY-1][tileX];
		if (map.getTileProperty(tileID, PROPERTY_ABYSS) != null &&
				!passableByUpper(tileX, tileY)) {
			event.fallIntoHole(tileX, tileY);
		}
	}
}
