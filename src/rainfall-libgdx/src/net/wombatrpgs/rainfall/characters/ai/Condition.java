/**
 *  Condition.java
 *  Created on Jan 29, 2013 11:07:31 PM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.rainfall.characters.ai;

import net.wombatrpgs.rainfall.characters.CharacterEvent;

/**
 * Part of an intent, this is really just a mask for a boolean function. Usually
 * conditions require more info so this should be subclasses, supplied with
 * information in the constructor, that's then used to evaluate at call time.
 * This could be an interface but isn't because of convenience constructors.
 */
public abstract class Condition {
	
	protected CharacterEvent actor;
	
	/**
	 * Creates a new condition for a specified actor.
	 * @param 	actor			The actor to create the condition for
	 */
	public Condition(CharacterEvent actor) {
		this.actor = actor;
	}
	
	/**
	 * Check the condition!
	 * @return					True if the condition is met, false otherwise
	 */
	public abstract boolean isMet();

}
