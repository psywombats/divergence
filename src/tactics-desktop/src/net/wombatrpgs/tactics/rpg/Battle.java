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

import net.wombatrpgs.mgne.core.MGlobal;
import net.wombatrpgs.mgne.core.interfaces.Queueable;
import net.wombatrpgs.mgne.core.interfaces.Updateable;
import net.wombatrpgs.mgne.io.CommandListener;
import net.wombatrpgs.mgne.maps.Level;
import net.wombatrpgs.mgneschema.io.data.InputCommand;
import net.wombatrpgs.tactics.core.TGlobal;
import net.wombatrpgs.tactics.maps.TacticsEvent;
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
	protected List<GameUnit> units;
	protected GameUnit actor;
	
	protected List<Queueable> assets;
	
	/**
	 * Creates a new battle on a certain map. Creates the tactics map to go with
	 * that map and initializes some empty participant lists.
	 * @param	level			The map to battle on
	 */
	public Battle(Level level) {
		map = new TacticsMap(level);
		units = new ArrayList<GameUnit>();
		assets = new ArrayList<Queueable>();
	}

	/**
	 * @see net.wombatrpgs.mgne.core.interfaces.Updateable#update(float)
	 * At the moment, we're just going to handle turn progression and leave
	 * things like "are we initialized yet" somewhere else.
	 */
	@Override
	public void update(float elapsed) {
		if (actor == null) {
			actor = nextActor();
		}
		int energySpent = actor.doneWithTurn();
		if (energySpent >= 0) {
			// the actor actually did something and is done
			actor.onTurnEnd();
			// grant everyone else energy equal to energy expended
			for (GameUnit unit : units) {
				if (unit != actor) {
					unit.grantEnergy(energySpent);
				}
			}
			// keep a 0-centered energy standard (ie, last mover is at 0)
			int energyCorrection = actor.getEnergy();
			for (GameUnit unit : units) {
				unit.grantEnergy(-energyCorrection);
			}
			// next!!
			actor = nextActor();
			actor.onTurnStart();
		}
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
		TGlobal.screen.addObject(map);
		TGlobal.screen.setTacticsMode(true);
		TGlobal.party.getHero().spawnAt(
				MGlobal.getHero().getTileX(),
				MGlobal.getHero().getTileY());
		
		// start!
		actor = nextActor();
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
	public void addUnit(GameUnit unit) {
		units.add(unit);
		assets.add(unit);
		
		unit.onAddedToBattle(this);
	}
	
	/**
	 * Adds a doll to the encapsulated map.
	 * @param	event			The doll to add
	 */
	public void addDoll(TacticsEvent event) {
		map.addDoll(event);
	}
	
	/**
	 * Finds the actor who should move next (usually the guy with the
	 * highest energy in the bunch)
	 * @return					The next unit to move
	 */
	protected GameUnit nextActor() {
		GameUnit best = units.get(0);
		for (GameUnit unit : units) {
			if (unit.getEnergy() > best.getEnergy()) {
				best = unit;
			}
		}
		return best;
	}

}
