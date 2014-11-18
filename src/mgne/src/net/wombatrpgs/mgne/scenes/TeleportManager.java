/**
 *  TeleportSettings.java
 *  Created on Feb 8, 2013 12:08:39 AM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mgne.scenes;

import net.wombatrpgs.mgne.core.MAssets;
import net.wombatrpgs.mgne.core.MGlobal;
import net.wombatrpgs.mgne.core.interfaces.FinishListener;
import net.wombatrpgs.mgne.core.interfaces.Queueable;
import net.wombatrpgs.mgne.maps.Level;
import net.wombatrpgs.mgneschema.maps.data.OrthoDir;
import net.wombatrpgs.mgneschema.settings.TeleportSettingsMDO;

/**
 * A thing to keep track of which scene scripts play before/after teleports
 */
public class TeleportManager implements Queueable {
	
	public static final String MDO_KEY = "default_teleport";
	public static final String KEY_ENTRY_DIR = "entryDir";
	
	protected TeleportSettingsMDO mdo;
	protected StoredSceneParser preParser, postParser;
	
	/**
	 * Constructs teleport settings from data.
	 * @param 	mdo				The settings to use to construct the settings
	 */
	public TeleportManager(TeleportSettingsMDO mdo) {
		this.mdo = mdo;
		preParser = new StoredSceneParser(mdo.pre);
		postParser = new StoredSceneParser(mdo.post);
	}
	
	/** @return The parser to play before a teleport */
	public StoredSceneParser getPre() { return preParser; }
	
	/** @return The parser to play after a teleport */
	public StoredSceneParser getPost() { return postParser; }

	/**
	 * @see net.wombatrpgs.mgne.core.interfaces.Queueable#queueRequiredAssets
	 * (MAssets)
	 */
	@Override
	public void queueRequiredAssets(MAssets manager) {
		preParser.queueRequiredAssets(manager);
		postParser.queueRequiredAssets(manager);
	}

	/**
	 * @see net.wombatrpgs.mgne.core.interfaces.Queueable#postProcessing
	 * (MAssets, int)
	 */
	@Override
	public void postProcessing(MAssets manager, int pass) {
		preParser.postProcessing(manager, pass);
		postParser.postProcessing(manager, pass);
	}
	
	/**
	 * Teleports the hero to the map, but has to interpret the map. This could
	 * be an actual name of a .tmx file or a database key. This will look to
	 * resolve to a .tmx if it ends with tmx, else resolve to database key.
	 * Calls some listener when the whole teleport is done. Null for either
	 * listener is fine.
	 * @param	mapName			The name of the level to teleport to
	 * @param 	tileX			The x-coord to teleport to (in tiles)
	 * @param 	tileY			The y-coord to teleport to (in tiles)
	 * @param	listener		A listener to call when teleport is done
	 * @param	midListener		A listener to call when screen is faded out
	 */
	public void teleport(String mapName, final int tileX, final int tileY,
			final FinishListener listener, final FinishListener midListener) {
		final Level map = MGlobal.levelManager.getLevel(mapName);
		preParser.run();
		preParser.addListener(new FinishListener() {
			@Override public void onFinish() {
				if (midListener != null) {
					midListener.onFinish();
				}
				teleportRaw(map, tileX, tileY);
				if (listener != null) {
					postParser.addListener(listener);
				}
				postParser.run();
				String entryDir = map.getProperty(KEY_ENTRY_DIR);
				if (entryDir != null && !entryDir.isEmpty()) {
					MGlobal.getHero().setFacing(OrthoDir.valueOf(entryDir));
				}
			};
		});
	}
	
	/**
	 * Same teleport, calls no listener.
	 * @param	mapName			The name of the level to teleport to
	 * @param 	tileX			The x-coord to teleport to (in tiles)
	 * @param 	tileY			The y-coord to teleport to (in tiles)
	 */
	public void teleport(String mapName, int tileX, int tileY) {
		teleport(mapName, tileX, tileY, null, null);
	}
	
	/**
	 * Teleports without a transition to a map given by name.
	 * @param	mapName			The name of the map to teleport to (.tmx?)
	 * @param 	tileX			The x-coord to teleport to (in tiles)
	 * @param 	tileY			The y-coord to teleport to (in tiles)
	 */
	public void teleportRaw(String mapName, int tileX, int tileY) {
		teleportRaw(MGlobal.levelManager.getLevel(mapName), tileX, tileY);
	}
	
	/**
	 * Teleports the hero to the map. This is a core teleport event and doesn't
	 * actually deal with the pre/post stuff... Assumes the teleport affects the
	 * hero and not some other goober.
	 * @param 	map				The level to teleport to
	 * @param 	tileX			The x-coord to teleport to (in tiles)
	 * @param 	tileY			The y-coord to teleport to (in tiles)
	 */
	public void teleportRaw(Level map, int tileX, int tileY) {
		MGlobal.audio.playBGM(map.getBGM());
		Level old = MGlobal.getHero().getParent();
		if (old != null) {
			old.onFocusLost();
		}
		map.addEvent(MGlobal.getHero(), tileX, map.getHeight() - tileY - 1);
		map.onFocusGained();
		MGlobal.getHero().onAddedToMap(map);
		MGlobal.levelManager.getScreen().getCamera().update(0);
		MGlobal.levelManager.setActive(map);
	}

}
