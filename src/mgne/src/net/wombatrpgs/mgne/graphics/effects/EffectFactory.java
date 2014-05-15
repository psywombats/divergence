/**
 *  EffectFactory.java
 *  Created on Apr 18, 2013 11:25:18 PM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mgne.graphics.effects;

import net.wombatrpgs.mgne.core.MGlobal;
import net.wombatrpgs.mgne.maps.Level;
import net.wombatrpgs.mgneschema.graphics.effects.EffectFogMDO;
import net.wombatrpgs.mgneschema.graphics.effects.data.EffectMDO;

/**
 * Creates effects from MDOs.
 */
@Deprecated
public class EffectFactory {
	
	/**
	 * Creates an effect from data.
	 * @param	creator			The level we're calling this for
	 * @param	mdo				The data to construct from
	 * @return					The constructed effect
	 */
	public static Effect create(Level creator, EffectMDO mdo) {
		if (EffectFogMDO.class.isAssignableFrom(mdo.getClass())) {
			return new EffectFog(creator, (EffectFogMDO) mdo);
		} else {
			MGlobal.reporter.warn("Unkown EffectMDO subclass: " + mdo.getClass());
			return null;
		}
	}
	
	/**
	 * Creates an effect from data by looking up its key in the database.
	 * @param	creator			The level we're calling this for
	 * @param 	mdoName			The key of the MDO to search for
	 * @return					The MDO itself
	 */
	public static Effect create(Level creator, String mdoName) {
		return create(creator, MGlobal.data.getEntryFor(mdoName, EffectMDO.class));
	}

}
