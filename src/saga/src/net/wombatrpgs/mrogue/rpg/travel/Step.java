/**
 *  Travel.java
 *  Created on Oct 5, 2013 3:56:16 AM for project MRogueSchema
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mrogue.rpg.travel;

import net.wombatrpgs.mrogue.rpg.CharacterEvent;

/**
 * A travel is a discrete move in a series that represents where a character
 * needs to move, such as taking a step, attacking a character, playing an
 * animation at a location, etc.
 */
public abstract class Step {
	
	protected CharacterEvent actor;
	protected float allotted;
	protected float totalElapsed;
	
	/**
	 * Creates a blank travel step.
	 * @param	actor			The character moving
	 */
	public Step(CharacterEvent actor) {
		this.totalElapsed = 0;
		this.actor = actor;
	}
	
	/**
	 * Should be called only during the frames when this step is active.
	 * @param	elapsed			The time since last update
	 */
	public void update(float elapsed) {
		this.totalElapsed += elapsed;
	}
	
	/**
	 * Gives this step its allotted time. For example, a chain of 4 events
	 * within a .1s delay would each receive .025s.
	 * @param 	allottedTime	How much time this event will take, in s
	 */
	public void setTime(float allottedTime) {
		this.allotted = allottedTime;
	}
	
	/**
	 * Called when the event is done executing this step. Should halt any
	 * movement and finalize any positions.
	 */
	public void onEnd() {
		// default is nothing
	}
}
