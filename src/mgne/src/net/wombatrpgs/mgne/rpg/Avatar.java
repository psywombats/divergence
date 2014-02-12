/**
 *  Hero.java
 *  Created on Nov 25, 2012 8:33:22 PM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mgne.rpg;

import net.wombatrpgs.mgne.core.MGlobal;
import net.wombatrpgs.mgne.io.CommandListener;
import net.wombatrpgs.mgne.maps.events.MapEvent;
import net.wombatrpgs.mgneschema.io.data.InputCommand;
import net.wombatrpgs.mgneschema.maps.EventMDO;
import net.wombatrpgs.mgneschema.maps.data.OrthoDir;

/**
 * The physical representation of the player on the world map.
 */
public class Avatar extends MapEvent implements CommandListener {
	
	protected static final String HERO_DEFAULT = "event_hero";
	
	protected OrthoDir dirToMove, lastMove;

	/**
	 * For real hero constructor. Looks up the avatar in the database and
	 * uses it to set up a map event.
	 */
	public Avatar() {
		super(MGlobal.data.getEntryFor(HERO_DEFAULT, EventMDO.class));
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
	 * @see net.wombatrpgs.mgne.maps.events.MapEvent#getName()
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
	 * @see net.wombatrpgs.mgne.io.CommandListener#onCommand
	 * (net.wombatrpgs.mgneschema.io.data.InputCommand)
	 */
	@Override
	public boolean onCommand(InputCommand command) {
		if (command == InputCommand.MOVE_STOP) {
			dirToMove = null;
		}
		if (MGlobal.levelManager.getActive().isMoving()) {
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
		if (attemptStep(dir)) {
			lastMove = dir;
			if (!parent.isMoving()) {
				parent.onTurn();
			}
		}
	}
	
	/**
	 * Interact with whatever we're standing on or facing.
	 * @see net.wombatrpgs.mgne.maps.events.MapEvent#onInteract()
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
