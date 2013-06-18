/**
 *  MoveFactory.java
 *  Created on Apr 11, 2013 8:18:05 PM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.rainfall.characters.moveset;

import net.wombatrpgs.rainfall.characters.CharacterEvent;
import net.wombatrpgs.rainfall.core.RGlobal;
import net.wombatrpgs.rainfallschema.characters.hero.moveset.AttackMDO;
import net.wombatrpgs.rainfallschema.characters.hero.moveset.RollMDO;
import net.wombatrpgs.rainfallschema.characters.hero.moveset.RunMDO;
import net.wombatrpgs.rainfallschema.characters.hero.moveset.data.MoveMDO;

/**
 * Generates moves because they depend on subclass.
 */
public class MoveFactory {
	
	public static MovesetAct generateMove(CharacterEvent actor, String moveKey) {
		MoveMDO moveMDO = RGlobal.data.getEntryFor(moveKey, MoveMDO.class);
		// TODO: it may be possible to generalize this
		MovesetAct act;
		if (RunMDO.class.isAssignableFrom(moveMDO.getClass())) {
			act = new ActRun(actor, (RunMDO) moveMDO);
		} else if (RollMDO.class.isAssignableFrom(moveMDO.getClass())) {
			act = new ActRoll(actor, (RollMDO) moveMDO);
		} else if (AttackMDO.class.isAssignableFrom(moveMDO.getClass())) {
			act = new ActAttack(actor, (AttackMDO) moveMDO);
		} else {
			RGlobal.reporter.warn("Unknown move class: " + moveMDO.getClass());
			act = null;
		}
		return act;
	}

}
