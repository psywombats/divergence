/**
 *  CharacterFactory.java
 *  Created on Jan 24, 2013 9:33:44 PM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.rainfall.characters;

import com.badlogic.gdx.graphics.g2d.tiled.TiledObject;

import net.wombatrpgs.rainfall.characters.enemies.EnemyEvent;
import net.wombatrpgs.rainfall.core.RGlobal;
import net.wombatrpgs.rainfall.maps.Level;
import net.wombatrpgs.rainfallschema.characters.CharacterEventMDO;
import net.wombatrpgs.rainfallschema.characters.enemies.EnemyEventMDO;
import net.wombatrpgs.rainfallschema.characters.hero.HeroMDO;

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
	public static CharacterEvent create(CharacterEventMDO mdo, 
			TiledObject object, Level parent, int x, int y) {
		// it may be possible to generalize this
		if (HeroMDO.class.isAssignableFrom(mdo.getClass())) {
			return new Hero(mdo, object, parent, x, y);
		} else if (EnemyEventMDO.class.isAssignableFrom(mdo.getClass())) {
			return new EnemyEvent((EnemyEventMDO) mdo, object, parent, x, y);
		} else {
			RGlobal.reporter.warn("Unknown subclass of chara MDO: " + mdo);
			return new CharacterEvent(mdo, object, parent, x ,y);
		}
	}

}
