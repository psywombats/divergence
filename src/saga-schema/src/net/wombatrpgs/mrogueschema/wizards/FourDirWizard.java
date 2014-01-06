/**
 *  FourDirWizard.java
 *  Created on May 27, 2013 12:23:37 PM for project MGNDBE
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mrogueschema.wizards;

import net.wombatrpgs.mgnse.wizard.Wizard;

/**
 * Constructs a four-dir sprite and associated MDOs.
 */
// TODO: extend this to two-dir and one-dir animations
public class FourDirWizard extends Wizard {
	
	protected FourDirDialog dialog;

	public FourDirWizard() {
		super();
	}

	/**
	 * @see net.wombatrpgs.mgnse.wizard.Wizard#getName()
	 */
	@Override
	public String getName() {
		return "Four-Dir Sprite";
	}

	/**
	 * @see net.wombatrpgs.mgnse.wizard.Wizard#run()
	 */
	@Override
	public void run() {
		dialog = new FourDirDialog(frame);
	}

}
