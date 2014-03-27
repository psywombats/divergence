/**
 *  TGlobal.java
 *  Created on Feb 12, 2014 2:36:20 AM for project tactics-desktop
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.tactics.core;

import java.util.ArrayList;
import java.util.List;

import net.wombatrpgs.mgne.core.MGlobal;
import net.wombatrpgs.mgne.core.interfaces.Queueable;
import net.wombatrpgs.tactics.rpg.PlayerParty;
import net.wombatrpgs.tactics.screen.TacticsScreen;
import net.wombatrpgs.tactics.ui.TacticsUI;

/**
 * Another big-ass global thing, but this one for Tactics!
 */
public class TGlobal {
	
	/** Screen used for all tactics battles etc */
	public static TacticsScreen screen;
	/** UI singleton */
	public static TacticsUI ui;
	/** Every player-controlled tactics unit, I guess? */
	public static PlayerParty party;
	
	private static List<Queueable> toLoad;
	
	/**
	 * Called from a hook in the game.
	 */
	public static void globalInit() {
		
		toLoad = new ArrayList<Queueable>();
		MGlobal.reporter.inform("Intializing tactics globals");
		
		ui = TacticsUI.init();
		toLoad.add(ui);
		
		initParty();
		
		// load all this crap
		MGlobal.assets.loadAssets(toLoad, "tactics global");
		
	}
	
	/**
	 * Creates a new party. Not sure how it should be init'd, actually.
	 */
	private static void initParty() {
		party = new PlayerParty();
	}

}
