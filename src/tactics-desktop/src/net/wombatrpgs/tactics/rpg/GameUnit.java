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
import net.wombatrpgs.tacticsschema.rpg.data.Flag;
import net.wombatrpgs.tacticsschema.rpg.data.Stat;

/**
 * A participant in the RPG. This class exists so TacticsUnit can handle the
 * moving-on-the-map and battle mechanics, while GameUnit can exist in a vacuum
 * and keep track of things like health, stats, attacking/getting hit, etc. This
 * is owned by a TacticsUnit.
 */
public class GameUnit {
	
	protected GameUnitMDO mdo;
	protected TacticsController controller;
	
	protected TacticsStats stats;
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
		
		stats = new TacticsStats(mdo.stats);
		abilities = new ArrayList<Ability>();
		for (String key : mdo.abilities) {
			abilities.add(new Ability(key, controller));
		}
	}
	
	/** @return The controlling logic for this game character */
	public TacticsController getController() { return controller; }
	
	/** @return The value of the stat with the provided key */
	public float stat(Stat key) { return stats.getStat(key); }
	
	/** @return True if this unit has the given flag set */
	public boolean flag(Flag key) { return stats.getFlag(key); }
	
	/** @return All the stuff this guy can currently do (calculated?) */
	public List<Ability> getAbilities() { return abilities; }
	
	/**
	 * Called when this unit is killed. Called by the controller, so don't do
	 * any ugly stuff in here or call it from GameUnit itself. In roguelikes,
	 * this is where "me is defeated" would go.
	 */
	public void onDeath() {
		
	}
	
	/**
	 * Called after damage calculations are said and done and this unit is in
	 * for some damage. 
	 * @param	damage			The damage to be dealt (in hp)
	 * @param	source			The unit that damage us, or null if a trap etc
	 */
	public void takeDamage(int damage, GameUnit source) {
		stats.setStat(Stat.HP, stat(Stat.HP) - damage);
		if (stat(Stat.HP) <= 0) {
			controller.onDeath();
		}
	}

}
