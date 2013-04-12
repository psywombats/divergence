/**
 *  MoveFactory.java
 *  Created on Apr 11, 2013 8:18:05 PM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.rainfall.characters.moveset;

import net.wombatrpgs.rainfall.characters.CharacterEvent;
import net.wombatrpgs.rainfall.core.RGlobal;
import net.wombatrpgs.rainfallschema.characters.hero.moveset.PushMDO;
import net.wombatrpgs.rainfallschema.characters.hero.moveset.RunMDO;
import net.wombatrpgs.rainfallschema.characters.hero.moveset.SummonMDO;
import net.wombatrpgs.rainfallschema.characters.hero.moveset.data.MoveMDO;

/**
 * Generates moves because they depend on subclass.
 */
public class MoveFactory {
	
	public static MovesetAct generateMove(CharacterEvent actor, String moveKey) {
		MoveMDO moveMDO = RGlobal.data.getEntryFor(moveKey, MoveMDO.class);
		// TODO: it may be possible to generalize this
		MovesetAct act;
		if (SummonMDO.class.isAssignableFrom(moveMDO.getClass())) {
			act = new ActSummon(actor, (SummonMDO) moveMDO);
		} else if (PushMDO.class.isAssignableFrom(moveMDO.getClass())) {
			act = new ActPush(actor, (PushMDO) moveMDO);
		} else if (RunMDO.class.isAssignableFrom(moveMDO.getClass())) {
			act = new ActRun(actor, (RunMDO) moveMDO);
		} else {
			RGlobal.reporter.warn("Unknown move class: " + moveMDO.getClass());
			act = null;
		}
		return act;
	}

}
