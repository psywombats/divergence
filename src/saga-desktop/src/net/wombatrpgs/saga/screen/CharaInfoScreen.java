/**
 *  CharaInfoScreen.java
 *  Created on Apr 11, 2014 6:38:33 PM for project saga-desktop
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.saga.screen;

import net.wombatrpgs.mgne.screen.Screen;
import net.wombatrpgs.mgne.ui.Nineslice;
import net.wombatrpgs.saga.rpg.Chara;

/**
 * Displays detailed stats on a character.
 */
public class CharaInfoScreen extends Screen {
	
	protected static int HEADER_WIDTH = 160;
	protected static int HEADER_HEIGHT = 64;
	protected static int STATS_WIDTH = 48;
	protected static int STATS_HEIGHT = 176;
	
	protected Chara chara;
	
	protected Nineslice headerBG, statsBG, inventoryBG;
	
	/**
	 * Default constructor, sets up the display.
	 * @param	chara			The character to display info on
	 */
	public CharaInfoScreen(Chara chara) {
		this.chara = chara;
		headerBG = new Nineslice(HEADER_WIDTH, HEADER_HEIGHT);
	}

}
