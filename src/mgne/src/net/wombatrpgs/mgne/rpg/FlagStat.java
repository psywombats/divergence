/**
 *  FlagStat.java
 *  Created on Feb 28, 2014 2:02:27 AM for project mgne
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mgne.rpg;

/**
 * A flag stat is something like DRAIN_DISABLED or whatever. If you have it
 * innately, it doesn't disappear if you equip/dequip a piece of armor with it
 * on.
 */
public class FlagStat extends Stat<FlagStatValue> {
	
	/**
	 * Creates a new stat with the given unique ID.
	 * @param	id				The unique identifier to use
	 */
	public FlagStat(String id) {
		super(id, new FlagStatValue());
	}

	/**
	 * @see net.wombatrpgs.mgne.rpg.Stat#combine(java.lang.Object, java.lang.Object)
	 */
	@Override
	public FlagStatValue combine(FlagStatValue s1, FlagStatValue s2) {
		return new FlagStatValue(s1.count + s2.count);
	}

	/**
	 * @see net.wombatrpgs.mgne.rpg.Stat#decombine(java.lang.Object, java.lang.Object)
	 */
	@Override
	public FlagStatValue decombine(FlagStatValue s1, FlagStatValue s2) {
		return new FlagStatValue(s1.count - s2.count);
	}
	


}
