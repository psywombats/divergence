/**
 *  CollisionResult.java
 *  Created on Nov 29, 2012 2:38:36 AM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.rainfall.physics;

/**
 * Struct to hold results of a collision.
 */
public class CollisionResult {
	
	/** first object in collidion, the one the mtv applies to */
	public Hitbox collide1;
	/** second object in the collision */
	public Hitbox collide2;
	/** whether the two actually collided */
	public boolean isColliding;
	/** minimum translation vector x */
	public int mtvX;
	/** minimum translation vector y; */
	public int mtvY;
	
	/** a good dummy false */
	public static final CollisionResult falseResult = new CollisionResult(false, 0, 0);
	
	/**
	 * Sets up a new empty collision result.
	 */
	public CollisionResult() {
		// tslib
	}
	
	/**
	 * Populates a new result struct. MTV is to be applied to the first object.
	 * @param 	isColliding		True if the boxes collided
	 * @param 	mtvX			The x-value of the mtv
	 * @param 	mtvY			The y-value of the mtv
	 */
	public CollisionResult(boolean isColliding, int mtvX, int mtvY) {
		this.isColliding = isColliding;
		this.mtvX = mtvX;
		this.mtvY = mtvY;
	}

}
