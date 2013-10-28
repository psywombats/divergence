/**
 *  BossPicSet.java
 *  Created on Oct 28, 2013 3:30:52 AM for project mrogue-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mrogue.graphics;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import net.wombatrpgs.mrogue.core.MGlobal;
import net.wombatrpgs.mrogue.rpg.Boss;
import net.wombatrpgs.mrogue.screen.ScreenObject;
import net.wombatrpgs.mrogueschema.characters.data.BossPicMDO;

/**
 * Boss game over hack shit.
 */
public class BossPicSet extends ScreenObject {
	
	protected static final String PIC_BLACK = "black.png";
	protected static final float DURATION = 10f;
	protected static final float FADE_TIME = 3f;
	
	protected List<Graphic> goScreens;
	protected Graphic backer;
	protected float total, trueTotal;
	protected float fadeTimer;

	/**
	 * Creates a new pic set from mdo data.
	 * @param mdos
	 */
	public BossPicSet(BossPicMDO[] mdos) {
		goScreens = new ArrayList<Graphic>();
		for (BossPicMDO picMDO : mdos) {
			Graphic g = new Graphic(picMDO.file);
			goScreens.add(g);
			assets.add(g);
		}
		backer = new Graphic(PIC_BLACK);
		assets.add(backer);
		total = 0;
		trueTotal = 0;
	}

	/**
	 * @see net.wombatrpgs.mrogue.screen.ScreenObject#update(float)
	 */
	@Override
	public void update(float elapsed) {
		super.update(elapsed);
		if (trueTotal < DURATION) {
			this.trueTotal += elapsed;
			this.total += elapsed * (1f - (trueTotal/DURATION));
		} else if (fadeTimer < FADE_TIME) {
			fadeTimer += elapsed;
		} else {
			Boss boss = (Boss) MGlobal.levelManager.getActive().getEventByName("boss");
			boss.onStasisEnd();
		}
	}

	/**
	 * @see net.wombatrpgs.mrogue.screen.ScreenObject#render
	 * (com.badlogic.gdx.graphics.OrthographicCamera)
	 */
	@Override
	public void render(OrthographicCamera camera) {
		super.render(camera);
		
		SpriteBatch batch = MGlobal.levelManager.getScreen().getUIBatch();
		Color old = batch.getColor().cpy();
		batch.setColor(1, 1, 1, 1f - fadeTimer/DURATION);
		
		backer.renderAt(batch, 0, 0);
		
		int at = ((int) Math.floor(total * 20f)) % goScreens.size();
		Graphic screen = goScreens.get(at);
		screen.renderAt(batch,
				MGlobal.window.getWidth()/2 - screen.getWidth()/2,
				MGlobal.window.getHeight()/2 - screen.getHeight()/2);
		
		batch.setColor(old);
	}

	/**
	 * @see net.wombatrpgs.mrogue.screen.ScreenObject#getZ()
	 */
	@Override
	public int getZ() {
		return 10;
	}
	
}
