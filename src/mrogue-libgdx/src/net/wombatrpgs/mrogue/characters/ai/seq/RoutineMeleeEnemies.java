/**
 *  RoutineAttackEnemies.java
 *  Created on Oct 16, 2013 3:21:00 AM for project mrogue-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mrogue.characters.ai.seq;

import net.wombatrpgs.mrogue.characters.CharacterEvent;
import net.wombatrpgs.mrogue.characters.ai.BTAction;
import net.wombatrpgs.mrogue.characters.ai.BTSequence;
import net.wombatrpgs.mrogue.characters.ai.act.ActAttackNearestEnemy;
import net.wombatrpgs.mrogue.characters.ai.cond.CondEnemiesSighted;

/**
 * A sequence to seek and destroy enemies in sight. Uses a dumb step-to.
 */
public class RoutineMeleeEnemies extends BTSequence {

	/**
	 * Constructs an attack routine for a given actor.
	 * @param	actor			The character that will be acting
	 */
	public RoutineMeleeEnemies(CharacterEvent actor) {
		super(actor);
		addChild(new CondEnemiesSighted(actor));
		addChild(new BTAction(actor, new ActAttackNearestEnemy(actor), true));
	}

}
