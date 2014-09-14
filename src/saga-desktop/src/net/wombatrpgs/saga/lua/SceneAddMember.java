/**
 *  SceneAddMember.java
 *  Created on Sep 13, 2014 9:21:40 PM for project saga-desktop
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.saga.lua;

import net.wombatrpgs.mgne.scenes.SceneCommand;
import net.wombatrpgs.mgne.scenes.SceneLib;
import net.wombatrpgs.saga.core.SGlobal;
import net.wombatrpgs.saga.rpg.chara.Chara;

import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.OneArgFunction;

/**
 * Adds another member to the party. Usually used for the 5th member NPCs.
 * Usage: {@code addMember(<charamdo>)}
 */
public class SceneAddMember extends OneArgFunction {

	/**
	 * @see org.luaj.vm2.lib.OneArgFunction#call(org.luaj.vm2.LuaValue)
	 */
	@Override
	public LuaValue call(final LuaValue arg) {
		SceneLib.addFunction(new SceneCommand() {
			
			String mdoKey;
			Chara chara;
			
			/* Initializer */ {
				mdoKey = arg.checkjstring();
				chara = new Chara(mdoKey);
			}
			
			@Override protected void addToQueue() {
				super.addToQueue();
				assets.add(chara);
			}
			
			@Override protected void internalRun() {
				SGlobal.heroes.addHero(chara);
			}
			
		});
		return LuaValue.NIL;
	}

}
