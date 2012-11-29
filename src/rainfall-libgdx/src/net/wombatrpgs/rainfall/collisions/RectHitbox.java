/**
 *  RectHitbox.java
 *  Created on Nov 29, 2012 1:28:40 AM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.rainfall.collisions;

import net.wombatrpgs.rainfall.maps.Positionable;

/**
 * Standard rectangular hitbox used by 99% of things in this game. It requires
 * knowing the location of its parent as all its coordinates are relative.
 */
public class RectHitbox extends Hitbox {
	
	private int x1, y1, x2, y2;
	
	/**
	 * Creates a new rectangular hitbox. All coordinates relative to the origin
	 * of the hitbox owner.
	 * @param	parent		The positionable owner of this hitbox
	 * @param 	x1			The upper left x-coord, in pixels 
	 * @param 	y1			The upper left y-coord, in pixels
	 * @param 	x2			The lower right x-coord, in pixels
	 * @param 	y2			The lower right y-coord, in pixels
	 */
	public RectHitbox(Positionable parent, int x1, int y1, int x2, int y2) {
		super(parent);
		this.x1 = x1;
		this.x2 = x2;
		this.y1 = y1;
		this.y2 = y2;
	}

	/**
	 * @see net.wombatrpgs.rainfall.collisions.Hitbox#isColliding
	 * (net.wombatrpgs.rainfall.collisions.Hitbox)
	 */
	@Override
	public CollisionResult isColliding(Hitbox other) {
		return other.isCollidingRect(this);
	}

	/**
	 * @see net.wombatrpgs.rainfall.collisions.Hitbox#isCollidingRect
	 * (net.wombatrpgs.rainfall.collisions.RectHitbox)
	 */
	@Override
	public CollisionResult isCollidingRect(RectHitbox other) {
		// TODO: optimization would avoid new-ing this object
		CollisionResult result = new CollisionResult();
		result.collide1 = this;
		result.collide2 = other;
		int ax1 = this.x1 + this.parent.getX();
		int ay1 = this.y1 + this.parent.getY();
		int ax2 = this.x2 + this.parent.getX();
		int ay2 = this.y2 + this.parent.getY();
		int bx1 = other.x1 + other.parent.getX();
		int by1 = other.y1 + other.parent.getY();
		int bx2 = other.x2 + other.parent.getX();
		int by2 = other.y2 + other.parent.getY();
		// if you switch to float, change > to >=
		boolean colliding =  !((ax1 >= bx2) || (bx1 >= ax2) || (ay1 >= by2) || (by1 >= ay2));
		if (!colliding) {
			result.isColliding = false;
			return result;
		}
		int mtvx = 0;
		int mtvy = 0;
		int pushLeft = ax2 - bx1;
		int pushRight = bx2 - ax1;
		int pushDown = ay2 - by1;
		int pushUp = by2 - ay1;
		
		int min = Integer.MAX_VALUE;
		if (pushLeft < min) min = pushLeft;
		if (pushRight < min) min = pushRight;
		if (pushUp < min) min = pushUp;
		if (pushDown < min) min = pushDown;
		
		if (min < 0) min *= -1;
		
		if (min == pushLeft) mtvx = -pushLeft;
		else if (min == pushRight) mtvx = pushRight;
		else if (min == pushUp) mtvy = pushUp;
		else if (min == pushDown) mtvy = -pushDown;
		result.isColliding = true;
		result.mtvX = mtvx;
		result.mtvY = mtvy;
		return result;
	}

}
