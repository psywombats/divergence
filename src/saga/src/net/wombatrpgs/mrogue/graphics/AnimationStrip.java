/**
 *  DirAnim.java
 *  Created on Nov 13, 2012 10:07:28 AM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mrogue.graphics;

import net.wombatrpgs.mrogue.core.Constants;
import net.wombatrpgs.mrogue.core.MGlobal;
import net.wombatrpgs.mrogue.core.Updateable;
import net.wombatrpgs.mrogue.maps.events.MapEvent;
import net.wombatrpgs.mrogueschema.graphics.AnimationMDO;
import net.wombatrpgs.mrogueschema.graphics.data.AnimationType;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * A specialized animation that makes up one of the four facings. Just a
 * general reminder that the images handled by this class need to be flipped
 * vertically in terms of markup.
 */
public class AnimationStrip implements 	Renderable,
										Updateable,
										PreRenderable {
	
	protected AnimationMDO mdo;
	protected Animation anim;
	protected MapEvent parent;
	
	protected Texture spritesheet;
	protected TextureRegion[] frames;
	protected TextureRegion currentFrame;
	protected boolean queued;
	protected boolean processed;
	
	protected float time;
	protected float maxTime;
	protected float bump;
	protected boolean moving;
	protected boolean looping;
	
	protected Color flashColor;
	protected float flashElapsed;
	protected float flashDuration;
	
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
		queued = false;
		processed = false;
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
	
	/** @return The frame with the given ordinal */
	public TextureRegion getFrame(int frame) { return frames[frame]; }
	
	/** @return The width (in px) of current frames */
	public int getWidth() { return currentFrame.getRegionWidth(); }
	
	/** @return The height (in px) of current frames */
	public int getHeight() { return currentFrame.getRegionHeight(); }
	
	/** @return True if this animation is currently playing */
	public boolean isMoving() { return moving; }

	/**
	 * @see net.wombatrpgs.mrogue.core.Updateable#update(float)
	 */
	@Override
	public void update(float elapsed) {
		if (!processed) {
			MGlobal.reporter.err("Unprocessed strip: " + this + ", key: " + mdo.key);
		}
		if (moving) {
			time += elapsed;
		}
		currentFrame = anim.getKeyFrame(time + bump, looping);
		if (flashColor != null) {
			flashElapsed += elapsed;
			if (flashElapsed > flashDuration) {
				flashColor = null;
			}
		}
	}

	/**
	 * @see net.wombatrpgs.mrogue.graphics.Renderable#render
	 * (com.badlogic.gdx.graphics.OrthographicCamera)
	 */
	@Override
	public void render(OrthographicCamera camera) {
		if (currentFrame != null) {
			Color old = null;
			if (flashColor != null) {
				parent.getParent().getBatch().end();
				old = parent.getParent().getBatch().getColor().cpy();
				float r;
				r = 2f * flashElapsed / (flashDuration);
				if (flashElapsed > flashDuration / 2f) {
					r = 2f - r;
				}
				Color cur = new Color(
						old.r * (1f-r) + flashColor.r * r,
						old.g * (1f-r) + flashColor.g * r,
						old.b * (1f-r) + flashColor.b * r,
						old.a * (1f-r) + flashColor.a * r);
				parent.getParent().getBatch().setColor(cur);
				parent.getParent().getBatch().begin();
			}
			parent.renderLocal(camera, currentFrame, 0, 0, 0);
			if (flashColor != null) {
				parent.getParent().getBatch().end();
				parent.getParent().getBatch().setColor(old);
				parent.getParent().getBatch().begin();
			}
		}
	}

	/**
	 * @see net.wombatrpgs.mrogue.graphics.Renderable#queueRequiredAssets
	 * (com.badlogic.gdx.assets.AssetManager)
	 */
	@Override
	public void queueRequiredAssets(AssetManager manager) {
		MGlobal.assetManager.load(Constants.SPRITES_DIR + mdo.file, Texture.class);
		queued = true;
	}

	/**
	 * @see net.wombatrpgs.rainfall.graphics.Renderable#postProcessing
	 * (com.badlogic.gdx.assets.AssetManager, int pass)
	 */
	@Override
	public void postProcessing(AssetManager manager, int pass) {
		String filename = Constants.SPRITES_DIR+mdo.file;
		if (MGlobal.assetManager.isLoaded(filename)) {
			spritesheet = MGlobal.assetManager.get(filename, Texture.class);
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
			MGlobal.reporter.err("Spritesheet not loaded: " + filename + " from " + this);
		}
		processed = true;
		update(0);
	}
	
	/**
	 * @see net.wombatrpgs.mrogue.graphics.PreRenderable#getRenderX()
	 */
	@Override
	public int getRenderX() {
		return (int) parent.getX();
	}

	/**
	 * @see net.wombatrpgs.mrogue.graphics.PreRenderable#getRenderY()
	 */
	@Override
	public int getRenderY() {
		return (int) parent.getY();
	}

	/**
	 * @see net.wombatrpgs.mrogue.graphics.PreRenderable#getRegions()
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
	 * Flashes a certain color for a certain time.
	 * @param	c				The color to flash
	 * @param	duration		How long the flash should take in total
	 */
	public void flash(Color c, float duration) {
		this.flashColor = c;
		this.flashDuration = duration;
		flashElapsed = 0;
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
