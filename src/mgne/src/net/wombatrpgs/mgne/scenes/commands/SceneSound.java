/**
 *  SceneSound.java
 *  Created on Sep 17, 2014 11:11:34 PM for project mgne
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mgne.scenes.commands;

import net.wombatrpgs.mgne.core.MGlobal;
import net.wombatrpgs.mgne.scenes.SceneCommand;
import net.wombatrpgs.mgne.scenes.SceneLib;

import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.OneArgFunction;

/**
 * Plays a sound from the sound manager.
 * Usage: {@code playSound(<soundshortcut>)}
 */
public class SceneSound extends OneArgFunction {

	/**
	 * @see org.luaj.vm2.lib.OneArgFunction#call(org.luaj.vm2.LuaValue)
	 */
	@Override
	public LuaValue call(final LuaValue soundArg) {
		SceneLib.addFunction(new SceneCommand() {
			
			String soundKey = soundArg.checkjstring();
			
			@Override protected void internalRun() {
				MGlobal.sfx.play(soundKey);
			}
			
		});
		return LuaValue.NIL;
	}

}
