/**
 *  BattleAnimWizard.java
 *  Created on May 22, 2014 10:30:47 AM for project saga-schema
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.sagaschema.wizards.banim;

import net.wombatrpgs.mgnse.wizard.Wizard;

/**
 * Construct an RM-line battle animation.
 */
public class BattleAnimWizard extends Wizard {

	protected BattleAnimDialog dialog;

	/**
	 * @see net.wombatrpgs.mgnse.wizard.Wizard#getName()
	 */
	@Override
	public String getName() {
		return "Battle Animation";
	}

	/**
	 * @see net.wombatrpgs.mgnse.wizard.Wizard#run()
	 */
	@Override
	public void run() {
		dialog = new BattleAnimDialog(frame);
	}
	
}
