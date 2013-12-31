/**
 *  RoutineOffenseAbility.java
 *  Created on Oct 27, 2013 8:07:03 AM for project mrogue-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mrogue.rpg.ai.seq;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import net.wombatrpgs.mrogue.rpg.CharacterEvent;
import net.wombatrpgs.mrogue.rpg.ai.BTSequence;
import net.wombatrpgs.mrogue.rpg.ai.RandomSelector;
import net.wombatrpgs.mrogue.rpg.ai.cond.CondChance;
import net.wombatrpgs.mrogueschema.characters.data.AbilityTargetType;

/**
 * Randomly use an offensive ability if targets are in range.
 */
public class RoutineOffenseAbility extends BTSequence {
	
	protected static final List<AbilityTargetType> offenseTargets =
			new ArrayList<AbilityTargetType>(EnumSet.of(
					AbilityTargetType.BALL,
					AbilityTargetType.MELEE,
					AbilityTargetType.PROJECTILE));

	/**
	 * Creates an offensive ability routine.
	 * @param	actor			The actor that will be acting
	 * @param	chance			The chance that the actor will take this action
	 */
	public RoutineOffenseAbility(CharacterEvent actor, float chance) {
		super(actor);
		
		addChild(new CondChance(actor, chance));
		
		RandomSelector sel = new RandomSelector(actor);
		for (AbilityTargetType target : offenseTargets) {
			sel.addChild(new RoutineAbilByTarget(actor, target));
		}
		addChild(sel);
	}
	
	/**
	 * Creates an offensive ability routine that always triggers if available.
	 * @param	actor			The actor that will be acting
	 */
	public RoutineOffenseAbility(CharacterEvent actor) {
		this(actor, 1);
	}

}
