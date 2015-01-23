/**
 *  BaconUI.java
 *  Created on Jan 22, 2015 9:11:13 PM for project bacon01-desktop
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.bacon01.ui;

import net.wombatrpgs.mgne.core.MGlobal;
import net.wombatrpgs.mgne.ui.UISettings;
import net.wombatrpgs.mgneschema.settings.UISettingsMDO;
import net.wombatrpgs.mgneschema.ui.TextBoxMDO;

/**
 * Bacon UI settings.
 */
public class BaconUI extends UISettings {

	public BaconUI(UISettingsMDO mdo) {
		super(mdo);
		boxMDO = MGlobal.data.getEntryFor(mdo.box, TextBoxMDO.class);
		box = new RadioBox(boxMDO, font);
	}

}
