/**
 *  GameUnit.java
 *  Created on Oct 5, 2013 3:27:36 PM for project mrogue-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.saga.rpg;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.assets.AssetManager;

import net.wombatrpgs.saga.core.Queueable;
import net.wombatrpgs.saga.core.Turnable;
import net.wombatrpgs.saga.rpg.ai.Allegiance;
import net.wombatrpgs.sagaschema.characters.data.CharacterMDO;
import net.wombatrpgs.sagaschema.characters.data.Relation;

/**
 * A GameUnit is designed to factor out some of that shittiness that happens
 * when one class represents a unit's physical manifestation and its status as
 * an actor in an RPG. You end up with animation and move stuff side by side
 * with stats and attack methods. Bad. So now there's a GameUnit and a
 * CharacterEvent. It might be worth mentioning that the schema for a
 * character is sill the same, it just gets its functionality split between
 * the two manifestation classes.
 */
public class GameUnit implements Turnable, Queueable {
	
	protected CharacterMDO mdo;
	protected List<Turnable> turnChildren, toRemove;
	protected List<Queueable> assets;
	
	protected String name;
	protected Stats baseStats;
	protected Stats currentStats;
	protected Allegiance allegiance;
	
	/**
	 * Creates a new character from data.
	 * @param	mdo				The data to create the character from
	 */
	public GameUnit(CharacterMDO mdo) {
		this.mdo = mdo;
		this.turnChildren = new ArrayList<Turnable>();
		this.toRemove = new ArrayList<Turnable>();
		this.assets = new ArrayList<Queueable>();
		baseStats = new Stats(mdo.stats);
		currentStats = new Stats(mdo.stats);
		allegiance = new Allegiance(this, mdo.faction);
		turnChildren.add(allegiance);
	}
	
	/** @return The current stats of this unit */
	public Stats getStats() { return currentStats; }
	
	/** @param name This unit's new player-facing name */
	public void setName(String name) { this.name = name; }
	
	/** @return The political views of this unit */
	public Allegiance getAllegiance() { return this.allegiance; }
	
	/** @return True if this unit has fallen in mortal combat */
	public boolean isDead() { return getStats().hp <= 0; }
	
	/** @param turn The new turn child to register */
	public void addTurnChild(Turnable turn) { turnChildren.add(turn); }
	
	/** @param turn The old turn child to remove */
	public void removeTurnChild(Turnable turn) { toRemove.add(turn); }
	
	/**
	 * @see net.wombatrpgs.saga.core.Queueable#queueRequiredAssets
	 * (com.badlogic.gdx.assets.AssetManager)
	 */
	@Override
	public void queueRequiredAssets(AssetManager manager) {
		for (Queueable asset : assets) {
			asset.queueRequiredAssets(manager);
		}
	}

	/**
	 * @see net.wombatrpgs.saga.core.Queueable#postProcessing
	 * (com.badlogic.gdx.assets.AssetManager, int)
	 */
	@Override
	public void postProcessing(AssetManager manager, int pass) {
		for (Queueable asset : assets) {
			asset.postProcessing(manager, pass);
		}
	}
	
	/**
	 * Fetches the player-facing name of this unit. This takes in-game
	 * considerations into effect, such as if the hero can see us.
	 * @return					This unit's player-facing name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Determines how we respond to another unit.
	 * @param	other			The unit to respond to
	 * @return					How we respond to them
	 */
	public Relation getRelationTo(GameUnit other) {
		return allegiance.getRelationTo(other);
	}
	
	/**
	 * @see net.wombatrpgs.saga.core.Turnable#onTurn()
	 */
	@Override
	public void onTurn() {
		for (Turnable t : turnChildren) {
			t.onTurn();
		}
		for (Turnable t : toRemove) {
			turnChildren.remove(t);
		}
	}
	
	/**
	 * Inflicts an amount of physical damage on this character. Deals with
	 * armor but not death. This is mostly so that printouts complete in the
	 * correct order.
	 * @param	damage			The amount of physical damage to deal, in hp
	 * @return					The amount of damage actually dealt, in hp
	 */
	public int takePhysicalDamage(int damage) {
		int dealt = currentStats.takePhysicalDamage(damage);
		return dealt;
	}
	
	/**
	 * Inflicts an amount of magic damage on this character. Deals with armor
	 * but not death. This is mostly so that printouts complete in the
	 * correct order.
	 * @param	damage			The amount of magic damage to deal, in hp
	 * @return					The amount of damage actually dealt, in hp
	 */
	public int takeMagicDamage(int damage) {
		int dealt = currentStats.takeMagicDamage(damage);
		return dealt;
	}
	
	/**
	 * Heals the unit by some amount up to its maximum HP.
	 * @param	healAmt			The amount to heal
	 * @return					The amount actually healed
	 */
	public int heal(int healAmt) {
		int healt = Math.min(currentStats.mhp - currentStats.hp, healAmt);
		currentStats.hp += healt;
		return healt;
	}
	
	/**
	 * Allies ourselves with a particular other unit.
	 * @param	other			The unit to ally with
	 */
	public void ally(GameUnit other) {
		allegiance.addToFriendlist(other);
		other.allegiance.addToFriendlist(this);
	}

}
