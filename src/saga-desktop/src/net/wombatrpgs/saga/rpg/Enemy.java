/**
 *  Enemy.java
 *  Created on Apr 23, 2014 9:04:42 PM for project saga-desktop
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.saga.rpg;

import java.util.ArrayList;
import java.util.List;

import net.wombatrpgs.mgne.core.MGlobal;

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
		List<CombatItem> usable = new ArrayList<CombatItem>();
		for (CombatItem item : getInventory().getItems()) {
			if (item.isBattleUsable()) {
				usable.add(item);
			}
		}
		CombatItem item = usable.get(MGlobal.rand.nextInt(usable.size()));
		item.modifyEnemyIntent(intent);
		return intent;
	}
	
}
