/**
 *  Hero.java
 *  Created on Nov 25, 2012 8:33:22 PM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mrogue.characters;

import net.wombatrpgs.mrogue.characters.travel.BumpStep;
import net.wombatrpgs.mrogue.characters.travel.MoveStep;
import net.wombatrpgs.mrogue.core.MGlobal;
import net.wombatrpgs.mrogue.io.CommandListener;
import net.wombatrpgs.mrogue.maps.Level;
import net.wombatrpgs.mrogueschema.characters.HeroMDO;
import net.wombatrpgs.mrogueschema.io.data.InputCommand;

/**
 * Placeholder class for the protagonist player.
 */
public class Hero extends CharacterEvent implements CommandListener {
	
	protected static final String HERO_DEFAULT = "hero_default";

	/**
	 * Placeholder constructor. When the hero is finally initialized properly
	 * this will change. Right now it sets up the hero on the map like any other
	 * event. Also sets up the moveset called "default_moveset" though that
	 * should be put in the hero MDO when it gets created.
	 * MR: Creates the hero, places it on a map, sets its x/y.
	 * @param	object			The tiled obejct that generated the character
	 * @param 	parent			The level the hero starts on
	 * @param	tileX			The x-coord to start on, in tiles
	 * @param	tileY			The y-coord to start on, in tiles
	 */
	public Hero(Level parent, int tileX, int tileY) {
		// TODO: Hero
		super(MGlobal.data.getEntryFor(HERO_DEFAULT, HeroMDO.class), parent, tileX, tileY);
		MGlobal.hero = this;
	}
	
	/**
	 * See above. Deprecated.
	 * @param mdo
	 * @param parent
	 */
	public Hero(HeroMDO mdo, Level parent) {
		super(mdo, parent);
	}

	/**
	 * @see net.wombatrpgs.mrogue.characters.CharacterEvent#reset()
	 */
	@Override
	public void reset() {
		// oh hell no we ain't dyin
	}

	/**
	 * @see net.wombatrpgs.mrogue.maps.events.MapEvent#getName()
	 */
	@Override
	public String getName() {
		return "hero";
	}

	/**
	 * @see net.wombatrpgs.mrogue.io.CommandListener#onCommand
	 * (net.wombatrpgs.mrogueschema.io.data.InputCommand)
	 */
	@Override
	public void onCommand(InputCommand command) {
		if (parent.isMoving()) {
			return;
		}
		int targetX = getTileX();
		int targetY = getTileY();
		switch (command) {
		case MOVE_UP:
			targetY += 1;
			break;
		case MOVE_DOWN:
			targetY -= 1;
			break;
		case MOVE_LEFT:
			targetX -= 1;
			break;
		case MOVE_RIGHT:
			targetX += 1;
			break;
		default:
			// nothing
		}
		if (parent.isTilePassable(this, targetX, targetY)) {
			travelPlan.add(new MoveStep(this, targetX, targetY));
			tileX = targetX;
			tileY = targetY;
		} else {
			travelPlan.add(new BumpStep(this, directionToTile(targetX, targetY)));
		}
		parent.startMoving();
	}
	
}
