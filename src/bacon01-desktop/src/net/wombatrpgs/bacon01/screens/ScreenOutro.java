/**
 *  ScreenOutro.java
 *  Created on Feb 11, 2015 4:02:08 AM for project bacon01-desktop
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.bacon01.screens;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.BitmapFont.HAlignment;

import net.wombatrpgs.bacon01.core.BGlobal;
import net.wombatrpgs.mgne.core.MGlobal;
import net.wombatrpgs.mgne.core.interfaces.FinishListener;
import net.wombatrpgs.mgne.graphics.FacesAnimation;
import net.wombatrpgs.mgne.graphics.FacesAnimationFactory;
import net.wombatrpgs.mgne.screen.Screen;
import net.wombatrpgs.mgne.ui.Graphic;
import net.wombatrpgs.mgne.ui.text.TextFormat;

/**
 *
 */
public class ScreenOutro extends Screen {
	
	protected static final int SHIP_X = 266;
	protected static final int SHIP_Y = 38;
	
	protected TextFormat format;
	protected Graphic moon, moon2;
	protected FacesAnimation ship, ship2;
	protected Graphic sides;
	protected Graphic ending;
	protected String line1, line2, line3, line4;
	protected float time, sinceTakeoff, sinceStall;
	protected boolean stall, moving;
	
	protected Graphic star1, star2, star3;
	
	public ScreenOutro() {
		moon = new Graphic("NewIntro01.png");
		moon2 = new Graphic("NewIntro02.png");
		ship = FacesAnimationFactory.create("anim_ship2");
		ship2 = FacesAnimationFactory.create("anim_ship3");
		sides = new Graphic("outro_sides.png");
		star1 = new Graphic("star1.png");
		star2 = new Graphic("star2.png");
		star3 = new Graphic("star3.png");
		
		assets.add(moon);
		assets.add(moon2);
		assets.add(ship);
		assets.add(ship2);
		assets.add(sides);
		assets.add(star1);
		assets.add(star2);
		assets.add(star3);
		
		line1 = "divergence";
		line2 = "by team psychotic-arcane-ego, February 2015";
		line3 = "Notes found : " + BGlobal.items.countPages() + " / 18";
		line4 = "Ending: " + ((BGlobal.items.countPages() == 18) ? "A" : "B");
		
		format = new TextFormat();
		format.align = HAlignment.CENTER;
		format.x = 0;
		format.y = getHeight() - 16;
		format.width = getWidth();
		format.height = getHeight();
		
		if (BGlobal.items.countPages() == 18) {
			ending = new Graphic("ending02.png");
		} else {
			ending = new Graphic("ending01.png");
		}
		assets.add(ending);
	}

	/**
	 * @see net.wombatrpgs.mgne.screen.Screen#update(float)
	 */
	@Override
	public void update(float elapsed) {
		time += elapsed;
		
		float wait = 2;
		if (time > wait) {
			if (!ship.isMoving()) {
				ship.startMoving();
			}
			sinceTakeoff = time - wait;
		}
		if (stall) {
			sinceStall += elapsed;
		}
		
		super.update(elapsed);
		ship.update(elapsed);
		ship2.update(elapsed);
	}

	/**
	 * @see net.wombatrpgs.mgne.screen.Screen#render(com.badlogic.gdx.graphics.g2d.SpriteBatch)
	 */
	@Override
	public void render(SpriteBatch batch) {
		super.render(batch);
		star2.renderAt(finalBatch, MGlobal.window.getWidth() / 2, MGlobal.window.getHeight() / 2 - sinceStall*3f);
		star3.renderAt(finalBatch, MGlobal.window.getWidth() / 2, MGlobal.window.getHeight() / 2 - sinceStall*7f);
		star2.renderAt(finalBatch, MGlobal.window.getWidth() / 2, MGlobal.window.getHeight()*3 / 2 - sinceStall*3f);
		star3.renderAt(finalBatch, MGlobal.window.getWidth() / 2, MGlobal.window.getHeight()*3 / 2 - sinceStall*7f);
		star1.renderAt(finalBatch, MGlobal.window.getWidth() / 2, MGlobal.window.getHeight()*3 / 2 - sinceStall*1f);
		star1.renderAt(finalBatch, MGlobal.window.getWidth() / 2, MGlobal.window.getHeight() / 2 - sinceStall*1f);
		moon.renderAt(finalBatch, MGlobal.window.getWidth()/2, moon.getHeight()/2 - sinceStall*32f);
		sides.renderAt(batch, getWidth()/2, getHeight()/2);
		
		int shipX = SHIP_X;
		int shipY = SHIP_Y;
		shipY += (sinceTakeoff*sinceTakeoff) * 12f + sinceTakeoff * 1f;
		if (shipY > getHeight()) {
			stall = true;
			shipY = getHeight();
		}
		if (sinceTakeoff > 0) {
			ship.renderAt(finalBatch, shipX, shipY);
		} else {
			ship2.renderAt(finalBatch, shipX, shipY);
		}
		
		String line;
		float off;
		if (sinceTakeoff > 20) {
			line = line4;
			off = 20;
		} else if (sinceTakeoff > 16) {
			line = line3;
			off = 16;
		} else if (sinceTakeoff > 12) {
			line = line2;
			off = 12;
		} else if (sinceTakeoff > 8) {
			line = line1;
			off = 8;
		} else {
			line = "";
			off = 0;
		}
		
		if (sinceTakeoff > 8) {
			float sinceShow = sinceTakeoff - off;
			int charsToShow = (int) (sinceShow * 15f);
			String show1 = line.substring(0, charsToShow >= line.length() ? line.length() : charsToShow);
			MGlobal.ui.getFont().draw(batch, format, show1, 0);
		}
		
		moon2.renderAt(finalBatch, MGlobal.window.getWidth()/2, moon.getHeight()/2 - sinceStall*32f);
		
		float a = (sinceTakeoff - 24) / 2f;
		if (a < 0) a = 0;
		finalBatch.setColor(new Color(1, 1, 1, a));
		ending.renderAt(finalBatch, MGlobal.window.getWidth()/2, MGlobal.window.getHeight()/2);
		finalBatch.setColor(new Color(1, 1, 1, 1));
		
		if (!moving && sinceTakeoff > 30) {
			moving = true;
			final Screen gameScreen = new ScreenTitle();
			MGlobal.assets.loadAsset(gameScreen, "game screen");
			MGlobal.levelManager.getTele().getPre().run();
			MGlobal.levelManager.getTele().getPre().addListener(new FinishListener() {
				@Override public void onFinish() {
					gameScreen.getTint().r = 0;
					gameScreen.getTint().g = 0;
					gameScreen.getTint().b = 0;
					MGlobal.screens.push(gameScreen);
					MGlobal.levelManager.getTele().getPost().run();
				};
			});
		}
	}

}
