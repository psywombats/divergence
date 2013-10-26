/**
 *  MapEvent.java
 *  Created on Dec 24, 2012 2:41:32 AM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mrogue.maps.events;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import net.wombatrpgs.mrogue.core.MGlobal;
import net.wombatrpgs.mrogue.graphics.PreRenderable;
import net.wombatrpgs.mrogue.maps.Level;
import net.wombatrpgs.mrogue.maps.MapThing;
import net.wombatrpgs.mrogue.maps.PositionSetable;
import net.wombatrpgs.mrogue.maps.layers.EventLayer;
import net.wombatrpgs.mrogue.rpg.CharacterEvent;
import net.wombatrpgs.mrogueschema.maps.data.EightDir;

/**
 * A map event is any map object defined in Tiled, including characters and
 * teleports and other fun stuff. Revised as of 2012-01-30 to be anything that
 * exists on a Tiled layer, even if it wasn't created in Tiled itself.
 * 
 * MR: MapEvent is anything that exists in the world of tiles, as opposed to a
 * thing that just lives on a map. It isn't necessarily a character
 */
public abstract class MapEvent extends MapThing implements	PositionSetable,
															Comparable<MapEvent>,
															PreRenderable {
	
	/** A thingy to fool the prerenderable, a sort of no-appear flag */
	protected static final TextureRegion NO_APPEARANCE = null;
	
	/** Is this object hidden from view/interaction due to cutscene? */
	protected boolean commandHidden;
	protected boolean switchHidden;
	/** Another toggle on our visibility - if it exists, link it to hidden */
	protected String showSwitch;
	protected String hideSwitch;
	
	/** Coords in pixels relative to map origin */
	protected float x, y;
	/** Coords in tiles, (0,0) is upper left */
	protected int tileX, tileY;
	/** Velocity the object is currently moving at in pixels/second */
	protected float vx, vy;
	/** Are we currently moving towards some preset destination? */
	protected boolean tracking;
	/** The place we're possibly moving for */
	protected float targetX, targetY;
	/** Gotta keep track of these for some reason (tracking reasons!) */
	protected float lastX, lastY;

	/**
	 * Creates a new map event for the level at the origin.
	 * @param 	parent		The parent level of the event
	 */
	protected MapEvent(Level parent) {
		super(parent);
		zeroCoords();
		// TODO: MapEvent
	}
	
	/**
	 * Creates a blank map event associated with no map. Assumes the subclass
	 * will do something interesting in its constructor.
	 */
	protected MapEvent() {
		zeroCoords();
	}
	
	/** @see net.wombatrpgs.mrogue.maps.Positionable#getX() */
	@Override
	public float getX() { return x; }

	/** @see net.wombatrpgs.mrogue.maps.Positionable#getY() */
	@Override
	public float getY() { return y; }

	/** @see net.wombatrpgs.mrogue.maps.PositionSetable#setX(int) */
	@Override
	public void setX(float x) { this.x = x; }

	/** @see net.wombatrpgs.mrogue.maps.PositionSetable#setY(int) */
	@Override
	public void setY(float y) { this.y = y; }
	
	/** @param tileX The new x-coord of this event (in tiles) */
	public void setTileX(int tileX) { this.tileX = tileX; }
	
	/** @param tileY The new y-coord of this event (in tiles) */
	public void setTileY(int tileY) { this.tileY = tileY; }
	
	/** @return x-coord of the center of this object, in px */
	public float getCenterX() { return x; }
	
	/** @return y-coord of the center of this object, in px */
	public float getCenterY() { return y; }
	
	/** @return x-coord of this object, in tiles */
	public int getTileX() { return tileX; }
	
	/** @return y-coord of this object, in tiles */
	public int getTileY() { return tileY; }
	
	/** @return The x-velocity of this object, in px/s */
	public float getVX() { return this.vx; }
	
	/** @return The y-velocity of this object, in px/s */
	public float getVY() { return this.vy; }
	
	/** @param f The offset to add to x */
	public void moveX(float f) { this.x += f; }
	
	/** @param y The offset to add to x */
	public void moveY(float g) { this.y += g; }
	
	/** @return True if this object is moving towards a location */
	public boolean isTracking() { return tracking; }

	
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
	
	/** @see net.wombatrpgs.mrogue.graphics.PreRenderable#getRenderX() */
	@Override
	public int getRenderX() { return (int) getX(); }

	/** @see net.wombatrpgs.mrogue.graphics.PreRenderable#getRenderY() */
	@Override
	public int getRenderY() { return (int) getY(); }

	/**
	 * Default is inivisible.
	 * @see net.wombatrpgs.mrogue.graphics.PreRenderable#getRegion()
	 */
	@Override
	public TextureRegion getRegion() {
		return NO_APPEARANCE;
	}
	
	/**
	 * Stops this event from a period of pathing towards its logical next
	 * turn position by permanently setting it to is next turn positon.
	 */
	public void stopMoving() {
		x = tileX * parent.getTileWidth();
		y = tileY * parent.getTileHeight();
		halt();
	}
	
	/**
	 * Update yoself! This is called from the rendering loop but it's with some
	 * filters set on it for target framerate. As of 2012-01-30 it's not called
	 * from the idiotic update loop.
	 * @param 	elapsed			Time elapsed since last update, in seconds
	 */
	public void update(float elapsed) {
		super.update(elapsed);
		
		if (Float.isNaN(vx) || Float.isNaN(vy)) {
			MGlobal.reporter.warn("NaN values in physics!! " + this);
		}
		integrate(elapsed);
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
				tracking = false;
			}
		}
		storeXY();
	}
	
	/**
	 * @see net.wombatrpgs.mrogue.maps.MapThing#render
	 * (com.badlogic.gdx.graphics.OrthographicCamera)
	 */
	@Override
	public void render(OrthographicCamera camera) {
		if (hidden()) return;
		super.render(camera);
	}

	/**
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(MapEvent other) {
		return -Math.round(10f * (getZ() - other.getZ()));
	}

	/**
	 * Sets the hide status of this map event via event command. Hidden events
	 * do not update or interact with other events. It's a way of having objects
	 * on the map but not using them until they're needed.
	 * @param 	hidden			True to hide the event, false to reveal it
	 */
	public void setCommandHidden(boolean hidden) {
		this.commandHidden = hidden;
	}
	
	/**
	 * Gets the name of this event as specified in Tiled. Null if the event is
	 * unnamed in tiled or was not created from tiled.
	 * @return
	 */
	public String getName() {
		// TODO: getName
		return "TODO: getName";
	}
	
	/**
	 * Checks if this event's in a specific group. Events can belong to multiple
	 * groups if their group name contains the separator character.
	 * @param 	groupName		The name of the group we may be in
	 * @return					True if in that group, false if not
	 */
	public boolean inGroup(String groupName) {
		// TODO: inGroup
		return false;
	}

	/**
	 * @see net.wombatrpgs.mrogue.maps.MapThing#renderLocal
	 * (com.badlogic.gdx.graphics.OrthographicCamera,
	 * com.badlogic.gdx.graphics.g2d.TextureRegion, int, int, int)
	 */
	public void renderLocal(OrthographicCamera camera, TextureRegion sprite, 
			int offX, int offY, int angle) {
		super.renderLocal(camera, sprite, getRenderX() + offX, getRenderY() + offY, 
				angle, 0);
	}
	
	/**
	 * Uses this event's x/y to render locally.
	 * @see net.wombatrpgs.mrogue.maps.MapThing#renderLocal
	 * (com.badlogic.gdx.graphics.OrthographicCamera, 
	 * com.badlogic.gdx.graphics.g2d.TextureRegion, int, int)
	 */
	public void renderLocal(OrthographicCamera camera, TextureRegion sprite) {
		super.renderLocal(camera, sprite, (int) getX(), (int) getY(), 0);
	}

	/**
	 * Determines if this object is currently in motion.
	 * @return					True if the object is moving, false otherwise
	 */
	public boolean isMoving() {
		return vx != 0 || vy != 0;
	}
	
	/**
	 * Determines if this object is passable by characters or not.
	 * @return					True if the object is passable, false otherwise
	 */
	public boolean isPassable() {
		return true;
	}
	
	/**
	 * Determines if this object blocks vision or not. Most don't.
	 * @return					True if the object is transparent, else false
	 */
	public boolean isTransparent() {
		return true;
	}
	
	/**
	 * Stops all movement in a key-friendly way.
	 */
	public void halt() {
		vx = 0;
		vy = 0;
	}
	
	/**
	 * Gets the y were we sort at. This is for relative positioning with the z-
	 * layer. Used for above/below hero in subclasses. By default is y-coord.
	 * @return
	 */
	public float getSortY() {
		return getY();
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
	 * Updates the effective velocity of this map object.
	 * @param 	vx			The new x-velocity of the object, in px/s
	 * @param 	vy			The new y-velocity of the object, in px/s
	 */
	public void setVelocity(float vx, float vy) {
		this.vx = vx;
		this.vy = vy;
	}
	
	/**
	 * Calculates distance. This is like 7th grade math class here.
	 * @param 	other			The other object in the calculation
	 * @return					The distance between this and other, in pixels
	 */
	public float distanceTo(MapEvent other) {
		float dx = other.x - x;
		float dy = other.y - y;
		return (float) Math.sqrt(dx*dx + dy*dy);
	}
	
	/**
	 * Calculates the direction towards some other map event.
	 * @param 	event			The event to get direction towards
	 * @return					The direction towards that event
	 */
	public EightDir directionTo(MapEvent event) {
		return directionTo(event.getTileX(), event.getTileY());
	}
	
	/**
	 * Calculates the direction towards some point on the map.
	 * @param	tileX			The x-coord to face towards (in tiles)
	 * @param	tileY			The y-coord to face towards (in tiles)
	 * @return
	 */
	public EightDir directionTo(int tileX, int tileY) {
		float dx = this.getTileX() - tileX;
		float dy = this.getTileY() - tileY;
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
	 * Called when this object is added to an object layer.
	 * @param 	layer			The layer this object is being added to
	 */
	public void onAdd(EventLayer layer) {
		this.lastX = getX();
		this.lastY = getY();
	}
	
	/**
	 * Called when this event is removed from an event layer. Nothing by
	 * default.
	 * @param	layer			The layer that kicked us out
	 */
	public void onRemove(EventLayer layer) {
		// noop
	}
	
	/**
	 * Called when this object begins updating its position in the move phase.
	 */
	public void onMoveStart() {
		int tWidth = parent.getTileWidth();
		int tHeight = parent.getTileHeight();
		targetLocation(tileX * tWidth, tileY * tHeight);
		float vx = (tileX*tWidth - x) / parent.getMoveTimeLeft();
		float vy = (tileY*tHeight - y) / parent.getMoveTimeLeft();
		setVelocity(vx, vy);
	}
	
	/**
	 * What happens when a character moves into this event? By default, nothing
	 * happens, but characters should be attacked, items should be auto-grabbed,
	 * and so on.
	 * @param	character		The jerk that ran into us
	 */
	public void collideWith(CharacterEvent character) {
		// default is nothing
	}
	
	/**
	 * Calculates the manhattan distance between this and some other location.
	 * @param	tileX			The x-coord to calc distance to, in tiles
	 * @param	tileY			The y-coord to calc distance to, in tiles
	 * @return
	 */
	public int tileDistanceTo(int tileX, int tileY) {
		return Math.abs(tileX - this.tileX) + Math.abs(tileY - this.tileY); 
	}
	
	/**
	 * Calculates the euclidean distance between this and some other tile loc.
	 * @param	tileX			The other's tile x-coord
	 * @param	tileY			The other's tile y-coord
	 * @return
	 */
	public float euclideanTileDistanceTo(int tileX, int tileY) {
		float dx = this.tileX - tileX;
		float dy = this.tileY - tileY;
		return (float) Math.sqrt(dx*dx + dy*dy);
	}
	
	/**
	 * Calculates the euclidean distance between this and some other event.
	 * @param	event			The event to get dist to
	 * @return					The distance to that event
	 */
	public float euclideanTileDistanceTo(MapEvent event) {
		return euclideanTileDistanceTo(event.getTileX(), event.getTileY());
	}
	
	/**
	 * Called when the cursor hovers over this mouse event. Should return
	 * something that will be fed to the player. Default returns "".
	 * @return					The player-facing description of this event
	 */
	public String mouseoverMessage() {
		return "";
	}
	
	/**
	 * Calculates the z-sort for this event.
	 * @return					The z-layer of this event
	 */
	protected float getZ() {
		return y;
	}
	
	/**
	 * Determines if an event is "hidden" either by switch or command.
	 * @return					True if the event is hidden, false otherwise
	 */
	protected boolean hidden() {
		return commandHidden || switchHidden;
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

}
