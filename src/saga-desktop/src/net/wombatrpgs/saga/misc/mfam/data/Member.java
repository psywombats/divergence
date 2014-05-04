/**
 *  MFamMonster.java
 *  Created on May 3, 2014 12:12:33 AM for project saga-desktop
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.saga.misc.mfam.data;

import net.wombatrpgs.saga.misc.mfam.MFamConstants;

/**
 * A monster representation in the optimizer.
 */
public class Member implements Comparable<Member> {
	
	/** The power of the meat of this monster */
	public int power;
	
	/** The meat level needed to become this monster */
	public int target;
	
	/** Just for fun */
	public String name;
	
	public Member(int power, int target, String name) {
		this.name = name;
		this.power = power;
		this.target = target;
	}
	
	public Member(int power, int target) {
		this(power, target, MFamConstants.NAMEGEN.randomName());
	}

	/** @see java.lang.Object#toString() */
	@Override public String toString() { return name + "(" + power + "," + target + ")"; }

	@Override public int compareTo(Member other) {
		return target - other.target;
	}
	
}
