/**
 *  Hud.java
 *  Created on Feb 6, 2013 1:56:43 AM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.rainfall.ui;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;

import net.wombatrpgs.rainfall.core.Queueable;
import net.wombatrpgs.rainfall.screen.ScreenShowable;
import net.wombatrpgs.rainfallschema.ui.HudMDO;

/**
 * Heads-up display! Everybody's favorite piece of UI. This version is stuck
 * into a UI object and should be told to draw itself as part of a screen.<br>
 * This specific HUD is probably overridden on a per-game basis. Christ knows I
 * just ripped it apart for Rainfall. Ugh.
 */
public class Hud implements ScreenShowable,
							Queueable {
	
	protected HudMDO mdo;
	protected boolean enabled;
	protected boolean ignoresTint;

	/**
	 * Creates a new HUD from data. Requires queueing.
	 * @param 	mdo				The data to create from
	 */
	public Hud(HudMDO mdo) {
		ignoresTint = true;
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
		// TODO: display children
	}

	/**
	 * @see net.wombatrpgs.rainfall.core.Queueable#queueRequiredAssets
	 * (com.badlogic.gdx.assets.AssetManager)
	 */
	@Override
	public void queueRequiredAssets(AssetManager manager) {
		// TODO: assets
	}

	/**
	 * @see net.wombatrpgs.rainfall.core.Queueable#postProcessing
	 * (com.badlogic.gdx.assets.AssetManager, int)
	 */
	@Override
	public void postProcessing(AssetManager manager, int pass) {
		// TODO: assets
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

}
