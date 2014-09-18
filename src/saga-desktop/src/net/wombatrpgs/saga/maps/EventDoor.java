/**
 *  EventDoor.java
 *  Created on Sep 17, 2014 11:54:08 PM for project saga-desktop
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.saga.maps;

import net.wombatrpgs.mgne.core.MGlobal;
import net.wombatrpgs.mgne.maps.events.MapEvent;
import net.wombatrpgs.saga.core.SGlobal;
import net.wombatrpgs.sagaschema.events.EventDoorMDO;

/**
 * A locked door.
 */
public class EventDoor extends MapEvent {
	
	protected EventDoorMDO mdo;

	/**
	 * Creates a new door from data.
	 * @param	mdo				The data to create from
	 */
	public EventDoor(EventDoorMDO mdo) {
		super(mdo);
		this.mdo = mdo;
	}

	/**
	 * @see net.wombatrpgs.mgne.maps.events.MapEvent#isHidden()
	 */
	@Override
	public boolean isHidden() {
		return super.isHidden() || MGlobal.memory.getSwitch(getSwitchName());
	}

	/**
	 * @see net.wombatrpgs.mgne.maps.events.MapEvent#isPassable()
	 */
	@Override
	public boolean isPassable() {
		return MGlobal.memory.getSwitch(getSwitchName());
	}

	/**
	 * @see net.wombatrpgs.mgne.maps.events.MapEvent#onInteract()
	 */
	@Override
	public boolean onInteract() {
		if (MGlobal.memory.getSwitch(getSwitchName())) {
			// already open
			return false;
		} else {
			if (SGlobal.heroes.isCarryingItemType(mdo.keyItem)) {
				MGlobal.ui.getBlockingBox().blockText(
						MGlobal.levelManager.getScreen(), "Used the key.");
				MGlobal.memory.setSwitch(getSwitchName());
			} else {
				MGlobal.ui.getBlockingBox().blockText(
						MGlobal.levelManager.getScreen(), "It's locked.");
			}
			return true;
		}
	}
	
	/**
	 * Builds the switch name string. Moved to a method because it was being
	 * constructed while the door was still at (0, 0).
	 * @return					The switch to use for memory
	 */
	protected String getSwitchName() {
		String name = "door_" + parent.getKeyName();
		name += "(" + getTileX() + "," + getTileY() + ")";
		return name;
	}

}
