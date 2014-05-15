/**
 *  HeroAssignable.java
 *  Created on May 15, 2014 2:23:23 AM for project mgne
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mgne.screen;

import net.wombatrpgs.mgne.core.Avatar;

/**
 * Anything that can return a hero.
 */
public interface HeroSource {
	
	/**
	 * Fetches the player's avatar that's parading around on the map. This
	 * replaces one of those public static monstrosities that's been kicking
	 * around for forever.
	 * @return					The representation of the player on the map
	 */
	public Avatar getHero();

}
