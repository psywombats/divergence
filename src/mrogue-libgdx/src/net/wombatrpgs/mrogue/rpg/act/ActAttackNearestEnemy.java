/**
 *  ActAttackNearestEnemy.java
 *  Created on Oct 16, 2013 4:02:06 AM for project mrogue-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mrogue.rpg.act;

import net.wombatrpgs.mrogue.core.MGlobal;
import net.wombatrpgs.mrogue.rpg.CharacterEvent;

/**
 * Attacks the closest enemy in line of sight.
 */
public class ActAttackNearestEnemy extends Action {
	
	protected ActPathfindToChar pather;

	/**
	 * Creates a new attack step for an actor.
	 * @param	actor			The chara that will be acting
	 */
	public ActAttackNearestEnemy(CharacterEvent actor) {
		super(actor);
		pather = new ActPathfindToChar(actor);
	}
	
	/**
	 * @see net.wombatrpgs.mrogue.rpg.act.Action#act()
	 */
	@Override
	public void act() {
		CharacterEvent best = actor.getUnit().getTarget().getParent();
		if (best == null) {
			MGlobal.reporter.warn("AANE'd but no enemies in range of " + actor);
		} else {
			pather.setTarget(best);
			pather.act();
		}
	}

}
