/**
 *  AbilFxTest.java
 *  Created on Oct 18, 2013 7:02:27 AM for project mrogue-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mrogue.graphics.effects;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;

import net.wombatrpgs.mrogue.core.Constants;
import net.wombatrpgs.mrogue.graphics.Graphic;
import net.wombatrpgs.mrogue.rpg.abil.Ability;
import net.wombatrpgs.mrogueschema.graphics.effects.AbilFxTestMDO;

/**
 * Some dumb proof of concept.
 */
public class AbilFxTest extends AbilFX {
	
	protected AbilFxTestMDO mdo;
	protected Graphic sphere;

	/**
	 * Creates an effect from data, parent.
	 * @param	mdo					The data to generate from
	 * @param	abil				The ability to generate for
	 */
	public AbilFxTest(AbilFxTestMDO mdo, Ability abil) {
		super(mdo, abil);
		this.mdo = mdo;
		sphere = new Graphic(Constants.TEXTURES_DIR, mdo.graphic);
		assets.add(sphere);
	}

	/**
	 * @see net.wombatrpgs.mrogue.graphics.effects.AbilFX#render
	 * (com.badlogic.gdx.graphics.OrthographicCamera)
	 */
	@Override
	public void render(OrthographicCamera camera) {
		Texture t = sphere.getTexture();
		float scale = 1f;
//		if (done < .2f) {
//			scale = abil.getRange() * done / .2f;
//		} else if (done > .8f) {
//			scale = abil.getRange() * (1-done) / .2f;
//		} else {
//			scale = abil.getRange();
//		}
		float atX = getX() + parent.getTileWidth()/2f - t.getWidth()*scale/2f;
		float atY = getY() + parent.getTileHeight()/2f - t.getHeight()*scale/2f;
		parent.getBatch().draw(t, atX, atY, scale*t.getWidth(), scale*t.getHeight());
	}

}
