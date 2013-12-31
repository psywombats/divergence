/**
 *  TileLayer.java
 *  Created on Nov 29, 2012 3:51:55 PM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mrogue.maps.layers;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;

import net.wombatrpgs.mrogue.core.MGlobal;
import net.wombatrpgs.mrogue.maps.Level;
import net.wombatrpgs.mrogue.maps.Tile;
import net.wombatrpgs.mrogue.maps.events.MapEvent;
import net.wombatrpgs.mrogue.screen.TrackerCam;

/**
 * A layer of tiles that is part of a level. It's named "grid" so as to not
 * conflict with the stubby libgdx idea of a TiledLayer which isn't a layer at
 * all, really.
 */
public class GridLayer extends Layer {
	
	protected Level parent;
	protected boolean isLower;
	protected Tile[][] tileData;
	protected float z;
	
	/**
	 * Creates a new object layer with a parent level and group of objects.
	 * Expects to be mutilated and pretty much bossed around by a map generator.
	 * @param 	parent			The parent level of the layer
	 * @param	tileData		The actual info about tiles on this layer
	 * @param	z				The z-depth of this layer
	 */
	public GridLayer(Level parent, Tile[][] tileData, float z) {
		this.z = z;
		this.parent = parent;
		this.tileData = tileData;
		this.isLower = Math.floor(z) == z;
	}
	
	/**
	 * Gets the z-value of the layer. Layers with the same z-value share
	 * collisions and collision detection. 0 represents the floor, and each
	 * subsequent integer is another floor.
	 * @return					The z-value (depth) of this layer
	 */
	public float getZ() {
		return z;
	}

	/**
	 * @see net.wombatrpgs.mrogue.maps.layers.Layer#render
	 * (com.badlogic.gdx.graphics.OrthographicCamera)
	 */
	@Override
	public void render(OrthographicCamera camera) {
		dumbRender(camera);
	}

	/**
	 * @see net.wombatrpgs.mrogue.graphics.Renderable#queueRequiredAssets
	 * (com.badlogic.gdx.assets.AssetManager)
	 */
	@Override
	public void queueRequiredAssets(AssetManager manager) {
		// TODO: queueRequiredAssets
	}

	/**
	 * @see net.wombatrpgs.mrogue.graphics.Renderable#postProcessing
	 * (com.badlogic.gdx.assets.AssetManager, int)
	 */
	@Override
	public void postProcessing(AssetManager manager, int pass) {
		// TODO: postProcessing
	}
	
	/**
	 * @see net.wombatrpgs.mrogue.maps.layers.Layer#isLowerChip()
	 */
	@Override
	public boolean isLowerChip() {
		return isLower;
	}

	/**
	 * @see net.wombatrpgs.mrogue.maps.layers.Layer#isPassable(MapEvent, int, int)
	 */
	@Override
	public boolean isPassable(MapEvent actor, final int x, final int y) {
		return	(x >= 0 && x < parent.getWidth()) &&
				(y >= 0 && y < parent.getHeight()) &&
				(tileData[y][x] == null || tileData[y][x].isPassable());
	}
	
	/**
	 * Checks if a tile at the given location is see-through. Does not check
	 * for out of bounds.
	 * @param	tileX			The x-coord of the tile to check (in tiles)
	 * @param	tileY			The y-coord of the tile to check (in tiles)
	 * @return					True if tile is transparent, false otherwise
	 */
	public boolean isTransparentAt(int tileX, int tileY) {
		return tileData[tileY][tileX] == null ||
				tileData[tileY][tileX].isTransparent();
	}
	
	/**
	 * Does an extremely inefficient rendering pass.
	 * @param	cam				The camera to render with
	 */
	protected void dumbRender(OrthographicCamera camera) {
		TrackerCam cam  = MGlobal.screens.peek().getCamera();
		int startX = (int) Math.floor((cam.position.x - MGlobal.window.getWidth()/2.f) / parent.getTileWidth());
		int startY = (int) Math.floor((cam.position.y - MGlobal.window.getHeight()/2.f) / parent.getTileHeight());
		int endX = (int) Math.ceil((cam.position.x + MGlobal.window.getWidth()/2.f) / parent.getTileWidth());
		int endY = (int) Math.ceil((cam.position.y + MGlobal.window.getHeight()/2.f) / parent.getTileHeight());
		if (startX < 0) startX = 0;
		if (startY < 0) startY = 0;
		if (endX > parent.getWidth()) endX = parent.getWidth();
		if (endY > parent.getHeight()) endY = parent.getHeight();
		boolean shaders = MGlobal.graphics.isShaderEnabled();
		Color old = parent.getBatch().getColor().cpy();
		Color trans = parent.getBatch().getColor().cpy();
		trans.a = .5f;
		parent.getBatch().begin();
		for (int x = startX; x < endX; x += 1) {
			for (int y = startY; y < endY; y += 1) {
				float atX = parent.getTileWidth() * x;
				float atY = parent.getTileHeight() * y;
				if (tileData[y][x] != null) {
					if (shaders) {
						tileData[y][x].renderLocal(camera, parent.getBatch(), atX, atY);
					} else {
						if (MGlobal.hero.inLoS(x, y)) {
							tileData[y][x].renderLocal(camera, parent.getBatch(), atX, atY);
						} else if (MGlobal.hero.seen(x, y)) {
							parent.getBatch().setColor(trans);
							tileData[y][x].renderLocal(camera, parent.getBatch(), atX, atY);
							parent.getBatch().setColor(old);
						}
					}
				}
			}
		}
		parent.getBatch().end();
		parent.getBatch().setColor(old);
	}
}
