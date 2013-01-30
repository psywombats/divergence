/**
 *  Enemy.java
 *  Created on Jan 23, 2013 9:14:38 PM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.rainfall.characters.enemies;

import net.wombatrpgs.rainfall.characters.CharacterEvent;
import net.wombatrpgs.rainfall.characters.ai.Intelligence;
import net.wombatrpgs.rainfall.core.RGlobal;
import net.wombatrpgs.rainfall.maps.Level;
import net.wombatrpgs.rainfallschema.characters.enemies.EnemyEventMDO;
import net.wombatrpgs.rainfallschema.characters.enemies.ai.IntelligenceMDO;

/**
 * The one and only class for those pesky badniks that hunt down the valiant
 * hero and hinder his quest to save the earth.
 */
public class Enemy extends CharacterEvent {
	
	protected EnemyEventMDO mdo;
	protected Intelligence ai;
	
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
		IntelligenceMDO aiMDO = RGlobal.data.getEntryFor(mdo.intelligence, IntelligenceMDO.class);
		ai = new Intelligence(aiMDO, this);
	}

	/**
	 * @see net.wombatrpgs.rainfall.characters.CharacterEvent#update(float)
	 */
	@Override
	public void update(float elapsed) {
		ai.act();
		super.update(elapsed);
	}
}
