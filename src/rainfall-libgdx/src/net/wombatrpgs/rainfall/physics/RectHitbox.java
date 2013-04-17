/**
 *  RectHitbox.java
 *  Created on Nov 29, 2012 1:28:40 AM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.rainfall.physics;

import java.util.ArrayList;
import java.util.List;

import net.wombatrpgs.rainfall.maps.Positionable;

/**
 * Standard rectangular hitbox used by 99% of things in this game. It requires
 * knowing the location of its parent as all its coordinates are relative.
 */
public class RectHitbox extends Hitbox {
	
	private float x1, y1, x2, y2;
	
	/**
	 * Creates a new rectangular hitbox. All coordinates relative to the origin
	 * of the hitbox owner.
	 * @param	parent		The positionable owner of this hitbox
	 * @param 	x1			The upper left x-coord, in pixels 
	 * @param 	y1			The upper left y-coord, in pixels
	 * @param 	x2			The lower right x-coord, in pixels
	 * @param 	y2			The lower right y-coord, in pixels
	 */
	public RectHitbox(Positionable parent, float x1, float y1, float x2, float y2) {
		super(parent);
		this.x1 = x1;
		this.x2 = x2;
		this.y1 = y1;
		this.y2 = y2;
	}

	/**
	 * @see net.wombatrpgs.rainfall.physics.Hitbox#getWidth()
	 */
	@Override
	public float getWidth() {
		return Math.abs(x1 - x2);
	}

	/**
	 * @see net.wombatrpgs.rainfall.physics.Hitbox#getHeight()
	 */
	@Override
	public float getHeight() {
		return Math.abs(y1 - y2);
	}

	/**
	 * @see net.wombatrpgs.rainfall.physics.Hitbox#getX()
	 */
	@Override
	public float getX() {
		return parent.getX() + x1;
	}

	/**
	 * @see net.wombatrpgs.rainfall.physics.Hitbox#getY()
	 */
	@Override
	public float getY() {
		return parent.getY() + y1;
	}

	/**
	 * @see java.lang.Object#clone()
	 */
	@Override
	protected Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

	/**
	 * @see net.wombatrpgs.rainfall.physics.Hitbox#isColliding
	 * (net.wombatrpgs.rainfall.physics.Hitbox)
	 */
	@Override
	public CollisionResult isColliding(Hitbox other) {
		return other.isCollidingRect(this);
	}

	/**
	 * @see net.wombatrpgs.rainfall.physics.Hitbox#isCollidingRect
	 * (net.wombatrpgs.rainfall.physics.RectHitbox)
	 */
	@Override
	public CollisionResult isCollidingRect(RectHitbox other) {
		// TODO: optimization would avoid new-ing this object
		CollisionResult result = new CollisionResult();
		result.collide1 = this;
		result.collide2 = other;
		float ax1 = this.x1 + this.parent.getX();
		float ay1 = this.y1 + this.parent.getY();
		float ax2 = this.x2 + this.parent.getX();
		float ay2 = this.y2 + this.parent.getY();
		float bx1 = other.x1 + other.parent.getX();
		float by1 = other.y1 + other.parent.getY();
		float bx2 = other.x2 + other.parent.getX();
		float by2 = other.y2 + other.parent.getY();
		// if you switch to float, change > to >=
		boolean colliding =  !((ax1 >= bx2) || (bx1 >= ax2) || (ay1 >= by2) || (by1 >= ay2));
		if (!colliding) {
			result.isColliding = false;
			return result;
		}
		float mtvx = 0;
		float mtvy = 0;
		float pushLeft = ax2 - bx1;
		float pushRight = bx2 - ax1;
		float pushDown = ay2 - by1;
		float pushUp = by2 - ay1;
		
		float min = Float.MAX_VALUE;
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
		System.out.println("mtv: " + mtvx + " , " + mtvy);
		return result;
	}
	
	/**
	 * Subtracts the area of one rectangle from this rectangle, returning a 
	 * collection of rectangles that defines the remaining polygon.
	 * @param 	other			The hitbox to subtract
	 * @return					The remaining polygon representation
	 */
	public List<RectHitbox> subtract(RectHitbox other) {
		// TODO: this an inner loop, get rid of the new!
		// I could probably make this a for loop but I honestly don't care
		List<RectHitbox> rects = new ArrayList<RectHitbox>();
		if (!other.isColliding(this).isColliding) {
			rects.add(this);
			return rects;
		}
		float px1 = x1 + parent.getX();
		float px2 = other.x1 + other.parent.getX();
		float px3 = other.x2 + other.parent.getX();
		float px4 = x2 + parent.getX();
		float py1 = y1 + parent.getY();
		float py2 = other.y1 + other.getY();
		float py3 = other.y2 + other.getY();
		float py4 = y2 + parent.getY();
		if (px1 < px2 && py1 < py2) {
			rects.add(new RectHitbox(parent, px1, py1, px2, py2));
		}
		if (px2 < px3 && py1 < py2) {
			rects.add(new RectHitbox(parent, px2, py1, px3, py2));
		}
		if (px3 < px4 && py1 < py2) {
			rects.add(new RectHitbox(parent, px3, py1, px4, py2));
		}
		if (px1 < px2 && py2 < py3) {
			rects.add(new RectHitbox(parent, px1, py2, px2, py3));
		}
		if (px3 < px4 && py2 < py3) {
			rects.add(new RectHitbox(parent, px3, py2, px4, py3));
		}
		if (px1 < px2 && py3 < py4) {
			rects.add(new RectHitbox(parent, px1, py3, px2, py4));
		}
		if (px2 < px3 && py3 < py4) {
			rects.add(new RectHitbox(parent, px2, py3, px3, py4));
		}
		if (px3 < px4 && py3 < py4) {
			rects.add(new RectHitbox(parent, px3, py3, px4, py4));
		}
		for (RectHitbox rect : rects) {
			rect.x1 -= parent.getX();
			rect.x2 -= parent.getX();
			rect.y1 -= parent.getY();
			rect.y2 -= parent.getY();
		}
		return rects;
	}

}
