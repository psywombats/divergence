/**
 *  GameUnit.java
 *  Created on Feb 22, 2014 11:52:30 PM for project tactics-desktop
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.tactics.rpg;

import java.util.ArrayList;
import java.util.List;

import net.wombatrpgs.tacticsschema.rpg.GameUnitMDO;

/**
 * A participant in the RPG. This class exists so TacticsUnit can handle the
 * moving-on-the-map and battle mechanics, while GameUnit can exist in a vacuum
 * and keep track of things like health, stats, attacking/getting hit, etc. This
 * is owned by a TacticsUnit.
 */
public class GameUnit {
	
	protected GameUnitMDO mdo;
	protected TacticsController controller;
	
	protected Stats stats;
	protected List<Ability> abilities;
	
	/**
	 * Creates a new game unit from parent data. This class shares an MDO with
	 * tactics unit because they're pretty much the same thing.
	 * @param	mdo				The data to generate from
	 * @param	parent			The tactics unit to generate for
	 */
	public GameUnit(GameUnitMDO mdo, TacticsController parent) {
		this.mdo = mdo;
		this.controller = parent;
		
		stats = new Stats(mdo.stats);
		abilities = new ArrayList<Ability>();
		for (String key : mdo.abilities) {
			abilities.add(new Ability(key, controller));
		}
	}
	
	/** @return The controlling logic for this game character */
	public TacticsController getController() { return controller; }
	
	/** @return The current stats of this unit */
	public Stats getStats() { return stats; }
	
	/** @return All the stuff this guy can currently do (calculated?) */
	public List<Ability> getAbilities() { return abilities; }

}
