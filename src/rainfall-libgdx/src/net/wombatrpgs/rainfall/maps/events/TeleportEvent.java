/**
 *  TeleportEvent.java
 *  Created on Dec 24, 2012 2:00:17 AM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.rainfall.maps.events;

import com.badlogic.gdx.graphics.g2d.tiled.TiledMap;
import com.badlogic.gdx.graphics.g2d.tiled.TiledObject;

import net.wombatrpgs.rainfall.collisions.CollisionResult;
import net.wombatrpgs.rainfall.collisions.Hitbox;
import net.wombatrpgs.rainfall.collisions.RectHitbox;
import net.wombatrpgs.rainfall.core.RGlobal;
import net.wombatrpgs.rainfall.maps.Level;
import net.wombatrpgs.rainfall.maps.MapObject;

/**
 * Constructs a teleportation device! (or event, just depends on your
 * perspective...)
 */
public class TeleportEvent extends MapEvent {
	
	protected Hitbox box;
	protected String mapID;
	protected int targetX;
	protected int targetY;
	
	protected static final String PROPERTY_X = "x";
	protected static final String PROPERTY_Y = "y";
	protected static final String PROPERTY_ID = "id";

	/**
	 * Creates a new teleport for the supplied parent level using coordinates
	 * inferred from the tiled object.
	 * @param 	parent		The parent levelt to make teleport for
	 * @param 	object		The object to infer coords from
	 */
	protected TeleportEvent(Level parent, TiledObject object) {
		super(parent, object);
		TiledMap map = parent.getMap();
		box = new RectHitbox(this, 0, -map.tileHeight, map.tileWidth, 0);
		
		// TODO: center the hero
		this.targetX = Integer.valueOf(object.properties.get(PROPERTY_X));
		this.targetY = Integer.valueOf(object.properties.get(PROPERTY_Y));
		this.mapID = object.properties.get(PROPERTY_ID);
	}

	/**
	 * @see net.wombatrpgs.rainfall.maps.events.MapEvent#getHitbox()
	 */
	@Override
	public Hitbox getHitbox() {
		return box;
	}

	/**
	 * @see net.wombatrpgs.rainfall.maps.events.MapEvent#onCollide
	 * (net.wombatrpgs.rainfall.maps.MapObject, 
	 * net.wombatrpgs.rainfall.collisions.CollisionResult)
	 */
	@Override
	public void onCollide(MapObject other, CollisionResult result) {
		super.onCollide(other, result);
		parent.teleportOff();
		Level map = RGlobal.levelManager.getLevel(mapID);
		map.teleportOn(targetX, map.getHeight() - targetY - 1);
	}
	
}
