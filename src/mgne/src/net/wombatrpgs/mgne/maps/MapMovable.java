/**
 *  MapMovable.java
 *  Created on Jan 28, 2014 11:49:39 PM for project saga
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mgne.maps;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import net.wombatrpgs.mgne.core.MGlobal;
import net.wombatrpgs.mgne.core.interfaces.FinishListener;
import net.wombatrpgs.mgne.graphics.interfaces.PosRenderable;
import net.wombatrpgs.mgne.physics.CollisionResult;
import net.wombatrpgs.mgne.physics.Hitbox;
import net.wombatrpgs.mgne.physics.NoHitbox;
import net.wombatrpgs.mgneschema.maps.data.DirEnum;
import net.wombatrpgs.mgneschema.maps.data.DirVector;
import net.wombatrpgs.mgneschema.maps.data.EightDir;

/**
 * A movable MapThing, but doesn't limit itself to Tiled events and the grid.
 */
public abstract class MapMovable extends MapThing implements PositionSetable {
	
	protected static float DEFAULT_VELOCITY = 102; // px/s
	
	/** Physics values */
	protected float x;
	protected float y;
	protected float vx, vy;
	protected float maxVelocity;
	
	/** Tracking values */
	protected List<DirEnum> path;
	protected boolean tracking;
	protected float targetX, targetY;
	public float lastX, lastY;
	public boolean corrected;
	protected float lastElapsed;
	protected float lastAttemptedVX, lastAttemptedVY;
	
	/** Misc */
	protected List<FinishListener> trackingListeners;
	
	/**
	 * Creates a new movable map thing. No data required.
	 */
	public MapMovable() {
		zeroCoords();
		trackingListeners = new ArrayList<FinishListener>();
		path = new ArrayList<DirEnum>();
		maxVelocity = DEFAULT_VELOCITY;
	}
	
	/** @see net.wombatrpgs.mgne.maps.Positionable#getX() */
	@Override public float getX() { return (int) x; }

	/** @see net.wombatrpgs.mgne.maps.Positionable#getY() */
	@Override public float getY() { return (int) y; }
	
	public float getPreciseX() { return x; }
	
	public float getPreciseY() { return y; }

	/** @see net.wombatrpgs.mgne.maps.PositionSetable#setX(float) */
	@Override public void setX(float x) { this.x = x; }

	/** @see net.wombatrpgs.mgne.maps.PositionSetable#setY(float) */
	@Override public void setY(float y) { this.y = y; }
	
	/** @return The x-velocity of this object, in px/s */
	public float getVX() { return this.vx; }
	
	/** @return The y-velocity of this object, in px/s */
	public float getVY() { return this.vy; }
	
	/** @param x The offset to add to x */
	public void moveX(float x) { this.x += x; }
	
	/** @param y The offset to add to x */
	public void moveY(float y) { this.y += y; }
	
	/** @return True if this object is moving towards a location */
	public boolean isTracking() { return tracking; }
	
	/** @return True if the object is moving, false otherwise */
	public boolean isMoving() { return vx != 0 || vy != 0; }
	
	/** @param listener The listener to call when tracking is finished */
	public void addTrackingListener(FinishListener listener) { trackingListeners.add(listener); }
	
	/**
	 * Gets the hitbox for this event. This usually corresponds to the hitbox
	 * of the sprite, which is usually 16*16. No hitbox if no appearance.
	 * @return					The hitbox of this event.
	 */
	public Hitbox getHitbox() {
		return NoHitbox.getInstance();
	}
	
	/**
	 * Updates the effective velocity of this map object.
	 * @param 	vx			The new x-velocity of the object, in px/s
	 * @param 	vy			The new y-velocity of the object, in px/s
	 */
	public void setVelocity(float vx, float vy) {
		this.vx = vx;
		this.vy = vy;
	}
	
	/**
	 * Determines if this object is "stuck" or not. This means it's tracking
	 * but hasn't moved much at all.
	 * @return					True if the event is stuck, false otherwise
	 */
	public boolean isStuck() {
		return 	isTracking() &&
				Math.abs(lastX - x) < Math.abs(vx) / 2.f &&
				Math.abs(lastY - y) < Math.abs(vy) / 2.f;
	}
	
	/**
	 * Calculates distance. This is like 7th grade math class here.
	 * @param 	other			The other object in the calculation
	 * @return					The distance between this and other, in pixels
	 */
	public float distanceTo(MapMovable other) {
		float dx = other.x - x;
		float dy = other.y - y;
		return (float) Math.sqrt(dx*dx + dy*dy);
	}
	
	/**
	 * Calculates the direction towards some other map event.
	 * @param 	event			The event to get direction towards
	 * @return					The direction towards that event
	 */
	public EightDir directionTo(MapMovable event) {
		return directionTo(event.getX(), event.getY());
	}
	
	/**
	 * Calculates the direction towards some point on the map.
	 * @param	x				The x-coord to face towards (in px)
	 * @param	y				The y-coord to face towards (in px)
	 * @return					The direction from this object to that point
	 */
	public EightDir directionTo(float x, float y) {
		float dx = getX() - x;
		float dy = getY() - y;
		float a = (float) Math.atan2(dx, dy);
		if (a <=  1f*Math.PI/8f && a >= -1f*Math.PI/8f) return EightDir.SOUTH;
		if (a <= -1f*Math.PI/8f && a >= -3f*Math.PI/8f) return EightDir.SOUTHEAST;
		if (a <= -3f*Math.PI/8f && a >= -5f*Math.PI/8f) return EightDir.EAST;
		if (a <= -5f*Math.PI/8f && a >= -7f*Math.PI/8f) return EightDir.NORTHEAST;
		if (a <= -7f*Math.PI/8f || a >=  7f*Math.PI/8f) return EightDir.NORTH;
		if (a <=  7f*Math.PI/8f && a >=  5f*Math.PI/8f) return EightDir.NORTHWEST;
		if (a <=  5f*Math.PI/8f && a >=  3f*Math.PI/8f) return EightDir.WEST;
		if (a <=  3f*Math.PI/8f && a >=  1f*Math.PI/8f) return EightDir.SOUTHWEST;
		MGlobal.reporter.warn("NaN or something in direction: " + a + " , " + dx + " , " + dy);
		return EightDir.NORTH;
	}
	
	/**
	 * @see net.wombatrpgs.mgne.maps.MapThing#update(float)
	 */
	@Override
	public void update(float elapsed) {
		super.update(elapsed);
		
		storeXY();
		
		// integration
		if (Float.isNaN(vx) || Float.isNaN(vy)) {
			MGlobal.reporter.warn("NaN values in physics!! " + this);
		}
		integrate(elapsed);
		
		// tracking
		if (tracking) {
			if ((x < targetX && lastX > targetX) || (x > targetX && lastX < targetX)) {
				x = targetX;
				vx = 0;
			}
			if ((y < targetY && lastY > targetY) || (y > targetY && lastY < targetY)) {
				y = targetY;
				vy = 0;
			}
			if (x == targetX && y == targetY) {
				halt();
				tracking = false;
			} else {
				float dx = targetX - x;
				float dy = targetY - y;
//				if (corrected) {
					vx = Math.signum(dx) * maxVelocity;
					vy = Math.signum(dy) * maxVelocity;
//				} else {
//					float len = (float) Math.sqrt(dx*dx + dy*dy);
//					vx = dx / len * maxVelocity;
//					vy = dy / len * maxVelocity;
//				}
			}
		}
		lastElapsed = elapsed;
	}

	/**
	 * @see net.wombatrpgs.mgne.maps.MapThing#onAddedToMap
	 * (net.wombatrpgs.mgne.maps.Level)
	 */
	@Override
	public void onAddedToMap(Level map) {
		super.onAddedToMap(map);
		this.lastX = getX();
		this.lastY = getY();
	}
	
	/**
	 * Checks if this map-movable object contains the given point, inclusive.
	 * @param	tileX			The x-coord to check (in tiles)
	 * @param	tileY			The y-coord to check (in tiles)
	 * @return					True if that tile lies within this object
	 */
	public boolean containsTile(int tileX, int tileY) {
		return	(x >= tileX * parent.getTileWidth()) && 
				(x <= (tileX+1) * parent.getTileWidth()) &&
				(y >= tileY * parent.getTileHeight()) && 
				(y <= (tileY+1) * parent.getTileHeight());
	}
	
	/**
	 * Renders some texture relative to this event. All pixels are virtual.
	 * @param	batch			The batch to render with
	 * @param	tex				The texture to render
	 * @param	offX			The offset to this location to render at (in px)
	 * @param	offY			The offset to this location to render at (in px)
	 */
	public void renderLocal(SpriteBatch batch, TextureRegion tex, float offX, float offY) {
		batch.begin();
		batch.draw(tex, getX() + offX, getY() + offY);
		batch.end();
	}
	
	/**
	 * Renders some object relative to this event. All pixels are virtual.
	 * @param	batch			The batch to render with
	 * @param	obj				The object to render
	 * @param	offX			The offset to this location to render at (in px)
	 * @param	offY			The offset to this location to render at (in px)
	 */
	public void renderLocal(SpriteBatch batch, PosRenderable obj, float offX, float offY) {
		obj.renderAt(batch, getX() + offX, getY() + offY);
	}

	/**
	 * Gives this map object a new target to track towards.
	 * @param 	targetX			The target location x-coord (in px)
	 * @param 	targetY			The target location y-coord (in px)
	 */
	public void targetLocation(float targetX, float targetY) {
		this.targetX = targetX;
		this.targetY = targetY;
		this.tracking = true;
	}
	
	/**
	 * Gives this map object a new tile target to track towards.
	 * @param	tileX			The target location x-coord (in tiles)
	 * @param	tileY			The target location y-coord (in tiles)
	 */
	public void targetTile(int tileX, int tileY) {
		targetLocation(
				tileX * parent.getTileWidth(),
				tileY * parent.getTileHeight());
	}
	
	/**
	 * Tracks a path of steps until the end of the path.
	 * @param	newSteps		The path to follow, usually from pathfinding
	 */
	public void followPath(List<? extends DirEnum> newSteps) {
		path.addAll(newSteps);
		targetNextTile();
	}
	
	/**
	 * Stops all movement in a key-friendly way.
	 */
	public void halt() {
		vx = 0;
		vy = 0;
	}
	
	/**
	 * Called once per collision with another object. Move everyone out of
	 * collision if necessary. Override if you want to do something special
	 * with this.
	 * @param 	other			The villain in this little scenario
	 * @param 	result			The result of colliding us with villain
	 */
	public void resolveCollision(MapMovable other, CollisionResult result) {
		// default - just get out of here
		applyMTV(other, result, 0f);
	}
	
	/**
	 * Called when this event collides with immovable terrain.
	 * @param 	result			The result of the wall collision
	 */
	public void resolveWallCollision(CollisionResult result) {
		applyMTV(null, result, 1f);
	}
	
	/**
	 * Moves objects out of collision with each other. Usually call this from
	 * onCollide, as a collision result is needed.
	 * @param 	other			The other object to bump
	 * @param 	result			The result of the two objects' collisions
	 * @param	ratio			Percent to apply to us, 1 = 100% move us
	 */
	protected void applyMTV(MapMovable other, CollisionResult result, float ratio) {
		if (this.getHitbox() == result.collide2) {
			result.mtvX *= -1;
			result.mtvY *= -1;
		}
		this.moveX(result.mtvX * ratio);
		this.moveY(result.mtvY * ratio);
		if (other != null && ratio != 1) {
			other.moveX(result.mtvX * -(1f - ratio));
			other.moveY(result.mtvY * -(1f - ratio));
		}
	}
	
	/**
	 * Does some constructor-like stuff to reset physical variables.
	 */
	protected void zeroCoords() {
		x = 0;
		y = 0;
		vx = 0;
		vy = 0;
	}
	
	/**
	 * Applies the physics integration for a timestep.
	 * @param 	elapsed			The time elapsed in that timestep
	 */
	protected void integrate(float elapsed) {
		x += vx * elapsed;
		y += vy * elapsed;
	}
	
	/**
	 * Updates last x/y.
	 */
	protected void storeXY() {
		lastX = x;
		lastY = y;
	}
	
	/**
	 * Moves the tracker to the next tile in the path.
	 */
	protected void targetNextTile() {
		DirEnum dir = path.get(0);
		path.remove(0);
		DirVector vec =  dir.getVector();
		float targetX = getX() + vec.x * parent.getTileWidth();
		float targetY = getY() + vec.y * parent.getTileHeight();
		targetLocation(targetX, targetY);
	}

}
