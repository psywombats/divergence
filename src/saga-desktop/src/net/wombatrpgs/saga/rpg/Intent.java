/**
 *  Intent.java
 *  Created on Apr 23, 2014 2:56:13 AM for project saga-desktop
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.saga.rpg;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Struct to keep track of what a player wants to do on a character's turn. Does
 * not have any verification, just passes straight back and forth from a combat
 * item. This is so that the same intent class can be used across all combat
 * items. When the player selects a combat item, that item is responsible for
 * producing an intent, then parsing the intent later on when it's that
 * character's turn in battle.
 */
public class Intent {
	
	protected Chara actor;
	protected CombatItem item;
	protected List<Chara> targets;
	protected Battle battle;
	
	/**
	 * Creates a new intent for the designated actor.
	 * @param	actor			The chara that will be acting
	 * @param	battle			The battle this intent is a part of
	 * @param	item			The combat item producing/parsing this intent
	 */
	public Intent(Chara actor, Battle battle, CombatItem item) {
		this.actor = actor;
		this.item = item;
		this.battle = battle;
		targets = new ArrayList<Chara>();
	}
	
	/** @return All targeted characters */
	public List<Chara> getTargets() { return targets; }
	
	/** @param More targets of this combat item */
	public void addTargets(Chara... targets) { Collections.addAll(this.targets, targets); }
	
	/** @param More targets of this combat item */
	public void addTargets(List<Chara> targets) { this.targets.addAll(targets); }
	
	/** @return The battle this intent is a part of */
	public Battle getBattle() { return battle; }
	
	/** Clears the targets list */
	public void clearTargets() { targets.clear(); }
	
	/**
	 * Works out which group the user probably selected based on the targets.
	 * Returns 0 if no group selected, or -1 if the game is broken.
	 * @return					The index of the selected group, or 0 if none
	 */
	public int inferSelectedGroup() {
		return battle.index(targets.get(0));
	}
	
	/**
	 * Creates a new listener that can be used to interface with the battle's
	 * selection methods.
	 * @param	listener		The listener from the intent construction
	 * @return					A new listener to send to the battle
	 */
	public TargetListener genDefaultListener(final IntentListener listener) {
		final Intent intent = this;
		return new TargetListener() {
			@Override public void onTargetSelection(List<Chara> targets) {
				if (targets == null) {
					listener.onIntent(null);
				} else {
					intent.addTargets(targets);
					listener.onIntent(intent);
				}
			}
		};
	}
	
	/**
	 * Waits for an intent to be constructed.
	 */
	public static interface IntentListener {
		
		/**
		 * Called when the player is done indicating their intent.
		 * @param	intent			The constructed intent, or null if canceled
		 */
		public void onIntent(Intent intent);
		
	}
	
	/**
	 * Listener for when targets are selected. Used for both groups and singles.
	 */
	public static interface TargetListener {
		
		/**
		 * Called when target(s) selected. Could be called with null if the
		 * player decided to cancel instead. Should probably never be empty?
		 * @param	targets			The target(s) the player selected, or null
		 */
		public void onTargetSelection(List<Chara> targets);
		
	}

}
