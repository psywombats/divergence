/**
 *  Event.java
 *  Created on Nov 12, 2012 11:13:21 AM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.rainfall.maps.events;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;

import net.wombatrpgs.mgne.global.Global;
import net.wombatrpgs.rainfall.collisions.Hitbox;
import net.wombatrpgs.rainfall.collisions.NoHitbox;
import net.wombatrpgs.rainfall.core.RGlobal;
import net.wombatrpgs.rainfall.graphics.FourDir;
import net.wombatrpgs.rainfall.maps.DirVector;
import net.wombatrpgs.rainfall.maps.Direction;
import net.wombatrpgs.rainfall.maps.Level;
import net.wombatrpgs.rainfallschema.graphics.FourDirMDO;
import net.wombatrpgs.rainfallschema.maps.CharacterEventMDO;
import net.wombatrpgs.rainfallschema.settings.GameSpeedMDO;

/**
 * A character event is an event with an MDO and an animation that looks kind of
 * like a character.
 */
public class CharacterEvent extends MapEvent {
	
	protected CharacterEventMDO mdo;
	protected FourDir appearance;

	/**
	 * Creates a new char event with the specified data at the specified coords.
	 * @param	parent	The parent level of the event
	 * @param 	mdo		The data to create the event with
	 * @param 	x		The x-coord of the event (in pixels)
	 * @param 	y		The y-coord of the event (in pixels)
	 */
	public CharacterEvent(Level parent, CharacterEventMDO mdo, float x, float y) {
		super(parent, x, y);
		this.mdo = mdo;
		if (mdo.appearance != null) {
			FourDirMDO dirMDO = (FourDirMDO) RGlobal.data.getEntryByKey(mdo.appearance);
			appearance = new FourDir(dirMDO, this);
		}
	}
	
	/**
	 * Creates a new character event with the specified data at the origin.
	 * @param 	parent	The parent level of the event
	 */
	protected CharacterEvent(Level parent) {
		super(parent);
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
				if (Math.abs(vx) > Math.abs(vy)) {
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
	 * Start moving in a particular direction. Does not switch immediately to
	 * that direction but rather adds some speed in that direction based on hero
	 * walk rate.
	 * @param 	dir			The direction to move in
	 */
	public void startMove(Direction dir) {
		addMoveComponent(dir.getVector());
	}

	/**
	 * Stop moving in a particular direction. Does not switch immediately to
	 * that direction but rather adds some speed in that direction based on hero
	 * walk rate.
	 * @param 	dir			The direction to cancel velocity in 
	 */
	public void stopMove(Direction dir) {
		DirVector vec = dir.getVector();
		vec.x *= -1;
		vec.y *= -1;
		addMoveComponent(vec);
	}
	
	/**
	 * The character starts moving in the specified direction. Uses its built-in
	 * speed. (but right now it just takes it from the speed mdo)
	 * @param 	vector			The vector direction to start moving in
	 */
	protected void addMoveComponent(DirVector vector) {
		GameSpeedMDO mdo = RGlobal.data.getEntryFor("game_speed", GameSpeedMDO.class);
		float newX = this.vx + vector.x * mdo.heroWalkRate;
		float newY = this.vy + vector.y * mdo.heroWalkRate;
		this.setVelocity(newX, newY);
	}

}
