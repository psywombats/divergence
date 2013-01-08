/**
 *  Moveset.java
 *  Created on Dec 29, 2012 12:49:44 PM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.rainfall.moveset;

import java.util.HashMap;
import java.util.Map;

import net.wombatrpgs.rainfall.core.RGlobal;
import net.wombatrpgs.rainfall.maps.Level;
import net.wombatrpgs.rainfallschema.hero.MovesetSchema;
import net.wombatrpgs.rainfallschema.hero.data.MovesetEntryMDO;
import net.wombatrpgs.rainfallschema.hero.moveset.MoveMDO;
import net.wombatrpgs.rainfallschema.hero.moveset.PushMDO;
import net.wombatrpgs.rainfallschema.hero.moveset.SummonMDO;
import net.wombatrpgs.rainfallschema.io.data.InputCommand;

/**
 * A moveset is the moves the hero can currently perform. These are usually
 * mapped to action keys. A quick overview: raw -> input buttons -> commands ->
 * moves.
 */
public class Moveset {
	
	protected MovesetSchema mdo; // caution -- may be null
	protected Map<InputCommand, Actionable> moves;
	
	/**
	 * Creates and initializes a ne blank moveset. Make sure to manually add
	 * moves or else the hero won't be able to do anything.
	 */
	public Moveset() {
		moves = new HashMap<InputCommand, Actionable>();
	}
	
	/**
	 * Creates and initializes a new moveset. It's fine to do things like change
	 * movesets on the fly as the hero gains and loses moves, but this is one
	 * way to create it from data.
	 * @param 	mdo			The data object to initialize from
	 */
	public Moveset(MovesetSchema mdo) {
		this();
		this.mdo = mdo;
		for (MovesetEntryMDO entryMDO : mdo.moves) {
			MoveMDO moveMDO = RGlobal.data.getEntryFor(entryMDO.move, MoveMDO.class);
			// TODO: it may be possible to generalize this
			if (SummonMDO.class.isAssignableFrom(moveMDO.getClass())) {
				moves.put(entryMDO.command, new ActSummon((SummonMDO) moveMDO));
			} else if (PushMDO.class.isAssignableFrom(moveMDO.getClass())) {
				moves.put(entryMDO.command, new ActPush((PushMDO) moveMDO));
			} else {
				RGlobal.reporter.warn("Unknown move class: " + moveMDO.getClass());
			}
		}
	}
	
	/**
	 * Acts according to the input command by mapping it into the moveset.
	 * @param 	command		The command to respond to
	 * @param	map			The map to act on
	 */
	public void act(InputCommand command, Level map) {
		if (moves.containsKey(command)) {
			moves.get(command).act(map);
		}
	}

}
