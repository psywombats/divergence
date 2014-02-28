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

/**
 * A simple extension to Stats that works with an enum set.
 */
public class Statset extends Stats {

	/**
	 * Creates a new statset from a collection of linkables.
	 * @param	numerics		The collection of all numeric linkables
	 * @param	flags			The collection of all flag linkables
	 */
	public Statset(Collection<NumericStatLinkable> numerics, Collection<FlagStatLinkable> flags) {
		super(convertNumeric(numerics), convertFlag(flags));
	}
	
	/**
	 * Retrieves the numeric value associated with a stat link.
	 * @param	link			The link to use for retrieving stat data
	 * @return					The value of that stat
	 */
	public Float getStat(NumericStatLinkable link) {
		return super.getStat(link.getStat().getID());
	}
	
	/**
	 * Retrieves the flag associated with a stat link.
	 * @param	link			The link to use for retrieving stat data
	 * @return					True if that flag is set
	 */
	public Boolean getFlag(FlagStatLinkable link) {
		return super.getFlag(link.getFlag().getID());
	}
	
	/**
	 * Iterates through a collection of linkables and extracts their associated
	 * numeric stats.
	 * @param	linkables		The collection to iterate through
	 * @return					The collection of linked stats
	 */
	protected static Collection<NumericStat> convertNumeric(Collection<NumericStatLinkable> linkables) {
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
	protected static Collection<FlagStat> convertFlag(Collection<FlagStatLinkable> linkables) {
		List<FlagStat> stats = new ArrayList<FlagStat>();
		for (FlagStatLinkable link : linkables) {
			stats.add(link.getFlag());
		}
		return stats;
	}

}
