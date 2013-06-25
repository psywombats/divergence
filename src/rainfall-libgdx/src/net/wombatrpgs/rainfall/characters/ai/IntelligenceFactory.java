/**
 *  IntelligenceFactory.java
 *  Created on Jun 22, 2013 12:37:55 PM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.rainfall.characters.ai;

import net.wombatrpgs.rainfall.characters.enemies.EnemyEvent;
import net.wombatrpgs.rainfall.core.RGlobal;
import net.wombatrpgs.rainfallschema.characters.enemies.ai.data.IntelligenceMDO;
import net.wombatrpgs.rainfallschema.characters.enemies.ai.BehaviorListMDO;
import net.wombatrpgs.rainfallschema.characters.enemies.ai.PlayerlikeIntelligenceMDO;

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
	public static Intelligence create(EnemyEvent actor, IntelligenceMDO mdo) {
		if (PlayerlikeIntelligenceMDO.class.isAssignableFrom(mdo.getClass())) {
			return new PlayerlikeIntelligence((PlayerlikeIntelligenceMDO) mdo, actor);
		} else if (BehaviorListMDO.class.isAssignableFrom(mdo.getClass())) {
			return new BehaviorList((BehaviorListMDO) mdo, actor);
		} else {
			RGlobal.reporter.warn("Unknown type of intelligence: " + mdo.getClass());
			return null;
		}
	}

}
