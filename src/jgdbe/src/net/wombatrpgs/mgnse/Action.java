/**
 *  Action.java
 *  Created on Aug 6, 2012 12:15:04 AM for project MGNSE
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mgnse;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

/**
 * All actions that can be fired by the database editor.
 */
public enum Action {
	
	OPEN_PROJECT		("Open...", 		KeyEvent.VK_O),
	QUIT_APP			("Quit", 			KeyEvent.VK_Q),
	SAVE				("Save", 			KeyEvent.VK_S),
	SAVE_ALL			("Save All",		KeyEvent.VK_S, ActionEvent.SHIFT_MASK),
	REVERT				("Revert",			KeyEvent.VK_R),
	REVERT_ALL			("Revert All",		KeyEvent.VK_R, ActionEvent.SHIFT_MASK),
	CLOSE_PROJECT		("Close", 			KeyEvent.VK_C, ActionEvent.SHIFT_MASK),
	NEW_ENTRY			("New Entry...",	KeyEvent.VK_N),
	DELETE_ENTRY		("Delete Entry",	KeyEvent.VK_D),
	CLONE_ENTRY			("Clone Entry",		KeyEvent.VK_C);
	
	private String displayName;
	private boolean shortcutExists = false, maskExists = false;
	private int vk, mask;
	
	/**
	 * Creates a new action with a menu shortcut and mask for that shortcut.
	 * @param displayName The name displayed on the menu for this item
	 * @param vk The virtual key used to trigger this
	 * @param mask The mask applied to the virtual key (from ActionEvent)
	 */
	Action(String displayName, int vk, int mask) {
		this.displayName = displayName;
		this.vk = vk;
		this.shortcutExists = true;
		this.mask = mask;
		this.maskExists = true;
	}
	
	/**
	 * Creates a new action with a menu shortcut.
	 * @param displayName The name displayed on the menu for this item
	 * @param vk The virtual key used to trigger this
	 */
	Action(String displayName, int vk) {
		this.displayName = displayName;
		this.vk = vk;
		this.shortcutExists = true;
	}
	
	/**
	 * Creates a new action without a menu shortcut.
	 * @param displayName The name displayed on the menu for this item
	 */
	Action(String displayName) {
		this.displayName = displayName;
	}
	
	@Override
	public String toString() {
		return displayName;
	}
	
	/** @return The virtual key code used to trigger this action */
	public int getVK() { return vk; }
	/** @return The mask to be applied to the keycode, null if none needed */
	public int getMask() { return mask; }
	/** @return True if this action can be triggered via keyboard command */
	public boolean shortcutExists() { return shortcutExists; }
	/** @return True if this shortcut requires a mask */
	public boolean maskExists() { return maskExists; }
}
