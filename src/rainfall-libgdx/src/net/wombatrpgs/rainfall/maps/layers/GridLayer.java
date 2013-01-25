/**
 *  TileLayer.java
 *  Created on Nov 29, 2012 3:51:55 PM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.rainfall.maps.layers;


import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.tiled.TiledLayer;
import com.badlogic.gdx.graphics.g2d.tiled.TiledMap;

import net.wombatrpgs.mgne.global.Global;
import net.wombatrpgs.rainfall.collisions.CollisionResult;
import net.wombatrpgs.rainfall.collisions.FallResult;
import net.wombatrpgs.rainfall.collisions.Hitbox;
import net.wombatrpgs.rainfall.collisions.RectHitbox;
import net.wombatrpgs.rainfall.graphics.Renderable;
import net.wombatrpgs.rainfall.maps.Level;
import net.wombatrpgs.rainfall.maps.Positionable;
import net.wombatrpgs.rainfall.maps.events.MapEvent;

/**
 * A layer of tiles that is part of a level. It's named "grid" so as to not
 * conflict with the stubby libgdx idea of a TiledLayer which isn't a layer at
 * all, really.
 */
public class GridLayer extends Layer implements Renderable {
	
	public static String PROPERTY_IMPASSABLE = "x";
	
	protected TiledMap map;
	protected Level parent;
	protected TiledLayer layer;
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
			Global.reporter.warn("Layer with no z-value on " + parent);
			return 0;
		}
	}

	/**
	 * @see net.wombatrpgs.rainfall.graphics.Renderable#render
	 * (com.badlogic.gdx.graphics.OrthographicCamera)
	 */
	@Override
	public void render(OrthographicCamera camera) {
		parent.getRenderer().render(camera, new int[] {layerID});
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
	 * (com.badlogic.gdx.assets.AssetManager)
	 */
	@Override
	public void postProcessing(AssetManager manager) {
		// this is handled in the parent
	}

	/**
	 * @see net.wombatrpgs.rainfall.maps.layers.Layer#applyPhysicalCorrections
	 * (net.wombatrpgs.rainfall.maps.events.MapEvent)
	 */
	@Override
	public void applyPhysicalCorrections(MapEvent event) {
		int atX1 = (int) Math.floor(((float) event.getX()) / map.tileWidth);
		int atX2 = (int) Math.ceil(((float) event.getX()) / map.tileWidth);
		int atY1 = (int) Math.floor(((float) event.getY()) / map.tileHeight);
		int atY2 = (int) Math.ceil(((float) event.getY()) / map.tileHeight);
		applyCorrectionsByTile(event, atX1, atY1);
		applyCorrectionsByTile(event, atX1, atY2);
		applyCorrectionsByTile(event, atX2, atY1);
		applyCorrectionsByTile(event, atX2, atY2);
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
//		int loX = (int) Math.ceil(((float) (box.getX())) / resX);
//		int hiX = (int) Math.floor(((float) (box.getX()+box.getWidth())) / resX);
//		int loY = (int) Math.ceil(((float) (box.getY())) / resY);
//		int hiY = (int) Math.floor(((float) (box.getY()+box.getHeight())) / resY);
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
				if (map.getTileProperty(tileID, PROPERTY_IMPASSABLE) != null) {
					result.cleanLanding = false;
				}
				break;
			}
		}
		return result;
	}

	/**
	 * Applies physical corrections from a single tile. Essentially this
	 * determines if the hero needs to be bumped, and if it does, delegates it.
	 * @param 	event			The event to correct
	 * @param 	tileX			The x-coord of the tile (in tiles)
	 * @param 	tileY			The y-coord of the tile (in tiles)
	 */
	private void applyCorrectionsByTile(MapEvent event, int tileX, int tileY) {
		if (tileX < 0 || tileX >= map.width || tileY < 0 || tileY >= map.height) {
			// the hero has stepped outside the map
			bump(event, tileX, tileY);
			return;
		}
		int tileID = layer.tiles[map.height-tileY-1][tileX];
		if (tileID == 0) {
			// there is no tile at this location
			if (isLowerChip()) {
				// there is no tile and we are the bottom
				checkForUpper(event, tileX, tileY);
			}
		} else if (map.getTileProperty(tileID, PROPERTY_IMPASSABLE) != null) {
			// the tile at this location is impassable
			checkForUpper(event, tileX, tileY);
		}
	}
	
	/**
	 * Same as the bump, but only this time make sure there isn't a bridge or
	 * something on the upper chip that makes this thing passable.
	 * @param 	event			The event that may require bumping
	 * @param 	tileX			The x-coord of the bump tile (in tiles)
	 * @param 	tileY			The y-coord of the bump tile (in tiles)
	 */
	private void checkForUpper(MapEvent event, int tileX, int tileY) {
		// TODO: if this affects performance, it can be precomputed
		for (GridLayer otherLayer : parent.getGridLayers()) {
			if (Math.floor(otherLayer.getZ()) == this.getZ() && otherLayer != this) {
				int tileID = otherLayer.getTiles()[map.height-tileY-1][tileX];
				if (map.getTileProperty(tileID, "passable") != null) {
					return;
				}
			}
		}
		// TODO: move this somewhere else
		for (EventLayer otherLayer : parent.getObjectLayers()) {
			if (otherLayer.isSpecialPassable(tileX, tileY)) {
				return;
			}
		}
		// no layers contained a passable upper chip
		bump(event, tileX, tileY);
	}
	
	/**
	 * Bumps the event off the tile.
	 * @param 	event			The event that needs bumping
	 * @param 	tileX			The x-coord of the bump tile (in tiles)
	 * @param 	tileY			The y-coord of the bump tile (in tiles)
	 */
	private void bump(MapEvent event, final int tileX, final int tileY) {
		// TODO: optimize this, remove the new
		Positionable loc = new Positionable() {
			@Override
			public int getX() { return tileX * map.tileWidth;}
			@Override
			public int getY() { return tileY * map.tileHeight; }
			@Override
			public SpriteBatch getBatch() { return null; }
		};
		RectHitbox tileBox = new RectHitbox(loc, 0, 0, map.tileWidth, map.tileHeight);
		CollisionResult result = tileBox.isColliding(event.getHitbox());
		// TODO: this code is duplicated elsewhere
		if (result.isColliding) {
			if (event.getHitbox() == result.collide2) {
				result.mtvX *= -1;
				result.mtvY *= -1;
			}
			event.moveX(result.mtvX);
			event.moveY(result.mtvY);
		}
	}
	
}
