/**
 *  IdealTarget.java
 *  Created on May 3, 2014 6:20:27 AM for project saga-desktop
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.saga.misc.mfam.instances;

import java.util.ArrayList;
import java.util.List;

import net.wombatrpgs.saga.misc.mfam.FitnessStats;
import net.wombatrpgs.saga.misc.mfam.data.Family;

/**
 * Like the original config targets, but hand-made.
 */
public class IdealStats extends FitnessStats {
	
	/**
	 * Creates the targets with hand-tuned values.
	 */
	public IdealStats() {
		super();
		
		familyCount = 26;
		groupCount = 11;
		
		sizeDistrib.put(1, 2);
		sizeDistrib.put(2, 3);
		sizeDistrib.put(3, 6);
		
		introLevels = 6;
		introDistrib.put(0, nullArray(11));
		introDistrib.put(1, nullArray(5));
		introDistrib.put(2, nullArray(4));
		introDistrib.put(3, nullArray(3));
		introDistrib.put(4, nullArray(2));
		introDistrib.put(5, nullArray(1));
		
		levelAvgs.put(0, 10f);
		levelAvgs.put(1, 9.5f);
		levelAvgs.put(2, 9f);
		levelAvgs.put(3, 7.5f);
		levelAvgs.put(4, 6f);
		levelAvgs.put(5, 4f);
		
		levelDevs.put(0, .5f);
		levelDevs.put(1, .5f);
		levelDevs.put(2, .6f);
		levelDevs.put(3, .7f);
		levelDevs.put(4, .6f);
		levelDevs.put(5, .3f);
		
		gains.put(+2, .02f);
		gains.put(+1, .30f);
		gains.put(+0, .33f);
		gains.put(-1, .20f);
		gains.put(-2, .10f);
		gains.put(-3, .05f);
		
		averageGain = 0f;
		gainDev = 1f;
		biggestGain = 2;
		biggestLoss = -4;
		
		transtoAvg = 9.5f;
		transtoDev = 2;
		transtoMin = 4;
		
		unreachCount = 0;
	}
	
	/**
	 * Creates an arraylist of nulls with the given size.
	 * @param	size			The size of the array to fill
	 * @return					An arraylist of that size with nulls
	 */
	protected List<Family> nullArray(int size) {
		List<Family> list = new ArrayList<Family>();
		while (list.size() < size) {
			list.add(null);
		}
		return list;
	}

}
