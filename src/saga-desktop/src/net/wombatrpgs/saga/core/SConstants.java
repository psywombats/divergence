/**
 *  SConstants.java
 *  Created on Apr 4, 2014 8:19:03 PM for project saga-desktop
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.saga.core;

import net.wombatrpgs.mgne.core.Constants;
import net.wombatrpgs.mgne.core.MGlobal;

/**
 * Game-specific constants for Saga.
 */
public class SConstants extends Constants {
	
	/** Keys for the unique MDOs in the database */
	public static final String KEY_SAGASETTINGS = "sagasettings_default";
	
	/** Battle indent string */
	public static final String TAB = MGlobal.charConverter.convert("$N$N");

}
