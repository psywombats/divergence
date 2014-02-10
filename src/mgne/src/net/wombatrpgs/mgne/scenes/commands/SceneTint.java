/**
 *  SceneWait.java
 *  Created on Jan 24, 2014 2:32:24 AM for project saga
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mgne.scenes.commands;

import net.wombatrpgs.mgne.scenes.SceneCommand;
import net.wombatrpgs.mgne.scenes.SceneLib;

import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;
import org.luaj.vm2.lib.VarArgFunction;

import com.badlogic.gdx.graphics.Color;

/**
 * Tints the screen a color over a period of time. Time is in s, rgb values are
 * from 0 to 1. If no time is given, assumes 0.
 * Usage: {@code tint(<r>, <g>, <b>, [time])}
 */
public class SceneTint extends VarArgFunction {

	/**
	 * @see org.luaj.vm2.lib.VarArgFunction#invoke(org.luaj.vm2.Varargs)
	 */
	@Override
	public Varargs invoke(final Varargs args) {
		SceneLib.addFunction(new SceneCommand() {
			
			Color tint;
			float r1, g1, b1;
			float r2, g2, b2;
			float time;

			@Override public void update(float elapsed) {
				super.update(elapsed);
				tint(timeSinceStart / timeToWait);
			}

			@Override protected void finish() {
				super.finish();
				tint(1);
			}

			@Override protected void internalRun() {
				tint = parent.getScreen().getTint();
				r1 = tint.r;
				g1 = tint.g;
				b1 = tint.b;
				r2 = args.tofloat(1);
				g2 = args.tofloat(2);
				b2 = args.tofloat(3);
				if (args.narg() < 4) {
					time = 0;
				} else {
					time = args.tofloat(4);
				}
				waitFor(time);
			}
			
			protected void tint(float ratio) {
				if (ratio > 1) ratio = 1;
				tint.r = r1 + ratio * (r2 - r1);
				tint.g = g1 + ratio * (g2 - g1);
				tint.b = b1 + ratio * (b2 - b1);
			}
		});
		
		return LuaValue.NIL;
	}
	
}
