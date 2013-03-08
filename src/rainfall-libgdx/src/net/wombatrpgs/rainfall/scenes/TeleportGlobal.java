/**
 *  TeleportSettings.java
 *  Created on Feb 8, 2013 12:08:39 AM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.rainfall.scenes;

import com.badlogic.gdx.assets.AssetManager;

import net.wombatrpgs.rainfall.core.Queueable;
import net.wombatrpgs.rainfall.core.RGlobal;
import net.wombatrpgs.rainfall.maps.Level;
import net.wombatrpgs.rainfall.maps.events.MapEvent;
import net.wombatrpgs.rainfallschema.cutscene.SceneMDO;
import net.wombatrpgs.rainfallschema.settings.TeleportSettingsMDO;

/**
 * A thing to keep track of which scene scripts play before/after teleports
 */
public class TeleportGlobal implements Queueable {
	
	public static final String DEFAULT_MDO_KEY = "default_teleport";
	
	protected TeleportSettingsMDO mdo;
	protected SceneParser preParser, postParser;
	
	/**
	 * Constructs teleport settings from data.
	 * @param 	mdo				The settings to use to construct the settings
	 */
	public TeleportGlobal(TeleportSettingsMDO mdo) {
		this.mdo = mdo;
		preParser = new SceneParser(RGlobal.data.getEntryFor(mdo.pre, SceneMDO.class));
		postParser = new SceneParser(RGlobal.data.getEntryFor(mdo.post, SceneMDO.class));
	}
	
	/** @return The parser to play before a teleport */
	public SceneParser getPre() { return preParser; }
	
	/** @return The parser to play after a teleport */
	public SceneParser getPost() { return postParser; }

	/**
	 * @see net.wombatrpgs.rainfall.core.Queueable#queueRequiredAssets
	 * (com.badlogic.gdx.assets.AssetManager)
	 */
	@Override
	public void queueRequiredAssets(AssetManager manager) {
		preParser.queueRequiredAssets(manager);
		postParser.queueRequiredAssets(manager);
	}

	/**
	 * @see net.wombatrpgs.rainfall.core.Queueable#postProcessing
	 * (com.badlogic.gdx.assets.AssetManager, int)
	 */
	@Override
	public void postProcessing(AssetManager manager, int pass) {
		preParser.postProcessing(manager, pass);
		postParser.postProcessing(manager, pass);
	}
	
	/**
	 * Teleports the hero to the map. This is a core teleport event and doesn't
	 * actually deal with the pre/post stuff... Assumes the teleport affects the
	 * hero and not some other goober.
	 * @param 	map				The level to teleport to
	 * @param 	tileX			The x-coord to teleport to (in tiles);
	 * @param 	tileY			The y-coord to teleport to (in tiles)
	 */
	public void teleport(Level map, int tileX, int tileY) {
		RGlobal.ui.getHud().setOverlayTintIgnore(false);
		MapEvent victim = RGlobal.hero;
		Level old = victim.getLevel();
		int z = old.getZ(victim);
		old.removeEvent(victim);
		old.update(0);
		map.teleportOn(tileX, tileY, z);
		RGlobal.ui.getHud().setOverlayTintIgnore(true);
	}

}
