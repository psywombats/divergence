/**
 *  SlotListener.java
 *  Created on Sep 23, 2014 12:18:37 AM for project saga-desktop
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.saga.ui;

/**
 * Listener for selecting an ability.
 */
public abstract class SlotListener {
	
	/**
	 * Called when the user selects a slot. Returns negative if cancelled.
	 * @param	selected		The slot that was selected, -1 for cancel
	 * @return					True to unfocus the selector
	 */
	public abstract boolean onSelection(int selected);
	
}
