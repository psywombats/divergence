/**
 *  SGlobal.java
 *  Created on Apr 4, 2014 6:35:33 PM for project saga-desktop
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.saga.core;

import java.util.ArrayList;
import java.util.List;

import net.wombatrpgs.mgne.core.MGlobal;
import net.wombatrpgs.mgne.core.interfaces.Queueable;
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
		
		List<Queueable> toLoad;
		
		// settings first
		settings = MGlobal.data.getEntryFor(SConstants.KEY_SAGASETTINGS, SagaSettings.class);
		toLoad = new ArrayList<Queueable>();
		
		// then everything else
		heroes = new HeroParty();
		toLoad.add(heroes);
		MGlobal.assets.loadAssets(toLoad, "SGlobal");
	}

}
