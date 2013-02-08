/**
 *  TeleportEvent.java
 *  Created on Dec 24, 2012 2:00:17 AM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.rainfall.maps.events;

import com.badlogic.gdx.graphics.g2d.tiled.TiledObject;

import net.wombatrpgs.rainfall.collisions.CollisionResult;
import net.wombatrpgs.rainfall.collisions.Hitbox;
import net.wombatrpgs.rainfall.collisions.RectHitbox;
import net.wombatrpgs.rainfall.core.RGlobal;
import net.wombatrpgs.rainfall.maps.Level;

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
	 * inferred from the tiled object. Called from the superclass's factory
	 * method.
	 * @param 	parent		The parent levelt to make teleport for
	 * @param 	object		The object to infer coords from
	 */
	public TeleportEvent(Level parent, TiledObject object) {
		super(parent, object, false, true);
		this.box = new RectHitbox(this, 0, -object.height, object.width, 0);
		
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
	 * (net.wombatrpgs.rainfall.maps.MapEvent, 
	 * net.wombatrpgs.rainfall.collisions.CollisionResult)
	 */
	@Override
	public boolean onCollide(MapEvent other, CollisionResult result) {
		if (other != RGlobal.hero) return true;
		if (RGlobal.teleport.getPre().hasExecuted()) {
			RGlobal.teleport.getPre().reset();
			int z = parent.getZ(other);
			Level old = other.getLevel();
			parent.teleportOff();
			old.update(0);
			// it's buggy, this shouldn't be necessary
			float oldR = RGlobal.screens.peek().getTint().r;
			float oldG = RGlobal.screens.peek().getTint().g;
			float oldB = RGlobal.screens.peek().getTint().b;
			RGlobal.screens.peek().getTint().r = 1;
			RGlobal.screens.peek().getTint().g = 1;
			RGlobal.screens.peek().getTint().b = 1;
			Level map = RGlobal.levelManager.getLevel(mapID);
			RGlobal.screens.peek().getTint().r = oldR;
			RGlobal.screens.peek().getTint().g = oldG;
			RGlobal.screens.peek().getTint().b = oldB;
			map.teleportOn(targetX, map.getHeight() - targetY - 1, z);
			map.addObject(RGlobal.teleport.getPost());
			RGlobal.teleport.getPost().run(map);
		} else {
			if (!parent.getObjects().contains(RGlobal.teleport.getPre())) {
				parent.addObject(RGlobal.teleport.getPre());
				RGlobal.teleport.getPre().run(parent);
			}
		}
		return true;
	}
	
}
