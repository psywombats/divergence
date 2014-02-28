/**
 *  FlagStatLinkable.java
 *  Created on Feb 28, 2014 5:25:24 PM for project mgne
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mgne.rpg;

/**
 * Represents an enum that can be associated with a flag.
 */
public interface FlagStatLinkable {
	
	/**
	 * Retrieves the flag associated with this enum value.
	 * @return				The associated flag type
	 */
	public FlagStat getFlag();

}
