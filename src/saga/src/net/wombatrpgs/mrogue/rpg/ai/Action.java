/**
 *  Action.java
 *  Created on Jan 5, 2014 1:10:21 PM for project saga
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mrogue.rpg.ai;

import net.wombatrpgs.mrogue.rpg.CharacterEvent;

/**
 * An interface to replace the MGN sort of action that was strictly limited to a
 * roguelike. This should make the behavior tree more extensible.
 */
public interface Action {
	
	/**
	 * This is called when the actor performs the given ability.
	 * @param	actor			The actor performing the actor
	 */
	public abstract void performWith(CharacterEvent actor);

}
