/**
 *  SceneWait.java
 *  Created on Jan 24, 2014 2:49:05 AM for project saga
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.saga.scenes.commands;

import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.OneArgFunction;

import net.wombatrpgs.saga.core.SGlobal;
import net.wombatrpgs.saga.scenes.SceneCommand;
import net.wombatrpgs.saga.scenes.SceneLib;
import net.wombatrpgs.saga.ui.text.TextBox;
import net.wombatrpgs.sagaschema.io.data.InputCommand;

/**
 * Waits for a certain amount of time to elapse. Given in seconds.
 * Usage: {@code wait(<time>)}
 */
public class SceneSpeak extends OneArgFunction {

	/**
	 * @see org.luaj.vm2.lib.OneArgFunction#call(org.luaj.vm2.LuaValue)
	 */
	@Override
	public LuaValue call(final LuaValue arg) {
		SceneLib.addFunction(new SceneCommand() {
			
			TextBox box;
			boolean blocking;
			boolean setText;
			
			@Override protected void internalRun() {
				blocking = false;
				box = SGlobal.ui.getBox();
				box.fadeIn(parent.getScreen());
				blocking = true;
				setText = false;
			}

			@Override public void update(float elapsed) {
				super.update(elapsed);
				if (box.isTweening()) return;
				if (!setText) {
					box.setLine(arg.toString());
					setText = true;
				}
			}
			
			@Override public boolean onCommand(InputCommand command) {
				if (blocking && command == InputCommand.UI_CONFIRM) {
					if (box.isFinished()) {
						box.fadeOut();
						blocking = false;
					} else {
						box.hurryUp();
					}
					return true;
				}
				return false;
			}
			
			@Override protected boolean shouldFinish() {
				return !blocking && super.shouldFinish();
			}

			@Override protected void finish() {
				super.finish();
			}
			
		});
		return LuaValue.NIL;
	}

}
