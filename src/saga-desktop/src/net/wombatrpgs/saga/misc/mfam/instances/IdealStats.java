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
		
		familyCount = 27;
		groupCount = 12;
		
		sizeDistrib.put(1, 3);
		sizeDistrib.put(2, 5);
		sizeDistrib.put(3, 2);
		sizeDistrib.put(4, 2);
		sizeDistrib.put(5, 0);
		sizeDistrib.put(6, 0);
		sizeDistrib.put(7, 0);
		sizeDistrib.put(8, 0);
		
		introLevels = 6;
		introDistrib.put(0, nullArray(11));
		introDistrib.put(1, nullArray(5));
		introDistrib.put(2, nullArray(4));
		introDistrib.put(3, nullArray(3));
		introDistrib.put(4, nullArray(2));
		introDistrib.put(5, nullArray(1));
		
		levelDistrib.put(0, 11);
		levelDistrib.put(1, 10);
		levelDistrib.put(2, 10);
		levelDistrib.put(3, 9);
		levelDistrib.put(4, 10);
		levelDistrib.put(5, 9);
		levelDistrib.put(6, 10);
		levelDistrib.put(7, 9);
		levelDistrib.put(8, 10);
		levelDistrib.put(9, 9);
		levelDistrib.put(10, 10);
		levelDistrib.put(11, 9);
		levelDistrib.put(12, 10);
		levelDistrib.put(13, 9);
		
		tierAvgs.put(0, 10f);
		tierAvgs.put(1, 9.5f);
		tierAvgs.put(2, 9f);
		tierAvgs.put(3, 7.5f);
		tierAvgs.put(4, 6f);
		tierAvgs.put(5, 4f);
		
		tierDevs.put(0, 0f);
		tierDevs.put(1, 0f);
		tierDevs.put(2, 0f);
		tierDevs.put(3, 0f);
		tierDevs.put(4, 0f);
		tierDevs.put(5, 0f);
		
		gains.put(+3, .00f);
		gains.put(+2, .00f);
		gains.put(+1, .30f);
		gains.put(+0, .40f);
		gains.put(-1, .20f);
		gains.put(-2, .10f);
		gains.put(-3, .00f);
		gains.put(-4, .00f);
		gains.put(-5, .00f);
		gains.put(-6, .00f);
		
		averageGain = 0f;
		gainDev = 1f;
		biggestGain = 1;
		biggestLoss = -4;
		
		biggestLevel = 10;
		smallestLevel = 9;
		
		transtoAvg = 9.5f;
		transtoDev = 2;
		transtoMin = 4;
		
		nontransCount = 15;
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
