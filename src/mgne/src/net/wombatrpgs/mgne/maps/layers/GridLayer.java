/**
 *  GridLayer.java
 *  Created on Jan 8, 2014 4:06:26 PM for project saga
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mgne.maps.layers;

import net.wombatrpgs.mgne.maps.Level;
import net.wombatrpgs.mgne.maps.Positionable;
import net.wombatrpgs.mgne.maps.events.MapEvent;
import net.wombatrpgs.mgne.physics.CollisionResult;
import net.wombatrpgs.mgne.physics.Hitbox;
import net.wombatrpgs.mgne.physics.RectHitbox;

/**
 * A layer of tiles that is part of a level. It's named "grid" so as to not
 * conflict with the stubby libgdx idea of a TiledLayer which isn't a layer at
 * all, really. This is split into two halves, a generated grid layer and a
 * loaded grid layer. Generated grid layers run off a tile grid, and the loaded
 * ones just make reference to the tiled layer that spawned them. This class
 * holds their common functionality.
 */
public abstract class GridLayer extends Layer implements Comparable<GridLayer> {
	
	protected float z;
	
	/**
	 * Creates a new grid layer of any type.
	 * @param	parent			The parent level of the layer
	 * @param	z				The z-float of this layer, follows some weird
	 * 							fractional for upper chip, whole for lower chip
	 * 							rules that should really be explained somewhere	
	 */
	public GridLayer(Level parent, float z) {
		super(parent);
		this.z = z;
	}
	
	/**
	 * @see net.wombatrpgs.mgne.maps.layers.Layer#isLowerChip()
	 */
	@Override
	public boolean isLowerChip() {
		return Math.floor(z) == z;
	}
	
	/**
	 * Checks if the tile at the given location has the given property.
	 * @param	tileX			The x-coord to check (in tiles)
	 * @param	tileY			The y-coord to check (in tiles)
	 * @param	property		The property to check for
	 * @return					True if that property exists there
	 */
	public abstract boolean hasPropertyAt(int tileX, int tileY, String property);
	
	/**
	 * Fetches the terrain ID (defined in Tiled usually) of the tile located
	 * at the given coordiantes.
	 * @param	tileX			The location to check (in tiles)
	 * @param	tileY			The location to check (in tiles)
	 * @return					The terrain ID at that location
	 */
	public abstract int getTerrainAt(int tileX, int tileY);
	
	/**
	 * An easy way to keep track of properties.
	 * @param 	key				The key of the desired property
	 * @return					The value of that property
	 */
	public abstract String getProperty(String key);
	
	/**
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(GridLayer other) {
		return (int) ((getZ() * 4) - (other.getZ() * 4));
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
	 * Buffets around an event based on its overlap with this terrain
	 * @param event
	 */
	public void applyPhysicalCorrections(MapEvent event) {
		Hitbox box = event.getHitbox();
		if (box == null || (box.getX() == 0 && box.getY() == 0)) return; // why?
		int atX1 = (int) Math.floor((float) box.getX() / (float) parent.getTileWidth());
		int atX2 = (int) Math.floor((float) (box.getX() + box.getWidth()) / (float) parent.getTileWidth());
		int atY1 = (int) Math.floor((float) box.getY() / (float) parent.getTileHeight());
		int atY2 = (int) Math.floor((float) (box.getY() + box.getHeight()) / (float) parent.getTileHeight());
		for (int atX = atX1; atX <= atX2; atX++) {
			for (int atY = atY1; atY <= atY2; atY++) {
				applyCorrectionsByTile(event, atX, atY);
			}
		}
	}
	
	/**
	 * Applies physical corrections from a single tile. Essentially this
	 * determines if the hero needs to be bumped, and if it does, delegates it.
	 * @param 	event			The event to correct
	 * @param 	tileX			The x-coord of the tile (in tiles)
	 * @param 	tileY			The y-coord of the tile (in tiles)
	 */
	private void applyCorrectionsByTile(MapEvent event, int tileX, int tileY) {
		if (tileX < 0 || tileX >= parent.getWidth() || tileY < 0 || tileY >= parent.getHeight()) {
			// the hero has stepped outside the map
			bump(event, tileX, tileY);
			return;
		}
		if (!isTilePassable(tileX, tileY)) {
			bump(event, tileX, tileY);
		}
	}
	
	/**
	 * Bumps the event off the tile.
	 * @param 	event			The event that needs bumping
	 * @param 	tileX			The x-coord of the bump tile (in tiles)
	 * @param 	tileY			The y-coord of the bump tile (in tiles)
	 */
	private void bump(MapEvent event, final int tileX, final int tileY) {
		// TODO: optimize this, remove the new
		if (parent.excludeTile(this, event, tileX, tileY)) return;
		RectHitbox tileBox;
		Positionable loc = new Positionable() {
			@Override
			public float getX() { return tileX * parent.getTileWidth();}
			@Override
			public float getY() { return tileY * parent.getTileHeight(); }
		};
		tileBox = new RectHitbox(loc, 0, 0, parent.getTileWidth(), parent.getTileHeight());
		CollisionResult result = tileBox.isColliding(event.getHitbox());
		if (result.isColliding) {
			if (event.getHitbox() == result.collide2) {
				result.mtvX *= -1;
				result.mtvY *= -1;
			}
			event.resolveWallCollision(result);
		}
	}

}
