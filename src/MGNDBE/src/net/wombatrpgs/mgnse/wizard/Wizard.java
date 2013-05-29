/**
 *  Wizard.java
 *  Created on May 27, 2013 12:06:39 PM for project MGNDBE
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mgnse.wizard;

import net.wombatrpgs.mgnse.MainFrame;

/**
 * Dumb thing that represents a generation wizard for MDO(s). One wizard is
 * created per database, so things should be recycled.
 */
public abstract class Wizard {
	
	protected MainFrame frame;
	
	public Wizard() {
		
	}
	
	public void setFrame(MainFrame frame) {
		this.frame = frame;
	}
	
	/** @return The name of the wizard for the menu */
	public abstract String getName();
	
	/** Called when the user selects this wizard */
	public abstract void run();

}
