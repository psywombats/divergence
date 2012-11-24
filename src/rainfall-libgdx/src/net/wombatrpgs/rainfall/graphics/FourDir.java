/**
 *  FourDir.java
 *  Created on Nov 12, 2012 11:18:45 AM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.rainfall.graphics;

import net.wombatrpgs.rainfall.RGlobal;
import net.wombatrpgs.rainfall.maps.Dir;
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
	protected Dir currentDir;
	
	/**
	 * Constructs and splices a 4dir.
	 * @param 	mdo			The MDO with relevant data
	 * @param 	parent		The parent this 4dir is tied to
	 */
	public FourDir(FourDirMDO mdo, MapObject parent) {
		this.mdo = mdo;
		this.parent = parent;
		currentDir = Dir.DOWN;
		sliceAnimations();
	}

	/**
	 * Populates the array of animations.
	 */
	protected void sliceAnimations() {
		animations = new DirAnim[Dir.values().length];
		animations[Dir.DOWN.ordinal()] = new DirAnim(
				RGlobal.data.getEntryFor(mdo.downAnim, AnimationMDO.class), parent);
		animations[Dir.UP.ordinal()] = new DirAnim(
				RGlobal.data.getEntryFor(mdo.upAnim, AnimationMDO.class), parent);
		animations[Dir.LEFT.ordinal()] = new DirAnim(
				RGlobal.data.getEntryFor(mdo.leftAnim, AnimationMDO.class), parent);
		animations[Dir.RIGHT.ordinal()] = new DirAnim(
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
		for (Dir dir : Dir.values()) {
			animations[dir.ordinal()].queueRequiredAssets(manager);
		}
	}

	/**
	 * @see net.wombatrpgs.rainfall.graphics.Renderable#postProcessing()
	 */
	@Override
	public void postProcessing() {
		for (Dir dir : Dir.values()) {
			animations[dir.ordinal()].postProcessing();
		}
	}
	
}
