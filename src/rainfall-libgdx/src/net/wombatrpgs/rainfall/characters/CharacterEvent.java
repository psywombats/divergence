/**
 *  CharacterEvent.java
 *  Created on Nov 12, 2012 11:13:21 AM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.rainfall.characters;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;

import net.wombatrpgs.mgne.global.Global;
import net.wombatrpgs.rainfall.collisions.Hitbox;
import net.wombatrpgs.rainfall.collisions.NoHitbox;
import net.wombatrpgs.rainfall.core.RGlobal;
import net.wombatrpgs.rainfall.graphics.FacesAnimation;
import net.wombatrpgs.rainfall.graphics.FacesAnimationFactory;
import net.wombatrpgs.rainfall.maps.DirVector;
import net.wombatrpgs.rainfall.maps.Direction;
import net.wombatrpgs.rainfall.maps.Level;
import net.wombatrpgs.rainfall.maps.MapObject;
import net.wombatrpgs.rainfall.maps.events.MapEvent;
import net.wombatrpgs.rainfallschema.graphics.DirMDO;
import net.wombatrpgs.rainfallschema.maps.CharacterEventMDO;
import net.wombatrpgs.rainfallschema.settings.GameSpeedMDO;

/**
 * A character event is an event with an MDO and an animation that looks kind of
 * like a character.
 */
public class CharacterEvent extends MapEvent {
	
	protected Map<Direction, Boolean> directionStatus;
	protected CharacterEventMDO mdo;
	protected FacesAnimation appearance;

	/**
	 * Creates a new char event with the specified data at the specified coords.
	 * @param 	mdo		The data to create the event with
	 * @param	parent	The parent level of the event
	 * @param 	x		The x-coord of the event (in pixels)
	 * @param 	y		The y-coord of the event (in pixels)
	 */
	public CharacterEvent(CharacterEventMDO mdo, Level parent, float x, float y) {
		super(parent, x, y, true, true);
		init(mdo);
	}
	
	/**
	 * Creates a new character event associated with no map from the MDO.
	 * @param 	mdo		The MDO to create the event from
	 */
	public CharacterEvent(CharacterEventMDO mdo) {
		super();
		init(mdo);
	}
	
	/**
	 * Creates a new character event with the specified data at the origin.
	 * @param 	parent	The parent level of the event
	 */
	protected CharacterEvent(Level parent) {
		super(parent);
	}
	
	/**
	 * Gets the direction this character is currently facing from its animation
	 * @return			The direction currently facing
	 */
	public Direction getFacing() {
		return appearance.getFacing();
	}
	
	/**
	 * Tells the animation to face a specific direction.
	 * @param 	dir		The directiont to face
	 */
	public void setFacing(Direction dir) {
		this.appearance.setFacing(dir);
	}
	
	/**
	 * Gives this character a new (temporary?) appearance with a four-dir anim
	 * @param 	oldAppearance	The new anim for this character
	 */
	public void setAppearance(FacesAnimation oldAppearance) {
		this.appearance = oldAppearance;
	}
	
	/**
	 * Gets the current appearance of this character event.
	 * @return			The current appearance of this character event
	 */
	public FacesAnimation getAppearance() {
		return appearance;
	}

	/**
	 * @see net.wombatrpgs.rainfall.graphics.Renderable#render
	 * (com.badlogic.gdx.graphics.OrthographicCamera)
	 */
	@Override
	public void render(OrthographicCamera camera) {
		if (appearance != null) {
			appearance.render(camera);
			camera.update();
		}
	}

	/**
	 * @see net.wombatrpgs.rainfall.maps.MapObject#queueRequiredAssets
	 * (com.badlogic.gdx.assets.AssetManager)
	 */
	@Override
	public void queueRequiredAssets(AssetManager manager) {
		appearance.queueRequiredAssets(manager);
	}

	/**
	 * @see net.wombatrpgs.rainfall.graphics.Renderable#postProcessing
	 * (com.badlogic.gdx.assets.AssetManager)
	 */
	@Override
	public void postProcessing(AssetManager manager) {
		appearance.postProcessing(manager);
	}

	/**
	 * @see net.wombatrpgs.rainfall.maps.MapObject#setVelocity(float, float)
	 */
	@Override
	public void setVelocity(float vx, float vy) {
		if (appearance != null && (vx != this.vx || vy != this.vy)) {
			if (vx == 0 && vy == 0) {
				appearance.stopMoving();
			} else {
				Direction newDir;
				if (Math.abs(vx) >= Math.abs(vy)) {
					if (vx * Direction.RIGHT.getVector().x> 0) {
						newDir = Direction.RIGHT;
					} else {
						newDir = Direction.LEFT;
					}
				} else {
					if (vy * Direction.DOWN.getVector().y > 0) {
						newDir = Direction.DOWN;
					} else {
						newDir = Direction.UP;
					}
				}
				appearance.startMoving(newDir);
			}
		}
		super.setVelocity(vx, vy);
	}

	/**
	 * @see net.wombatrpgs.rainfall.maps.MapObject#getHitbox()
	 */
	@Override
	public Hitbox getHitbox() {
		switch (mdo.collision) {
		case ANIMATION_SPECIFIC_RECTANGLE:
			return appearance.getHitbox();
		case SOMETHING_ELSE_TELL_PSY_RIGHT_AWAY:
			Global.reporter.warn("Got a hitbox for something totally weird");
			return NoHitbox.getInstance();
		case NONE:
			return NoHitbox.getInstance();
		default:
			Global.reporter.warn("No hitbox setting found on " + this);
			return NoHitbox.getInstance();
		}
	}

	/** @see net.wombatrpgs.rainfall.maps.MapObject#isOverlappingAllowed() */
	@Override
	public boolean isOverlappingAllowed() { return false; }
	
	/**
	 * Makes this event face towards an object on the map.
	 * @param 	object		The object to face
	 */
	public void faceToward(MapObject object) {
		int dx = object.getX() - this.getX();
		int dy = object.getY() - this.getY();
		if (Math.abs(dx) > Math.abs(dy)) {
			if (dx > 0) {
				setFacing(Direction.RIGHT);
			} else {
				setFacing(Direction.LEFT);
			}
		} else {
			if (dy > 0) {
				setFacing(Direction.UP);
			} else {
				setFacing(Direction.DOWN);
			}
		}
	}
	
	/**
	 * Start moving in a particular direction. Does not switch immediately to
	 * that direction but rather adds some speed in that direction based on hero
	 * walk rate.
	 * @param 	dir			The direction to move in
	 */
	public void startMove(Direction dir) {
		if (!directionStatus.get(dir)) {
			addMoveComponent(dir.getVector());
			directionStatus.put(dir, true);
		}
	}

	/**
	 * Stop moving in a particular direction. Does not switch immediately to
	 * that direction but rather adds some speed in that direction based on hero
	 * walk rate.
	 * @param 	dir			The direction to cancel velocity in 
	 */
	public void stopMove(Direction dir) {
		if (directionStatus.get(dir)) {
			DirVector vec = dir.getVector();
			vec.x *= -1;
			vec.y *= -1;
			addMoveComponent(vec);
			directionStatus.put(dir, false);
		}
	}
	
	/**
	 * Stops all movement in a key-friendly way.
	 */
	public void halt() {
		appearance.stopMoving();
		for (Direction dir : Direction.values()) {
			stopMove(dir);
		}
	}
	
	/**
	 * The character starts moving in the specified direction. Uses its built-in
	 * speed. (but right now it just takes it from the speed mdo)
	 * @param 	vector		The vector direction to start moving in
	 */
	protected void addMoveComponent(DirVector vector) {
		GameSpeedMDO mdo = RGlobal.data.getEntryFor("game_speed", GameSpeedMDO.class);
		float newX = this.vx + vector.x * mdo.heroWalkRate;
		float newY = this.vy + vector.y * mdo.heroWalkRate;
		this.setVelocity(newX, newY);
	}
	
	/**
	 * Creates this event from an MDO.
	 * @param 	mdo			The MDO to create the event from
	 */
	protected void init(CharacterEventMDO mdo) {
		this.mdo = mdo;
		if (mdo.appearance != null) {
			DirMDO dirMDO = (DirMDO) RGlobal.data.getEntryByKey(mdo.appearance);
			appearance = FacesAnimationFactory.create(dirMDO, this);
		}
		directionStatus = new HashMap<Direction, Boolean>();
		directionStatus.put(Direction.DOWN, false);
		directionStatus.put(Direction.UP, false);
		directionStatus.put(Direction.LEFT, false);
		directionStatus.put(Direction.RIGHT, false);
	}

}
