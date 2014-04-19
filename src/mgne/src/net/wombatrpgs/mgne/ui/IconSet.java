/**
 *  IconSet.java
 *  Created on Apr 2, 2013 2:37:29 PM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mgne.ui;

import com.badlogic.gdx.graphics.Pixmap;

import net.wombatrpgs.mgne.core.Constants;
import net.wombatrpgs.mgne.core.MAssets;
import net.wombatrpgs.mgne.core.interfaces.Queueable;
import net.wombatrpgs.mgneschema.graphics.IconSetMDO;

/**
 * This thing takes an MDO and sets up its icons appropriately.
 */
public class IconSet implements Queueable {
	
	protected IconSetMDO mdo;
	protected Pixmap icon16, icon32, icon128;
	
	/**
	 * Creates an icon set from mdo. After processing will automagically change
	 * the window.
	 * @param 	mdo				The data to make icons from.
	 */
	public IconSet(IconSetMDO mdo) {
		this.mdo = mdo;
	}

	/**
	 * @see net.wombatrpgs.mgne.core.interfaces.Queueable#queueRequiredAssets
	 * (MAssets)
	 */
	@Override
	public void queueRequiredAssets(MAssets manager) {
		manager.load(Constants.UI_DIR + mdo.icon16, Pixmap.class);
		manager.load(Constants.UI_DIR + mdo.icon32, Pixmap.class);
		manager.load(Constants.UI_DIR + mdo.icon128, Pixmap.class);
	}

	/**
	 * @see net.wombatrpgs.mgne.core.interfaces.Queueable#postProcessing
	 * (MAssets, int)
	 */
	@Override
	public void postProcessing(MAssets manager, int pass) {
		Pixmap[] maps = new Pixmap[3];
		maps[0] = manager.get(Constants.UI_DIR + mdo.icon16, Pixmap.class);
		maps[1] = manager.get(Constants.UI_DIR + mdo.icon32, Pixmap.class);
		maps[2] = manager.get(Constants.UI_DIR + mdo.icon128, Pixmap.class);
	}

}
