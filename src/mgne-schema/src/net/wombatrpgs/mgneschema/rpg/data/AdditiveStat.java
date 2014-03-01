/**
 *  AdditiveStat.java
 *  Created on Feb 27, 2014 11:25:14 PM for project mgne
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mgneschema.rpg.data;

/**
 * Any stat that combines by addition and therefore has a zero value of zero.
 */
public class AdditiveStat extends NumericStat {

	/**
	 * Constructs a new additive stat with the given ID.
	 * @param	id				The unique identifier for the stat
	 */
	public AdditiveStat(String id) {
		super(id, 0f);
	}

	/**
	 * @see net.wombatrpgs.mgneschema.rpg.data.StatType#combine(java.lang.Object, java.lang.Object)
	 */
	@Override
	public Float combine(Float s1, Float s2) {
		return s1 + s2;
	}

	/**
	 * @see net.wombatrpgs.mgneschema.rpg.data.StatType#decombine(java.lang.Object, java.lang.Object)
	 */
	@Override
	public Float decombine(Float s1, Float s2) {
		return s1 - s2;
	}

}
