/**
 *  AbilFxFactory.java
 *  Created on Oct 18, 2013 7:05:57 AM for project mrogue-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mrogue.graphics.effects;

import net.wombatrpgs.mrogue.core.MGlobal;
import net.wombatrpgs.mrogue.rpg.abil.Ability;
import net.wombatrpgs.mrogueschema.graphics.effects.AbilFxDistortMDO;
import net.wombatrpgs.mrogueschema.graphics.effects.AbilFxFlamesMDO;
import net.wombatrpgs.mrogueschema.graphics.effects.AbilFxFlybyMDO;
import net.wombatrpgs.mrogueschema.graphics.effects.AbilFxTestMDO;
import net.wombatrpgs.mrogueschema.graphics.effects.data.AbilFxMDO;

/**
 * Converts arbitrary abilfxmdo into actual effects. Wheeee getClass()!
 */
public class AbilFxFactory {
	
	/**
	 * Creates an effect given a key to data, parent.
	 * @param	mdoKey			The key to the data to generate from
	 * @param	parent			The ability to generate for
	 * @return
	 */
	public static AbilFX createFX(String mdoKey, Ability parent) {
		AbilFxMDO mdo = MGlobal.data.getEntryFor(mdoKey, AbilFxMDO.class);
		return createFX(mdo, parent);
	}
	
	/**
	 * Creates an effect given data, parent.
	 * @param	mdo				The data to generate from
	 * @param	parent			The ability to generate for
	 * @return
	 */
	public static AbilFX createFX(AbilFxMDO mdo, Ability parent) {
		if (AbilFxTestMDO.class.isAssignableFrom(mdo.getClass())) {
			return new AbilFxTest((AbilFxTestMDO) mdo, parent);
		} else if (AbilFxFlybyMDO.class.isAssignableFrom(mdo.getClass())) {
			return new AbilFxFlyby((AbilFxFlybyMDO) mdo, parent);
		} else if (AbilFxDistortMDO.class.isAssignableFrom(mdo.getClass())) {
			return new AbilFxDistort((AbilFxDistortMDO) mdo, parent);
		} else if (AbilFxFlamesMDO.class.isAssignableFrom(mdo.getClass())) {
			return new AbilFxFlames((AbilFxFlamesMDO) mdo, parent);
		} else {
			MGlobal.reporter.err("Unknown abilfx type: " + mdo);
			return null;
		}
	}

}
