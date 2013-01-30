/**
 *  FacesAnimationFactory.java
 *  Created on Jan 24, 2013 8:51:45 PM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.rainfall.graphics;

import net.wombatrpgs.rainfall.core.RGlobal;
import net.wombatrpgs.rainfall.maps.events.MapEvent;
import net.wombatrpgs.rainfallschema.graphics.DirMDO;
import net.wombatrpgs.rainfallschema.graphics.FourDirMDO;
import net.wombatrpgs.rainfallschema.graphics.TwoDirMDO;

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
		} else {
			RGlobal.reporter.warn("Unknown subclass of DirMDO: " + mdo);
			return null;
		}
	}

}
