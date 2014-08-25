/**
 *  Hero.java
 *  Created on Nov 25, 2012 8:33:22 PM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mgne.core;

import java.util.ArrayList;
import java.util.List;

import net.wombatrpgs.mgne.core.interfaces.FinishListener;
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
	
	protected List<FinishListener> stepListeners;
	protected boolean paused;

	/**
	 * For real hero constructor. Looks up the avatar in the database and
	 * uses it to set up a map event.
	 */
	public Avatar() {
		super(MGlobal.data.getEntryFor(HERO_DEFAULT, EventMDO.class));
		stepListeners = new ArrayList<FinishListener>();
		addStepTracker();
	}

	/**
	 * @see net.wombatrpgs.saga.rpg.CharacterEvent#reset()
	 */
	@Override
	public void reset() {
		// oh hell no we ain't dyin
		// ^^ p sure that's been around since the Blockbound days
	}

	/**
	 * @see net.wombatrpgs.mgne.maps.events.MapEvent#getName()
	 */
	@Override
	public String getName() {
		return "hero";
	}
	
	/**
	 * Sets the paused state. Paused heroes can't move or interact.
	 * @param	paused			True to pause, false to unpause
	 */
	public void pause(boolean paused) {
		this.paused = paused;
	}
	
	/**
	 * Adds a callback for when the hero finishes taking a step. Make sure to
	 * remove it when no longer necessary.
	 * @param	listener		The listener to call
	 */
	public void addStepListener(FinishListener listener) {
		stepListeners.add(listener);
	}
	
	/**
	 * Removes a callback for hero stepping.
	 * @param	listener		The listener to remove
	 */
	public void removeStepListener(FinishListener listener) {
		if (!stepListeners.contains(listener)) {
			MGlobal.reporter.warn("Tried to remove non-added stepper");
		}
		stepListeners.remove(listener);
	}

	/**
	 * @see net.wombatrpgs.mgne.io.CommandListener#onCommand
	 * (net.wombatrpgs.mgneschema.io.data.InputCommand)
	 */
	@Override
	public boolean onCommand(InputCommand command) {
		if (!tracking && !paused) {
			switch (command) {
			case MOVE_LEFT:			move(OrthoDir.WEST);	break;
			case MOVE_UP:			move(OrthoDir.NORTH);	break;
			case MOVE_RIGHT:		move(OrthoDir.EAST);	break;
			case MOVE_DOWN:			move(OrthoDir.SOUTH);	break;
			case WORLD_INTERACT:	interact();				break;
			default:				return false;
			}
		}
		return true;
	}
	
	/**
	 * Moves in a certain dir on the map?
	 * @param	dir				The direction to move
	 */
	protected void move(OrthoDir dir) {
		attemptStep(dir);
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
	
	/**
	 * Adds the step tracker for when steps finish.
	 */
	protected void addStepTracker() {
		addTrackingListener(new FinishListener() {
			@Override public void onFinish() {
				for (FinishListener listener : stepListeners) {
					listener.onFinish();
				}
				addStepTracker();
			}
		});
	}
}
