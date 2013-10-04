/**
 *  FacesAnimationFactory.java
 *  Created on Jan 24, 2013 8:51:45 PM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mrogue.graphics;

import net.wombatrpgs.mrogue.core.MGlobal;
import net.wombatrpgs.mrogue.maps.events.MapEvent;
import net.wombatrpgs.mrogueschema.graphics.AnimationMDO;
import net.wombatrpgs.mrogueschema.graphics.DirMDO;
import net.wombatrpgs.mrogueschema.graphics.FourDirMDO;
import net.wombatrpgs.mrogueschema.graphics.TwoDirMDO;

/**
 * Generates the right subclass of an animation factory based on an MDO.
 */
public class FacesAnimationFactory {
	
	/**
	 * Creates the right subclass of the mdo based on its type. The other
	 * parameters are passed straight on to the constructor.
	 * @param 	mdo				The MDO to make the object for
	 * @param	parent			The map event to make the object for
	 * @return
	 */
	public static FacesAnimation create(DirMDO mdo, MapEvent parent) {
		// TODO: it may be possible to generalize this
		if (FourDirMDO.class.isAssignableFrom(mdo.getClass())) {
			return new FourDir((FourDirMDO) mdo, parent);
		} else if (TwoDirMDO.class.isAssignableFrom(mdo.getClass())) {
			return new TwoDir((TwoDirMDO) mdo, parent);
		} else if (AnimationMDO.class.isAssignableFrom(mdo.getClass())) {
			return new OneDir((AnimationMDO) mdo, parent);
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
	 * @param 	parent			The parent map event
	 * @return					That object, formatted and shit
	 */
	public static FacesAnimation create(String mdoKey, MapEvent parent) {
		DirMDO mdo = MGlobal.data.getEntryFor(mdoKey, DirMDO.class);
		return create(mdo, parent);
	}

}
