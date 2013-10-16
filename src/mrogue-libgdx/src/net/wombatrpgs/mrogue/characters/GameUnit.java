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
	
	protected String name;
	
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
	
	/** @return The physical manifestation of this game unit */
	public CharacterEvent getParent() { return parent; }
	
	/** @return This unit's player-facing name */
	public String getName() { return MGlobal.hero.inLoS(parent) ? name : "something"; }
	
	/** @param This unit's new player-facing name */
	public void setName(String name) { this.name = name; }
	
	/**
	 * Launches a basic melee attack against the other unit.
	 * @param other
	 */
	public void attack(GameUnit other) {
		String us = getName();
		String them = other.getName();
		if (other.getStats().getDodgeChance() < MGlobal.rand.nextFloat()) {
			int dealt = other.takePhysicalDamage(getStats().getDamage());
			if (visible(this, other)) {
				if (dealt > 0) {
					out.msg(us + " attacks " + them + " for " + dealt + " damages.");
				} else {
					out.msg(us + " fails to harm " + them + ".");
				}
			}
			other.ensureAlive();
		} else {
			if (visible(this, other)) {
				out.msg(us + " misses " + them + ".");
			}
		}
	}
	
	/**
	 * Inflicts a set amount of damage. Does not handle death or any other
	 * stats like attack or defense.
	 * @param	damage			The amount of damage to take, in hp
	 */
	public void takeRawDamage(int damage) {
		currentStats.takeRawDamage(damage);
	}
	
	/**
	 * Inflicts an amount of physical damage on this character. Deals with
	 * armor but not death. This is mostly so that printouts complete in the
	 * correct order.
	 * @param	damage			The amount of physical damage to deal, in hp
	 * @return					The amount of damage actually dealt, in hp
	 */
	public int takePhysicalDamage(int damage) {
		return currentStats.takePhysicalDamage(damage);
	}
	
	/**
	 * Makes sure our health is above 0. If it isn't, kill self.
	 */
	public void ensureAlive() {
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
	 * A shortcut for an ugly if statement. Checks if the hero can see any of
	 * the units provided.
	 * @param	units			The list of units to check
	 * @return					True if any of the units are visible
	 */
	public boolean visible(GameUnit... units) {
		for (GameUnit unit : units) {
			if (MGlobal.hero.inLoS(unit.getParent())) {
				return true;
			}
		}
		return false;
	}

}
