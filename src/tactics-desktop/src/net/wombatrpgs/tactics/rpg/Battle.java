/**
 *  Battle.java
 *  Created on Feb 12, 2014 3:37:33 AM for project tactics-desktop
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.tactics.rpg;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.assets.AssetManager;

import net.wombatrpgs.mgne.core.interfaces.Queueable;
import net.wombatrpgs.mgne.core.interfaces.Updateable;
import net.wombatrpgs.mgne.io.CommandListener;
import net.wombatrpgs.mgne.maps.Level;
import net.wombatrpgs.mgneschema.io.data.InputCommand;
import net.wombatrpgs.tactics.core.TGlobal;
import net.wombatrpgs.tactics.io.CMapTactics;
import net.wombatrpgs.tactics.maps.TacticsMap;

/**
 * Contains information on the ongoing battle. There's no need to make the map
 * do the heavy lifting as far as turn order etc is concerned. Battles are
 * Queueable and require loading before they will work properly.
 */
public class Battle implements	CommandListener,
								Updateable,
								Queueable {
	
	protected TacticsMap map;
	protected List<TacticsController> units;
	protected TacticsController actor;
	
	protected List<Queueable> assets;
	
	/**
	 * Creates a new battle on a certain map. Creates the tactics map to go with
	 * that map and initializes some empty participant lists.
	 * @param	level			The map to battle on
	 */
	public Battle(Level level) {
		map = new TacticsMap(level, this);
		units = new ArrayList<TacticsController>();
		assets = new ArrayList<Queueable>();
	}
	
	/** @return The tactics map on which battle takes place */
	public TacticsMap getMap() { return map; }
	
	/** @return All units presently in this battle */
	public List<TacticsController> getUnits() { return units; }

	/**
	 * @see net.wombatrpgs.mgne.core.interfaces.Updateable#update(float)
	 * At the moment, we're just going to handle turn progression and leave
	 * things like "are we initialized yet" somewhere else.
	 */
	@Override
	public void update(float elapsed) {
		handleActor();
	}

	/**
	 * @see net.wombatrpgs.mgne.io.CommandListener#onCommand
	 * (net.wombatrpgs.mgneschema.io.data.InputCommand)
	 */
	@Override
	public boolean onCommand(InputCommand command) {
		return actor.onCommand(command);
	}
	
	/**
	 * @see net.wombatrpgs.mgne.core.interfaces.Queueable#queueRequiredAssets
	 * (com.badlogic.gdx.assets.AssetManager)
	 */
	@Override
	public void queueRequiredAssets(AssetManager manager) {
		for (Queueable asset : assets) {
			asset.queueRequiredAssets(manager);
		}
	}

	/**
	 * @see net.wombatrpgs.mgne.core.interfaces.Queueable#postProcessing
	 * (com.badlogic.gdx.assets.AssetManager, int)
	 */
	@Override
	public void postProcessing(AssetManager manager, int pass) {
		for (Queueable asset : assets) {
			asset.postProcessing(manager, pass);
		}
	}

	/**
	 * Let the battle commence! This will cause the battle to take over and
	 * switch to tactics mode.
	 */
	public void startBattle() {
		
		// Get our objects onto the screen
		TGlobal.screen.setTacticsMode(true);
		map.onBattleStart();
		
		// command map
		TGlobal.screen.pushCommandContext(new CMapTactics());
		
		// start!
		deploy();
		handleActor();
	}
	
	/**
	 * When the fighting's said and done, this gets called. It's public mostly
	 * for testing purposes.
	 */
	public void stopBattle() {
		
		// Get our objects off the screen
		TGlobal.screen.setTacticsMode(false);
		map.onBattleStop();
		
		// command map
		TGlobal.screen.popCommandContext();
	}
	
	/**
	 * Pushes the entire hero party into this battle. Does not deal with
	 * placement, recruitment, etc, just gets them in the general actor
	 * runaround thing. Their event will also need to be placed.
	 */
	public void addParty() {
		TGlobal.party.addToBattle(this);
	}
	
	/**
	 * Adds some rando unit to the battle. As before, does not deal with events
	 * and just sets things up internally. This method should probably be called
	 * only from GameUnit, because it knows how to add itself.
	 * @param	unit			The unit to add
	 */
	public void addUnit(TacticsController unit) {
		units.add(unit);
		assets.add(unit);
		
		unit.onAddedToBattle(this);
	}
	
	/**
	 * Takes care of turn order and the currently acting actor.
	 */
	protected void handleActor() {
		if (actor == null) {
			actor = nextActor();
			actor.onTurnStart();
		}
		int energySpent = actor.doneWithTurn();
		if (energySpent >= 0) {
			// the actor actually did something and is done
			actor.onTurnEnd();
			// grant everyone else energy equal to energy expended
			for (TacticsController unit : units) {
				if (unit != actor) {
					unit.grantEnergy(energySpent);
				}
			}
			// keep a 0-centered energy standard (ie, last mover is at 0)
			int energyCorrection = actor.getEnergy();
			for (TacticsController unit : units) {
				unit.grantEnergy(-energyCorrection);
			}
			// next!!
			actor = nextActor();
			actor.onTurnStart();
		}
	}
	
	/**
	 * Finds the actor who should move next (usually the guy with the
	 * highest energy in the bunch)
	 * @return					The next unit to move
	 */
	protected TacticsController nextActor() {
		TacticsController best = units.get(0);
		for (TacticsController unit : units) {
			if (unit.getEnergy() > best.getEnergy()) {
				best = unit;
			}
		}
		return best;
	}
	
	/**
	 * Deployment phase - player places their characters.
	 */
	protected void deploy() {
		TacticsController hero = TGlobal.party.getHero();
		for (TacticsController unit : TGlobal.party.getUnits()) {
			if (unit != hero) {
				unit.spawnNear(hero);
			}
		}
	}

}
