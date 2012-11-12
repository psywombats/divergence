/**
 *  Anim.java
 *  Created on Nov 11, 2012 2:04:42 AM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.rainfall.graphics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import net.wombatrpgs.rainfall.RGlobal;
import net.wombatrpgs.rainfallschema.graphics.AnimationMDO;

/**
 * Give MDO, receive animation. Animation is a filmstrip made of frames from a
 * spritesheet. They can be merged together to produce a character sprite.
 */
public class Anim implements Renderable {
	
	protected AnimationMDO mdo;
	
	protected Animation anim;
	protected Texture spritesheet;
	protected SpriteBatch batch;
	
	protected TextureRegion currentFrame;
	protected TextureRegion[] frames;
	protected float time;
	
	/**
	 * Creates a new animation, supposedly ready to render. Make sure the
	 * relevant file has been loaded from the asset manager first.
	 * @param 	mdo		The data necessary for crafting an animation
	 */
	public Anim(AnimationMDO mdo) {
		this.mdo = mdo;
		spritesheet = RGlobal.assetManager.get(RGlobal.SPRITES_DIR+mdo.file, Texture.class);
		frames = new TextureRegion[mdo.frameCount];
		for (int i = 0; i < mdo.frameCount; i++) {
			frames[i] = new TextureRegion(spritesheet,
					mdo.offX + mdo.frameWidth * i,
					mdo.offY,
					mdo.frameWidth,
					mdo.frameHeight);
		}
		anim = new Animation(1.0f/mdo.frameCount, frames);
		batch = new SpriteBatch();
		time = 0f;
	}

	/**
	 * @see net.wombatrpgs.rainfall.graphics.Renderable#render()
	 */
	@Override
	public void render() {
		time += Gdx.graphics.getDeltaTime();
		currentFrame = anim.getKeyFrame(time, true);
		batch.begin();
		batch.draw(currentFrame, 512, 256);
		batch.end();
	}
	
}
