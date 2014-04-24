/**
 *  EnemyParty.java
 *  Created on Apr 23, 2014 9:10:52 PM for project saga-desktop
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.saga.rpg;

import java.util.ArrayList;
import java.util.List;

import net.wombatrpgs.sagaschema.rpg.chara.PartyMDO;

/**
 * A party controlled by the enemy AI.
 */
public class EnemyParty extends Party {
	
	protected List<Enemy> allEnemies;

	/**
	 * Superclass constructor.
	 * @param	mdo				The data to create party from
	 */
	public EnemyParty(PartyMDO mdo) {
		super(mdo);
		allEnemies = new ArrayList<Enemy>();
	}
	
	/** @return All enemies in this party */
	public List<Enemy> getEnemies() { return allEnemies; }

	/**
	 * @see net.wombatrpgs.saga.rpg.Party#instantiateChara(java.lang.String)
	 */
	@Override
	protected Chara instantiateChara(String mdoKey) {
		Enemy enemy = new Enemy(mdoKey);
		allEnemies.add(enemy);
		return enemy;
	}

}
