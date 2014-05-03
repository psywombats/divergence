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
	
	/** Battle indent string */
	public static final String NBSP = MGlobal.charConverter.convert("$N");
	public static final String TAB = NBSP + NBSP;

}
