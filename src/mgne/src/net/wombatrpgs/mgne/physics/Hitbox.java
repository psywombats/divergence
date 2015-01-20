/**
 *  Hitbox.java
 *  Created on Nov 28, 2012 11:13:57 PM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mgne.physics;

import net.wombatrpgs.mgne.core.MGlobal;
import net.wombatrpgs.mgne.maps.Positionable;

/**
 * Something that can collide and be possessed by physical objects. Isn't it
 * wonderful?
 */
public abstract class Hitbox {
	
	protected Positionable parent;
	
	/**
	 * Constructs a new hitbox at the given location of the parent.
	 * @param parent
	 */
	public Hitbox(Positionable parent) {
		this.parent = parent;
	}
	
	/** @return the x-coord (in pixels) where this hitbox begins */
	public float getX() { return parent.getX(); }
	
	/** @return the y-coord (in pixels) where this hitbox begins */
	public float getY() { return parent.getY(); }
	
	/** @return parent The new source for offsets on this hitbox */
	public void setParent(Positionable parent) { this.parent = parent; }
	
	/**
	 * Calculate the polygon defining a shadow cast from the given point against
	 * this hitbox.
	 * @param	sourceX			The x-coord of the originating light
	 * @param	sourceY			The y-coord of the originating light
	 * @param lightRadius TODO
	 * @return
	 */
	public abstract ShadowResult shadowcast(float sourceX, float sourceY, float lightRadius);
	
	/**
	 * Gets the width of this hitbox, that is, the distance between the point
	 * farthest to the left and the point farthest to the right.
	 * @return					The width of this hitbox
	 */
	public abstract float getWidth();
	
	/**
	 * Gets the height of this hitbox, that is, the distance between the point
	 * farthest to the top and the point farthest to the bottom.
	 * @return					The height of this hitbox
	 */
	public abstract float getHeight();
	
	/**
	 * Decide if this hitbox collides with the other hitbox via any means
	 * necessary. This should most likely be heavily optimized, as every event
	 * on the map will be spewing these at each other. And in an exponential
	 * fashion.
	 * @param 	other			The shape we *may* be colliding with
	 * @return					The result of the collision test
	 */
	public abstract CollisionResult isColliding(Hitbox other);
	
	/**
	 * Double-dispatch: collision detection for rectangles.
	 * @param 	other			The rect to check against
	 * @return					The result of the collision test
	 */
	public abstract CollisionResult isCollidingRect(RectHitbox other);
	
	/**
	 * Clones this specific hitbox... I've never had much luck with this in
	 * Java, to be honest.
	 * @return					A field-for-field copy of this hitbox
	 */
	public Hitbox duplicate() {
		try {
			return (Hitbox) this.clone();
		} catch (CloneNotSupportedException e) {
			MGlobal.reporter.err("Cloning fuckup?", e);
			return null;
		}
	}

}
