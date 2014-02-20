/**
 *  Option.java
 *  Created on Feb 19, 2014 12:17:17 AM for project mgne
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mgne.ui;

/**
 * Class to encapsulate an option with some associated callback. Meant to be fed
 * as part of a list to an option selector.
 */
public abstract class Option {
	
	protected String text;
	
	/**
	 * Creates a new option with some text. This constructor should be useful
	 * paired with an anonymous override.
	 * @param	text			The text this option will display.
	 */
	public Option(String text) {
		this.text = text;
	}
	
	/** @return The text associated with this option */
	public String getText() { return this.text; }
	
	/**
	 * Callback for when/if this option is selected.
	 * @return					True if this menu should be closed
	 */
	public abstract boolean onSelect();

}
