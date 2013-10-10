/**
 *  EffectFactory.java
 *  Created on Apr 18, 2013 11:25:18 PM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.rainfall.graphics.effects;

import net.wombatrpgs.rainfall.core.RGlobal;
import net.wombatrpgs.rainfall.maps.Level;
import net.wombatrpgs.rainfallschema.graphics.effects.data.EffectFogMDO;
import net.wombatrpgs.rainfallschema.graphics.effects.data.EffectMDO;
import net.wombatrpgs.rainfallschema.graphics.effects.data.EffectOcclusionMDO;
import net.wombatrpgs.rainfallschema.graphics.effects.data.EffectPixelWeatherMDO;

/**
 * Creates effects from MDOs.
 */
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
		} else if (EffectPixelWeatherMDO.class.isAssignableFrom(mdo.getClass())) {
			return new EffectPixelWeather(creator, (EffectPixelWeatherMDO) mdo);
		} else if (EffectOcclusionMDO.class.isAssignableFrom(mdo.getClass())) {
			return new EffectOcclusion(creator, (EffectOcclusionMDO) mdo);
		} else {
			RGlobal.reporter.warn("Unkown EffectMDO subclass: " + mdo.getClass());
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
		return create(creator, RGlobal.data.getEntryFor(mdoName, EffectMDO.class));
	}

}
