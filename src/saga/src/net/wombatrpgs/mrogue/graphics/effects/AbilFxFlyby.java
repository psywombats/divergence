/**
 *  AbilFxFlyby.java
 *  Created on Oct 27, 2013 11:44:35 PM for project mrogue-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mrogue.graphics.effects;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.OrthographicCamera;

import net.wombatrpgs.mrogue.core.Constants;
import net.wombatrpgs.mrogue.core.MGlobal;
import net.wombatrpgs.mrogue.graphics.Graphic;
import net.wombatrpgs.mrogue.maps.objects.Picture;
import net.wombatrpgs.mrogue.rpg.abil.Ability;
import net.wombatrpgs.mrogueschema.graphics.effects.AbilFxFlybyMDO;

/**
 * A bunch of pictures mob the enemy.
 */
public class AbilFxFlyby extends AbilFX {
	
	protected AbilFxFlybyMDO mdo;
	protected List<Picture> pix;

	/**
	 * Creates a new abil fx given data, ability.
	 * @param	mdo				The data to generate with
	 * @param	abil			The ability to generate for
	 */
	public AbilFxFlyby(AbilFxFlybyMDO mdo, Ability abil) {
		super(mdo, abil);
		this.mdo = mdo;
		
		pix = new ArrayList<Picture>();
		for (int i = 0; i < mdo.count; i += 1) {
			Graphic g = new Graphic(Constants.SPRITES_DIR, mdo.graphic);
			Picture pic = new Picture(g, 0);
			assets.add(pic);
			assets.add(g);
			pix.add(pic);
		}
	}

	/**
	 * @see net.wombatrpgs.mrogue.graphics.effects.AbilFX#spawn()
	 */
	@Override
	public void spawn() {
		super.spawn();
		float targetY = MGlobal.window.getWidth()/2 +
				(targets.get(0).getY() - MGlobal.hero.getY());
		for (Picture pic : pix) {
			pic.setX(-pic.getWidth() + MGlobal.rand.nextInt(mdo.spreadX*2) - mdo.spreadX);
			pic.setY(-pic.getHeight()/2 + targetY + MGlobal.rand.nextInt(mdo.spreadX*2) - mdo.spreadX);
		}
	}

	/**
	 * @see net.wombatrpgs.mrogue.graphics.effects.AbilFX#render
	 * (com.badlogic.gdx.graphics.OrthographicCamera)
	 */
	@Override
	public void render(OrthographicCamera camera) {
		super.render(camera);
		for (Picture pic : pix) {
			pic.render(camera);
		}
	}

	/**
	 * @see net.wombatrpgs.mrogue.graphics.effects.AbilFX#update(float)
	 */
	@Override
	public void update(float elapsed) {
		super.update(elapsed);
		for (Picture pic : pix) {
			pic.setX(pic.getX() + (elapsed / mdo.duration) *
					(MGlobal.window.getWidth()+pic.getWidth()));
		}
	}

}
