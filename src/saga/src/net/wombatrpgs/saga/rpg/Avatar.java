/**
 *  Hero.java
 *  Created on Nov 25, 2012 8:33:22 PM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.saga.rpg;

import net.wombatrpgs.saga.core.SGlobal;
import net.wombatrpgs.saga.io.CommandListener;
import net.wombatrpgs.saga.maps.Level;
import net.wombatrpgs.sagaschema.characters.HeroMDO;
import net.wombatrpgs.sagaschema.io.data.InputCommand;

/**
 * The physical representation of the player on the world map.
 */
public class Avatar extends CharacterEvent implements CommandListener {
	
	public static final int ABILITIES_MAX = 6;
	
	protected static final String HERO_DEFAULT = "hero_default";

	/**
	 * Placeholder constructor. When the hero is finally initialized properly
	 * this will change. Right now it sets up the hero on the map like any other
	 * event. Also sets up the moveset called "default_moveset" though that
	 * should be put in the hero MDO when it gets created.
	 * MR: Creates the hero
	 * @param	parent			The level to make the hero on
	 */
	public Avatar(Level parent) {
		super(SGlobal.data.getEntryFor(HERO_DEFAULT, HeroMDO.class));
		this.parent = parent;
	}
	
	/**
	 * See above. Deprecated.
	 * @param mdo
	 * @param parent
	 */
	public Avatar(HeroMDO mdo, Level parent) {
		super(mdo, parent);
	}

	/**
	 * @see net.wombatrpgs.saga.rpg.CharacterEvent#reset()
	 */
	@Override
	public void reset() {
		// oh hell no we ain't dyin
	}

	/**
	 * @see net.wombatrpgs.saga.maps.events.MapEvent#getName()
	 */
	@Override
	public String getName() {
		return "hero";
	}

	/**
	 * @see net.wombatrpgs.saga.io.CommandListener#onCommand
	 * (net.wombatrpgs.sagaschema.io.data.InputCommand)
	 */
	@Override
	public boolean onCommand(InputCommand command) {
		// TODO: onCommand
		return true;
	}
}
