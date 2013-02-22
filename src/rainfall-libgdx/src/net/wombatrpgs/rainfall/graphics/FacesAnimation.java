/**
 *  FacesAnimation.java
 *  Created on Jan 24, 2013 7:45:30 PM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.rainfall.graphics;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import net.wombatrpgs.rainfall.core.Updateable;
import net.wombatrpgs.rainfall.maps.events.MapEvent;
import net.wombatrpgs.rainfall.physics.Hitbox;
import net.wombatrpgs.rainfallschema.graphics.DirMDO;
import net.wombatrpgs.rainfallschema.maps.data.Direction;

/**
 * Any animation used to represent a moving entity on the map. Generally it's
 * a congolomeration of faces that switch out depending on how the entity's
 * moving.
 */
public abstract class FacesAnimation implements Renderable,
												PreRenderable,
												Updateable {
	
	protected static final float FLICKER_DURATION = .3f; // in s
	
	protected DirMDO mdo;
	protected Direction currentDir;
	protected MapEvent parent;
	protected AnimationStrip[] animations;
	protected int facings;
	protected boolean flickering;
	protected float time;
	
	/**
	 * Sets up an animation from data for a specific map.
	 * @param 	mdo				The data to create from
	 * @param 	parent			The parent map to spawn on
	 * @param	facings			How many facings make up this animation
	 */
	public FacesAnimation(DirMDO mdo, MapEvent parent, int facings) {
		this.mdo = mdo;
		this.parent = parent;
		this.facings = facings;
		setFacing(Direction.DOWN);
		animations = new AnimationStrip[facings];
		flickering = false;
		time = 0;
	}
	
	/** @return The direction currently facing */
	public Direction getFacing() { return currentDir; }
	
	/** @param dir The new current direction */
	public void setFacing(Direction dir) { this.currentDir = dir; }
	
	/**
	 * Gets the hitbox of the current facing. Usually a rectangle... at least,
	 * that's what's in the database at the moment.
	 * @return					The hitbox of the current facing, 99.9% rect
	 */
	public Hitbox getHitbox() {
		return animations[currentDirOrdinal()].getHitbox();
	}
	
	/**
	 * The /real/ rendering method.
	 * @see net.wombatrpgs.rainfall.graphics.Renderable#render
	 * (com.badlogic.gdx.graphics.OrthographicCamera)
	 * @param 	camera			The camera used to render
	 */
	public void coreRender(OrthographicCamera camera) {
		animations[currentDirOrdinal()].render(camera);
	}
	
	/**
	 * Gets the width of the current frame. Current frame is selected by child.
	 * @return					The width of current frame in px
	 */
	public int getWidth() {
		return animations[currentDirOrdinal()].getWidth();
	}
	
	/**
	 * Gets the height of the current frame. Current frame is selected by child.
	 * @return					The height of current frame in px
	 */
	public int getHeight() {
		return animations[currentDirOrdinal()].getHeight();
	}

	/**
	 * @see net.wombatrpgs.rainfall.graphics.Renderable#queueRequiredAssets
	 * (com.badlogic.gdx.assets.AssetManager)
	 */
	@Override
	public void queueRequiredAssets(AssetManager manager) {
		for (int i = 0; i < facings; i++) {
			animations[i].queueRequiredAssets(manager);
		}
	}

	/**
	 * @see net.wombatrpgs.rainfall.graphics.Renderable#postProcessing
	 * (com.badlogic.gdx.assets.AssetManager , int)
	 */
	@Override
	public void postProcessing(AssetManager manager, int pass) {
		for (int i = 0; i < facings; i++) {
			animations[i].postProcessing(manager, pass);
		}
	}
	
	/**
	 * There's a coreRender method to override instead
	 * @see net.wombatrpgs.rainfall.graphics.Renderable#render
	 * (com.badlogic.gdx.graphics.OrthographicCamera)
	 */
	@Override
	public final void render(OrthographicCamera camera) {
		if (shouldAppear()) {
			coreRender(camera);
		}
	}

	/**
	 * @see net.wombatrpgs.rainfall.core.Updateable#update(float)
	 */
	@Override
	public void update(float elapsed) {
		time += elapsed;
		for (int i = 0; i < facings; i++) {
			// we could just update the only one being displayed...
			animations[i].update(elapsed);
		}
	}

	/**
	 * @see net.wombatrpgs.rainfall.graphics.PreRenderable#getRenderX()
	 */
	@Override
	public int getRenderX() {
		return parent.getX();
	}

	/**
	 * @see net.wombatrpgs.rainfall.graphics.PreRenderable#getRenderY()
	 */
	@Override
	public int getRenderY() {
		return parent.getY();
	}

	/**
	 * @see net.wombatrpgs.rainfall.graphics.PreRenderable#getRegion()
	 */
	@Override
	public TextureRegion getRegion() {
		if (shouldAppear()) {
			return animations[currentDirOrdinal()].getRegion();
		} else {
			return null;
		}
	}

	/**
	 * Just starts moving.
	 */
	public void startMoving() {
		Direction oldDir = getFacing();
		for (Direction dir : Direction.values()) {
			startMoving(dir);
		}
		setFacing(oldDir);
	}
	
	/**
	 * Starts moving in the indicated direction.
	 * @param 	dir			The new direction to move in
	 */
	public void startMoving(Direction dir) {
		for (int i = 0; i < facings; i++) {
			animations[i].startMoving();
		}
		setFacing(dir);
	}
	
	/**
	 * Halts all animation movement.
	 */
	public void stopMoving() {
		for (int i = 0; i < facings; i++) {
			animations[i].stopMoving();
		}
	}
	
	/**
	 * Resets all the timings on this animation.
	 */
	public void reset() {
		for (int i = 0; i < facings; i++) {
			animations[i].reset();
		}
	}
	
	/**
	 * Turns on/off this animation's flickering.
	 * @param 	flicker			True if we should flicker, false otherwise
	 */
	public void setFlicker(boolean flicker) {
		this.flickering = flicker;
	}
	
	/**
	 * This is used to index into the animations array. Returns the index.
	 * @return					The index of the current anim in the anim array
	 */
	protected abstract int currentDirOrdinal();
	
	/**
	 * Populates the array of animations.
	 */
	protected abstract void sliceAnimations();
	
	/**
	 * Determines if this animation is visible at the moment. This isn't in the
	 * culling sense but more of the "did I drink an invisibility potion"
	 * sense.
	 * @return					True if should be rendered, false otherwise
	 */
	protected boolean shouldAppear() {
		if (flickering) {
			int ms = (int) (time * 1000);
			int d = (int) (FLICKER_DURATION * 1000);
			if (ms % d < d/2) {
				return false;
			}
		}
		return true;
	}

}
