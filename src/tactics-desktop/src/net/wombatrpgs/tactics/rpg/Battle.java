/**
 *  Battle.java
 *  Created on Feb 12, 2014 3:37:33 AM for project tactics-desktop
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.tactics.rpg;

import java.util.ArrayList;
import java.util.List;

import net.wombatrpgs.mgne.core.interfaces.Updateable;
import net.wombatrpgs.mgne.io.CommandListener;
import net.wombatrpgs.mgne.maps.Level;
import net.wombatrpgs.mgneschema.io.data.InputCommand;
import net.wombatrpgs.tactics.core.TGlobal;
import net.wombatrpgs.tactics.maps.TacticsMap;

/**
 * Contains information on the ongoing battle. There's no need to make the map
 * do the heavy lifting as far as turn order etc is concerned.
 */
public class Battle implements	CommandListener,
								Updateable {
	
	protected TacticsMap map;
	
	protected List<GameUnit> allCombatants;
	protected GameUnit actor;
	
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
			for (GameUnit unit : allCombatants) {
				if (unit != actor) {
					unit.grantEnergy(energySpent);
				}
			}
			// keep a 0-centered energy standard (ie, last mover is at 0)
			int energyCorrection = actor.getEnergy();
			for (GameUnit unit : allCombatants) {
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
	 * Let the battle commence! This will cause the battle to take over and
	 * switch to tactics mode.
	 */
	public void startBattle() {
		TGlobal.screen.addObject(map);
		TGlobal.screen.setTacticsMode(true);
		addParty();
	}
	
	/**
	 * Pushes the entire hero party into this battle. Does not deal with
	 * placement, recruitment, etc, just gets them in the general actor
	 * runaroundt thing. Their event will also need to be placed.
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
	public void addCombatant(GameUnit unit) {
		allCombatants.add(unit);
	}
	
	/**
	 * Finds the actor who should move next (usually the guy with the
	 * highest energy in the bunch)
	 * @return					The next unit to move
	 */
	protected GameUnit nextActor() {
		GameUnit best = allCombatants.get(0);
		for (GameUnit unit : allCombatants) {
			if (unit.getEnergy() > best.getEnergy()) {
				best = unit;
			}
		}
		return best;
	}

}
