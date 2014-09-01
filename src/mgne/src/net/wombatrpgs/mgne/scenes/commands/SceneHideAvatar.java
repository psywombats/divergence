/**
 *  SceneHideAvatar.java
 *  Created on Aug 31, 2014 6:04:28 PM for project mgne
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mgne.scenes.commands;

import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.OneArgFunction;

import net.wombatrpgs.mgne.core.MGlobal;
import net.wombatrpgs.mgne.scenes.SceneCommand;
import net.wombatrpgs.mgne.scenes.SceneLib;

/**
 * Hides the hero from view, temporarily mostly.
 * Usage: {@code wait(<hidden>)}
 */
public class SceneHideAvatar extends OneArgFunction {

	/**
	 * @see org.luaj.vm2.lib.OneArgFunction#call(org.luaj.vm2.LuaValue)
	 */
	@Override
	public LuaValue call(final LuaValue arg) {
		SceneLib.addFunction(new SceneCommand() {
			
			boolean hide = arg.checkboolean();

			@Override protected void internalRun() {
				if (hide) {
					MGlobal.getHero().hide();
				} else {
					MGlobal.getHero().show();
				}
			}
			
		});
		
		return LuaValue.NIL;
	}

}
