/**
 *  BattleAnimShader.java
 *  Created on Jul 31, 2014 12:07:33 AM for project saga-desktop
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.saga.graphics.banim;

import net.wombatrpgs.mgne.core.MGlobal;
import net.wombatrpgs.mgne.graphics.ShaderFromData;
import net.wombatrpgs.saga.screen.ScreenBattle;
import net.wombatrpgs.sagaschema.graphics.banim.BattleAnimShaderMDO;
import net.wombatrpgs.sagaschema.graphics.banim.data.ShaderScopeType;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * A battle animation that uses a shader.
 */
public class BattleAnimShader extends BattleAnim {
	
	protected BattleAnimShaderMDO mdo;
	
	protected ShaderFromData shader;

	/**
	 * Creates an animation from data.
	 * @param	mdo				The data to create from
	 */
	public BattleAnimShader(BattleAnimShaderMDO mdo) {
		super(mdo);
		this.mdo = mdo;
		
		shader = MGlobal.graphics.constructShader(mdo.shader);
	}

	/**
	 * @see net.wombatrpgs.mgne.graphics.interfaces.PosRenderable#renderAt
	 * (com.badlogic.gdx.graphics.g2d.SpriteBatch, float, float)
	 */
	@Override
	public void renderAt(SpriteBatch batch, float x, float y) {
		// nothing, obviously
	}

	/**
	 * @see net.wombatrpgs.saga.graphics.banim.BattleAnim#isDone()
	 */
	@Override
	public boolean isDone() {
		return sinceStart >= mdo.duration;
	}

	/**
	 * @see net.wombatrpgs.saga.graphics.banim.BattleAnim#start(ScreenBattle)
	 */
	@Override
	public void start(ScreenBattle screen) {
		super.start(screen);
		if (mdo.scope == ShaderScopeType.ENEMY_AREA) {
			screen.setEnemiesShader(shader);
		} else {
			screen.setPortraitShader(shader);
		}
	}

	/**
	 * @see net.wombatrpgs.saga.graphics.banim.BattleAnim#update(float)
	 */
	@Override
	public void update(float elapsed) {
		super.update(elapsed);
		shader.begin();
		shader.setUniformf("u_elapsed", (sinceStart / mdo.duration));
	}

	/**
	 * @see net.wombatrpgs.saga.graphics.banim.BattleAnim#finish(ScreenBattle)
	 */
	@Override
	public void finish(ScreenBattle screen) {
		super.finish(screen);
		screen.resetEnemiesShader();
		screen.resetPortraitShader();
	}

}
