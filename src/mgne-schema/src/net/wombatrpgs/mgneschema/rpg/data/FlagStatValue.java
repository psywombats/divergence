/**
 *  FlagStatValue.java
 *  Created on Feb 28, 2014 2:40:56 AM for project mgne
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mgneschema.rpg.data;

/**
 * Struct for flag stats. Stores how many hits but has an equality operator
 * for ease of use.
 */
public class FlagStatValue {
	
	public int count;
	
	/** @param count The initial count to start with */
	public FlagStatValue(int count) { this.count = count; }
	
	/** Creates a new value at 0 */
	public FlagStatValue() { this(0); }
	
	/** Checks if this flag is set */
	public boolean on() { return count > 0; }
	
	/** Increments the number of items/abilities that are setting this flag */
	public void increment() { count += 1; }
	
	/** Decrements the number of items/abilities that are setting this flag */
	public void decrement() { count -= 1; }
	
	/** @param True to increment the value, false to decrement it */
	public void modify(boolean on) { count += on ? 1 : -1; }
	
}
