/**
 *  SceneRemoveMember.java
 *  Created on Sep 13, 2014 9:32:04 PM for project saga-desktop
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.saga.lua;

import net.wombatrpgs.mgne.core.MGlobal;
import net.wombatrpgs.mgne.scenes.SceneCommand;
import net.wombatrpgs.mgne.scenes.SceneLib;
import net.wombatrpgs.saga.core.SGlobal;
import net.wombatrpgs.saga.rpg.chara.Chara;

import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.ZeroArgFunction;

/**
 * Removes the 5th visiting member from the party.
 * Usage: {@code removeMember()}
 */
public class SceneRemoveMember extends ZeroArgFunction {

	/**
	 * @see org.luaj.vm2.lib.ZeroArgFunction#call()
	 */
	@Override
	public LuaValue call() {
		SceneLib.addFunction(new SceneCommand() {

			@Override protected void internalRun() {
				if (SGlobal.heroes.size() == 5) {
					Chara hero = SGlobal.heroes.getFront(4);
					SGlobal.heroes.removeHero(hero);
				} else {
					MGlobal.reporter.warn("Tried to remove permanent member!");
				}
			}
			
		});
		return LuaValue.NIL;
	}

}
