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

import net.wombatrpgs.rainfall.collisions.CollisionResult;
import net.wombatrpgs.rainfall.collisions.RectHitbox;
import net.wombatrpgs.rainfall.graphics.Renderable;
import net.wombatrpgs.rainfall.maps.Level;
import net.wombatrpgs.rainfall.maps.MapEvent;
import net.wombatrpgs.rainfall.maps.Positionable;

/**
 * A layer of tiles that is part of a level. It's named "grid" so as to not
 * conflict with the stubby libgdx idea of a TiledLayer which isn't a layer at
 * all, really.
 */
public class GridLayer extends Layer implements Renderable {
	
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
	 * (net.wombatrpgs.rainfall.maps.MapEvent)
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
	
	public void applyCorrectionsByTile(MapEvent event, final int tileX, final int tileY) {
		if (tileX < 0 || tileX >= map.width || tileY < 0 || tileY >= map.height) return;
		int tileID = layer.tiles[map.height-tileY-1][tileX];
		if (map.getTileProperty(tileID, "impassable") != null) {
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
	
}
