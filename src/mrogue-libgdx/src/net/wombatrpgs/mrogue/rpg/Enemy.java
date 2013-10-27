/**
 *  Enemy.java
 *  Created on Jan 23, 2013 9:14:38 PM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mrogue.rpg;

import net.wombatrpgs.mrogue.maps.Level;
import net.wombatrpgs.mrogue.rpg.ai.BTNode;
import net.wombatrpgs.mrogue.rpg.ai.IntelligenceFactory;
import net.wombatrpgs.mrogueschema.characters.EnemyMDO;

/**
 * The one and only class for those pesky badniks that hunt down the valiant
 * hero and hinder his quest to save the earth.
 */
public class Enemy extends CharacterEvent {
	
	protected EnemyMDO mdo;
	protected BTNode intelligence;
	
	/**
	 * Creates a new enemy on a map from a database entry.
	 * @param 	mdo				The MDO with data to create from
	 * @param	object			The object on the map that made us
	 * @param 	parent			The parent map of the object
	 */
	public Enemy(EnemyMDO mdo, Level parent) {
		super(mdo, parent);
		this.mdo = mdo;
		this.intelligence = IntelligenceFactory.createIntelligence(mdo.intelligence, this);
	}

	/**
	 * @see net.wombatrpgs.mrogue.rpg.CharacterEvent#act()
	 */
	@Override
	public void act() {
		if (unit.getStats().hp <= 0) {
			this.ticksRemaining += 100000;
			return;
		} else {
			intelligence.getStatusAndAct();
		}
	}
	
	/**
	 * Computes the danger level of this enemy. This is equal to the danger of
	 * its archetype plus danger for any addon stats.
	 * @return					The danger of this enemy.
	 */
	public int getDangerLevel() {
		// the rest will come later
		return mdo.danger;
	}

}
