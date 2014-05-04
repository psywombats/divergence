/**
 *  MFamGroup.java
 *  Created on May 3, 2014 12:12:15 AM for project saga-desktop
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.saga.misc.mfam.data;

import java.util.ArrayList;
import java.util.List;

/**
 * A meat gruop in the optimizer.
 */
public class Group {
	
	/** The variable number of families in this group */
	public List<Family> families;
	
	public Group() {
		families = new ArrayList<Family>();
	}
	
	public Group(Family... families) {
		this();
		for (Family family : families) {
			addFamily(family);
		}
	}
	
	/**
	 * Updates the family membership info in the right places.
	 * @param	family			The family to add
	 */
	public void addFamily(Family family) {
		families.add(family);
	}

	/** @see java.lang.Object#toString() */
	@Override public String toString() {
		String result = "[";
		for (Family f : families) {
			result += f + ", ";
		}
		if (result.length() > 2) {
			result = result.substring(0, result.length() - 2);
		}
		result += "]";
		return result;
	}
}
