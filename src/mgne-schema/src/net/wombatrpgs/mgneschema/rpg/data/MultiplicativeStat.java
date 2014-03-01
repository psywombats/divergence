/**
 *  MultiplicativeStat.java
 *  Created on Feb 27, 2014 11:55:40 PM for project mgne
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mgneschema.rpg.data;

/**
 * A stat that multiplies! Like ADOM walking speed, so two 50% reductions don't
 * render you unable to move.
 */
public class MultiplicativeStat extends NumericStat {

	/**
	 * Creates a new stat with the given id.
	 * @param	id				The unique id for this stat
	 */
	public MultiplicativeStat(String id) {
		super(id, 1f);
	}

	/**
	 * @see net.wombatrpgs.mgneschema.rpg.data.StatType#combine(java.lang.Object, java.lang.Object)
	 */
	@Override
	public Float combine(Float s1, Float s2) {
		return s1 * s2;
	}

	/**
	 * @see net.wombatrpgs.mgneschema.rpg.data.StatType#decombine(java.lang.Object, java.lang.Object)
	 */
	@Override
	public Float decombine(Float s1, Float s2) {
		return s1 / s2;
	}

}
