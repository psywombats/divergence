/**
 *  Hud.java
 *  Created on Feb 6, 2013 1:56:43 AM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mrogue.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.BitmapFont.HAlignment;

import net.wombatrpgs.mrogue.core.MGlobal;
import net.wombatrpgs.mrogue.graphics.Graphic;
import net.wombatrpgs.mrogue.ui.text.FontHolder;
import net.wombatrpgs.mrogue.ui.text.TextBoxFormat;
import net.wombatrpgs.mrogueschema.maps.data.OrthoDir;
import net.wombatrpgs.mrogueschema.ui.FontMDO;
import net.wombatrpgs.mrogueschema.ui.HudMDO;
import net.wombatrpgs.mrogueschema.ui.NumberSetMDO;

/**
 * Heads-up display! Everybody's favorite piece of UI. This version is stuck
 * into a UI object and should be told to draw itself as part of a screen.<br>
 * This specific HUD is probably overridden on a per-game basis. Christ knows I
 * just ripped it apart for Rainfall. Ugh.
 */
public class Hud extends UIElement {
	
	protected static final int TEXT_WIDTH = 256;
	
	protected HudMDO mdo;
	
	protected Graphic frame;
	protected Graphic hpBase, hpRib, hpTail;
	protected Graphic mpBase, mpRib, mpTail;
	protected Graphic nhpBase, nhpRib, nhpTail;
	protected Graphic nmpBase, nmpRib, nmpTail;
	protected Graphic alphaMask;
	protected NumberSet numbersHP, numbersMP;
	protected TextBoxFormat format;
	protected FontHolder font;
	
	protected float mp, mmp, hp, mhp;
	
	protected boolean enabled;
	protected boolean ignoresTint;
	protected boolean awaitingReset;
	protected int currentHPDisplay, currentMPDisplay;
	protected float timeToDigitHP, timeToDigitMP;

	/**
	 * Creates a new HUD from data. Requires queueing.
	 * @param 	mdo				The data to create from
	 */
	public Hud(HudMDO mdo) {
		this.mdo = mdo;
		ignoresTint = true;
		
		frame = startGraphic(mdo.frameGraphic);
		alphaMask = startGraphic(mdo.alphaMask);
		
		hpBase = startGraphic(mdo.hpBaseGraphic);
		hpRib = startGraphic(mdo.hpRibGraphic);
		hpTail = startGraphic(mdo.hpTailGraphic);
		mpBase = startGraphic(mdo.mpBaseGraphic);
		mpRib = startGraphic(mdo.mpRibGraphic);
		mpTail = startGraphic(mdo.mpTailGraphic);
		nhpBase = startGraphic(mdo.hpBaseGraphic);
		nhpRib = startGraphic(mdo.nhpRibGraphic);
		nhpTail = startGraphic(mdo.nhpTailGraphic);
		nmpBase = startGraphic(mdo.nmpBaseGraphic);
		nmpRib = startGraphic(mdo.nmpRibGraphic);
		nmpTail = startGraphic(mdo.nmpTailGraphic);
		
		frame.setTextureHeight(mdo.frameHeight);
		frame.setTextureWidth(mdo.frameWidth);
		
		awaitingReset = true;
		currentHPDisplay = 0;
		currentMPDisplay = 0;
		
		numbersHP = new NumberSet(MGlobal.data.getEntryFor(mdo.numberSet, NumberSetMDO.class));
		numbersMP = new NumberSet(MGlobal.data.getEntryFor(mdo.mpNumberSet, NumberSetMDO.class));
		assets.add(numbersHP);
		assets.add(numbersMP);
		
		format = new TextBoxFormat();
		font = new FontHolder(MGlobal.data.getEntryFor(mdo.font, FontMDO.class));
		assets.add(font);
	}
	
	/** @return True if the hud is displaying right now */
	public boolean isEnabled() { return enabled; }
	
	/** @param True if the hud is going to be displayed */
	public void setEnabled(boolean enabled) { this.enabled = enabled; }
	
	/** @see net.wombatrpgs.mrogue.screen.ScreenObject#ignoresTint() */
	@Override public boolean ignoresTint() { return ignoresTint; }

	/**
	 * @see net.wombatrpgs.mrogue.core.Updateable#update(float)
	 */
	@Override
	public void update(float elapsed) {
		if (MGlobal.stasis) return;
		if (awaitingReset) {
			currentHPDisplay = MGlobal.hero.getStats().hp;
			currentMPDisplay = MGlobal.hero.getStats().mp;
			awaitingReset = false;
			timeToDigitHP = 0;
			timeToDigitMP = 0;
		}
		timeToDigitHP += elapsed;
		timeToDigitMP += elapsed;
		while (timeToDigitHP > mdo.digitDelay) {
			timeToDigitHP -= mdo.digitDelay;
			if (currentHPDisplay > MGlobal.hero.getStats().hp) {
				currentHPDisplay -= 1;
			} else if (currentHPDisplay < MGlobal.hero.getStats().hp){
				currentHPDisplay += 1;
			}
		}
		while (timeToDigitMP > mdo.mpDigitDelay) {
			timeToDigitMP -= mdo.mpDigitDelay;
			if (currentMPDisplay > MGlobal.hero.getStats().mp) {
				currentMPDisplay -= 1;
			} else if (currentMPDisplay < MGlobal.hero.getStats().mp){
				currentMPDisplay += 1;
			}
		}
		mhp = MGlobal.hero.getStats().mhp;
		hp = currentHPDisplay;
		mmp = MGlobal.hero.getStats().mmp;
		mp = currentMPDisplay;
		if (MGlobal.raveMode) {
			hp = MGlobal.rand.nextInt((int) (MGlobal.hero.getStats().mhp * 2.5f));
			mp = MGlobal.rand.nextInt((int) (MGlobal.hero.getStats().mmp * 2.5f));
		}
	}

	/**
	 * @see net.wombatrpgs.mrogue.graphics.Renderable#render
	 * (com.badlogic.gdx.graphics.OrthographicCamera)
	 */
	@Override
	public void render(OrthographicCamera camera) {
		SpriteBatch batch = getBatch();
		if (mdo.anchorDir == OrthoDir.SOUTH) {
			float ratioHP = hp/mhp;
			float ratioMP = mp/mmp;
			renderBar(camera, batch, nhpBase, nhpRib, nhpTail, mdo.hpStartX,
					mdo.hpStartY, 1, mdo.hpWidth);
			renderBar(camera, batch, hpBase, hpRib, hpTail, mdo.hpStartX,
					mdo.hpStartY, ratioHP, mdo.hpWidth);
			renderBar(camera, batch, nmpBase, nmpRib, nmpTail, mdo.mpStartX,
					mdo.mpStartY, 1, mdo.mpWidth);
			renderBar(camera, batch, mpBase, mpRib, mpTail, mdo.mpStartX,
					mdo.mpStartY, ratioMP, mdo.mpWidth);
			frame.renderAt(batch, mdo.offX, mdo.offY);
			
			if (ratioHP > .31) {
				numbersHP.renderNumberAt((int) hp,
						mdo.offX + mdo.numOffX,
						mdo.offY + mdo.numOffY,
						.8f, 1, .8f);
			} else if (ratioHP > .11) {
				numbersHP.renderNumberAt((int) hp,
						mdo.offX + mdo.numOffX,
						mdo.offY + mdo.numOffY,
						1, 1, .6f);
			} else if (ratioHP > 0) {
				numbersHP.renderNumberAt((int) hp,
						mdo.offX + mdo.numOffX,
						mdo.offY + mdo.numOffY,
						1, .6f, .6f);
			}
			numbersMP.renderNumberAt((int) mp,
						mdo.offX + mdo.numMPOffX,
						mdo.offY + mdo.numMPOffY,
						.8f, .8f, 1);
			
			// head masking
			batch.setBlendFunction(GL20.GL_ONE, GL20.GL_ZERO);
			Gdx.graphics.getGL20().glColorMask(false, false, false, true);
			alphaMask.renderAt(batch, mdo.offX, mdo.offY);
			batch.setBlendFunction(GL20.GL_ONE_MINUS_DST_ALPHA, GL20.GL_DST_ALPHA);
			Gdx.graphics.getGL20().glColorMask(true, true, true, true);
			batch.begin();
			TextureRegion tex = MGlobal.hero.getAppearance().getFrame(3, 1);
			batch.draw(tex,
					mdo.offX + mdo.headX - tex.getRegionWidth()/2,
					mdo.offY + mdo.headY - tex.getRegionHeight()/2);
			batch.end();
			batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
			
			format.align = HAlignment.LEFT;
			format.x = mdo.offX - 14;
			format.y = (int) (mdo.offY - font.getLineHeight());
			format.width = TEXT_WIDTH;
			format.height = 32;
			String text = "floor: " + MGlobal.hero.getParent().getFloor() + "/13";
			if (!MGlobal.raveMode) font.draw(batch, format, text, 0);
		}
	}
	
	/**
	 * Sets whether the hud ignores tint. It should be ignoring tint for things
	 * like environmental tint but following it for things like transitions.
	 * Basically this solves the old RM problem that pictures would have to fade
	 * out separately if you were using them as a HUD.
	 * @param 	ignoreTint			True if this hud should ignore tint.
	 */
	public void setOverlayTintIgnore(boolean ignoreTint) {
		this.ignoresTint = ignoreTint;
	}
	
	/**
	 * Forces the HUD to update its numerical displays.
	 */
	public void forceReset() {
		currentHPDisplay = MGlobal.hero.getStats().hp;
		currentMPDisplay = MGlobal.hero.getStats().mp;
	}
	
	/** A huge awful method for HP bars */
	private void renderBar(OrthographicCamera camera, SpriteBatch batch,
			Graphic base, Graphic rib, Graphic tail, int startX, int startY,
			float ratio, int width) {
		if (ratio <= 0) return;
		base.renderAt(batch, mdo.offX, mdo.offY);
		rib.renderAt(batch,
				mdo.offX + startX,
				mdo.offY + startY,
				ratio * width / 2.0f,
				1);
		if (ratio == 1) {
			tail.renderAt(batch,
					mdo.offX + width * ratio,
					mdo.offY);
		}
	}

}
