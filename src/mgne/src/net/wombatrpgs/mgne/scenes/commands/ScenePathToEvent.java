/**
 *  ScenPathTo.java
 *  Created on Sep 1, 2014 3:34:45 PM for project mgne
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mgne.scenes.commands;

import java.util.List;

import net.wombatrpgs.mgne.core.MGlobal;
import net.wombatrpgs.mgne.maps.events.MapEvent;
import net.wombatrpgs.mgne.scenes.SceneCommand;
import net.wombatrpgs.mgne.scenes.SceneLib;
import net.wombatrpgs.mgne.util.AStarPathfinder;
import net.wombatrpgs.mgneschema.maps.data.OrthoDir;

import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;
import org.luaj.vm2.lib.VarArgFunction;

/**
 * Oh god, A* pathfinding to another event! Even worse!
 * Usage: {@code path(<event> or <eventname>, <other> or <othername>, [wait])}
 */
public class ScenePathToEvent extends VarArgFunction {
	
	/**
	 * @see org.luaj.vm2.lib.VarArgFunction#invoke(org.luaj.vm2.Varargs)
	 */
	@Override
	public Varargs invoke(final Varargs args) {
		SceneLib.addFunction(new SceneCommand() {
			
			LuaValue eventArg = args.arg(1);
			LuaValue otherArg = args.arg(2);
			boolean wait = args.narg() >= 3 ? args.checkboolean(3) : true;

			@Override protected void internalRun() {
				MapEvent event = argToEvent(eventArg);
				MapEvent other = argToEvent(otherArg);
				AStarPathfinder pather = new AStarPathfinder(
						MGlobal.levelManager.getActive(),
						event.getTileX(), event.getTileY(),
						other.getTileX(), other.getTileY());
				List<OrthoDir> path = pather.getOrthoPath(null);
				if (path != null) {
					event.followPath(path);
				}
			}
			
			@Override protected boolean shouldFinish() {
				if (!super.shouldFinish()) return false;
				if (wait) {
					return !argToLua(eventArg).get("isTracking").call().checkboolean();
				} else {
					return true;
				}
			}
			
		});
		return LuaValue.NIL;
	}
	
}