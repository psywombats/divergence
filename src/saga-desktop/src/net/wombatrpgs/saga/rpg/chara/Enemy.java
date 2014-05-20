/**
 *  Enemy.java
 *  Created on Apr 23, 2014 9:04:42 PM for project saga-desktop
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.saga.rpg.chara;

import net.wombatrpgs.mgne.core.MGlobal;
import net.wombatrpgs.saga.rpg.Battle;
import net.wombatrpgs.saga.rpg.Intent;
import net.wombatrpgs.saga.rpg.items.CombatItem;

/**
 * A character controlled by the AI.
 */
public class Enemy extends Chara {

	/**
	 * Creates an enemy from character data. Characters share a common mdo, but
	 * what class they become depends on context.
	 * @param	mdoKey			The key to the charamdo to create from
	 */
	public Enemy(String mdoKey) {
		super(mdoKey);
	}
	
	/**
	 * Indicate intent as part of a battle. This is the AI move, the enemy is
	 * supposed to come up with an intent formed from one of its combat items
	 * based on, er, relevant battle data. For now it just picks randomly.
	 * @param	battle			The battle this enemy will be acting in
	 * @return					The intent of this enemy in the battle
	 */
	public Intent act(Battle battle) {
		Intent intent = new Intent(this, battle);
		CombatItem item = getRandomCombatItem();
		if (item == null) {
			MGlobal.reporter.err("Monster has no useable abils: " + this);
		} else {
			item.modifyEnemyIntent(intent);
		}
		return intent;
	}
	
}
