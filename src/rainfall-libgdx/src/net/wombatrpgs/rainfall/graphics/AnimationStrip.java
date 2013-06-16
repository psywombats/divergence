/**
 *  DirAnim.java
 *  Created on Nov 13, 2012 10:07:28 AM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.rainfall.graphics;

import java.util.ArrayList;
import java.util.List;

import net.wombatrpgs.rainfall.core.Constants;
import net.wombatrpgs.rainfall.core.RGlobal;
import net.wombatrpgs.rainfall.core.Updateable;
import net.wombatrpgs.rainfall.maps.events.MapEvent;
import net.wombatrpgs.rainfall.physics.Hitbox;
import net.wombatrpgs.rainfall.physics.RectHitbox;
import net.wombatrpgs.rainfallschema.graphics.AnimationMDO;
import net.wombatrpgs.rainfallschema.graphics.data.AnimationType;
import net.wombatrpgs.rainfallschema.graphics.data.DynamicBoxMDO;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * A specialized animation that makes up one of the four facings
 */
public class AnimationStrip implements 	Renderable,
										Updateable,
										PreRenderable {
	
	protected AnimationMDO mdo;
	protected Animation anim;
	protected MapEvent parent;
	protected Hitbox box;
	protected List<Hitbox> attackBoxes;
	
	protected Texture spritesheet;
	protected TextureRegion[] frames;
	protected TextureRegion currentFrame;
	
	protected float time;
	protected float maxTime;
	protected float bump;
	protected boolean moving;
	protected boolean looping;
	
	/**
	 * Creates a new animation from the relevant information and with the
	 * map object as its positioning parent for rendering.
	 * @param 	mdo			The animation data
	 * @param 	parent		The parent map event
	 */
	public AnimationStrip(AnimationMDO mdo, MapEvent parent) {
		this.mdo = mdo;
		this.parent = parent;
		this.time = 0;
		this.bump = 0;
		this.maxTime = ((float) mdo.frameCount) / ((float) mdo.animSpeed);
		this.moving = false;
		if (mdo.hit1x == null) mdo.hit1x = 0;
		if (mdo.hit1y == null) mdo.hit1y = 0;
		if (mdo.hit2x == null) mdo.hit2x = mdo.frameWidth;
		if (mdo.hit2y == null) mdo.hit2y = mdo.frameHeight;
		box = new RectHitbox(parent, 
				mdo.hit1x, mdo.frameHeight-mdo.hit2y, 
				mdo.hit2x, mdo.frameHeight-mdo.hit1y);
		attackBoxes = new ArrayList<Hitbox>();
		if (mdo.attackBoxes != null && mdo.attackBoxes.length > 0) {
			for (DynamicBoxMDO boxMDO : mdo.attackBoxes) {
				RectHitbox rect = new RectHitbox(parent,
						boxMDO.x1, boxMDO.y1,
						boxMDO.x2, boxMDO.y2);
				attackBoxes.add(rect);
			}
		}
	}
	
	/**
	 * Creates a new animation from the relevant MDO information. Does not
	 * associate a map event for positioning, so that should be supplied later.
	 * @param 	mdo				The data for object creation
	 */
	public AnimationStrip(AnimationMDO mdo) {
		this(mdo, null);
	}
	
	/** @param bump Bump up animation time by some amount */
	public void setBump(float bump) { this.bump = bump; }
	
	/** @return Time elapsed since started playing */
	public float getTime() { return this.time; }
	
	/** @return Time total to elapse */
	public float getMaxTime() { return this.maxTime; }
	
	/** @return How many frames this anim goes through in a second */
	public float getFPS() { return this.mdo.animSpeed; }
	
	/** @return The width (in px) of current frames */
	public int getWidth() { return currentFrame.getRegionWidth(); }
	
	/** @return The height (in px) of current frames */
	public int getHeight() { return currentFrame.getRegionHeight(); }
	
	/** @return True if this animation has attack box markup */
	public boolean hasHitData() { return attackBoxes.size() > 0; }

	/**
	 * @see net.wombatrpgs.rainfall.core.Updateable#update(float)
	 */
	@Override
	public void update(float elapsed) {
		if (moving) {
			time += elapsed;
		}
		currentFrame = anim.getKeyFrame(time + bump, looping);
	}

	/**
	 * @see net.wombatrpgs.rainfall.graphics.Renderable#render
	 * (com.badlogic.gdx.graphics.OrthographicCamera)
	 */
	@Override
	public void render(OrthographicCamera camera) {
		if (currentFrame != null) {
			parent.renderLocal(camera, currentFrame, 0, 0, 0);
		}
	}

	/**
	 * @see net.wombatrpgs.rainfall.graphics.Renderable#queueRequiredAssets
	 * (com.badlogic.gdx.assets.AssetManager)
	 */
	@Override
	public void queueRequiredAssets(AssetManager manager) {
		RGlobal.assetManager.load(Constants.SPRITES_DIR + mdo.file, Texture.class);
	}

	/**
	 * @see net.wombatrpgs.rainfall.graphics.Renderable#postProcessing
	 * (com.badlogic.gdx.assets.AssetManager, int pass)
	 */
	@Override
	public void postProcessing(AssetManager manager, int pass) {
		String filename = Constants.SPRITES_DIR+mdo.file;
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
			RGlobal.reporter.warn("Spritesheet not loaded: " + filename);
		}
		update(0);
	}
	
	/**
	 * @see net.wombatrpgs.rainfall.graphics.PreRenderable#getRenderX()
	 */
	@Override
	public int getRenderX() {
		return (int) parent.getX();
	}

	/**
	 * @see net.wombatrpgs.rainfall.graphics.PreRenderable#getRenderY()
	 */
	@Override
	public int getRenderY() {
		return (int) parent.getY();
	}

	/**
	 * @see net.wombatrpgs.rainfall.graphics.PreRenderable#getRegions()
	 */
	@Override
	public TextureRegion getRegion() {
		return currentFrame;
	}
	
	/**
	 * Determines if this thing should play multiple times. Actually just
	 * consults the MDO.
	 * @return					True if should only play once
	 */
	public boolean runsOnlyOnce() {
		return mdo.mode == AnimationType.PLAY_ONCE;
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
		update(0);
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
	 * Gets the database-defined hitbox of this animation. Usually a rect.
	 * @return					The hitbox used by this anim
	 */
	public Hitbox getHitbox() {
		return box;
	}
	
	/**
	 * Gets the database-defined attack box of the current frame of this
	 * animation. Usually a rectangle. The rules for a mismatch in box number
	 * and frame number are in the MDO comments somewhere.
	 * @return					The hitbox of the current attack of this frame
	 */
	public Hitbox getAttackBox() {
		int frameNo = getFrameNumber();
		int maxBoxes = attackBoxes.size();
		if (getFrameNumber() >= maxBoxes) {
			if (attackBoxes.size() == 0) {
				return null;
			}
			return attackBoxes.get(maxBoxes - 1);
		} else {
			return attackBoxes.get(frameNo);
		}
	}
	
	/**
	 * Determines if this strip has finished playing. Strips on infinite loop
	 * never finish playing...
	 * @return					True if this strip has played once
	 */
	public boolean isFinished() {
		return time > maxTime;
	}
	
	/**
	 * Gives this strip a new position parent.
	 * @param 	parent			The new position parent map event
	 */
	public void setParent(MapEvent parent) {
		this.parent = parent;
		box.setParent(parent);
		for (Hitbox attackBox : attackBoxes) {
			attackBox.setParent(parent);
		}
	}
	
	/**
	 * Calculates the current frame of this animation. This is calculated from
	 * elapsed time. It's protected because the strip should abstract away
	 * frames from its called.
	 * @return					The index of the current frame
	 */
	protected int getFrameNumber() {
		return anim.getKeyFrameIndex(time);
	}

}
