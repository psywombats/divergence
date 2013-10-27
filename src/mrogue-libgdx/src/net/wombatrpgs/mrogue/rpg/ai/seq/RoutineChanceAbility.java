/**
 *  RoutineChanceAbility.java
 *  Created on Oct 27, 2013 7:45:30 AM for project mrogue-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mrogue.rpg.ai.seq;

import net.wombatrpgs.mrogue.rpg.CharacterEvent;
import net.wombatrpgs.mrogue.rpg.abil.AbilEffect;
import net.wombatrpgs.mrogue.rpg.ai.BTSequence;

/**
 * Uses an ability of a certain type with a certain probability.
 */
public class RoutineChanceAbility extends BTSequence {

	/**
	 * Creates a chance ability type routine.
	 * @param	actor			The character that will be taking action
	 * @param	type			The type of action to take
	 * @param	chance			The chance to take that action
	 */
	public RoutineChanceAbility(CharacterEvent actor, Class<? extends AbilEffect> type, float chance) {
		super(actor);
		
		addChild(new RoutineChance(
				actor,
				new RoutineAbilByEffect(actor, type),
				chance));
	}

}
