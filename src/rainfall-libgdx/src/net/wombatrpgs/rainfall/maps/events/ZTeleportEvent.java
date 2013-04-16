/**
 *  ZTeleport.java
 *  Created on Jan 4, 2013 5:20:10 PM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.rainfall.maps.events;

import com.badlogic.gdx.maps.MapObject;

import net.wombatrpgs.rainfall.maps.Level;
import net.wombatrpgs.rainfall.maps.layers.EventLayer;
import net.wombatrpgs.rainfall.physics.CollisionResult;
import net.wombatrpgs.rainfall.physics.Hitbox;
import net.wombatrpgs.rainfall.physics.RectHitbox;

/**
 * A teleport from one z-layer of a map to another. Mappers place these things
 * on maps in the form of 1*2 tile events.
 */
public class ZTeleportEvent extends MapEvent {
	
	protected Hitbox box;
	protected int lowerIndex;
	
	/**
	 * Constructs a new z-teleport event. Called by the superclass factory
	 * method.
	 * @param 	parent			The parent level of this event
	 * @param 	object			The tiled object that this object is based on
	 * @param	lowerIndex		The lower z-index of this pair of events
	 */
	protected ZTeleportEvent(Level parent, MapObject object, int lowerIndex) {
		super(parent, object, false, true);
		float h = -1 * extractHeight(parent, object);
		this.box = new RectHitbox(this, 0, h, extractHeight(parent, object), 0);
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
	 * (net.wombatrpgs.rainfall.maps.MapThing, 
	 * net.wombatrpgs.rainfall.physics.CollisionResult)
	 */
	@Override
	public boolean onCollide(MapEvent other, CollisionResult result) {
		int newZ;
		if (parent.getZ(this) == lowerIndex) {
			if (below(other)) return true;
			newZ = lowerIndex + 1;
			other.setY(other.getY()+1);
		} else {
			if (above(other)) return true;
			newZ = lowerIndex;
			other.setY(other.getY()-1);
		}
		other.changeZ(newZ);
		return true;
	}
	
	/**
	 * @see net.wombatrpgs.rainfall.maps.MapThing#onAdd
	 * (net.wombatrpgs.rainfall.maps.layers.EventLayer)
	 */
	@Override
	public void onAdd(EventLayer layer) {
		super.onAdd(layer);
		layer.setPassable(
				(int) Math.round(x/parent.getTileWidth()), 
				(int) Math.round(y/parent.getTileHeight()-2));
		layer.setPassable(
				(int) Math.round(x/parent.getTileWidth()), 
				(int) Math.round(y/parent.getTileHeight()-1));
	}

	/**
	 * @see net.wombatrpgs.rainfall.maps.events.MapEvent#reset()
	 */
	@Override
	public void reset() {
		// aw hell no
	}

	/** @return true if other is in upper quadrant */
	private boolean above(MapEvent other) {
		float heroY = other.getY();
		float ourY = getY();
		float ourMid = ourY - parent.getTileHeight() - 15;
		return heroY > ourMid;
	}
	
	/** @return true if other is in lower quadrant */
	private boolean below(MapEvent other) {
		return !above(other);
	}

}
