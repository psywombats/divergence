/**
 *  Warhead.java
 *  Created on Feb 24, 2014 8:12:20 PM for project tactics-desktop
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.tactics.rpg;

import java.util.List;

import net.wombatrpgs.tacticsschema.rpg.abil.data.WarheadMDO;

/**
 * An action that is performed on a thing has a warhead that happens to that
 * thing. A Warhead instance is associated with an Ability instance and is
 * responsible for applying the effects of that ability on call to the targets.
 * 
 * Despite being called a "warhead" it could still heal you or some shit.
 */
public abstract class Warhead {
	
	protected WarheadMDO mdo;
	protected Ability ability;
	
	/**
	 * Creates a new warhead. This should be invoked by the warhead factory.
	 * @param	mdo				The data to create warhead from
	 * @param	ability			The ability this warhead will be invoked for
	 */
	protected Warhead(WarheadMDO mdo, Ability ability) {
		this.mdo = mdo;
		this.ability = ability;
	}
	
	/**
	 * Called by the ability when it's time to fiiiiiyahhh ugh kill me now.
	 * @param	targets			The jerks to affect
	 */
	public abstract void invoke(List<TacticsController> targets);

}
