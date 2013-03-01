/**
 *  MapEvent.java
 *  Created on Dec 24, 2012 2:41:32 AM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.rainfall.maps.events;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.tiled.TiledObject;

import net.wombatrpgs.rainfall.characters.CharacterEvent;
import net.wombatrpgs.rainfall.core.RGlobal;
import net.wombatrpgs.rainfall.graphics.PreRenderable;
import net.wombatrpgs.rainfall.maps.Level;
import net.wombatrpgs.rainfall.maps.MapObject;
import net.wombatrpgs.rainfall.maps.PositionSetable;
import net.wombatrpgs.rainfall.maps.layers.EventLayer;
import net.wombatrpgs.rainfall.physics.CollisionResult;
import net.wombatrpgs.rainfall.physics.Hitbox;
import net.wombatrpgs.rainfall.physics.NoHitbox;

/**
 * A map event is any map object defined in Tiled, including characters and
 * teleports and other fun stuff. Revised as of 2012-01-30 to be anything that
 * exists on a Tiled layer, even if it wasn't created in Tiled itself.
 */
public abstract class MapEvent extends MapObject implements PositionSetable,
															Comparable<MapEvent>,
															PreRenderable {
	
	/** Max time to fall into a hole */
	protected static final float FULL_FALL = 1f;
	/** A thingy to fool the prerenderable, a sort of no-appear flag */
	protected static final TextureRegion NO_APPEARANCE = null;
	protected static final String PROPERTY_Z = "z";
	
	/** Our patron object on the tiled map */
	protected TiledObject object;
	
	/** Will this event ever move? May be deprecated */
	protected boolean mobile;
	/** Should this object have its collisions checked? */
	protected boolean checkCollisions;
	
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
	
	/** How much time we've spent falling (in s) */
	protected float fallTime;
	/** Are we sinking into the abyss? */
	protected boolean falling;
	/** Our target hole location... (in px) */
	protected float holeX, holeY;
	/** Where we started falling in from (in px) */
	protected float startX, startY;
	
	/**
	 * Creates a new map event for the level at the specified coords.
	 * @param 	parent			The parent level of the event
	 * @param 	object`			The Tiled object used to complete the event
	 * @param 	x				The x-coord to start at (in pixels)
	 * @param 	y				The y-coord to start at (in pixels);
	 * @param	mobile			True if this object will be moving, else false
	 * @param	checkCollisions	True if collision detection should be enabled
	 */
	protected MapEvent(Level parent, TiledObject object, float x, float y, 
			boolean mobile, boolean checkCollisions) {
		super(parent);
		zeroCoords();
		this.x = x;
		this.y = y;
		this.object = object;
		this.mobile = mobile;
		this.checkCollisions = checkCollisions;
	}

	/**
	 * Creates a new map event for the level at the origin.
	 * @param 	parent		The parent level of the event
	 */
	protected MapEvent(Level parent) {
		super(parent);
		zeroCoords();
	}
	
	/**
	 * Creates a blank map event associated with no map. Assumes the subclass
	 * will do something interesting in its constructor.
	 */
	protected MapEvent() {
		zeroCoords();
	}
	
	/**
	 * Creates a new event for the supplied parent level using coordinates
	 * inferred from the tiled object.
	 * @param 	parent			The parent levelt to make teleport for
	 * @param 	object			The object to infer coords from
	 * @param	mobile			True if this object will be moving, false otherwise
	 * @param	checkCollision	True if this event should check for collisions
	 */
	public MapEvent(Level parent, TiledObject object, boolean mobile, boolean checkCollisions) {
		this(	parent, 
				object,
				object.x, 
				parent.getHeight()*parent.getTileHeight()-object.y,
				mobile,
				checkCollisions);
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
	
	/** @return The x-velocity of this object, in px/s */
	public float getVX() { return this.vx; }
	
	/** @return The y-velocity of this object, in px/s */
	public float getVY() { return this.vy; }
	
	/** @param f The offset to add to x */
	public void moveX(float f) { this.x += f; }
	
	/** @param y The offset to add to x */
	public void moveY(float y) { this.y += y; }
	
	/** @return True if this object is moving towards a location */
	public boolean isTracking() { return tracking; }
	
	/** @return True if this event moves, false otherwise */
	public boolean isMobile() { return mobile; }
	
	/** @return True if collisions should be checked on this event */
	public boolean isCollisionEnabled() { return checkCollisions; }
	
	/** @param enabled True if collisions should be checked on this event */
	public void setCollisionsEnabled(boolean enabled) { this.checkCollisions = enabled; }
	
	/** @return True if we're falling into a hole, false otherwise */
	public boolean isFalling() { return this.falling; }
	
	/** @param The new max targetable speed by this event */
	public void setMaxVelocity(float maxVelocity) { this.maxVelocity = maxVelocity; }
	
	/**
	 * Sorts map objects based on z-depth.
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(MapEvent other) {
		if (other.getY() < y) {
			return -1;
		} else if (other.getY() > y) {
			return 1;
		} else {
			return 0;
		}
	}
	
	/**
	 * @see net.wombatrpgs.rainfall.graphics.PreRenderable#getRenderX()
	 */
	@Override
	public int getRenderX() {
		return getX();
	}

	/**
	 * @see net.wombatrpgs.rainfall.graphics.PreRenderable#getRenderY()
	 */
	@Override
	public int getRenderY() {
		return getY();
	}

	/**
	 * Default is inivisible.
	 * @see net.wombatrpgs.rainfall.graphics.PreRenderable#getRegion()
	 */
	@Override
	public TextureRegion getRegion() {
		return NO_APPEARANCE;
	}
	
	/**
	 * Update yoself! This is called from the rendering loop but it's with some
	 * filters set on it for target framerate. As of 2012-01-30 it's not called
	 * from the idiotic update loop.
	 * @param 	elapsed			Time elapsed since last update, in seconds
	 */
	public void update(float elapsed) {
		super.update(elapsed);
		if (tracking) {
			float dx = targetX - x;;
			float dy = targetY - y;
			float norm = (float) Math.sqrt(dx*dx + dy*dy);
			if (norm != 0) {
				dx /= norm;
				dy /= norm;
			}
			internalTargetVelocity(maxVelocity * dx, maxVelocity * dy);
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
		if (Float.isNaN(vx) || Float.isNaN(vy)) {
			RGlobal.reporter.warn("NaN values in physics!! " + this);
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
		if (falling) {
			fallTime += elapsed;
			x = startX + (holeX - startX) * (fallTime / FULL_FALL);
			y = startY + (holeY - startY) * (fallTime / FULL_FALL);
			if (fallTime > FULL_FALL) {
				endFall();
			}
		}
		lastX = x;
		lastY = y;
	}

	/**
	 * This version kills itself unless it was present on the map in the first
	 * place.
	 * @see net.wombatrpgs.rainfall.maps.MapObject#reset()
	 */
	@Override
	public void reset() {
		if (object == null) {
			getLevel().removeEvent(this);
		} else {
			setX(object.x);
			setY(parent.getHeight()*parent.getTileHeight()-object.y);
			// ha! I told you storing this would come in handy!
			getLevel().changeZ(this, Float.valueOf(object.properties.get(PROPERTY_Z))+.5f);
		}
	}

	/**
	 * @see net.wombatrpgs.rainfall.maps.MapObject#onAddedToMap
	 * (net.wombatrpgs.rainfall.maps.Level)
	 */
	@Override
	public void onAddedToMap(Level map) {
		super.onAddedToMap(map);
		if (object != null) {
			object.properties.put(PROPERTY_Z, String.valueOf(map.getZ(this))); // trust me
		}
		
		// (but really it's a hack so we can restore when we reset)
	}

	/**
	 * Determine whether overlapping with this object in general is allowed.
	 * This is sort of a physicsy thing. Allowing it implies no physical presence
	 * on the map, even if this object has a hitbox. Disallowing it is usually a
	 * signal that collisions need to be resolvled.
	 * @return					True if overlapping with this object is okay
	 */
	public boolean isOverlappingAllowed() {
		return true;
	}
	
	/**
	 * Determines if the character is able to move of their own volition. False
	 * in cases such as stunning or falling.
	 * @return					True if moving is legal, false otherwise
	 */
	public boolean canMove() {
		return !falling;
	}
	
	/**
	 * Gets the name of this event as specified in Tiled. Null if the event is
	 * unnamed in tiled or was not created from tiled.
	 * @return
	 */
	public String getName() {
		if (object != null) {
			return object.properties.get("name");
		} else {
			return null;
		}
	}
	
	/**
	 * @see net.wombatrpgs.rainfall.maps.MapObject#renderLocal
	 * (com.badlogic.gdx.graphics.OrthographicCamera,
	 * com.badlogic.gdx.graphics.g2d.TextureRegion, int, int, int)
	 */
	public void renderLocal(OrthographicCamera camera, TextureRegion sprite, 
			int offX, int offY, int angle) {
		super.renderLocal(camera, sprite, getX() + offX, getY() + offY, angle, fallTime/FULL_FALL);
	}
	
	/**
	 * Uses this event's x/y to render locally.
	 * @see net.wombatrpgs.rainfall.maps.MapObject#renderLocal
	 * (com.badlogic.gdx.graphics.OrthographicCamera, 
	 * com.badlogic.gdx.graphics.g2d.TextureRegion, int, int)
	 */
	public void renderLocal(OrthographicCamera camera, TextureRegion sprite) {
		super.renderLocal(camera, sprite, getX(), getY(), fallTime/FULL_FALL);
	}

	/**
	 * Determines if this object is currently in motion.
	 * @return					True if the object is moving, false otherwise
	 */
	public boolean isMoving() {
		return Math.abs(vx) > .1 || Math.abs(vy) > .1;
	}
	
	/**
	 * Stops all movement in a key-friendly way.
	 */
	public void halt() {
		targetVX = 0;
		targetVY = 0;
		vx = 0;
		vy = 0;
	}
	
	/**
	 * Checks if the block can land on top of this event. If this is true, then
	 * the block will land and whatever hit detection will go on when the block
	 * lands, in theory. If it's false, the block will kill itself. False by
	 * default.
	 * @return					True if block landing here is legal
	 */
	public boolean supportsBlockLanding() {
		return false;
	}
	
	/**
	 * Gets the hitbox associated with this map object at this point in time.
	 * It's abstract so that events with different animations can return the
	 * appropriate object for each call. Default returns no hitbox
	 * @return				The hitbox being used at the moment, never null
	 */
	public Hitbox getHitbox() {
		return NoHitbox.getInstance();
	}
	
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
	 * Gives this map object a new target to track towards.
	 * @param 	targetX		The target location x-coord (in px)
	 * @param 	targetY		The target location y-coord (in px)
	 */
	public void targetLocation(float targetX, float targetY) {
		this.targetX = targetX;
		this.targetY = targetY;
		this.tracking = true;
	}

	/**
	 * Updates the target velocity of this map object.
	 * @param 	targetVX	The target x-velocity of the object, in px/s
	 * @param 	targetVY	The target y-velocity of the object, in px/s
	 */
	public final void targetVelocity(float targetVX, float targetVY) {
		internalTargetVelocity(targetVX, targetVY);
		this.tracking = false;
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
	 * Called once per collision with another object. Move everyone out of
	 * collision if necessary. Override if you want to do something special
	 * with this.
	 * @param 	other			The villain in this little scenario
	 * @param 	result			The result of colliding us with villain
	 */
	public void resolveCollision(MapEvent other, CollisionResult result) {
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
	 * This is like 7th grade math class here.
	 * @param 	other			The other object in the calculation
	 * @return					The distance between this and other, in pixels
	 */
	public float distanceTo(MapEvent other) {
		float dx = other.x - x;
		float dy = other.y - y;
		return (float) Math.sqrt(dx*dx + dy*dy);
	}

	/**
	 * Called when this object is colliding with another event in the wild. If
	 * this is true, unless you move out of collision, it'll be called every
	 * frame. So if you want to have a hitbox but not be a physical entity
	 * really, then don't respond to this method. There should be some sort of
	 * double dispatch here really. Maybe. Default does nothing.
	 * @param 	other			The other object involved in the collision
	 * @param	result			Info about the collision
	 * @return					True if collision is "consumed" at once,
	 * 							false if collision response should be applied
	 */
	public boolean onCollide(MapEvent other, CollisionResult result) { 
		return false;
	}
	
	/**
	 * Called when this object is added to an object layer. Nothing by default.
	 * @param 	layer			The layer this object is being added to
	 */
	public void onAdd(EventLayer layer) {
		// nothing by default
	}
	
	/**
	 * Called when this event collides with immovable terrain.
	 * @param 	result			The result of the wall collision
	 */
	public void resolveWallCollision(CollisionResult result) {
		applyMTV(null, result, 1f);
	}
	
	/**
	 * Start falling into a hole. Oh no!
	 * @param 	tileX			The x-coord of the hole (in tiles)
	 * @param 	tileY			The y-coord of the hole (in tiles);
	 */
	public void fallIntoHole(int tileX, int tileY) {
		if (!falling) {
			falling = true;
			holeX = tileX * parent.getTileWidth();
			holeY = (tileY - .5f) * parent.getTileHeight();
			startX = x;
			startY = y;
		}
	}
	
	/**
	 * Called after finishing falling into a hole. Default removes us from map.
	 */
	public void endFall() {
		parent.removeEvent(this);
		zeroCoords();
	}
	
	/**
	 * Change out z to the appropriate new value. This does what it should,
	 * just override it if you need to bring things with you.
	 * @param 	newZ			The new z-layer to put us on
	 */
	public void changeZ(int newZ) {
		parent.changeZ(this, newZ);
	}
	
	/**
	 * Chunking refers to the process of breaking this event into pieces for the
	 * purposes of simulating tile depth. This should enabled for tall objects
	 * and disabled when the graphic is undergound special effects, like falling
	 * into holes. Default returns true if the event isn't falling into a hole,
	 * and there's a graphic to display.
	 * @return					True if this event should be chunked
	 */
	public boolean requiresChunking() {
		return (this.getRegion() != null && !isFalling());
	}
	
	/**
	 * Moves objects out of collision with each other. Usually call this from
	 * onCollide, as a collision result is needed.
	 * @param 	other			The other object to bump
	 * @param 	result			The result of the two objects' collisions
	 * @param	ratio			Percent to apply to us, 1 = 100% move us
	 */
	protected void applyMTV(MapEvent other, CollisionResult result, float ratio) {
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
	 * Internal method of targeting velocities. Feel free to override this one.
	 * @param 	targetVX			The new target x-velocity (in px/s)
	 * @param 	targetVY			The new target y-velocity (in px/s)
	 */
	protected void internalTargetVelocity(float targetVX, float targetVY) {
		this.targetVX = targetVX;
		this.targetVY = targetVY;
	}
	
	/**
	 * Does some constructor-like stuff to reset physical variables.
	 */
	protected void zeroCoords() {
		x = 0;
		y = 0;
		targetVX = 0;
		targetVY = 0;
		vx = 0;
		vy = 0;
		fallTime = 0;
		holeX = 0;
		holeY = 0;
		falling = false;
	}	

}
