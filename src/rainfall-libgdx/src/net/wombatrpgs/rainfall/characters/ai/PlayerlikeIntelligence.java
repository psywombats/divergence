/**
 *  PlayerlikeIntelligence.java
 *  Created on Jun 22, 2013 12:36:22 PM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.rainfall.characters.ai;

import net.wombatrpgs.rainfall.characters.enemies.EnemyEvent;
import net.wombatrpgs.rainfall.core.RGlobal;
import net.wombatrpgs.rainfallschema.characters.enemies.ai.PlayerlikeIntelligenceMDO;

/**
 * Intelligence for ABS-participants. Moves them like a human would, to some
 * extent. It's an alternative to the doofus Blockbound enemies.
 */
public class PlayerlikeIntelligence extends Intelligence {

	/**
	 * Inherited constructor.
	 * @param	actor			The enemy that will be controlled
	 */
	public PlayerlikeIntelligence(PlayerlikeIntelligenceMDO mdo, EnemyEvent actor) {
		super(mdo, actor);
	}

	/**
	 * @see net.wombatrpgs.rainfall.characters.ai.Intelligence#act()
	 */
	@Override
	public void act() {
		actor.targetLocation(RGlobal.hero.getX(), RGlobal.hero.getY());
	}

}
