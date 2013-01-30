/**
 *  MapObject.java
 *  Created on Nov 12, 2012 11:10:52 AM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.rainfall.maps;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import net.wombatrpgs.rainfall.characters.CharacterEvent;
import net.wombatrpgs.rainfall.collisions.CollisionResult;
import net.wombatrpgs.rainfall.collisions.Hitbox;
import net.wombatrpgs.rainfall.core.RGlobal;
import net.wombatrpgs.rainfall.graphics.Renderable;
import net.wombatrpgs.rainfall.maps.layers.EventLayer;

/**
 * All objects that appear in Tiled maps that are not tiles extend this class.
 * This includes both characters and events. 
 */
public abstract class MapObject implements Renderable, PositionSetable, Comparable<MapObject> {
	
	/** Level this object exists on */
	protected Level parent;
	/** Coords in pixels relative to map origin */
	protected float x, y;

	/** Velocity the object is trying to reach in pixels/second */
	protected float targetVX, targetVY;
	/** Velocity the object is currently moving at in pixels/second */
	protected float vx, vy;
	
	/** How fast this object accelerates when below its top speed, in px/s^2 */
	protected float acceleration;
	/** How fast this object deccelerates when above its top speed, in px/s^2 */
	protected float decceleration;
	/** The top speed this object can voluntarily reach, in px/s */
	protected float maxVelocity;
	
	/** Are we currently moving towards some preset destination? */
	protected boolean tracking;
	/** The place we're possibly moving for */
	protected float targetX, targetY;
	/** Gotta keep track of these for some reason (tracking reasons!) */
	protected float lastX, lastY;
	
	/**
	 * Creates a new map object for a given level and position.
	 * @param 	parent		The level this map object is on
	 * @param 	x			The x-coord in pixels of this object on the map
	 * @param 	y			The y-coord in pixels of this object on the map
	 */
	public MapObject(Level parent, float x, float y) {
		this(parent);
		this.x = x;
		this.y = y;
	}
	
	/**
	 * Creates a new map object for a given level at the origin.
	 * @param 	parent		The level this map object is on
	 */
	protected MapObject(Level parent) {
		this();
		this.parent = parent;
	}
	
	/**
	 * Creates a new map object floating in limbo land.
	 */
	protected MapObject() {
		acceleration = 0;
		decceleration = 0;
		x = 0;
		y = 0;
		targetVX = 0;
		targetVY = 0;
		vx = 0;
		vy = 0;
	}

	/** @see net.wombatrpgs.rainfall.maps.Positionable#getX() */
	@Override
	public int getX() { return Math.round(x); }

	/** @see net.wombatrpgs.rainfall.maps.Positionable#getY() */
	@Override
	public int getY() { return Math.round(y); }
	
	/** @see net.wombatrpgs.rainfall.maps.PositionSetable#setX(int) */
	@Override
	public void setX(int x) { this.x = x; }

	/** @see net.wombatrpgs.rainfall.maps.PositionSetable#setY(int) */
	@Override
	public void setY(int y) { this.y = y; }

	/** @see net.wombatrpgs.rainfall.maps.Positionable#getBatch() */
	@Override
	public SpriteBatch getBatch() { return parent.getBatch(); }
	
	/** @param f The offset to add to x */
	public void moveX(float f) { this.x += f; }
	
	/** @param y The offset to add to x */
	public void moveY(float y) { this.y += y; }
	
	/** @return The map the hero is currently on */
	public Level getLevel() { return parent; }
	
	/**
	 * Sorts map objects based on z-depth.
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(MapObject other) {
		if (other.getY() < y) {
			return -1;
		} else if (other.getY() > y) {
			return 1;
		} else {
			return 0;
		}
	}

	/**
	 * This is actually the update part of the render loop.
	 * @see net.wombatrpgs.rainfall.graphics.Renderable#render
	 * (com.badlogic.gdx.graphics.OrthographicCamera)
	 */
	@Override
	public void render(OrthographicCamera camera) {
		float elapsed = Gdx.graphics.getDeltaTime();
		float real = 1.0f / elapsed;
		if (real < RGlobal.constants.rate()) {
			elapsed = (1.0f / RGlobal.constants.rate());
		}
		update(elapsed);
	}

	/**
	 * Gets the hitbox associated with this map object at this point in time.
	 * It's abstract so that events with different animations can return the
	 * appropriate object for each call.
	 * @return				The hitbox being used at the moment, never null
	 */
	public abstract Hitbox getHitbox();
	
	/**
	 * Called when this object is colliding with another event in the wild. If
	 * this is true, unless you move out of collision, it'll be called every
	 * frame. So if you want to have a hitbox but not be a physical entity
	 * really, then don't respond to this method. There should be some sort of
	 * double dispatch here really. Maybe.
	 * @param 	other		The other object involved in the collision
	 * @param	result		Info about the collision
	 * @return				True if collision is "consumed" without response,
	 * 						false if collision response should be applied
	 */
	public abstract boolean onCollide(MapObject other, CollisionResult result);
	
	/**
	 * A double-dispatch method for characters when they collide with one
	 * another.
	 * @param 	other		The other object-character in the collision
	 * @param 	result		Info about the collision
	 * @return				True if collision is "consumed" without response,
	 * 						false if collision response should be applied
	 */
	public boolean onCharacterCollide(CharacterEvent other, CollisionResult result) {
		return false;
	}
	
	/**
	 * Determine whether overlapping with this object in general is allowed.
	 * This is sort of a physicsy thing. Allowing it implies no physical presence
	 * on the map, even if this object has a hitbox. Disallowing it is usually a
	 * signal that collisions need to be resolvled.
	 * @return					True if overlapping with this object is okay
	 */
	public abstract boolean isOverlappingAllowed();

	/**
	 * Updates the target velocity of this map object.
	 * @param 	targetVX		The target x-velocity of the object, in px/s
	 * @param 	targetVY		The target y-velocity of the object, in px/s
	 */
	public void targetVelocity(float targetVX, float targetVY) {
		this.targetVX = targetVX;
		this.targetVY = targetVY;
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
	 * Renders a texture at this object's location using its own batch and
	 * coords appropriate to the drawing.
	 * @param 	sprite		The region to render
	 * @param	camera		The current camera
	 */
	public void renderLocal(OrthographicCamera camera, TextureRegion sprite) {
		parent.getBatch().draw(
				sprite, 
				x + Gdx.graphics.getWidth()/2 - camera.position.x, 
				y + Gdx.graphics.getHeight()/2 - camera.position.y);
	}
	
	/**
	 * Called when this object is added to an object layer. Nothing by default.
	 * @param 	layer			The layer this object is being added to
	 */
	public void onAdd(EventLayer layer) {
		// nothing by default
	}
	
	/**
	 * Called when this object is tele'd onto a map.
	 * @param 	map				The map this object is being removed from
	 */
	public void onTeleOn(Level map) {
		this.parent = map;
	}
	
	/**
	 * Called when this object is tele'd or otherwise removed from a map.
	 * @param 	map				The map this object is being removed from
	 */
	public void onTeleOff(Level map) {
		this.parent = null;
	}
	
	/**
	 * Called once per collision with another object. Move everyone out of
	 * collision if necessary. Override if you want to do something special
	 * with this.
	 * @param 	other			The villain in this little scenario
	 * @param 	result			The result of colliding us with villain
	 */
	public void resolveCollision(MapObject other, CollisionResult result) {
		// default - just get out of here
		applyMTV(other, result, 0f);
	}
	
	/**
	 * A double dispatch to override maybe.
	 * @param	 other			The character we collided with
	 * @param 	result			How exactly we collided
	 */
	public void resolveCharacterCollision(CharacterEvent other, CollisionResult result) {
		// default - just get out of here
		applyMTV(other, result, 0f);
	}
	
	/**
	 * Moves objects out of collision with each other. Usually call this from
	 * onCollide, as a collision result is needed.
	 * @param 	other			The other object to bump
	 * @param 	result			The result of the two objects' collisions
	 * @param	ratio			Percent to apply to us, 1 = 100% move us
	 */
	protected void applyMTV(MapObject other, CollisionResult result, float ratio) {
		if (this.getHitbox() == result.collide2) {
			result.mtvX *= -1;
			result.mtvY *= -1;
		}
		this.moveX(result.mtvX * ratio);
		this.moveY(result.mtvY * ratio);
		other.moveX(result.mtvX * -(1f - ratio));
		other.moveY(result.mtvY * -(1f - ratio));
	}
	
	/**
	 * Update yoself! This is called from the rendering loop but it's with some
	 * filters set on it for target framerate.
	 * @param 	elapsed			Time elapsed since last update, in seconds
	 */
	protected void update(float elapsed) {
		if (tracking) {
			float dx = targetX - x;;
			float dy = targetY - y;
			float norm = (float) Math.sqrt(dx*dx + dy*dy);
			dx /= norm;
			dy /= norm;
			targetVX = maxVelocity * dx;
			targetVY = maxVelocity * dy;
		}
		float deltaVX, deltaVY;
		if (vx != targetVX) {
			if (Math.abs(vx) < maxVelocity) {
				deltaVX = acceleration * elapsed;
			} else {
				deltaVX = decceleration * elapsed;
			}
			if (vx < targetVX) {
				vx = Math.min(vx + deltaVX, targetVX);
			} else {
				vx = Math.max(vx - deltaVX, targetVX);
			}
		}
		if (vy != targetVY) {
			if (Math.abs(vy) < maxVelocity) {
				deltaVY = acceleration * elapsed;
			} else {
				deltaVY = decceleration * elapsed;
			}
			if (vy < targetVY) {
				vy = Math.min(vy + deltaVY, targetVY);
			} else {
				vy = Math.max(vy - deltaVY, targetVY);
			}
		}
		x += vx * elapsed;
		y += vy * elapsed;
		if (tracking) {
			if ((x < targetX && lastX > targetX) || (x > targetX && lastX < targetX)) {
				x = targetX;
				vx = 0;
				targetVX = 0;
			}
			if ((y < targetY && lastY > targetY) || (y > targetY && lastY < targetY)) {
				y = targetY;
				vy = 0;
				targetVY = 0;
			}
			if (x == targetX && y == targetY) {
				tracking = false;
			}
		}
		lastX = x;
		lastY = y;
	}

}
