/**
 *  SGlobal.java
 *  Created on Apr 4, 2014 6:35:33 PM for project saga-desktop
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.saga.core;

import net.wombatrpgs.mgne.core.MGlobal;
import net.wombatrpgs.saga.rpg.HeroParty;
import net.wombatrpgs.sagaschema.settings.SagaSettings;

/**
 * The counterpart to MGlobal. Holds Saga-related global information.
 */
public class SGlobal {
	
	/** RPG information */
	public static HeroParty heroes;
	
	/** Settings */
	public static SagaSettings settings;
	
	/**
	 * Sets up all the global variables. Called once when game is created.
	 */
	public static void globalInit() {
		
		// settings first
		settings = MGlobal.data.getEntryFor(SConstants.KEY_SAGASETTINGS, SagaSettings.class);
		
		// then everything else
		heroes = new HeroParty();
	}

}
