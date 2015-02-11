/**
 *  ScenePan.java
 *  Created on Feb 10, 2015 2:23:38 AM for project mgne
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mgne.scenes.commands;

import net.wombatrpgs.mgne.core.MGlobal;
import net.wombatrpgs.mgne.core.interfaces.FinishListener;
import net.wombatrpgs.mgne.maps.Positionable;
import net.wombatrpgs.mgne.scenes.SceneCommand;
import net.wombatrpgs.mgne.scenes.SceneLib;

import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.TwoArgFunction;

/**
 *
 */
public class ScenePan extends TwoArgFunction {

	/**
	 * @see org.luaj.vm2.lib.TwoArgFunction#call(org.luaj.vm2.LuaValue, org.luaj.vm2.LuaValue)
	 */
	@Override
	public LuaValue call(final LuaValue arg1, final LuaValue arg2) {
		SceneLib.addFunction(new SceneCommand() {
			
			int stepsX, stepsY;
			boolean finishedPanning;
			
			/* Initializer */ {
				this.stepsX = arg1.checkint();
				this.stepsY = arg2.checkint();
			}
			
			@Override protected void internalRun() {
				final float targetX = MGlobal.getHero().getX() + stepsX * 16;
				final float targetY = MGlobal.getHero().getY() + stepsY * 16;
				MGlobal.screens.peek().getCamera().panTo(new Positionable() {
					@Override public float getX() { return targetX; }
					@Override public float getY() { return targetY; }
				}, new FinishListener() {
					@Override public void onFinish() {
						finishedPanning = true;
					}
				});
			}

			@Override protected boolean shouldFinish() {
				return finishedPanning;
			}
			
		});
		return LuaValue.NIL;
	}

}
