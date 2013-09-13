/**
 *  Stats.java
 *  Created on Aug 19, 2013 5:15:10 PM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.rainfall.characters;

import net.wombatrpgs.rainfallschema.characters.data.StatsMDO;

/**
 * A collection of RPG stats associated with a character, item, etc. The idea is
 * that it can be added to another stats object in case of a boost, maintained
 * by a character to keep track of current stats, etc. Etc.
 */
public class Stats {
	
	private int mhp;
	private int msp;
	private float spRate;
	
	/**
	 * Creates a new stats object from data.
	 * @param	stats			The stats data to create from
	 */
	public Stats(StatsMDO stats) {
		this.mhp = stats.mhp;
		this.msp = stats.msp;
		this.spRate = stats.staminaRegen;
	}
	
	/** @return Max health points */
	public int getMHP() { return mhp; }
	
	/** @return Max stamina */
	public int getMSP() { return msp; }
	
	/** @return How fast stamina is regenerated, in sp/s */
	public float getSPRate() { return spRate; }

}
