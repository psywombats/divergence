/**
 *  Stats.java
 *  Created on Feb 12, 2014 11:15:12 PM for project tactics-desktop
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.tactics.rpg;

import net.wombatrpgs.tacticsschema.rpg.data.StatsMDO;

/**
 * A bunch of RPG statistics mashed together and addable etc. Definitely not
 * immutable. This is just a wrapper for a MDO really... because fuck it, data
 * is data.
 */
public class TacticsStats {
	
	protected StatsMDO mdo;
	
	/**
	 * Creates a new stats object from a bit of data.
	 * @param	mdo				The data to create from
	 */
	public TacticsStats(StatsMDO mdo) {
		this.mdo = mdo;
	}
	
	/** @return The move range of the unit, in tiles */
	public int getMove() { return mdo.move; }

}
