/**
 *  DummyWizard.java
 *  Created on May 27, 2013 12:11:41 PM for project MGNDBE
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mgnse.wizard;

import net.wombatrpgs.mgnse.MainFrame;

/**
 * hurf durf i am gandlaf
 */
public class DummyWizard extends Wizard {

	public DummyWizard(MainFrame frame) {
		super();
	}

	/**@see net.wombatrpgs.mgnse.wizard.Wizard#getName() */
	@Override
	public String getName() {
		return "Dummy Wizard";
	}

	/** @see net.wombatrpgs.mgnse.wizard.Wizard#run() */
	@Override
	public void run() {
		System.out.println("gandlaf");
	}

}
