/**
 *  RectHitbox.java
 *  Created on Nov 29, 2012 1:28:40 AM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mgne.physics;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;

import net.wombatrpgs.mgne.maps.Positionable;
import net.wombatrpgs.mgne.physics.ShadowResult.Fin;

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

	/**
	 * @see net.wombatrpgs.mgne.physics.Hitbox#shadowcast(float, float, float)
	 */
	@Override
	public ShadowResult shadowcast(float sourceX, float sourceY, float lightRadius) {
		List<Vector2> points = new ArrayList<Vector2>();
		Vector2 parentPos = new Vector2(parent.getX(), parent.getY());
		points.add(new Vector2(x1, y1).add(parentPos));
		points.add(new Vector2(x2, y1).add(parentPos));
		points.add(new Vector2(x2, y2).add(parentPos));
		points.add(new Vector2(x1, y2).add(parentPos));
		
		float sumX = 0;
		float sumY = 0;
		for (Vector2 point : points) {
			sumX += point.x;
			sumY += point.y;
		}
		Vector2 centroid = new Vector2(sumX / points.size(), sumY / points.size());
		
		List<Boolean> facings = new ArrayList<Boolean>();
		for (int i = 0; i < points.size(); i += 1) {
			Vector2 previous  = getWrapped(points, i-1);
			Vector2 point = points.get(i);
			Vector2 normal = new Vector2(point.y - previous.y, -(point.x - previous.x));
			normal.nor();
			
			Vector2 toLight = new Vector2(sourceX, sourceY);
			toLight.sub(new Vector2((point.x + previous.x) / 2, (point.y + previous.y) / 2));
			toLight.nor();
			
			facings.add(normal.dot(toLight) < 0);
		}
		
		int startIndex = 0, endIndex = 0;
		for (int i = 0; i < points.size(); i += 1) {
			boolean firstFace = getWrapped(facings, i);
			boolean secondFace = getWrapped(facings, i+1);
			if (firstFace && !secondFace) {
				startIndex = i;
			} else if (!firstFace && secondFace) {
				endIndex = i;
			}
		}
		
		if (startIndex > endIndex) {
			endIndex += points.size();
		}
		
		ShadowResult result = new ShadowResult();
		List<Vector2> roots = new ArrayList<Vector2>();
		roots.add(getWrapped(points, startIndex));
		roots.add(getWrapped(points, endIndex));
		for (int i = 0; i < roots.size(); i += 1) {
			Vector2 root = roots.get(i);
			Vector2 toLight = new Vector2(sourceX, sourceY);
			toLight.sub(new Vector2(root));
			Vector2 normal = new Vector2(toLight.x, -toLight.y);
			normal.nor();
			toLight.nor();
			Vector2 toCentroid = new Vector2(centroid);
			toCentroid.sub(root);
			toCentroid.nor();
			if (toCentroid.dot(normal) < 0) {
				normal.x *= -1;
				normal.y *= -1;
			}
			Vector2 penuLoc = new Vector2(sourceX, sourceY);
			penuLoc.mulAdd(normal, lightRadius);
			Vector2 umbraLoc = new Vector2(sourceX, sourceY);
			umbraLoc.mulAdd(normal, -lightRadius);
			
			Vector2 toPenu = new Vector2(root);
			toPenu.sub(penuLoc);
			toPenu.nor();
			Vector2 toUmbra = new Vector2(root);
			toUmbra.sub(umbraLoc);
			toUmbra.nor();
			
			Fin fin = result.new Fin();
			fin.root = root;
			fin.penumbra = new Vector2(root).mulAdd(toPenu, 250);
			fin.umbra = new Vector2(root).mulAdd(toUmbra, 250);
			
			if (i == 0) {
				result.fin1 = fin;
			} else {
				result.fin2 = fin;
			}
		}
		
		List<Vector2> finalPoints = new ArrayList<Vector2>();
		finalPoints.add(points.get(startIndex));
		for (int i = startIndex; i <= endIndex; i += 1) {
			if (i == startIndex) {
				finalPoints.add(result.fin1.umbra);
			} else if (i == endIndex) {
				finalPoints.add(result.fin2.umbra);
			} else {
				Vector2 point = getWrapped(points, i);
				Vector2 toLight = new Vector2(sourceX, sourceY);
				toLight.sub(point);
				toLight.nor();
				
				finalPoints.add(new Vector2(point).mulAdd(toLight, -250));
			}
		}
		finalPoints.add(getWrapped(points, endIndex));
		
		result.umbraVertices = new float[finalPoints.size()*2];
		for (int i = 0; i < finalPoints.size(); i += 1) {
			result.umbraVertices[i*2] = finalPoints.get(i).x;
			result.umbraVertices[i*2+1] = finalPoints.get(i).y;
		}
		
		return result;
	}

	private <T> T getWrapped(List<T> list, int n) {
		int size = list.size();
		return list.get((n % size + size) % size);
	}
}
