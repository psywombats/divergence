/**
 *  Turnable.java
 *  Created on Feb 18, 2014 6:39:06 PM for project mgne
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mgne.core.interfaces;

/**
 * Something that happens every time the hero takes a "turn." What this is
 * kind of depends on the game, but it's just something that happens
 * periodically. Maybe for Rainfall-esque games it just happens every second
 * or w/e. For now it's called whenever the hero walks, and the implementers are
 * mostly NPCs taking steps.
 */
public interface Turnable {

	/**
	 * Called whenever the hero takes a turn.
	 */
	public void onTurn();
	
}
