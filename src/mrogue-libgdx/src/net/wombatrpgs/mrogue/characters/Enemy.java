/**
 *  Enemy.java
 *  Created on Jan 23, 2013 9:14:38 PM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mrogue.characters;

import net.wombatrpgs.mrogue.core.MGlobal;
import net.wombatrpgs.mrogue.maps.Level;
import net.wombatrpgs.mrogueschema.characters.EnemyMDO;
import net.wombatrpgs.mrogueschema.maps.data.Direction;

/**
 * The one and only class for those pesky badniks that hunt down the valiant
 * hero and hinder his quest to save the earth.
 */
public class Enemy extends CharacterEvent {
	
	protected EnemyMDO mdo;
	
	/**
	 * Creates a new enemy on a map from a database entry.
	 * @param 	mdo				The MDO with data to create from
	 * @param	object			The object on the map that made us
	 * @param 	parent			The parent map of the object
	 */
	public Enemy(EnemyMDO mdo, Level parent) {
		super(mdo, parent);
		this.mdo = mdo;
	}

	/**
	 * @see net.wombatrpgs.mrogue.characters.CharacterEvent#act()
	 */
	@Override
	public void act() {
		ticksRemaining += 1000;
		Direction dir = Direction.values()[MGlobal.rand.nextInt(Direction.values().length)];
		attemptStep(dir);
	}

}
