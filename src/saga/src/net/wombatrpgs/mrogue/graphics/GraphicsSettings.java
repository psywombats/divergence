/**
 *  GraphicsSettings.java
 *  Created on Sep 3, 2013 5:43:35 PM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mrogue.graphics;

import com.badlogic.gdx.assets.AssetManager;

import net.wombatrpgs.mrogue.core.Queueable;
import net.wombatrpgs.mrogueschema.settings.GraphicsSettingsMDO;
import net.wombatrpgs.mrogueschema.settings.data.ShaderEnabledState;
import net.wombatrpgs.mrogueschema.test.data.TestState;

/**
 * Settings for graphics? These probably shouldn't get changed much in-game,
 * unless it's from some options menu that's yet to be implemented. Also
 * contains some global helper methods, like shadow rendering.
 */
public class GraphicsSettings implements Queueable {
	
	/** Max number of tiles an event can have in height, in tiles */
	public static final int MAX_EVENT_HEIGHT = 4;
	
	protected GraphicsSettingsMDO mdo;
	
	/**
	 * Creates a new graphics settings from data. Should only be called once per
	 * game lifetime.
	 * @param	mdo				The data to load settings from
	 */
	public GraphicsSettings(GraphicsSettingsMDO mdo) {
		this.mdo = mdo;

	}

	/**
	 * @see net.wombatrpgs.mrogue.core.Queueable#queueRequiredAssets
	 * (com.badlogic.gdx.assets.AssetManager)
	 */
	@Override
	public void queueRequiredAssets(AssetManager manager) {

	}

	/**
	 * @see net.wombatrpgs.mrogue.core.Queueable#postProcessing
	 * (com.badlogic.gdx.assets.AssetManager, int)
	 */
	@Override
	public void postProcessing(AssetManager manager, int pass) {

	}
	
	/** @return True if we should be running shader debug prints */
	public boolean isShaderDebugEnabled() { return mdo.shaderDebug == TestState.ENABLED; }
	
	public boolean isShaderEnabled() { return mdo.enabled == ShaderEnabledState.ENABLED; }

}
