/**
 *  ActPathfindToChar.java
 *  Created on Oct 16, 2013 3:08:22 PM for project mrogue-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mrogue.rpg.act;

import java.util.List;

import net.wombatrpgs.mrogue.core.MGlobal;
import net.wombatrpgs.mrogue.rpg.CharacterEvent;
import net.wombatrpgs.mrogue.rpg.ai.AStarPathfinder;
import net.wombatrpgs.mrogueschema.maps.data.EightDir;

/**
 * Smart move towards character!
 */
public class ActPathfindToChar extends Action {
	
	protected ActStep step;
	protected AStarPathfinder finder;
	protected CharacterEvent target;
	
	/**
	 * Creates a new pathfind step for an actor.
	 * @param	actor			The chara that will be acting
	 */
	public ActPathfindToChar(CharacterEvent actor) {
		super(actor);
		step = new ActStep(actor);
		finder = new AStarPathfinder();
	}
	
	/**
	 * Creates a pathfinder to a given target for an actor.
	 * @param	actor			The chara that will be acting
	 * @param	target			The target that we'll be hunting
	 */
	public ActPathfindToChar(CharacterEvent actor, CharacterEvent target) {
		this(actor);
		this.target = target;
	}
	
	/**
	 * Starts heading for a new target.
	 * @param	target			The target to home in on
	 */
	public void setTarget(CharacterEvent target) {
		this.target = target;
	}

	/**
	 * @see net.wombatrpgs.mrogue.rpg.act.Action#act()
	 */
	@Override
	public void act() {
		finder.setMap(actor.getParent());
		finder.setStart(actor.getTileX(), actor.getTileY());
		finder.setTarget(target.getTileX(), target.getTileY());
		List<EightDir> path = finder.getPath(actor);
		if (path == null) {
			// there was no path so we'll just head there.
			step.setDirection(actor.directionTo(target));
		} else if (path.size() == 0) {
			MGlobal.reporter.warn("Pathfinding to self?");
			step.setDirection(actor.getFacing().toEight());
			step.act();
		} else {
			step.setDirection(path.get(0));
		}
		step.act();
	}

}
