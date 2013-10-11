/**
 *  GameUnit.java
 *  Created on Oct 5, 2013 3:27:36 PM for project mrogue-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mrogue.characters;

import net.wombatrpgs.mrogue.core.MGlobal;
import net.wombatrpgs.mrogue.ui.Narrator;
import net.wombatrpgs.mrogueschema.characters.CharacterMDO;

/**
 * A GameUnit is designed to factor out some of that shittiness that happens
 * when one class represents a unit's physical manifestation and its status as
 * an actor in an RPG. You end up with animation and move stuff side by side
 * with stats and attack methods. Bad. So now there's a GameUnit and a
 * CharacterEvent. It might be worth mentioning that the schema for a
 * character is sill the same, it just gets its functionality split between
 * the two manifestation classes.
 */
public class GameUnit {
	
	/** lol this thing is because I'm too lazy to type MGlobal.ughhhh.nar ugh */
	protected static Narrator out;
	
	protected CharacterMDO mdo;
	protected CharacterEvent parent;
	
	protected Stats baseStats;
	protected Stats currentStats;
	
	/**
	 * Creates a new character from data.
	 * @param	mdo				The data to create the character from
	 * @param	parent			The owner of this character, ie, its body
	 */
	public GameUnit(CharacterMDO mdo, CharacterEvent parent) {
		this.mdo = mdo;
		this.parent = parent;
		baseStats = new Stats(mdo.stats);
		currentStats = new Stats(mdo.stats);
		if (out == null) out = MGlobal.ui.getNarrator();
	}
	
	/** @return The current stats of this unit */
	public Stats getStats() { return currentStats; }
	
	/**
	 * Launches a basic melee attack against the other unit.
	 * @param other
	 */
	public void attack(GameUnit other) {
		int damage = 10;
		out.msg(getName() + " attacks " + other.getName() + " for " + damage + " damages.");
		other.takeDamage(damage);
	}
	
	/**
	 * Inflicts a set amount of damage. Handles ugly things like death as well,
	 * but not stats such as attack or defense.
	 * @param	damage			The amount of damage to take, in hp
	 */
	public void takeDamage(int damage) {
		currentStats.takeDamage(damage);
		if (currentStats.getHP() <= 0) {
			die();
		}
	}
	
	/**
	 * Called when this unit dies of unnatural causes, such as being slaughtered
	 * by an enemy. Not meant for things like screen removal. Everything about
	 * kill credits and whatever should go here.
	 */
	public void die() {
		parent.getLevel().removeEvent(parent);
		out.msg(getName() + " is killed.");
	}
	
	/**
	 * Gives this character its player-visible name.
	 * @return					The name of this unit
	 */
	public String getName() {
		if (parent == MGlobal.hero) {
			return "the fearless hero";
		} else {
			return "the nameless moron";
		}
	}

}
