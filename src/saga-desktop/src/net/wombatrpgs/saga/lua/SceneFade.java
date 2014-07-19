/**
 *  SceneFade.java
 *  Created on 2014/07/18 13:52:30 for project saga-desktop
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.saga.lua;

import net.wombatrpgs.mgne.core.MGlobal;
import net.wombatrpgs.mgne.core.interfaces.FinishListener;
import net.wombatrpgs.mgne.scenes.SceneCommand;
import net.wombatrpgs.mgne.scenes.SceneLib;
import net.wombatrpgs.saga.screen.SagaScreen;
import net.wombatrpgs.saga.screen.SagaScreen.FadeType;

import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.OneArgFunction;

/**
 * Fades the screen in, out, or back to normal.
 */
public class SceneFade extends OneArgFunction {

	private static final String ARG_BLACK = "black";
	private static final String ARG_WHITE = "white";
	
	private static FadeType lastFade;
	
	/**
	 * @see org.luaj.vm2.lib.OneArgFunction#call(org.luaj.vm2.LuaValue)
	 */
	@Override
	public LuaValue call(final LuaValue arg) {
		SceneLib.addFunction(new SceneCommand() {
			
			boolean done = false;
			String argString = arg.checkjstring();

			@Override protected void internalRun() {
				done = false;
				SagaScreen current = (SagaScreen) MGlobal.screens.peek();
				FadeType type;
				if (argString.equals(ARG_BLACK)) {
					type = FadeType.TO_BLACK;
				} else if (argString.equals(ARG_WHITE)) {
					type = FadeType.TO_WHITE;
				} else {
					// janky and dumb, this should check the screen instead
					if (lastFade == FadeType.TO_BLACK) {
						type = FadeType.FROM_BLACK;
					} else {
						type = FadeType.FROM_WHITE;
					}
				}
				current.fade(type, new FinishListener() {
					@Override public void onFinish() {
						done = true;
					}
				});
				lastFade = type;
			}

			@Override protected boolean shouldFinish() {
				return super.shouldFinish() && done;
			}
			
		});
		return LuaValue.NIL;
	}

}
