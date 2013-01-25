/**
 *  Enemy.java
 *  Created on Jan 23, 2013 9:14:38 PM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.rainfall.characters.enemies;

import net.wombatrpgs.rainfall.characters.CharacterEvent;
import net.wombatrpgs.rainfall.maps.Level;
import net.wombatrpgs.rainfallschema.enemies.EnemyEventMDO;

/**
 * The one and only class for those pesky badniks that hunt down the valiant
 * hero and hinder his quest to save the earth.
 */
public class Enemy extends CharacterEvent {
	
	protected EnemyEventMDO mdo;
	
	/**
	 * Creates a new enemy on a map from a database entry.
	 * @param 	mdo				The MDO with data to create from
	 * @param 	parent			The parent map of the object
	 * @param	x				The initial x-coord (in tiles)
	 * @param	y				The intitila y-coord (in tiles)
	 */
	public Enemy(EnemyEventMDO mdo, Level parent, float x, float y) {
		super(mdo, parent, x, y);
		this.mdo = mdo;
	}

}
