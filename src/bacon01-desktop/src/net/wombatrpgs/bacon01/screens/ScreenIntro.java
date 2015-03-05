/**
 *  ScreenIntro.java
 *  Created on Jan 30, 2015 2:18:36 AM for project bacon01-desktop
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.bacon01.screens;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import net.wombatrpgs.mgne.core.Constants;
import net.wombatrpgs.mgne.core.MAssets;
import net.wombatrpgs.mgne.core.MGlobal;
import net.wombatrpgs.mgne.core.interfaces.FinishListener;
import net.wombatrpgs.mgne.graphics.AnimationStrip;
import net.wombatrpgs.mgne.graphics.ShaderFromData;
import net.wombatrpgs.mgne.maps.objects.Picture;
import net.wombatrpgs.mgne.screen.Screen;
import net.wombatrpgs.mgne.ui.Graphic;
import net.wombatrpgs.mgneschema.graphics.AnimationMDO;
import net.wombatrpgs.mgneschema.settings.IntroSettingsMDO;

/**
 * bacon
 */
public class ScreenIntro extends Screen {
	
	protected static final float SHIP_X = 94f;
	protected static final float START_Y = 210f;
	protected static final float SWITCH_Y = 40f;
	protected static final float END_Y = 14f;
	
	protected IntroSettingsMDO mdo;
	
	protected Graphic black;
	protected Picture moon, weirdMoon, moonFlash, moonOverlay;
	protected AnimationStrip animFly, animLand;
	protected ShaderFromData shader;
	protected float totalElapsed;
	protected float thresh;
	
	protected boolean sfxd, mutateSFX;
	protected boolean moving;
	
	protected boolean showLand, showFly;
	protected float shipX, shipY;
	protected Color shipColor = Color.WHITE;
	
	public ScreenIntro() {
		mdo = MGlobal.data.getEntryFor(Constants.KEY_INTRO, IntroSettingsMDO.class);
		
		moon = new Picture("moon.png", 0);
		weirdMoon = new Picture("moon_weird.png", 1);
		moonFlash = new Picture("moon_flash.png", 2);
		moonOverlay = new Picture("moon_overlay.png", 3);
		assets.add(moon);
		assets.add(weirdMoon);
		assets.add(moonFlash);
		assets.add(moonOverlay);
		moon.setColor(new Color(1, 1, 1, 1));
		weirdMoon.setColor(new Color(1, 1, 1, 0));
		moonFlash.setColor(new Color(1, 1, 1, 0));
		addChild(moon);
		addChild(weirdMoon);
		addChild(moonFlash);
		addChild(moonOverlay);
		
		black = new Graphic("black.png");
		assets.add(black);
		
		animFly = new AnimationStrip(MGlobal.data.getEntryFor("anim_ship_flying", AnimationMDO.class));
		animLand = new AnimationStrip(MGlobal.data.getEntryFor("anim_ship_landing", AnimationMDO.class));
		assets.add(animFly);
		assets.add(animLand);
		animFly.startMoving();
		animLand.startMoving();
		
		shader = MGlobal.graphics.constructShader("shader_static");
	}
	
	/**
	 * @see net.wombatrpgs.mgne.screen.Screen#postProcessing(net.wombatrpgs.mgne.core.MAssets, int)
	 */
	@Override
	public void postProcessing(MAssets manager, int pass) {
		super.postProcessing(manager, pass);
		moon.setX(moon.getWidth() / 2);
		moon.setY(moon.getHeight() / 2);
		weirdMoon.setX(weirdMoon.getWidth() / 2);
		weirdMoon.setY(weirdMoon.getHeight() / 2);
		moonFlash.setX(moonFlash.getWidth() / 2);
		moonFlash.setY(moonFlash.getHeight() / 2);
		
		black.setTextureWidth(getWidth());
		black.setTextureHeight(getHeight());
	}

	/**
	 * @see net.wombatrpgs.mgne.screen.Screen#update(float)
	 */
	@Override
	public void update(float elapsed) {
		super.update(elapsed);
		totalElapsed += elapsed;
		
		if (totalElapsed <= mdo.timeFlyin) {
			moonOverlay.setX(-1000);
			moonOverlay.setY(-1000);
		} else {
			moonOverlay.setX(moonFlash.getWidth() / 2);
			moonOverlay.setY(moonFlash.getHeight() / 2);
		}
		
		shader.begin();
		shader.setUniformf("u_elapsed", totalElapsed);
		shader.setUniformf("u_thresh", thresh);
		shader.end();
		
		float r;
		if (totalElapsed > mdo.timeExplode && totalElapsed < mdo.timeSkyrip) {
			r = (totalElapsed - mdo.timeExplode) / (mdo.timeSkyrip - mdo.timeExplode);
			
			if (!sfxd) {
				sfxd = true;
				MGlobal.audio.playSFX("intro_explode");
			}
			
			if (r < .035) {
				moonFlash.setColor(new Color(1, 1, 1, 1));
			} else {
				moonFlash.setColor(new Color(1, 1, 1, 0));
			}
			
		} else if (totalElapsed > mdo.timeSkyrip && totalElapsed < mdo.timeGlow) {
			r = (totalElapsed - mdo.timeSkyrip) / (mdo.timeGlow - mdo.timeSkyrip);
			
			if (r < .32) {
				thresh = r / .3f * .5f;
			} else if (r > .7) {
				thresh = ((1f-r) / .3f) * .5f;
			} else {
				thresh = .5f;
			}
			
			float c = 1f-(thresh*2f);
			moon.setColor(new Color(c, c, c, 1));
			
		} else if (totalElapsed > mdo.timeGlow && totalElapsed < mdo.timeWait2) {
			r = (totalElapsed - mdo.timeGlow) / (mdo.timeWait2 - mdo.timeGlow);
			
			thresh = 0;
			int glows = mdo.glowCycles;
			float alpha = (float) (1f - (.5f + Math.cos(r * glows * Math.PI*2) * .5f));
			if (alpha < .1f) {
				mutateSFX = true;
			} else if (alpha > .9f && mutateSFX) {
				MGlobal.audio.playSFX("intro_mutate");
			}
			weirdMoon.setColor(new Color(1f, 1f, 1f, alpha));
			
		} else if (totalElapsed > mdo.timeFlyin && totalElapsed < mdo.timeLand) {
			r = (totalElapsed - mdo.timeFlyin) / (mdo.timeLand - mdo.timeFlyin);
			
			animFly.update(elapsed);
			showFly = true;
			shipX = SHIP_X;
			shipY = START_Y + (SWITCH_Y - START_Y) * r;
			shipColor = new Color(1, 1, 1, Math.min(1f, r*2));
			
		} else if (totalElapsed > mdo.timeLand && totalElapsed < mdo.timeWait3) {
			r = (totalElapsed - mdo.timeLand) / (mdo.timeWait3 - mdo.timeLand);
			
			animLand.update(elapsed);
			showFly = false;
			showLand = true;
			shipX = SHIP_X;
			shipY = SWITCH_Y + (END_Y - SWITCH_Y) * r;
			
		} else if (totalElapsed > mdo.timeEnd) {
			if (!moving) {
				moveToNext();
			}
		}
	}

	/**
	 * @see net.wombatrpgs.mgne.screen.Screen#render(com.badlogic.gdx.graphics.g2d.SpriteBatch)
	 */
	@Override
	public void render(SpriteBatch batch) {
		batch.setShader(shader);
		black.renderAt(batch, black.getWidth() / 2, black.getHeight() / 2);
		batch.setShader(null);
		super.render(batch);
		
		Color oldColor = batch.getColor();
		batch.setColor(shipColor);
		if (showLand) {
			animLand.renderAt(batch, shipX, shipY);
		} else if (showFly) {
			animFly.renderAt(batch, shipX, shipY);
		}
		batch.setColor(oldColor);
		moonOverlay.render(batch);
	}

	protected void moveToNext() {
		moving = true;
		final Screen gameScreen = MGlobal.game.makeLevelScreen();
		MGlobal.assets.loadAsset(gameScreen, "game screen");
		MGlobal.game.readyLevelScreen(gameScreen);
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

	/**
	 * @see net.wombatrpgs.mgne.screen.Screen#dispose()
	 */
	@Override
	public void dispose() {
		super.dispose();
		shader.dispose();
		black.dispose();
	}

}
