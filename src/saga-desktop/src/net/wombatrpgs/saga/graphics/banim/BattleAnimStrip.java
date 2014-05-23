/**
 *  BattleAnimStrip.java
 *  Created on May 23, 2014 8:42:47 AM for project saga-desktop
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.saga.graphics.banim;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import net.wombatrpgs.mgne.core.Constants;
import net.wombatrpgs.mgne.ui.Graphic;
import net.wombatrpgs.sagaschema.graphics.banim.BattleAnimStripMDO;
import net.wombatrpgs.sagaschema.graphics.banim.data.BattleStepMDO;

/**
 * A strip-based battle animation.
 */
public class BattleAnimStrip extends BattleAnim {
	
	protected BattleAnimStripMDO mdo;
	
	protected Map<BattleStepMDO, Graphic> sprites;
	protected float duration;

	/**
	 * Inherited constructor.
	 * @param	mdo				The data to create from
	 */
	public BattleAnimStrip(BattleAnimStripMDO mdo) {
		super(mdo);
		this.mdo = mdo;
		
		sprites = new HashMap<BattleStepMDO, Graphic>();
		for (BattleStepMDO stepMDO : mdo.steps) {
			Graphic sprite = new Graphic(Constants.SPRITES_DIR, stepMDO.sprite);
			sprites.put(stepMDO, sprite);
			assets.add(sprite);
			float stepEnd = stepMDO.start + stepMDO.duration;
			if (stepEnd > duration) {
				duration = stepEnd;
			}
		}
	}

	/**
	 * The provided x/y should be the center of the target's portrait.
	 * @see net.wombatrpgs.mgne.graphics.interfaces.PosRenderable#renderAt
	 * (com.badlogic.gdx.graphics.g2d.SpriteBatch, float, float)
	 */
	@Override
	public void renderAt(SpriteBatch batch, float x, float y) {
		for (BattleStepMDO stepMDO : mdo.steps) {
			if (sinceStart > stepMDO.start &&
					sinceStart < stepMDO.start + stepMDO.duration) {
				Graphic sprite = sprites.get(stepMDO);
				sprite.renderAt(batch,
						stepMDO.x + x - sprite.getWidth() / 2,
						-stepMDO.y + y - sprite.getHeight() / 2);
			}
		}
	}

	/**
	 * @see net.wombatrpgs.saga.graphics.banim.BattleAnim#isDone()
	 */
	@Override
	public boolean isDone() {
		return sinceStart > duration;
	}

	/**
	 * @see net.wombatrpgs.saga.graphics.banim.BattleAnim#dispose()
	 */
	@Override
	public void dispose() {
		super.dispose();
		for (Graphic sprite : sprites.values()) {
			sprite.dispose();
		}
	}

}
