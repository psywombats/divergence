/**
 *  DirAnim.java
 *  Created on Nov 13, 2012 10:07:28 AM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.rainfall.graphics;

import net.wombatrpgs.mgne.global.Global;
import net.wombatrpgs.rainfall.RGlobal;
import net.wombatrpgs.rainfall.maps.MapObject;
import net.wombatrpgs.rainfallschema.graphics.AnimationMDO;
import net.wombatrpgs.rainfallschema.test.SpriteRenderTestMDO;

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
	protected Texture spritesheet;
	protected TextureRegion[] frames;
	
	protected float time = 0f;
	protected boolean moving;
	
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
	public void setMoving() {
		if (!moving) time = 0;
		moving = true;
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
		TextureRegion currentFrame = anim.getKeyFrame(time, true);
		parent.renderLocal(camera, currentFrame);
	}

	/**
	 * @see net.wombatrpgs.rainfall.graphics.Renderable#queueRequiredAssets
	 * (com.badlogic.gdx.assets.AssetManager)
	 */
	@Override
	public void queueRequiredAssets(AssetManager manager) {
		AnimationMDO animMDO = RGlobal.data.getEntryFor(mdo.file, AnimationMDO.class);
		RGlobal.reporter.inform("We're trying to load from " + RGlobal.SPRITES_DIR + animMDO.file);	
		RGlobal.assetManager.load(RGlobal.SPRITES_DIR + animMDO.file, Texture.class);
	}

	/**
	 * @see net.wombatrpgs.rainfall.graphics.Renderable#postProcessing()
	 */
	@Override
	public void postProcessing() {
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
			anim = new Animation(1.0f/mdo.frameCount, frames);
		} else {
			Global.reporter.warn("Spritesheet not loaded: " + filename);
		}
	}

}
