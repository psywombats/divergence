/**
 *  ActRandAbility.java
 *  Created on Oct 27, 2013 8:18:43 AM for project mrogue-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mrogue.rpg.act;

import java.util.Collections;
import java.util.List;

import net.wombatrpgs.mrogue.core.MGlobal;
import net.wombatrpgs.mrogue.rpg.CharacterEvent;
import net.wombatrpgs.mrogue.rpg.abil.Ability;

/**
 * A superclass for all acts that randomly select an ability based on some
 * filter and availability.
 */
public abstract class ActRandAbility extends Action {
	
	protected Ability nextAbil;
	
	/**
	 * Creates a new ability selection act.
	 * @param	actor			The character that will be acting
	 */
	public ActRandAbility(CharacterEvent actor) {
		super(actor);
	}
	
	/**
	 * Cycles through abilities and selects the next usable one.
	 */
	protected void loadNextAbil() {
		List<Ability> all = filter();
		Collections.shuffle(all);
		for (Ability abil : all) {
			if (actor.getUnit().canUse(abil)) {
				nextAbil = abil;
				break;
			}
		}
	}
	
	/**
	 * @see net.wombatrpgs.mrogue.rpg.act.Action#act()
	 */
	@Override
	public void act() {
		if (nextAbil != null) {
			nextAbil.act();
			loadNextAbil();
		} else {
			MGlobal.reporter.warn("Tried to use an invalid ability type: " + actor + " , " + this);
		}
	}

	/**
	 * @see net.wombatrpgs.mrogue.rpg.act.Action#baseCost()
	 */
	@Override
	public int baseCost() {
		if (nextAbil != null) {
			return nextAbil.baseCost();
		} else {
			return super.baseCost();
		}
	}

	/**
	 * Checks if this action is usable. If used and not usable, will probably
	 * blow up somewhere.
	 * @return					True if this action can be taken
	 */
	public boolean canUse() {
		if (nextAbil == null) {
			loadNextAbil();
		}
		return nextAbil != null;
	}
	
	/**
	 * This should take the actors current abilities and filter them by some
	 * rule.
	 * @return					All abilities that are available for use
	 */
	protected abstract List<Ability> filter();

}
