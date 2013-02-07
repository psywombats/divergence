/**
 *  Moveset.java
 *  Created on Dec 29, 2012 12:49:44 PM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.rainfall.moveset;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.assets.AssetManager;

import net.wombatrpgs.rainfall.characters.CharacterEvent;
import net.wombatrpgs.rainfall.characters.Hero;
import net.wombatrpgs.rainfall.core.Queueable;
import net.wombatrpgs.rainfall.core.RGlobal;
import net.wombatrpgs.rainfall.maps.Level;
import net.wombatrpgs.rainfallschema.characters.hero.MovesetMDO;
import net.wombatrpgs.rainfallschema.characters.hero.data.MovesetEntryMDO;
import net.wombatrpgs.rainfallschema.characters.hero.moveset.MoveMDO;
import net.wombatrpgs.rainfallschema.characters.hero.moveset.PushMDO;
import net.wombatrpgs.rainfallschema.characters.hero.moveset.SummonMDO;
import net.wombatrpgs.rainfallschema.io.data.InputCommand;

/**
 * A moveset is the moves the hero can currently perform. These are usually
 * mapped to action keys. A quick overview: raw -> input buttons -> commands ->
 * moves.
 */
public class Moveset implements Queueable {
	
	protected MovesetMDO mdo; // caution -- may be null
	protected Map<InputCommand, MovesetAct> moves;
	
	/**
	 * Creates and initializes a ne blank moveset. Make sure to manually add
	 * moves or else the hero won't be able to do anything.
	 */
	public Moveset() {
		moves = new HashMap<InputCommand, MovesetAct>();
	}
	
	/** @return The underlying map from commands to actions */
	public Map<InputCommand, MovesetAct> getMoves() { return moves; }
	
	/**
	 * @see net.wombatrpgs.rainfall.core.Queueable#queueRequiredAssets
	 * (com.badlogic.gdx.assets.AssetManager)
	 */
	@Override
	public void queueRequiredAssets(AssetManager manager) {
		for (MovesetAct move : moves.values()) {
			move.queueRequiredAssets(manager);
		}
	}

	/**
	 * @see net.wombatrpgs.rainfall.core.Queueable#postProcessing
	 * (com.badlogic.gdx.assets.AssetManager, int)
	 */
	@Override
	public void postProcessing(AssetManager manager, int pass) {
		for (MovesetAct move : moves.values()) {
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
			MoveMDO moveMDO = RGlobal.data.getEntryFor(entryMDO.move, MoveMDO.class);
			// TODO: it may be possible to generalize this
			if (SummonMDO.class.isAssignableFrom(moveMDO.getClass())) {
				moves.put(entryMDO.command, new ActSummon(hero, (SummonMDO) moveMDO));
			} else if (PushMDO.class.isAssignableFrom(moveMDO.getClass())) {
				moves.put(entryMDO.command, new ActPush(hero, (PushMDO) moveMDO));
			} else {
				RGlobal.reporter.warn("Unknown move class: " + moveMDO.getClass());
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
		if (moves.containsKey(command)) {
			moves.get(command).act(map, actor);
		}
	}

}
