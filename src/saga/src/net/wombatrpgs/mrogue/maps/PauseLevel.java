/**
 *  PauseLevel.java
 *  Created on Feb 5, 2013 12:45:38 AM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mrogue.maps;

/**
 * How an object responds to pausing in the current level.
 */
public enum PauseLevel {
	
	SURRENDERS_EASILY,		// Pauses when hero loses control, used for things
							// on the map that don't move while the hero doesn't
	
	PAUSE_RESISTANT,		// Does not pause unless the player loses control of
							// the game, good for controllers and timers
	
	IMMUNE_TO_PAUSE,		// Never pause, ever. Useful for listeners and lower
							// level engine stuff, if that's ever on a map
	

}
