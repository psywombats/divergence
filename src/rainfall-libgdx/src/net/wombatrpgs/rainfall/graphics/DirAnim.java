/**
 *  DirAnim.java
 *  Created on Nov 13, 2012 10:07:28 AM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.rainfall.graphics;

import net.wombatrpgs.mgne.global.Global;
import net.wombatrpgs.rainfall.collisions.Hitbox;
import net.wombatrpgs.rainfall.collisions.RectHitbox;
import net.wombatrpgs.rainfall.core.RGlobal;
import net.wombatrpgs.rainfall.maps.MapObject;
import net.wombatrpgs.rainfallschema.graphics.AnimationMDO;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * A specialized animation that makes up one of the four facings
 */
public class DirAnim implements Renderable {
	
	protected AnimationMDO mdo;
	protected Animation anim;
	protected MapObject parent;
	protected Hitbox box;
	protected Texture spritesheet;
	protected TextureRegion[] frames;
	
	protected float time = 0f;
	protected boolean moving;
	protected boolean looping;
	
	/**
	 * Creates a new animation from the relevant information and with the
	 * map object as its positioning parent for rendering.
	 * @param 	mdo			The animation data
	 * @param 	parent		The parent map object
	 */
	public DirAnim(AnimationMDO mdo, MapObject parent) {
		this.mdo = mdo;
		this.parent = parent;
		moving = false;
		if (mdo.hit1x == null) mdo.hit1x = 0;
		if (mdo.hit1y == null) mdo.hit1y = 0;
		if (mdo.hit2x == null) mdo.hit2x = mdo.frameWidth;
		if (mdo.hit2y == null) mdo.hit2y = mdo.frameHeight;
		box = new RectHitbox(parent, 
				mdo.hit1x, mdo.frameHeight-mdo.hit2y, 
				mdo.hit2x, mdo.frameHeight-mdo.hit1y);
	}
	
	/**
	 * Call this when this direction becomes active.
	 */
	public void switchTo() {
		time = 0;
		moving = false;
	}
	
	/**
	 * Call when this animation begins to move.
	 */
	public void startMoving() {
		moving = true;
	}
	
	/**
	 * Call when this animation stops moving.
	 */
	public void stopMoving() {
		reset();
		moving = false;
	}
	
	/**
	 * Resets the time and animation status of this animation.
	 */
	public void reset() {
		time = 0;
	}

	/**
	 * @see net.wombatrpgs.rainfall.graphics.Renderable#render
	 * (com.badlogic.gdx.graphics.OrthographicCamera)
	 */
	@Override
	public void render(OrthographicCamera camera) {
		if (moving) {
			time += Gdx.graphics.getDeltaTime();
		}
		TextureRegion currentFrame = anim.getKeyFrame(time, looping);
		parent.renderLocal(camera, currentFrame);
	}

	/**
	 * @see net.wombatrpgs.rainfall.graphics.Renderable#queueRequiredAssets
	 * (com.badlogic.gdx.assets.AssetManager)
	 */
	@Override
	public void queueRequiredAssets(AssetManager manager) {
		RGlobal.assetManager.load(RGlobal.SPRITES_DIR + mdo.file, Texture.class);
	}

	/**
	 * @see net.wombatrpgs.rainfall.graphics.Renderable#postProcessing
	 * (com.badlogic.gdx.assets.AssetManager)
	 */
	@Override
	public void postProcessing(AssetManager manager) {
		String filename = RGlobal.SPRITES_DIR+mdo.file;
		if (RGlobal.assetManager.isLoaded(filename)) {
			spritesheet = RGlobal.assetManager.get(filename, Texture.class);
			frames = new TextureRegion[mdo.frameCount];
			for (int i = 0; i < mdo.frameCount; i++) {
				frames[i] = new TextureRegion(spritesheet,
						mdo.offX + mdo.frameWidth * i,
						mdo.offY,
						mdo.frameWidth,
						mdo.frameHeight);
			}
			anim = new Animation(1.0f/mdo.animSpeed, frames);
			switch(mdo.mode) {
			case REPEAT:
				looping = true;
				anim.setPlayMode(Animation.LOOP);
				break;
			case DO_NOTHING:
				// TODO: that's not right! Do nothing. Make it do nothing.
				looping = false;
				anim.setPlayMode(Animation.LOOP);
				break;
			case PLAY_ONCE:
				looping = false;
				anim.setPlayMode(Animation.NORMAL);
				break;
			}
		} else {
			Global.reporter.warn("Spritesheet not loaded: " + filename);
		}
	}
	
	/**
	 * Gets the database-defined hitbox of this animation. Usually a rect.
	 * @return				The hitbox used by this anim
	 */
	public Hitbox getHitbox() {
		return box;
	}

}
