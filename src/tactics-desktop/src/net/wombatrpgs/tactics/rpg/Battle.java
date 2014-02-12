/**
 *  Battle.java
 *  Created on Feb 12, 2014 3:37:33 AM for project tactics-desktop
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.tactics.rpg;

import java.util.ArrayList;
import java.util.List;

import net.wombatrpgs.mgne.io.CommandListener;
import net.wombatrpgs.mgne.maps.Level;
import net.wombatrpgs.mgneschema.io.data.InputCommand;
import net.wombatrpgs.tactics.maps.TacticsMap;

/**
 * Contains information on the ongoing battle. There's no need to make the map
 * do the heavy lifting as far as turn order etc is concerned.
 */
public class Battle implements CommandListener {
	
	protected TacticsMap map;
	
	protected List<GameUnit> allCombatants;
	
	/**
	 * Creates a new battle on a certain map. Creates the tactics map to go with
	 * that map and initializes some empty participant lists.
	 * @param	level			The map to battle on
	 */
	public Battle(Level level) {
		map = new TacticsMap(level);
		allCombatants = new ArrayList<GameUnit>();
	}

	/**
	 * @see net.wombatrpgs.mgne.io.CommandListener#onCommand
	 * (net.wombatrpgs.mgneschema.io.data.InputCommand)
	 */
	@Override
	public boolean onCommand(InputCommand command) {
		return false;
	}

}
