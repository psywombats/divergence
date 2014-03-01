/**
 *  Stats.java
 *  Created on Feb 12, 2014 11:15:12 PM for project tactics-desktop
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.tactics.rpg;

import java.util.Arrays;

import net.wombatrpgs.mgne.rpg.Statset;
import net.wombatrpgs.tacticsschema.rpg.data.Flag;
import net.wombatrpgs.tacticsschema.rpg.data.FlagEntryMDO;
import net.wombatrpgs.tacticsschema.rpg.data.Stat;
import net.wombatrpgs.tacticsschema.rpg.data.StatEntryMDO;
import net.wombatrpgs.tacticsschema.rpg.data.StatSetMDO;

/**
 * A bunch of RPG statistics mashed together and addable etc. Definitely not
 * immutable. This is just a wrapper for a MDO really... because fuck it, data
 * is data.
 * 
 * Significantly revised on 2014-02-28 to make use of MGN stats.
 */
public class TacticsStats extends Statset {
	
	/**
	 * Creates a new set of tactics with all identity values.
	 */
	public TacticsStats() {
		super(Arrays.asList(Stat.values()), Arrays.asList(Flag.values()));
	}
	
	/**
	 * Creates a new set of tactics with the values specified in the set, and
	 * identity values for everything else.
	 * @param	mdo				The data with stat values
	 */
	public TacticsStats(StatSetMDO mdo) {
		this();
		for (StatEntryMDO statMDO : mdo.stats) {
			setStat(statMDO.stat, statMDO.value);
		}
		for (FlagEntryMDO flagMDO : mdo.flags) {
			updateFlag(flagMDO.flag, true);
		}
	}

}
