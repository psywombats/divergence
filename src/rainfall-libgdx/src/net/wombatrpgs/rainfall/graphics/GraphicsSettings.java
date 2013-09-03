/**
 *  GraphicsSettings.java
 *  Created on Sep 3, 2013 5:43:35 PM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.rainfall.graphics;

import com.badlogic.gdx.assets.AssetManager;

import net.wombatrpgs.rainfall.core.Queueable;
import net.wombatrpgs.rainfall.core.RGlobal;
import net.wombatrpgs.rainfallschema.graphics.AnimationMDO;
import net.wombatrpgs.rainfallschema.settings.GraphicsSettingsMDO;

/**
 * Settings for graphics? These probably shouldn't get changed much in-game,
 * unless it's from some options menu that's yet to be implemented.
 */
public class GraphicsSettings implements Queueable {
	
	protected GraphicsSettingsMDO mdo;
	protected AnimationStrip shadow;
	
	/**
	 * Creates a new graphics settings from data. Should only be called once per
	 * game lifetime.
	 * @param	mdo				The data to load settings from
	 */
	public GraphicsSettings(GraphicsSettingsMDO mdo) {
		this.mdo = mdo;
		this.shadow = new AnimationStrip(RGlobal.data.getEntryFor(mdo.shadowSprite, AnimationMDO.class));
	}

	/**
	 * @see net.wombatrpgs.rainfall.core.Queueable#queueRequiredAssets
	 * (com.badlogic.gdx.assets.AssetManager)
	 */
	@Override
	public void queueRequiredAssets(AssetManager manager) {
		shadow.queueRequiredAssets(manager);
	}

	/**
	 * @see net.wombatrpgs.rainfall.core.Queueable#postProcessing
	 * (com.badlogic.gdx.assets.AssetManager, int)
	 */
	@Override
	public void postProcessing(AssetManager manager, int pass) {
		shadow.postProcessing(manager, pass);
	}
	
	/**
	 * Retrieves the global shadow object shown beneath characters. Although the
	 * type is AnimationStrip, chances are it's just a single frame.
	 * @return					The shadow sprite
	 */
	public AnimationStrip getShadow() {
		return shadow;
	}
	
	/**
	 * Retrieves the shadow fudge factor.
	 * @return					The shadow offset y, in px
	 */
	public int getShadowY() {
		return mdo.shadowY;
	}

}
