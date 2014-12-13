/**
 *  Statset.java
 *  Created on Feb 28, 2014 5:30:16 PM for project mgne
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mgne.rpg;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;

import net.wombatrpgs.mgneschema.rpg.data.FlagStat;
import net.wombatrpgs.mgneschema.rpg.data.FlagStatLinkable;
import net.wombatrpgs.mgneschema.rpg.data.NumericStat;
import net.wombatrpgs.mgneschema.rpg.data.NumericStatLinkable;

/**
 * A simple extension to Stats that works with an enum set.
 */
public abstract class StatEnumLink extends Stats {
	
	@JsonIgnore protected static Map<String, FlagStat> flagTypes;
	@JsonIgnore protected static Map<String, NumericStat> statTypes;
	
	/**
	 * Retrieves the numeric value associated with a stat link.
	 * @param	link			The link to use for retrieving stat data
	 * @return					The value of that stat
	 */
	public Float stat(NumericStatLinkable link) {
		return super.stat(link.getStat().getID());
	}
	
	/**
	 * Retrieves the flag associated with a stat link.
	 * @param	link			The link to use for retrieving stat data
	 * @return					True if that flag is set
	 */
	public Boolean flag(FlagStatLinkable link) {
		return super.flag(link.getFlag().getID());
	}
	
	/**
	 * Sets the numeric value associated with a stat link to some new value.
	 * @param	link			The link to the stat to update
	 * @param	value			The new value for that stat
	 */
	public void setStat(NumericStatLinkable link, float value) {
		super.setStat(link.getStat().getID(), value);
	}
	
	/**
	 * Adds some value to the stat, usually addition but actually combines.
	 * @param	link			The link to the stat to update
	 * @param	value			The value to add to that stat
	 */
	public void add(NumericStatLinkable link, float value) {
		super.addStat(link.getStat().getID(), value);
	}
	
	/**
	 * Adds some value to the stat, usually addition but actually combines.
	 * @param	link			The link to the stat to update
	 * @param	value			The value to add to that stat
	 */
	public void subtract(NumericStatLinkable link, float value) {
		super.subtractStat(link.getStat().getID(), value);
	}
	
	/**
	 * Updates the flag count of a stat associated with a link.
	 * @param	flag			The link to the flag to update
	 * @param	value			True to set that flag, false to unset it
	 */
	public void updateFlag(FlagStatLinkable flag, boolean value) {
		super.updateFlag(flag.getFlag().getID(), value);
	}
	
	/**
	 * Sets all the flags in the collection.
	 * @param	links			All the links to set
	 * @param	value			True to set the flags, false to unset them
	 */
	public void updateFlags(Collection<? extends FlagStatLinkable> links, boolean value) {
		for (FlagStatLinkable link : links) {
			updateFlag(link, value);
		}
	}
	
	/**
	 * Iterates through a collection of linkables and extracts their associated
	 * numeric stats.
	 * @param	linkables		The collection to iterate through
	 * @return					The collection of linked stats
	 */
	protected static Collection<NumericStat> convertNumeric(Collection<? extends NumericStatLinkable> linkables) {
		List<NumericStat> stats = new ArrayList<NumericStat>();
		for (NumericStatLinkable link : linkables) {
			stats.add(link.getStat());
		}
		return stats;
	}
	
	/**
	 * Iterates through a collection of linkables and extracts their associated
	 * flag stats.
	 * @param	linkables		The collection to iterate through
	 * @return					The collection of linked flags
	 */
	protected static Collection<FlagStat> convertFlag(Collection<? extends FlagStatLinkable> linkables) {
		List<FlagStat> stats = new ArrayList<FlagStat>();
		for (FlagStatLinkable link : linkables) {
			stats.add(link.getFlag());
		}
		return stats;
	}
	
	/**
	 * @see net.wombatrpgs.mgne.rpg.Stats#statTypes()
	 */
	@Override
	protected Map<String, NumericStat> statTypes() {
		if (statTypes == null) {
			regenerateTypes();
		}
		return statTypes;
	}

	/**
	 * @see net.wombatrpgs.mgne.rpg.Stats#flagTypes()
	 */
	@Override
	protected Map<String, FlagStat> flagTypes() {
		if (flagTypes == null) {
			regenerateTypes();
		}
		return flagTypes;
	}
	
	/** @return The collection of all numeric stats in game */
	protected abstract Collection<? extends NumericStatLinkable> numerics();
	
	/** @return The collection of all flag stats in game */
	protected abstract Collection<? extends FlagStatLinkable> flags();
	
	/**
	 * Constructs the internal numeric/flag maps (caching).
	 */
	protected void regenerateTypes() {
		Collection<NumericStat> allStats = convertNumeric(numerics());
		Collection<FlagStat> allFlags = convertFlag(flags());
		statTypes = new HashMap<String, NumericStat>();
		flagTypes = new HashMap<String, FlagStat>();
		for (NumericStat stat : allStats) {
			statTypes.put(stat.getID(), stat);
		}
		for (FlagStat flag : allFlags) {
			flagTypes.put(flag.getID(), flag);
		}
	}

}
