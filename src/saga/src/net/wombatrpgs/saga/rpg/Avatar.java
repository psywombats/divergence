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
import net.wombatrpgs.saga.maps.events.MapEvent;
import net.wombatrpgs.sagaschema.characters.HeroMDO;
import net.wombatrpgs.sagaschema.io.data.InputCommand;
import net.wombatrpgs.sagaschema.maps.data.OrthoDir;

/**
 * The physical representation of the player on the world map.
 */
public class Avatar extends CharacterEvent implements CommandListener {
	
	protected static final String HERO_DEFAULT = "hero_default";
	
	protected OrthoDir dirToMove, lastMove;

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
		dirToMove = null;
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
	 * @see net.wombatrpgs.saga.rpg.CharacterEvent#stopMoving()
	 */
	@Override
	public void stopMoving() {
		if (dirToMove == null) {
			super.stopMoving();
		} else {
			if (lastMove != dirToMove) {
				super.stopMoving();
			}
			lastStep = null;
			travelPlan.clear();
			move(dirToMove);
		}
	}

	/**
	 * @see net.wombatrpgs.saga.io.CommandListener#onCommand
	 * (net.wombatrpgs.sagaschema.io.data.InputCommand)
	 */
	@Override
	public boolean onCommand(InputCommand command) {
		if (command == InputCommand.MOVE_STOP) {
			dirToMove = null;
		}
		if (SGlobal.levelManager.getActive().isMoving()) {
			switch (command) {
			case MOVE_LEFT:			dirToMove = OrthoDir.WEST;		break;
			case MOVE_UP:			dirToMove = OrthoDir.NORTH;		break;
			case MOVE_RIGHT:		dirToMove = OrthoDir.EAST;		break;
			case MOVE_DOWN:			dirToMove = OrthoDir.SOUTH;		break;
			default:												break;
			}
			return true;
		} else {
			switch (command) {
			case MOVE_LEFT:			move(OrthoDir.WEST);	break;
			case MOVE_UP:			move(OrthoDir.NORTH);	break;
			case MOVE_RIGHT:		move(OrthoDir.EAST);	break;
			case MOVE_DOWN:			move(OrthoDir.SOUTH);	break;
			case WORLD_INTERACT:	interact();				break;
			default:				return false;
			}
			return true;
		}
	}
	
	/**
	 * Moves in a certain dir on the map?
	 * @param	dir				The direction to move
	 */
	protected void move(OrthoDir dir) {
		attemptStep(dir);
		lastMove = dir;
		if (!parent.isMoving()) {
			parent.onTurn();
		}
	}
	
	/**
	 * Interact with whatever we're standing on or facing.
	 * @see net.wombatrpgs.saga.maps.events.MapEvent#onInteract()
	 */
	protected void interact() {
		for (MapEvent event : parent.getEventsAt(getTileX(), getTileY())) {
			if (event == this) continue;
			if (!event.isPassable()) continue;
			if (event.onInteract()) return;
		}
		OrthoDir facing = getFacing();
		int tileX = (int) (getTileX() + facing.getVector().x);
		int tileY = (int) (getTileY() + facing.getVector().y);
		for (MapEvent event : parent.getEventsAt(tileX, tileY)) {
			if (event.isPassable()) continue;
			if (event.onInteract()) return;
		}
	}
}
