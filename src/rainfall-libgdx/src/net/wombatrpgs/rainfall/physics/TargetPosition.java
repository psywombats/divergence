/**
 *  TargetPosition.java
 *  Created on Jan 10, 2013 5:29:36 PM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.rainfall.physics;

import net.wombatrpgs.rainfall.maps.Positionable;

/**
 * A Positionable that can have its coords changed around easily enough.
 */
public class TargetPosition implements Positionable {
	
	protected float x, y;
	
	/**
	 * Creates a new target position centered at the provided coords.
	 * @param 	x			The initial x-coord of the position
	 * @param 	y			The initial y-coord of the position
	 */
	public TargetPosition(float x, float y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * @see net.wombatrpgs.rainfall.maps.Positionable#getX()
	 */
	@Override
	public float getX() {
		return x;
	}

	/**
	 * @see net.wombatrpgs.rainfall.maps.Positionable#getY()
	 */
	@Override
	public float getY() {
		return y;
	}
	
	/** @param x The new x-coord of this target */
	public void setX(float x) { this.x = x; }
	
	/** @param x The new x-coord of this target */
	public void setY(float y) { this.y = y; }

}
