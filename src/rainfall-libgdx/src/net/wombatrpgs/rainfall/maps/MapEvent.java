/**
 *  Event.java
 *  Created on Nov 12, 2012 11:13:21 AM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.rainfall.maps;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;

import net.wombatrpgs.mgne.global.Global;
import net.wombatrpgs.rainfall.collisions.CollisionResult;
import net.wombatrpgs.rainfall.collisions.Hitbox;
import net.wombatrpgs.rainfall.collisions.NoHitbox;
import net.wombatrpgs.rainfall.core.RGlobal;
import net.wombatrpgs.rainfall.graphics.FourDir;
import net.wombatrpgs.rainfallschema.graphics.FourDirMDO;
import net.wombatrpgs.rainfallschema.maps.EventMDO;

/**
 * Any object static object on a Tiled map is an Event.
 */
public class MapEvent extends MapObject {
	
	protected EventMDO mdo;
	protected FourDir appearance;

	/**
	 * Creates a new event with the specified data at the specified coords.
	 * @param	parent	The parent level of the event
	 * @param 	mdo		The data to create the event with
	 * @param 	x		The x-coord of the event (in pixels)
	 * @param 	y		The y-coord of the event (in pixels)
	 */
	public MapEvent(Level parent, EventMDO mdo, int x, int y) {
		super(parent, x, y);
		this.mdo = mdo;
		if (mdo.appearance != null) {
			FourDirMDO dirMDO = (FourDirMDO) RGlobal.data.getEntryByKey(mdo.appearance);
			appearance = new FourDir(dirMDO, this);
		}
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
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(MapObject other) {
		if (other.y < y) {
			return -1;
		} else if (other.y > y) {
			return 1;
		} else {
			return 0;
		}
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

	/**
	 * This default implementation moves us out of collision.
	 * @see net.wombatrpgs.rainfall.maps.MapObject#onCollide
	 * (net.wombatrpgs.rainfall.collisions.CollisionResult)
	 */
	@Override
	public void onCollide(MapObject other, CollisionResult result) {
		if (!other.isOverlappingAllowed() && !this.isOverlappingAllowed()) {
			// resolve the collision!!
			// flip if we're not primary
			if (this.getHitbox() == result.collide2) {
				result.mtvX *= -1;
				result.mtvY *= -1;
			}
			this.x += result.mtvX;
			this.y += result.mtvY;
		}
	}

	/** @see net.wombatrpgs.rainfall.maps.MapObject#isOverlappingAllowed() */
	@Override
	public boolean isOverlappingAllowed() { return false; }

}
