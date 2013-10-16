/**
 *  ActAttackNearestEnemy.java
 *  Created on Oct 16, 2013 4:02:06 AM for project mrogue-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mrogue.characters.ai.act;

import net.wombatrpgs.mrogue.characters.CharacterEvent;
import net.wombatrpgs.mrogue.characters.GameUnit;
import net.wombatrpgs.mrogue.core.MGlobal;

/**
 * Attacks the closest enemy in line of sight.
 */
public class ActAttackNearestEnemy extends Action {
	
	protected ActStepChara step;

	/**
	 * Creates a new attack step for an actor.
	 * @param	actor			The chara that will be acting
	 */
	public ActAttackNearestEnemy(CharacterEvent actor) {
		super(actor);
		step = new ActStepChara(actor);
	}
	
	/**
	 * @see net.wombatrpgs.mrogue.characters.ai.act.Action#act()
	 */
	@Override
	public void act() {
		float minDist = Float.MAX_VALUE;
		CharacterEvent best = null;
		for (GameUnit enemy : actor.getUnit().getVisibleEnemies()) {
			float dist = enemy.getParent().distanceTo(actor);
			if (dist < minDist) {
				minDist = dist;
				best = enemy.getParent();
			}
		}
		if (best == null) {
			MGlobal.reporter.warn("AANE'd but no enemies in range of " + actor);
		} else {
			step.setTarget(best);
			step.act();
		}
	}

}
