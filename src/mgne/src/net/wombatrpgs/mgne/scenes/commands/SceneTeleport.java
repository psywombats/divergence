/**
 *  SceneTeleport.java
 *  Created on Mar 31, 2014 11:19:55 AM for project mgne
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mgne.scenes.commands;

import net.wombatrpgs.mgne.core.MGlobal;
import net.wombatrpgs.mgne.core.interfaces.FinishListener;
import net.wombatrpgs.mgne.maps.Level;
import net.wombatrpgs.mgne.scenes.SceneCommand;
import net.wombatrpgs.mgne.scenes.SceneLib;

import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.ThreeArgFunction;

/**
 * Teleports hero to some remote locale.
 */
public class SceneTeleport extends ThreeArgFunction {

	/**
	 * @see org.luaj.vm2.lib.ThreeArgFunction#call
	 * (org.luaj.vm2.LuaValue, org.luaj.vm2.LuaValue, org.luaj.vm2.LuaValue)
	 */
	@Override public LuaValue call(final LuaValue map, final LuaValue x, final LuaValue y) {
		SceneLib.addFunction(new SceneCommand() {
			
			String mapName;
			int tileX, tileY;
			boolean teleportFinished;
			
			/* Initializer */ {
				mapName = map.checkjstring();
				tileX = x.checkint();
				tileY = y.checkint();
				teleportFinished = false;
			}

			@Override protected void internalRun() {
				final Level map = MGlobal.levelManager.getLevel(mapName);
				FinishListener onFinish = new FinishListener() {
					@Override public void onFinish() {
						teleportFinished = true;
						map.update(0);
					}
				};
				MGlobal.levelManager.getTele().teleport(mapName,
						tileX,
						map.getHeight() - (tileY+1),
						onFinish);
			}

			@Override protected boolean shouldFinish() {
				return super.shouldFinish() && teleportFinished;
			}
			
		});

		return LuaValue.NIL;
	}

}
