/**
 *  Interface.java
 *  Created on Feb 28, 2014 5:22:01 PM for project mgne
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mgne.rpg;

/**
 * Represents an enum that can be used to link to a stat and ID for that stat.
 */
public interface NumericStatLinkable {
	
	/**
	 * Retrieves the numeric stat associated with this enumerated value
	 * @return				The associated stat
	 */
	public NumericStat getStat();

}
