/**
 *  Ability.java
 *  Created on Feb 24, 2014 7:23:31 PM for project tactics-desktop
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.tactics.rpg;

import net.wombatrpgs.tacticsschema.rpg.abil.AbilityMDO;

/**
 * Something you can do on your turn. This includes bumping into things, casting
 * majick spells, buffing things, etc. It really should be called "action" I
 * guess but w/e. This can be executed by players and enemies. The targeting
 * is dispatched to the controller that will decide on a target by either AI or
 * player control, and the action is actually performed here.
 * 
 * Each unit will have its own instance. (ie no flyweight)
 */
public class Ability {
	
	protected AbilityMDO mdo;
	
	/**
	 * Creates an ability from MDO.
	 * @param	mdo				The data to create ability from
	 */
	public Ability(AbilityMDO mdo) {
		this.mdo = mdo;
	}

}
