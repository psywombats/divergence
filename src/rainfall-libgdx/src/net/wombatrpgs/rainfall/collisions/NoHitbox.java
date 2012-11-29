/**
 *  NoHitbox.java
 *  Created on Nov 29, 2012 1:44:45 AM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.rainfall.collisions;

/**
 * This is not a hitbox. It's for objects with hitboxes. As such, it's
 * impossible to collide with it. Effectively a singleton as it's a bunch of
 * dummy methods and doing a new every time is sloppy.
 */
public class NoHitbox extends Hitbox {
	
	/** The one and only singleton */
	private static NoHitbox instance = new NoHitbox();
	
	/**
	 * Dummy constructor.
	 */
	public NoHitbox() {
		super(null);
	}
	
	/**
	 * Gets the singleton instance of this class
	 * @return				The one and only No Hitbox
	 */
	public static NoHitbox getInstance() {
		return instance;
	}

	/**
	 * @see net.wombatrpgs.rainfall.collisions.Hitbox#isColliding
	 * (net.wombatrpgs.rainfall.collisions.Hitbox)
	 */
	@Override
	public CollisionResult isColliding(Hitbox other) {
		return CollisionResult.falseResult;
	}

	/**
	 * @see net.wombatrpgs.rainfall.collisions.Hitbox#isCollidingRect
	 * (net.wombatrpgs.rainfall.collisions.RectHitbox)
	 */
	@Override
	public CollisionResult isCollidingRect(RectHitbox other) {
		return CollisionResult.falseResult;
	}

}
