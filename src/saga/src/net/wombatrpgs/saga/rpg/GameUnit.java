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
import net.wombatrpgs.sagaschema.characters.data.CharacterMDO;

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
	
	/**
	 * Creates a new character from data.
	 * @param	mdo				The data to create the character from
	 */
	public GameUnit(CharacterMDO mdo) {
		this.mdo = mdo;
		this.turnChildren = new ArrayList<Turnable>();
		this.toRemove = new ArrayList<Turnable>();
		this.assets = new ArrayList<Queueable>();
	}
	
	/** @param name This unit's new player-facing name */
	public void setName(String name) { this.name = name; }
	
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
	
}
