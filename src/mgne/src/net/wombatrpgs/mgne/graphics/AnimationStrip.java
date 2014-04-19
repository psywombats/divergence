/**
 *  DirAnim.java
 *  Created on Nov 13, 2012 10:07:28 AM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mgne.graphics;

import net.wombatrpgs.mgne.core.Constants;
import net.wombatrpgs.mgne.core.MAssets;
import net.wombatrpgs.mgne.core.MGlobal;
import net.wombatrpgs.mgne.core.interfaces.Updateable;
import net.wombatrpgs.mgne.graphics.interfaces.Disposable;
import net.wombatrpgs.mgne.graphics.interfaces.PosRenderable;
import net.wombatrpgs.mgneschema.graphics.AnimationMDO;
import net.wombatrpgs.mgneschema.graphics.data.AnimationType;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * A specialized animation that makes up one of the four facings. Just a
 * general reminder that the images handled by this class need to be flipped
 * vertically in terms of markup.
 */
public class AnimationStrip implements	PosRenderable,
										Updateable,
										Disposable {
	
	protected AnimationMDO mdo;
	
	protected transient Animation anim;
	protected transient Texture spritesheet;
	protected transient TextureRegion[] frames;
	protected transient TextureRegion currentFrame;
	
	protected float time;
	protected float maxTime;
	protected float bump;
	protected boolean moving;
	protected boolean looping;
	
	protected Color flashColor;
	protected float flashElapsed;
	protected float flashDuration;
	
	/**
	 * Creates a new animation from the relevant information.
	 * @param 	mdo				The animation data
	 */
	public AnimationStrip(AnimationMDO mdo) {
		this.mdo = mdo;
		this.time = 0;
		this.bump = 0;
		this.maxTime = ((float) mdo.frameCount) / ((float) mdo.animSpeed);
		this.moving = false;
		if (mdo.hit1x == null) mdo.hit1x = 0;
		if (mdo.hit1y == null) mdo.hit1y = 0;
		if (mdo.hit2x == null) mdo.hit2x = mdo.frameWidth;
		if (mdo.hit2y == null) mdo.hit2y = mdo.frameHeight;
	}
	
	/** Kryo constructor */
	protected AnimationStrip() { }
	
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
	 * @see net.wombatrpgs.mgne.graphics.interfaces.Disposable#dispose()
	 */
	@Override
	public void dispose() {
		spritesheet.dispose();
	}

	/**
	 * @see net.wombatrpgs.mgne.core.interfaces.Updateable#update(float)
	 */
	@Override
	public void update(float elapsed) {
		if (moving) {
			time += elapsed;
		}
		if (MGlobal.game.synchronizeSprites()) {
			if (!moving) {
				currentFrame = anim.getKeyFrame(0, looping);
			} else {
				// we're calculating seconds elapsed since the game started
				float s = (System.nanoTime()) / 1000000000f;
				currentFrame = anim.getKeyFrame(s, looping);
			}
		} else {
			currentFrame = anim.getKeyFrame(time + bump, looping);
		}
		if (flashColor != null) {
			flashElapsed += elapsed;
			if (flashElapsed > flashDuration) {
				flashColor = null;
			}
		}
	}

	/**
	 * @see net.wombatrpgs.mgne.graphics.interfaces.PosRenderable#renderAt
	 * (com.badlogic.gdx.graphics.g2d.SpriteBatch, float, float)
	 */
	@Override
	public void renderAt(SpriteBatch batch, float x, float y) {
		if (currentFrame != null) {
			Color old = null;
			if (flashColor != null) {
				old = batch.getColor().cpy();
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
				batch.setColor(cur);
			}
			batch.begin();
			batch.draw(currentFrame, x, y);
			batch.end();
			if (flashColor != null) {
				batch.setColor(old);
			}
		}
	}

	/**
	 * @see net.wombatrpgs.mgne.graphics.interfaces.Renderable#queueRequiredAssets
	 * (MAssets)
	 */
	@Override
	public void queueRequiredAssets(MAssets manager) {
		manager.load(Constants.SPRITES_DIR + mdo.file, Texture.class);
	}

	/**
	 * @see net.wombatrpgs.rainfall.graphics.Renderable#postProcessing
	 * (com.badlogic.gdx.assets.AssetManager, int pass)
	 */
	@Override
	public void postProcessing(MAssets manager, int pass) {
		String filename = Constants.SPRITES_DIR+mdo.file;
		if (manager.isLoaded(filename)) {
			spritesheet = manager.get(filename, Texture.class);
			frames = new TextureRegion[mdo.frameCount];
			for (int i = 0; i < mdo.frameCount; i++) {
				frames[i] = new TextureRegion(spritesheet,
						mdo.offX + mdo.frameWidth * i,
						mdo.offY,
						mdo.frameWidth,
						mdo.frameHeight);
			}
			switch(mdo.mode) {
			case REPEAT:
				looping = true;
				anim = new Animation(1.0f/mdo.animSpeed, frames);
				anim.setPlayMode(Animation.LOOP);
				break;
			case DO_NOTHING:
				// big number = infinity
				looping = false;
				anim = new Animation(1000000f, frames);
				anim.setPlayMode(Animation.LOOP);
				break;
			case PLAY_ONCE:
				looping = false;
				anim = new Animation(1.0f/mdo.animSpeed, frames);
				anim.setPlayMode(Animation.NORMAL);
				break;
			}
		} else {
			MGlobal.reporter.err("Spritesheet not loaded: " + filename + " from " + this);
		}
		update(0);
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
	 * Fetches the currently playing frame.
	 * @return					The currently displaying frame, if any
	 */
	public TextureRegion getCurrentFrame() {
		return currentFrame;
	}

	/**
	 * Call this when this direction becomes active.
	 */
	public void switchTo() {
		reset();
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
	 * Calculates the current frame of this animation. This is calculated from
	 * elapsed time. It's protected because the strip should abstract away
	 * frames from its called.
	 * @return					The index of the current frame
	 */
	protected int getFrameNumber() {
		return anim.getKeyFrameIndex(time);
	}

}
