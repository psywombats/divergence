/**
 *  SceneBattle.java
 *  Created on Apr 19, 2014 1:32:17 AM for project saga-desktop
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.saga.lua;

import net.wombatrpgs.mgne.core.MGlobal;
import net.wombatrpgs.mgne.scenes.SceneCommand;
import net.wombatrpgs.mgne.scenes.SceneLib;
import net.wombatrpgs.saga.rpg.Battle;

import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.OneArgFunction;

/**
 * Starts a battle against the party specificied by the argument data key.
 */
public class SceneBattle extends OneArgFunction {

	/**
	 * @see org.luaj.vm2.lib.OneArgFunction#call(org.luaj.vm2.LuaValue)
	 */
	@Override
	public LuaValue call(final LuaValue arg) {
		SceneLib.addFunction(new SceneCommand() {
			
			String mdoKey;
			Battle battle;
			
			/* Initializer */ {
				mdoKey = arg.toString();
				// battle = new Battle(mdoKey);
			}

			@Override protected void addToQueue() {
				super.addToQueue();
				// assets.add(battle);
			}

			@Override protected void internalRun() {
				// battle stuff moved here, problems before with null heroes
				battle = new Battle(mdoKey);
				MGlobal.assets.loadAsset(battle, "scene battle " + mdoKey);
				battle.start();
			}

			@Override protected void finish() {
				super.finish();
				battle.dispose();
			}

			@Override protected boolean shouldFinish() {
				return super.shouldFinish() && battle.isDone();
			}
			
		});
		return LuaValue.NIL;
	}

}
