/**
 *  SceneWait.java
 *  Created on Jan 24, 2014 2:49:05 AM for project saga
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mgne.scenes.commands;

import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.OneArgFunction;

import net.wombatrpgs.mgne.core.MGlobal;
import net.wombatrpgs.mgne.scenes.SceneCommand;
import net.wombatrpgs.mgne.scenes.SceneLib;
import net.wombatrpgs.mgne.ui.text.TextBox;
import net.wombatrpgs.mgneschema.io.data.InputCommand;

/**
 * Waits for a certain amount of time to elapse. Given in seconds.
 * Usage: {@code wait(<time>)}
 */
public class SceneSpeak extends OneArgFunction {
	
	protected static final float FADE_TIME = .1f;

	/**
	 * @see org.luaj.vm2.lib.OneArgFunction#call(org.luaj.vm2.LuaValue)
	 */
	@Override
	public LuaValue call(final LuaValue arg) {
		SceneLib.addFunction(new SceneCommand() {
			
			TextBox box;
			String text;
			boolean blocking;
			boolean setText;
			
			/* Initializer */ {
				text = arg.toString();
			}
			
			@Override protected void internalRun() {
				box = MGlobal.ui.getBox();
				box.fadeIn(parent.getScreen(), FADE_TIME);
				blocking = true;
				setText = false;
			}

			@Override public void update(float elapsed) {
				super.update(elapsed);
				if (box.isTweening()) return;
				if (!setText) {
					box.setText(text);
					setText = true;
				}
			}
			
			@Override public boolean onCommand(InputCommand command) {
				if (blocking && command == InputCommand.UI_CONFIRM) {
					if (box.isFinished()) {
						box.fadeOut(FADE_TIME);
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
