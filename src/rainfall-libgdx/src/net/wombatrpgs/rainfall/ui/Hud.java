/**
 *  Hud.java
 *  Created on Feb 6, 2013 1:56:43 AM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.rainfall.ui;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import net.wombatrpgs.rainfall.core.Queueable;
import net.wombatrpgs.rainfall.core.RGlobal;
import net.wombatrpgs.rainfall.graphics.Graphic;
import net.wombatrpgs.rainfall.screen.ScreenShowable;
import net.wombatrpgs.rainfallschema.maps.data.Direction;
import net.wombatrpgs.rainfallschema.ui.HudMDO;
import net.wombatrpgs.rainfallschema.ui.NumberSetMDO;

/**
 * Heads-up display! Everybody's favorite piece of UI. This version is stuck
 * into a UI object and should be told to draw itself as part of a screen.<br>
 * This specific HUD is probably overridden on a per-game basis. Christ knows I
 * just ripped it apart for Rainfall. Ugh.
 */
public class Hud implements ScreenShowable,
							Queueable {
	
	protected HudMDO mdo;
	
	protected List<Queueable> assets;
	protected Graphic frame;
	protected Graphic hpBase, hpRib, hpTail;
	protected Graphic mpBase, mpRib, mpTail;
	protected NumberSet numbers;
	
	protected boolean enabled;
	protected boolean ignoresTint;

	/**
	 * Creates a new HUD from data. Requires queueing.
	 * @param 	mdo				The data to create from
	 */
	public Hud(HudMDO mdo) {
		this.mdo = mdo;
		ignoresTint = true;
		assets = new ArrayList<Queueable>();
		
		frame = startGraphic(mdo.frameGraphic);
		hpBase = startGraphic(mdo.hpBaseGraphic);
		hpRib = startGraphic(mdo.hpRibGraphic);
		hpTail = startGraphic(mdo.hpTailGraphic);
		mpBase = startGraphic(mdo.mpBaseGraphic);
		mpRib = startGraphic(mdo.mpRibGraphic);
		mpTail = startGraphic(mdo.mpTailGraphic);
		
		frame.setTextureHeight(mdo.frameHeight);
		frame.setTextureWidth(mdo.frameWidth);
		
		numbers = new NumberSet(RGlobal.data.getEntryFor(mdo.numberSet, NumberSetMDO.class));
		assets.add(numbers);
	}
	
	/** @return True if the hud is displaying right now */
	public boolean isEnabled() { return enabled; }
	
	/** @param True if the hud is going to be displayed */
	public void setEnabled(boolean enabled) { this.enabled = enabled; }
	
	/** @see net.wombatrpgs.rainfall.screen.ScreenShowable#ignoresTint() */
	@Override public boolean ignoresTint() { return ignoresTint; }

	/**
	 * @see net.wombatrpgs.rainfall.core.Updateable#update(float)
	 */
	@Override
	public void update(float elapsed) {
		// TODO: update and set based on HP
	}

	/**
	 * @see net.wombatrpgs.rainfall.graphics.Renderable#render
	 * (com.badlogic.gdx.graphics.OrthographicCamera)
	 */
	@Override
	public void render(OrthographicCamera camera) {
		SpriteBatch batch = RGlobal.screens.peek().getUIBatch();
		if (mdo.anchorDir == Direction.DOWN) {
			float mhp = RGlobal.hero.getStats().getMHP();
			float hp = RGlobal.hero.getHP();
			float mmp = 100;
			float mp = 100;
			float ratioHP = hp/mhp;
			float ratioMP = mp/mmp;
			if (hp > 0) {
				hpBase.renderAt(batch, mdo.offX, mdo.offY);
				hpRib.renderAt(batch,
						mdo.offX + mdo.hpStartX,
						mdo.offY + mdo.hpStartY,
						ratioHP * mdo.hpWidth / 2.0f,
						1);
				hpTail.renderAt(batch,
						mdo.offX + mdo.hpWidth * ratioHP,
						mdo.offY);
			}
			if (mp > 0) {
				mpBase.renderAt(batch, mdo.offX, mdo.offY);
				mpRib.renderAt(batch,
						mdo.offX + mdo.mpStartX,
						mdo.offY + mdo.mpStartY,
						ratioMP * mdo.mpWidth / 2.0f,
						1);
				mpTail.renderAt(batch,
						mdo.offX + mdo.mpWidth * ratioMP,
						mdo.offY);
			}
			frame.renderAt(batch, mdo.offX, mdo.offY);
			if (hp > 0) {
				numbers.renderNumberAt((int) hp,
						mdo.offX + mdo.numOffX,
						mdo.offY + mdo.numOffY);
			}
		}
	}

	/**
	 * @see net.wombatrpgs.rainfall.core.Queueable#queueRequiredAssets
	 * (com.badlogic.gdx.assets.AssetManager)
	 */
	@Override
	public void queueRequiredAssets(AssetManager manager) {
		for (Queueable asset : assets) {
			asset.queueRequiredAssets(manager);
		}
	}

	/**
	 * @see net.wombatrpgs.rainfall.core.Queueable#postProcessing
	 * (com.badlogic.gdx.assets.AssetManager, int)
	 */
	@Override
	public void postProcessing(AssetManager manager, int pass) {
		for (Queueable asset : assets) {
			asset.postProcessing(manager, pass);
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
	 * Initializes a graphic from file name and then adds it to assets.
	 * @param 	fileName		The name of the file to load
	 * @return					The created graphic
	 */
	public Graphic startGraphic(String fileName) {
		Graphic graphic = new Graphic(fileName);
		assets.add(graphic);
		return graphic;
	}

}
