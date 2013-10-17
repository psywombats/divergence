/**
 *  GameUnit.java
 *  Created on Oct 5, 2013 3:27:36 PM for project mrogue-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mrogue.characters;

import java.util.ArrayList;
import java.util.List;

import net.wombatrpgs.mrogue.core.MGlobal;
import net.wombatrpgs.mrogue.core.Turnable;
import net.wombatrpgs.mrogue.ui.Narrator;
import net.wombatrpgs.mrogueschema.characters.CharacterMDO;
import net.wombatrpgs.mrogueschema.characters.data.Relation;

/**
 * A GameUnit is designed to factor out some of that shittiness that happens
 * when one class represents a unit's physical manifestation and its status as
 * an actor in an RPG. You end up with animation and move stuff side by side
 * with stats and attack methods. Bad. So now there's a GameUnit and a
 * CharacterEvent. It might be worth mentioning that the schema for a
 * character is sill the same, it just gets its functionality split between
 * the two manifestation classes.
 */
public class GameUnit implements Turnable {
	
	/** lol this thing is because I'm too lazy to type MGlobal.ughhhh.nar ugh */
	protected static Narrator out;
	
	protected CharacterMDO mdo;
	protected CharacterEvent parent;
	protected List<Turnable> turnChildren;
	
	protected String name;
	protected Stats baseStats;
	protected Stats currentStats;
	protected Allegiance allegiance;
	
	/**
	 * Creates a new character from data.
	 * @param	mdo				The data to create the character from
	 * @param	parent			The owner of this character, ie, its body
	 */
	public GameUnit(CharacterMDO mdo, CharacterEvent parent) {
		this.mdo = mdo;
		this.parent = parent;
		this.turnChildren = new ArrayList<Turnable>();
		baseStats = new Stats(mdo.stats);
		currentStats = new Stats(mdo.stats);
		allegiance = new Allegiance(this, mdo.faction);
		turnChildren.add(allegiance);
		
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
	
	/** @return The political views of this unit */
	public Allegiance getAllegiance() { return this.allegiance; }
	
	/** @return True if this unit has fallen in mortal combat */
	public boolean isDead() { return getStats().getHP() <= 0; }
	
	/**
	 * Determines how we respond to another unit.
	 * @param	other			The unit to respond to
	 * @return					How we respond to them
	 */
	public Relation getRelationTo(GameUnit other) {
		return allegiance.getRelationTo(other);
	}
	
	/**
	 * Determines how we respond to another character.
	 * @param	other			The chara we respond to
	 * @return					How we respond to them
	 */
	public Relation getRelationTo(CharacterEvent other) {
		return getRelationTo(other.getUnit());
	}
	
	/**
	 * @see net.wombatrpgs.mrogue.core.Turnable#onTurn()
	 */
	@Override
	public void onTurn() {
		for (Turnable t : turnChildren) {
			t.onTurn();
		}
	}

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
		other.onAttackBy(this);
	}
	
	/**
	 * Called whenever a hit launches an attack against us, successful or not.
	 * The outcome of the attack itself is handled elsewhere.
	 * @param	other			The unit that launched the attack
	 */
	public void onAttackBy(GameUnit other) {
		allegiance.addToHitlist(other);
		for (CharacterEvent chara : parent.getLevel().getCharacters()) {
			if (chara!= parent && chara.inLoS(parent) && chara.inLoS(other.parent)) {
				if (chara.getUnit().getRelationTo(this).avenge) {
					chara.getUnit().getAllegiance().addToHitlist(other);
				}
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
		if (visible(this)) {
			out.msg(getName() + " is killed.");
		}
	}
	
	/**
	 * Gets what we're supposed to be killing at the moment, or null if no
	 * target. This scans visible enemies and identifies the nearest enemy.
	 * @return
	 */
	public GameUnit getTarget() {
		float minDist = Float.MAX_VALUE;
		GameUnit best = null;
		for (GameUnit enemy : getVisibleEnemies()) {
			float dist = enemy.getParent().distanceTo(getParent());
			if (dist < minDist) {
				minDist = dist;
				best = enemy;
			}
		}
		return best;
	}
	
	/**
	 * Gathers a list of all enemy units in sight. Relies on other LoS methods.
	 * @return					A list of all hostiles in the area
	 */
	public List<GameUnit> getVisibleEnemies() {
		List<GameUnit> units = getVisibleUnits();
		List<GameUnit> enemies = new ArrayList<GameUnit>();
		for (GameUnit unit : units) {
			if (getRelationTo(unit).attackOnSight && !(unit == this)) {
				enemies.add(unit);
			}
		}
		return enemies;
	}
	
	/**
	 * Gathers a list of all units in our line of sight. In practice this is
	 * achieved by querying everyone on the map for their position information,
	 * checking if they're in LoS. It's probably kind of cumbersome, and it may
	 * be worth handling this differently in the future.
	 * @return					A list of all units in the area
	 */	
	public List<GameUnit> getVisibleUnits() {
		List<GameUnit> units = new ArrayList<GameUnit>();
		for (CharacterEvent chara : parent.getLevel().getCharacters()) {
			if (parent.inLoS(chara)) {
				units.add(chara.getUnit());
			}
		}
		return units;
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
