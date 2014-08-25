/**
 *  LocatedAnim.java
 *  Created on Jul 30, 2014 1:22:38 PM for project saga-desktop
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.saga.graphics.banim;

/**
 * Struct for a battle animation and its location.
 */
public class LocatedAnim {
	
	public BattleAnimStrip strip;
	public int x, y;
	
	/**
	 * Creates a new strip located at 0, 0,.
	 * @param	strip			The strip to create for
	 */
	public LocatedAnim(BattleAnimStrip strip) {
		this.strip = strip;
		this.x = 0;
		this.y = 0;
	}
}
