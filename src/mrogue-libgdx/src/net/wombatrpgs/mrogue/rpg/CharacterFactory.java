/**
 *  CharacterFactory.java
 *  Created on Jan 24, 2013 9:33:44 PM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mrogue.rpg;

import net.wombatrpgs.mrogue.core.MGlobal;
import net.wombatrpgs.mrogue.maps.Level;
import net.wombatrpgs.mrogueschema.characters.BossMDO;
import net.wombatrpgs.mrogueschema.characters.EnemyMDO;
import net.wombatrpgs.mrogueschema.characters.HeroMDO;
import net.wombatrpgs.mrogueschema.characters.data.CharacterMDO;

/**
 * A factory for creating characters from a generic MDO.
 */
public class CharacterFactory {
	
	/**
	 * Creates a character event of the appropriate subclass by passing some
	 * arguments along to the correct constructor.
	 * @param mdo			The MDO with the data to generate from
	 * @param object		The Tiled object on the map that made us, or null
	 * @param parent		The parent level
	 * @param x				The initial x-coord (in tiles)
	 * @param y				The initial y-coord (in tiles)
	 * @return
	 */
	public static CharacterEvent create(CharacterMDO mdo, Level parent) {
		// it may be possible to generalize this
		if (HeroMDO.class.isAssignableFrom(mdo.getClass())) {
			return new Hero((HeroMDO) mdo, parent);
		} else if (BossMDO.class.isAssignableFrom(mdo.getClass())) {
			return new Boss(parent, 0, 0);
		} else if (EnemyMDO.class.isAssignableFrom(mdo.getClass())) {
			return new Enemy((EnemyMDO) mdo, parent);
		} else {
			MGlobal.reporter.warn("Generic character spawned: " + mdo.key);
			return new CharacterEvent(mdo, parent);
		}
	}

}
