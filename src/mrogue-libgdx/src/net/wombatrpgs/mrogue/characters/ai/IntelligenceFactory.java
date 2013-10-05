/**
 *  IntelligenceFactory.java
 *  Created on Jun 22, 2013 12:37:55 PM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mrogue.characters.ai;

import net.wombatrpgs.mrogue.characters.Enemy;
import net.wombatrpgs.mrogue.core.MGlobal;
import net.wombatrpgs.mrogueschema.characters.ai.BehaviorListMDO;
import net.wombatrpgs.mrogueschema.characters.ai.data.IntelligenceMDO;

/**
 * A factory to generate intelligence subclasses from subclass MDOs.
 */
public class IntelligenceFactory {
	
	/**
	 * Creates intelligences from data.
	 * @param	enemy			The enemy that will be controlled
	 * @param 	mdo				The data to create intelligence from
	 * @return					The created intelligence
	 */
	public static Intelligence create(Enemy actor, IntelligenceMDO mdo) {
		if (BehaviorListMDO.class.isAssignableFrom(mdo.getClass())) {
			return new BehaviorList((BehaviorListMDO) mdo, actor);
		} else {
			MGlobal.reporter.warn("Unknown type of intelligence: " + mdo.getClass());
			return null;
		}
	}

}
