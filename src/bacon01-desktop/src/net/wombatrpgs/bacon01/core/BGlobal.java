/**
 *  SGlobal.java
 *  Created on Apr 4, 2014 6:35:33 PM for project saga-desktop
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.bacon01.core;

import java.util.ArrayList;
import java.util.List;

import net.wombatrpgs.bacon01.rpg.Inventory;
import net.wombatrpgs.mgne.core.MGlobal;
import net.wombatrpgs.mgne.core.interfaces.Queueable;

/**
 * The counterpart to MGlobal. Holds Saga-related global information.
 */
public class BGlobal {
	
	/** Miscellaneous globals */
	public static int saveSlot;
	
	/** Hero's inventory */
	public static Inventory items;
	
	/**
	 * Sets up all the global variables. Called once when game is created.
	 */
	public static void globalInit() {
		
		List<Queueable> toLoad;
		toLoad = new ArrayList<Queueable>();
		
		items = new Inventory();
		
		// then everything else
		MGlobal.assets.loadAssets(toLoad, "BGlobal");
		
		// debug save-y stuff
		String savefile = MGlobal.args.get("savefile");
		if (savefile != null) {
			MGlobal.memory.loadAndSetScreen(savefile);
		}
		saveSlot = -1;
	}

}
