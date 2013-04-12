/**
 *  Moveset.java
 *  Created on Dec 29, 2012 12:49:44 PM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.rainfall.characters.moveset;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.assets.AssetManager;

import net.wombatrpgs.rainfall.characters.CharacterEvent;
import net.wombatrpgs.rainfall.characters.Hero;
import net.wombatrpgs.rainfall.core.Constants;
import net.wombatrpgs.rainfall.core.Queueable;
import net.wombatrpgs.rainfall.maps.Level;
import net.wombatrpgs.rainfallschema.characters.hero.MovesetMDO;
import net.wombatrpgs.rainfallschema.characters.hero.data.MovesetEntryMDO;
import net.wombatrpgs.rainfallschema.io.data.InputCommand;

/**
 * A moveset is the moves the hero can currently perform. These are usually
 * mapped to action keys. A quick overview: raw -> input buttons -> commands ->
 * moves.
 */
public class Moveset implements Queueable {
	
	protected MovesetMDO mdo; // caution -- may be null
	protected Map<InputCommand, MovesetAct> startCommands;
	protected Map<InputCommand, MovesetAct> stopCommands;
	
	/**
	 * Creates and initializes a ne blank moveset. Make sure to manually add
	 * moves or else the hero won't be able to do anything.
	 */
	public Moveset() {
		startCommands = new HashMap<InputCommand, MovesetAct>();
		stopCommands = new HashMap<InputCommand, MovesetAct>();
	}
	
	/** @return The underlying map from commands to actions */
	public Map<InputCommand, MovesetAct> getStartCommands() { return startCommands; }
	
	/** @return The underlying map from commands to un-actions */
	public Map<InputCommand, MovesetAct> getStopCommands() { return stopCommands; }
	
	/**
	 * @see net.wombatrpgs.rainfall.core.Queueable#queueRequiredAssets
	 * (com.badlogic.gdx.assets.AssetManager)
	 */
	@Override
	public void queueRequiredAssets(AssetManager manager) {
		for (MovesetAct move : startCommands.values()) {
			move.queueRequiredAssets(manager);
		}
	}

	/**
	 * @see net.wombatrpgs.rainfall.core.Queueable#postProcessing
	 * (com.badlogic.gdx.assets.AssetManager, int)
	 */
	@Override
	public void postProcessing(AssetManager manager, int pass) {
		for (MovesetAct move : startCommands.values()) {
			move.postProcessing(manager, pass);
		}
	}

	/**
	 * Creates and initializes a new moveset. It's fine to do things like change
	 * movesets on the fly as the hero gains and loses moves, but this is one
	 * way to create it from data.
	 * @param	hero		RGlobal.hero reference doesn't exist yet, use this
	 * @param 	mdo			The data object to initialize from
	 */
	public Moveset(Hero hero, MovesetMDO mdo) {
		this();
		this.mdo = mdo;
		for (MovesetEntryMDO entryMDO : mdo.moves) {
			MovesetAct move = MoveFactory.generateMove(hero, entryMDO.move);
			startCommands.put(entryMDO.startCommand, move);
			if (entryMDO.stopCommand != null && !entryMDO.stopCommand.equals(Constants.NULL_MDO)) {
				stopCommands.put(entryMDO.stopCommand, move);
			}
		}
	}
	
	/**
	 * Acts according to the input command by mapping it into the moveset.
	 * @param 	command		The command to respond to
	 * @param	map			The map to act on
	 * @param	actor		The actor performing the command
	 */
	public void act(InputCommand command, Level map, CharacterEvent actor) {
		if (startCommands.containsKey(command)) {
			startCommands.get(command).act(map, actor);
		}
		if (stopCommands.containsKey(command)) {
			stopCommands.get(command).stop(map, actor);
		}
	}

}
