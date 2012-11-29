/**
 *  FourDir.java
 *  Created on Nov 12, 2012 11:18:45 AM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.rainfall.graphics;

import net.wombatrpgs.rainfall.collisions.Hitbox;
import net.wombatrpgs.rainfall.core.RGlobal;
import net.wombatrpgs.rainfall.maps.Direction;
import net.wombatrpgs.rainfall.maps.MapObject;
import net.wombatrpgs.rainfallschema.graphics.AnimationMDO;
import net.wombatrpgs.rainfallschema.graphics.FourDirMDO;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;

/**
 * A holder for four different animations that make up a character's up, right,
 * left, and down.
 */
public class FourDir implements Renderable {
	
	protected FourDirMDO mdo;
	protected MapObject parent;
	protected DirAnim[] animations;
	protected Direction currentDir;
	
	/**
	 * Constructs and splices a 4dir.
	 * @param 	mdo			The MDO with relevant data
	 * @param 	parent		The parent this 4dir is tied to
	 */
	public FourDir(FourDirMDO mdo, MapObject parent) {
		this.mdo = mdo;
		this.parent = parent;
		currentDir = Direction.DOWN;
		sliceAnimations();
	}

	/**
	 * Populates the array of animations.
	 */
	protected void sliceAnimations() {
		animations = new DirAnim[Direction.values().length];
		animations[Direction.DOWN.ordinal()] = new DirAnim(
				RGlobal.data.getEntryFor(mdo.downAnim, AnimationMDO.class), parent);
		animations[Direction.UP.ordinal()] = new DirAnim(
				RGlobal.data.getEntryFor(mdo.upAnim, AnimationMDO.class), parent);
		animations[Direction.LEFT.ordinal()] = new DirAnim(
				RGlobal.data.getEntryFor(mdo.leftAnim, AnimationMDO.class), parent);
		animations[Direction.RIGHT.ordinal()] = new DirAnim(
				RGlobal.data.getEntryFor(mdo.rightAnim, AnimationMDO.class), parent);
	}

	/**
	 * @see net.wombatrpgs.rainfall.graphics.Renderable#render
	 * (com.badlogic.gdx.graphics.OrthographicCamera)
	 */
	@Override
	public void render(OrthographicCamera camera) {
		animations[currentDir.ordinal()].render(camera);
	}

	/**
	 * @see net.wombatrpgs.rainfall.graphics.Renderable#queueRequiredAssets
	 * (com.badlogic.gdx.assets.AssetManager)
	 */
	@Override
	public void queueRequiredAssets(AssetManager manager) {
		for (Direction dir : Direction.values()) {
			animations[dir.ordinal()].queueRequiredAssets(manager);
		}
	}

	/**
	 * @see net.wombatrpgs.rainfall.graphics.Renderable#postProcessing
	 * (com.badlogic.gdx.assets.AssetManager)
	 */
	@Override
	public void postProcessing(AssetManager manager) {
		for (Direction dir : Direction.values()) {
			animations[dir.ordinal()].postProcessing(manager);
		}
	}
	
	/**
	 * Starts moving in the indicated direction.
	 * @param 	dir			The new direction to move in
	 */
	public void startMoving(Direction dir) {
		currentDir = dir;
		animations[currentDir.ordinal()].startMoving();
	}
	
	/**
	 * Halts all animation movement.
	 */
	public void stopMoving() {
		animations[currentDir.ordinal()].stopMoving();
	}
	
	/**
	 * Gets the hitbox of the current facing. Usually a rectangle... at least,
	 * that's what's in the database at the moment.
	 * @return				The hitbox of the current facing, 99.9% rect
	 */
	public Hitbox getHitbox() {
		return animations[currentDir.ordinal()].getHitbox();
	}
	
}
