/**
 *  Statset.java
 *  Created on Feb 28, 2014 5:30:16 PM for project mgne
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mgne.rpg;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.wombatrpgs.mgneschema.rpg.data.FlagStat;
import net.wombatrpgs.mgneschema.rpg.data.FlagStatLinkable;
import net.wombatrpgs.mgneschema.rpg.data.NumericStat;
import net.wombatrpgs.mgneschema.rpg.data.NumericStatLinkable;

/**
 * A simple extension to Stats that works with an enum set.
 */
public class StatEnumLink extends Stats {

	/**
	 * Creates a new statset from a collection of linkables.
	 * @param	allNumerics		The collection of all numeric linkables
	 * @param	allFlags			The collection of all flag linkables
	 */
	public StatEnumLink(Collection<? extends NumericStatLinkable> allNumerics,
			Collection<? extends FlagStatLinkable> allFlags) {
		super(convertNumeric(allNumerics), convertFlag(allFlags));
	}
	
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

}
