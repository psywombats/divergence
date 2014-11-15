/**
 *  Stat.java
 *  Created on Feb 27, 2014 10:00:32 PM for project mgne
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mgneschema.rpg.data;

/**
 * A global holder for both numeric and flag stats. They both support being
 * combined but eh I guess that's about it. This doesn't represent the strength
 * of Eric per se, but rather that Eric could potentially have a strength, and
 * that LIO and Alan have strength as well.
 * 
 * @param <T> The stat type, either a boolean for a flag or else int/float. This
 * means things like str and agi are over Integer and flags CANNOT_DRAIN are
 * over bool. Probably subclassed.
 */
public abstract class StatType<T> {
	
	protected String id;
	protected T zero;
	
	/**
	 * Creates a new stat with a unique identifier.
	 * @param	id				The unique ID for this stat (ie 'str', 'agi')
	 * @param	zero			What this stat should be when nothing is
	 * 							applied, not a "default value" but say the
	 * 							identity value (0 for add, 1 for mult)
	 */
	public StatType(String id, T zero) {
		this.id = id;
		this.zero = zero;
	}
	
	/** @return The unique id for this stat */
	public String getID() { return id; }
	
	/** @return The identity for this stat */
	public T getZero() { return zero; }
	
	/**
	 * Adds one stat value to the other. Used for things like equipping and
	 * temporary boosts.
	 * @param	s1				One of the stat values to be added
	 * @param	s2				The other stat value
	 * @return					The final stat value after the operation
	 */
	public abstract T combine(T s1, T s2);
	
	/**
	 * Decrements one stat from the other. Used for things like deequipping and
	 * removing temporary boosts.
	 * @param	s1				The base stat value
	 * @param	s2				The value being removed
	 * @return					The final stat value after the operation
	 */
	public abstract T decombine(T s1, T s2);

}
