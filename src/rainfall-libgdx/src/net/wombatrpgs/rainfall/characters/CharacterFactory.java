/**
 *  CharacterFactory.java
 *  Created on Jan 24, 2013 9:33:44 PM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.rainfall.characters;

import net.wombatrpgs.rainfall.characters.enemies.Enemy;
import net.wombatrpgs.rainfall.core.RGlobal;
import net.wombatrpgs.rainfall.maps.Level;
import net.wombatrpgs.rainfallschema.enemies.EnemyEventMDO;
import net.wombatrpgs.rainfallschema.hero.HeroMDO;
import net.wombatrpgs.rainfallschema.maps.CharacterEventMDO;

/**
 * A factory for creating characters from a generic MDO.
 */
public class CharacterFactory {
	
	/**
	 * Creates a character event of the appropriate subclass by passing some
	 * arguments along to the correct constructor.
	 * @param mdo			The MDO with the data to generate from
	 * @param parent		The parent level
	 * @param x				The initial x-coord (in tiles)
	 * @param y				The initial y-coord (in tiles)
	 * @return
	 */
	public static CharacterEvent create(CharacterEventMDO mdo, Level parent, int x, int y) {
		// TODO: it may be possible to generalize this
		// TODO: this is a hack for hero ID-ing
		if (HeroMDO.class.isAssignableFrom(mdo.getClass())) {
			return new Hero(mdo, parent, x, y);
		} else if (EnemyEventMDO.class.isAssignableFrom(mdo.getClass())) {
			return new Enemy((EnemyEventMDO) mdo, parent, x, y);
		} else {
			RGlobal.reporter.warn("Unknown subclass of chara MDO: " + mdo);
			return new CharacterEvent(mdo, parent, x ,y);
		}
	}

}
