/**
 *  Cursor.java
 *  Created on Feb 14, 2014 3:01:40 AM for project tactics-desktop
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.tactics.ui;

import java.util.ArrayList;
import java.util.List;

import net.wombatrpgs.mgne.maps.events.MapEvent;
import net.wombatrpgs.mgneschema.maps.EventMDO;
import net.wombatrpgs.mgneschema.maps.data.OrthoDir;
import net.wombatrpgs.tacticsschema.ui.CursorMDO;

/**
 * Cursor for selection on tactics map.
 */
public class MapCursor extends MapEvent {
	
	protected CursorMDO mdo;
	protected List<OrthoDir> nextSteps;
	
	/**
	 * Creates a new cursor by converting a cursor mdo to an event mdo.
	 * @param	mdo				The data to create from
	 */
	public MapCursor(CursorMDO mdo) {
		super(convertMDO(mdo));
		this.mdo = mdo;
		nextSteps = new ArrayList<OrthoDir>();
	}
	
	/**
	 * Moves the cursor in a direction if possible. This will actually append
	 * onto any outstanding movements so that the cursor will end up where the
	 * player wants it, whether or not the animation is currently done.
	 * @param	dir				The next direction to move the cursor
	 */
	public void move(OrthoDir dir) {
		nextSteps.add(nextSteps.size(), dir);
		tileX += dir.getVector().x;
		tileY += dir.getVector().y;
		targetNextStep();
	}
	
	/**
	 * @see net.wombatrpgs.mgne.maps.events.MapEvent#update(float)
	 */
	@Override
	public void update(float elapsed) {
		super.update(elapsed);
		targetNextStep();
	}

	/**
	 * @see net.wombatrpgs.mgne.maps.events.MapEvent#getRenderX()
	 */
	@Override
	public int getRenderX() {
		return super.getRenderX() - parent.getTileWidth()/2;
	}

	/**
	 * @see net.wombatrpgs.mgne.maps.events.MapEvent#getRenderY()
	 */
	@Override
	public int getRenderY() {
		return super.getRenderY() - parent.getTileHeight()/2;
	}

	/**
	 * @see net.wombatrpgs.mgne.maps.events.MapEvent#isPassable()
	 */
	@Override
	public boolean isPassable() {
		return true;
	}

	/**
	 * Converts a cursor MDO into something that can be used to create an
	 * event.
	 * @param	mdo				The data to convert from
	 * @return					The converted data
	 */
	protected static EventMDO convertMDO(CursorMDO mdo) {
		EventMDO eventMDO = new EventMDO();
		eventMDO.appearance = mdo.appearance;
		return eventMDO;
	}
	
	/**
	 * Moves the next step up, if need be.
	 */
	protected void targetNextStep() {
		if (!isTracking() && nextSteps.size() > 0) {
			OrthoDir step = nextSteps.get(0);
			nextSteps.remove(0);
			targetLocation(
					x + step.getVector().x * parent.getTileWidth(),
					y + step.getVector().y * parent.getTileHeight());
			vx = mdo.speed * step.getVector().x;
			vy = mdo.speed * step.getVector().y;
		}
	}

}
