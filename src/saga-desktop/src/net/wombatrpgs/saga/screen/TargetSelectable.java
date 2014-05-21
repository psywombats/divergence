/**
 *  TargetSelectable.java
 *  Created on May 21, 2014 11:19:48 AM for project saga-desktop
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.saga.screen;

import net.wombatrpgs.saga.rpg.chara.Chara;
import net.wombatrpgs.saga.ui.CharaSelector.SelectionListener;

/**
 * A screen that can provide a chara target for items etc.
 */
public interface TargetSelectable {
	
	/**
	 * Returns the user of the item. This is opposed to the target, which is
	 * selected. An item being used from the party inventory has no user and
	 * should return null. The item will interpret this and use the same caster
	 * as target. Only really important for stat-based heals.
	 * @return					The chara using the item, or null if by menu
	 */
	public Chara getUser();
	
	/**
	 * Called by the item to request that the menu request that the user select
	 * a target for the item. Should just pass the listener into a character
	 * selector or something. Cancelling should be enabled.
	 * @param	listener		The listener to call once selection is made
	 */
	public void awaitSelection(SelectionListener listener);
	
	/**
	 * Indicate that character or item information change has occurred and the
	 * UI on the screen should update accordingly.
	 */
	public void refresh();

}
