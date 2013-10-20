/**
 *  Action.java
 *  Created on Oct 5, 2013 6:48:05 PM for project mrogue-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mrogue.rpg.act;

import net.wombatrpgs.mrogue.rpg.CharacterEvent;



/**
 * An action that a character can perform to consume its turn.
 */
public abstract class Action {
	
	protected static final int BASE_COST = 1000;
	
	protected CharacterEvent actor;
	
	/**
	 * Creates a new action with no defined event. Must be set before use.
	 */
	public Action() {
		// welp
	}
	
	/** @return The character performing this action */
	public CharacterEvent getActor() { return actor; }
	
	/**
	 * Creates a new action for a specified event. Can be changed later.
	 * @param	actor			The object that will be supplying the actor
	 */
	public Action(CharacterEvent actor) {
		this();
		this.actor = actor;
	}
	
	/**
	 * Sets a new owner for this action. This isn't all that uncommon, as
	 * intelligences that share actions can use the same ones.
	 * @param	actor				The new object for supplying the actor
	 */
	public void setActor(CharacterEvent actor) {
		this.actor = actor;
	}
	
	/**
	 * Calculates the cost associated of taking this action. Calculated from a
	 * base movement cost and the actor's speed.
	 * @return					The cost associated with this action, in ticks
	 */
	public final int getCost() {
		return (int) Math.ceil((float) baseCost() * actor.getStats().getSpeedMod());
	}
	
	/**
	 * Takes a turn and performs the action. This should be called during the
	 * act call of the owner.
	 */
	public abstract void act();
	
	/**
	 * The internal cost of taking the action, if the actor had 100 speed.
	 * @return					The cost associated with this action, in ticks
	 */
	protected int baseCost() {
		return BASE_COST;
	}

}
