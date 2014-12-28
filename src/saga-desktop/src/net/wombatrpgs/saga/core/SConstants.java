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
	
	/** Version info */
	public static final String VERSION = "0.1.1007";
	public static final int BUILD = 7;
	public static final String GAME_NAME = "saga";
	
	/** Battle indent string */
	public static final String NBSP = MGlobal.charConverter.convert("$N");
	public static final String TAB = NBSP + NBSP;
	
	/** SFX keys */
	public static final String SFX_GET = "get";
	public static final String SFX_FAIL = "fail";
	public static final String SFX_SAVE = "save";
	public static final String SFX_INN = "inn";
	public static final String SFX_BATTLE = "battle";
	public static final String SFX_CURE = "cure";

}
