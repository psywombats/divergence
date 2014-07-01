/**
 *  WipeEffect.java
 *  Created on Jun 30, 2014 5:28:53 PM for project saga-desktop
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.saga.graphics;

import com.badlogic.gdx.graphics.glutils.FrameBuffer;

import net.wombatrpgs.mgne.core.interfaces.FinishListener;
import net.wombatrpgs.mgne.graphics.Effect;
import net.wombatrpgs.mgneschema.graphics.ShaderMDO;

/**
 * Effect for fancy screen transitions.
 * 
 * Deprecated, functionality needs to be split across two shaders in csreen.
 */
@Deprecated
public class WipeEffect extends Effect {
	
	protected static final float WIPE_TIME = .6f;	// in s
	
	protected FinishListener onWipeFinish;
	protected float sinceWipe;
	protected boolean wiping;

	/**
	 * Inherited constructor.
	 * @param	mdo				The shader data to use
	 */
	public WipeEffect(ShaderMDO mdo) {
		super(mdo);
	}
	
	/**
	 * @see net.wombatrpgs.mgne.graphics.Effect#update(float)
	 */
	@Override
	public void update(float elapsed) {
		super.update(elapsed);
		if (wiping) {
			sinceWipe += elapsed;
			if (sinceWipe > WIPE_TIME) {
				wiping = false;
				sinceWipe = 0;
				onWipeFinish.onFinish();
			}
		}
	}
	
	/**
	 * @see net.wombatrpgs.mgne.graphics.Effect#apply
	 * (com.badlogic.gdx.graphics.glutils.FrameBuffer)
	 */
	@Override
	public void apply(FrameBuffer original) {
		shader.begin();
		shader.setUniformf("elapsed", sinceWipe / WIPE_TIME);
		
		super.apply(original);
	}

	/**
	 * Fades out the screen with the appropriate transition.
	 * @param	listener		The listener to call when transition is done
	 */
	public void wipe(FinishListener listener) {
		this.onWipeFinish = listener;
		wiping = true;
		sinceWipe = 0;
	}

}
