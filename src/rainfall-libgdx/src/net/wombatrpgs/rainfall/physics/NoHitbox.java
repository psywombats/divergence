/**
 *  NoHitbox.java
 *  Created on Nov 29, 2012 1:44:45 AM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.rainfall.physics;

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
	 * @see net.wombatrpgs.rainfall.physics.Hitbox#getWidth()
	 */
	@Override
	public float getWidth() {
		return 0;
	}

	/**
	 * @see net.wombatrpgs.rainfall.physics.Hitbox#getHeight()
	 */
	@Override
	public float getHeight() {
		return 0;
	}

	/**
	 * @see net.wombatrpgs.rainfall.physics.Hitbox#getX()
	 */
	@Override
	public float getX() {
		return 0;
	}

	/**
	 * @see net.wombatrpgs.rainfall.physics.Hitbox#getY()
	 */
	@Override
	public float getY() {
		return 0;
	}

	/**
	 * @see java.lang.Object#clone()
	 */
	@Override
	protected Object clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		return super.clone();
	}

	/**
	 * @see net.wombatrpgs.rainfall.physics.Hitbox#isColliding
	 * (net.wombatrpgs.rainfall.physics.Hitbox)
	 */
	@Override
	public CollisionResult isColliding(Hitbox other) {
		return CollisionResult.falseResult;
	}

	/**
	 * @see net.wombatrpgs.rainfall.physics.Hitbox#isCollidingRect
	 * (net.wombatrpgs.rainfall.physics.RectHitbox)
	 */
	@Override
	public CollisionResult isCollidingRect(RectHitbox other) {
		return CollisionResult.falseResult;
	}

}
