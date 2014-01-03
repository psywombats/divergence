/**
 *  ActAbilityByTarget.java
 *  Created on Oct 27, 2013 8:23:23 AM for project mrogue-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mrogue.rpg.act;

import java.util.ArrayList;
import java.util.List;

import net.wombatrpgs.mrogue.rpg.CharacterEvent;
import net.wombatrpgs.mrogue.rpg.abil.Ability;
import net.wombatrpgs.mrogueschema.characters.data.AbilityTargetType;

/**
 * Randomly uses an ability with the specified targeting type, assuming such a
 * target is in range.
 */
public class ActAbilByTarget extends ActRandAbility {
	
	protected AbilityTargetType type;

	/**
	 * Creates a new random ability routine.
	 * @param	actor			The actor that will be acting
	 * @param	type			The type of ability to use
	 */
	public ActAbilByTarget(CharacterEvent actor, AbilityTargetType type) {
		super(actor);
		this.type = type;
	}

	/**
	 * @see net.wombatrpgs.mrogue.rpg.act.ActRandAbility#filter()
	 */
	@Override
	protected List<Ability> filter() {
		List<Ability> results = new ArrayList<Ability>();
		for (Ability abil : actor.getUnit().getAbilities()) {
			if (abil.getType() == type && abil.anyInRange()) {
				results.add(abil);
			}
		}
		return results;
	}

}
