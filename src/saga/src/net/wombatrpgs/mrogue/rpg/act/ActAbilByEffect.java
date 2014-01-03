/**
 *  ActTeleport.java
 *  Created on Oct 27, 2013 7:27:02 AM for project mrogue-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mrogue.rpg.act;

import java.util.ArrayList;
import java.util.List;

import net.wombatrpgs.mrogue.rpg.CharacterEvent;
import net.wombatrpgs.mrogue.rpg.abil.AbilEffect;
import net.wombatrpgs.mrogue.rpg.abil.Ability;

/**
 * Creates a new action that randomly uses an ability from all the abilities
 * that the character knows of that type.
 */
public class ActAbilByEffect extends ActRandAbility {
	
	protected Ability nextAbil;
	protected Class<? extends AbilEffect> type;

	/**
	 * Creates a new rand ability for the specified actor.
	 * @param	actor			The actor that will be performing
	 * @param	type			The effect type of ability to use
	 */
	public ActAbilByEffect(CharacterEvent actor, Class<? extends AbilEffect> type) {
		super(actor);
		this.type = type;
		loadNextAbil();
	}

	/**
	 * @see net.wombatrpgs.mrogue.rpg.act.ActRandAbility#filter()
	 */
	@Override
	protected List<Ability> filter() {
		List<Ability> results = new ArrayList<Ability>();
		for (Ability abil : actor.getUnit().getAbilities()) {
			if (type.isAssignableFrom(abil.getEffectClass())) {
				results.add(abil);
			}
		}
		return results;
	}
	
}
