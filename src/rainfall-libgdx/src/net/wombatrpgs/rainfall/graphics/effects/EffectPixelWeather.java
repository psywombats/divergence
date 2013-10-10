/**
 *  EffectPixelWeather.java
 *  Created on Oct 9, 2013 3:38:05 AM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.rainfall.graphics.effects;

import net.wombatrpgs.rainfall.core.RGlobal;
import net.wombatrpgs.rainfall.graphics.AnimationStrip;
import net.wombatrpgs.rainfall.maps.Level;
import net.wombatrpgs.rainfallschema.graphics.AnimationMDO;
import net.wombatrpgs.rainfallschema.graphics.effects.data.EffectPixelWeatherMDO;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;

/**
 * Pixely tiling weather stuff.
 */
public class EffectPixelWeather extends Effect {
	
	protected EffectPixelWeatherMDO mdo;
	protected AnimationStrip anim;

	/**
	 * Creates a new pixel weather effect from data.
	 * @param	parent			The parent level
	 * @param	mdo				The data to create effect from
	 */
	public EffectPixelWeather(Level parent, EffectPixelWeatherMDO mdo) {
		super(parent, mdo);
		this.mdo = mdo;
		anim = new AnimationStrip(RGlobal.data.getEntryFor(mdo.tex, AnimationMDO.class));
	}
	
	/**
	 * @see net.wombatrpgs.rainfall.graphics.Renderable#render
	 * (com.badlogic.gdx.graphics.OrthographicCamera)
	 */
	@Override
	public void render(OrthographicCamera camera) {
		batch.begin();
		for (int x = 0; x < RGlobal.window.getViewportWidth(); x += anim.getWidth()) {
			for (int y = 0; y < RGlobal.window.getViewportHeight(); y += anim.getHeight()) {
				batch.draw(anim.getRegion(), x, y);
			}
		}
		batch.end();
	}
	
	/**
	 * @see net.wombatrpgs.rainfall.core.Queueable#queueRequiredAssets
	 * (com.badlogic.gdx.assets.AssetManager)
	 */
	@Override
	public void queueRequiredAssets(AssetManager manager) {
		anim.queueRequiredAssets(manager);
	}

	/**
	 * @see net.wombatrpgs.rainfall.core.Queueable#postProcessing
	 * (com.badlogic.gdx.assets.AssetManager, int)
	 */
	@Override
	public void postProcessing(AssetManager manager, int pass) {
		anim.postProcessing(manager, pass);
	}
	
	/**
	 * @see net.wombatrpgs.rainfall.graphics.effects.Effect#update(float)
	 */
	@Override
	public void update(float elapsed) {
		super.update(elapsed);
		if (!anim.isMoving()) {
			anim.startMoving();
		}
		anim.update(elapsed);
	}

	
}
