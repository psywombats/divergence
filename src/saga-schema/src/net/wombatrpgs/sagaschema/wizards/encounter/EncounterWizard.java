/**
 *  EncounterWizard.java
 *  Created on Sep 7, 2014 1:47:17 PM for project saga-schema
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.sagaschema.wizards.encounter;

import net.wombatrpgs.mgnse.wizard.Wizard;

/**
 * Wizard to make random encounters
 */
public class EncounterWizard extends Wizard {
	
	protected EncounterDialog dialog;

	/**
	 * @see net.wombatrpgs.mgnse.wizard.Wizard#getName()
	 */
	@Override
	public String getName() {
		return "Encounter Wizard";
	}

	/**
	 * @see net.wombatrpgs.mgnse.wizard.Wizard#run()
	 */
	@Override
	public void run() {
		dialog = new EncounterDialog(frame);
	}

}
