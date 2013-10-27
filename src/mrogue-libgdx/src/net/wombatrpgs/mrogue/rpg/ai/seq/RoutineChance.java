/**
 *  RoutineChance.java
 *  Created on Oct 27, 2013 7:20:04 AM for project mrogue-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mrogue.rpg.ai.seq;

import net.wombatrpgs.mrogue.rpg.CharacterEvent;
import net.wombatrpgs.mrogue.rpg.ai.BTNode;
import net.wombatrpgs.mrogue.rpg.ai.BTSequence;
import net.wombatrpgs.mrogue.rpg.ai.cond.CondChance;

/**
 * A routine chance takes some action if a probability check succeeds.
 */
public class RoutineChance extends BTSequence {
	
	/**
	 * Creates a new chance routine without any content. Will only execute
	 * further children if the first check succeeds.
	 * @param	actor			The actor that will be taking action
	 * @param	chance			The chance this sequence will execute
	 */
	public RoutineChance(CharacterEvent actor, float chance) {
		super(actor);
		addChild(new CondChance(actor, chance));
	}
	
	/**
	 * Creates a new chance routine for the specified character. Executes the
	 * child node only if the RNG is less than chance.
	 * @param	actor			The actor to create the node for
	 * @param	child			The child to conditionally execute
	 * @param	chance			The chance (0-1) that the child will execute
	 */
	public RoutineChance(CharacterEvent actor, BTNode child, float chance) {
		this(actor, chance);
		addChild(child);
	}

}
