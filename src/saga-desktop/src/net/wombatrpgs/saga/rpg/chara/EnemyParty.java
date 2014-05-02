/**
 *  EnemyParty.java
 *  Created on Apr 23, 2014 9:10:52 PM for project saga-desktop
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.saga.rpg.chara;

import java.util.ArrayList;
import java.util.List;

import net.wombatrpgs.mgne.core.MGlobal;
import net.wombatrpgs.saga.core.SGlobal;
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
	}
	
	/** @return All enemies in this party */
	public List<Enemy> getEnemies() { return allEnemies; }
	
	/**
	 * Calculates how much GP the player should get for beating this party by
	 * summing the death gold of its members.
	 * @return					The gold the player wins for defeating us
	 */
	public int getDeathGold() {
		int gp = 0;
		for (Chara chara : allEnemies) {
			gp += chara.getDeathGold();
		}
		return gp;
	}
	
	/**
	 * Selects a meat-dropping chara that dropped meat this battle. Sometimes
	 * returns null if no characters are meaty or meat is not abundant.
	 * @return					The chara who dropped meat, or null for none
	 */
	public Chara chooseMeatFamily() {
		int chance = SGlobal.settings.getMeatChance();
		if (MGlobal.rand.nextInt(100) > chance) {
			return null;
		}
		List<Chara> candidates = new ArrayList<Chara>();
		for (int i = 0; i < groupCount(); i += 1) {
			Chara candidate = getFront(i);
			if (candidate.getFamily() != null) {
				candidates.add(candidate);
			}
		}
		if (candidates.size() == 0) {
			return null;
		}
		return candidates.get(MGlobal.rand.nextInt(candidates.size()));
	}

	/**
	 * @see net.wombatrpgs.saga.rpg.chara.Party#instantiateChara(java.lang.String)
	 */
	@Override
	protected Chara instantiateChara(String mdoKey) {
		Enemy enemy = new Enemy(mdoKey);
		if (allEnemies == null) {
			allEnemies = new ArrayList<Enemy>();
		}
		allEnemies.add(enemy);
		return enemy;
	}

}
