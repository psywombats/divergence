/**
 *  AbilMelee.java
 *  Created on Feb 24, 2014 10:06:42 PM for project tactics-desktop
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.tactics.rpg.warhead;

import java.util.List;

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
	protected AbilMelee(WarMeleeMDO mdo, Ability ability) {
		super(mdo, ability);
		this.mdo = mdo;
	}

	/**
	 * @see net.wombatrpgs.tactics.rpg.Warhead#invoke(java.util.List)
	 */
	@Override
	public void invoke(List<TacticsController> targets) {
		System.out.println("PUANCH");
	}

}
