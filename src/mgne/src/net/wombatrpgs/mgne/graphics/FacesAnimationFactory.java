/**
 *  FacesAnimationFactory.java
 *  Created on Jan 24, 2013 8:51:45 PM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mgne.graphics;

import net.wombatrpgs.mgne.core.MGlobal;
import net.wombatrpgs.mgneschema.graphics.AnimationMDO;
import net.wombatrpgs.mgneschema.graphics.DirMDO;
import net.wombatrpgs.mgneschema.graphics.FourDirMDO;
import net.wombatrpgs.mgneschema.graphics.TwoDirMDO;

/**
 * Generates the right subclass of an animation factory based on an MDO.
 */
public class FacesAnimationFactory {
	
	/**
	 * Creates the right subclass of the mdo based on its type. The other
	 * parameters are passed straight on to the constructor.
	 * @param 	mdo				The data to make the object for
	 * @return					The animation from the data
	 */
	public static FacesAnimation create(DirMDO mdo) {
		if (FourDirMDO.class.isAssignableFrom(mdo.getClass())) {
			return new FourDir((FourDirMDO) mdo);
		} else if (TwoDirMDO.class.isAssignableFrom(mdo.getClass())) {
			return new TwoDir((TwoDirMDO) mdo);
		} else if (AnimationMDO.class.isAssignableFrom(mdo.getClass())) {
			return new OneDir((AnimationMDO) mdo);
		} else {
			MGlobal.reporter.warn("Unknown subclass of DirMDO: " + mdo);
			return null;
		}
	}
	
	/**
	 * Creates the right subclass of FacesAnimation by checking the database.
	 * The other parameters are straight from its constructor. Differs from the
	 * above in that it looks up in the database instead of you.
	 * @param 	mdoKey			The key to check in the database
	 * @return					That object, formatted and shit
	 */
	public static FacesAnimation create(String mdoKey) {
		DirMDO mdo = MGlobal.data.getEntryFor(mdoKey, DirMDO.class);
		return create(mdo);
	}

}
