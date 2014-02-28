/**
 *  Stats.java
 *  Created on Feb 27, 2014 6:26:00 PM for project mgne
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mgne.rpg;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import net.wombatrpgs.mgne.core.MGlobal;

/**
 * RPG statistics helper class. Games should subclass this with their own
 * version that includes specifics and potentially advanced getters. Sort of
 * based on how te4 handles things.
 */
public class Stats {
	
	protected List<NumericStat> statTypes;
	protected List<FlagStat> flagTypes;
	
	protected Map<String, Float> stats;
	protected Map<String, FlagStatValue> flags;
	
	/**
	 * Creates a new stats object with the given types of stats. Populates the
	 * initial values with the identity for each stat type. Mainly meant to be
	 * called by a subclass constructor.
	 * @param	statTypes		All numeric types this stats object contains
	 * @param	flagTypes		All flag types this stats object contains
	 */
	public Stats(Collection<NumericStat> allStats, Collection<FlagStat> allFlags) {
		statTypes = new ArrayList<NumericStat>();
		flagTypes = new ArrayList<FlagStat>();
		for (NumericStat stat : statTypes) {
			statTypes.add(stat);
			stats.put(stat.getID(), stat.getZero());
		}
		for (FlagStat flag : allFlags) {
			flagTypes.add(flag);
			flags.put(flag.getID(), flag.getZero());
		}
	}
	
	/**
	 * Retrieves the value of a stat based on the ID string. The id is meant to
	 * be something like 'str' and then this will return the strength value. If
	 * no value for that stat is recorded, returns the identity for that stat,
	 * and if no stat with that ID exists, reports an error and returns null.
	 * @param	id				The unique identifier of the stat to look up
	 * @return					The value of that stat
	 */
	public Float getStat(String id) {
		Float value = stats.get(id);
		if (value != null) {
			return value;
		} else {
			for (NumericStat stat : statTypes) {
				if (stat.getID().equals(id)) {
					return stat.getZero();
				}
			}
			MGlobal.reporter.warn("No stat found with id: " + id + " on " + this);
			return null;
		}
	}
	
	/**
	 * Retrieves the value of a stat based on the ID string. The id should be
	 * the name of a flag stat. If no value for that stat is recorded, defaults
	 * to off.
	 * @param	id				The unique identifier of the flag to look up
	 * @return					True if that flag is set, false otherwise
	 */
	public Boolean getFlag(String id) {
		FlagStatValue value = flags.get(id);
		if (value != null) {
			return value.on();
		} else {
			return false;
		}
	}

}
