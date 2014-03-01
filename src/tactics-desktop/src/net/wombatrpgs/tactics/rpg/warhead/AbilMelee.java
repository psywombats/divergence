/**
 *  AbilMelee.java
 *  Created on Feb 24, 2014 10:06:42 PM for project tactics-desktop
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.tactics.rpg.warhead;

import net.wombatrpgs.tactics.rpg.Ability;
import net.wombatrpgs.tactics.rpg.TacticsController;
import net.wombatrpgs.tactics.rpg.Warhead;
import net.wombatrpgs.tacticsschema.rpg.abil.WarMeleeMDO;

/**
 * Punch a dude in the face
 */
public class AbilMelee extends Warhead {
	
	protected WarMeleeMDO mdo;

	/**
	 * Inherited constructor.
	 * @param	mdo				The data to create from
	 * @param	ability			The ability to create for
	 */
	public AbilMelee(WarMeleeMDO mdo, Ability ability) {
		super(mdo, ability);
		this.mdo = mdo;
	}

	/**
	 * @see net.wombatrpgs.tactics.rpg.Warhead#invoke
	 * (net.wombatrpgs.tactics.rpg.TacticsController)
	 */
	@Override
	public void invoke(TacticsController target) {
		target.getUnit().takeDamage(mdo.power, parent.getOwner());
	}

}
