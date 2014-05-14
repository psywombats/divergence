/**
 *  MFamSettings.java
 *  Created on May 3, 2014 12:21:19 AM for project saga-desktop
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.saga.misc.mfam;

import java.util.Random;

/**
 * Optimizer invariants.
 */
public class MFamConstants {
	
	/** Monsters in each family to optimize */
	public static final int FAMILY_SIZE = 5;
	
	/** Resolution for meat powers and targets */
	public static final int POWER_MAX = 13;
	
	/** Name generation stuff? */
	public static final boolean FUN_MODE = false;
	
	/** String source from GAR */
	public static StringSource NAMEGEN = new StringSource();
	
	/** RNG */
	public static Random rand = new Random();

}
