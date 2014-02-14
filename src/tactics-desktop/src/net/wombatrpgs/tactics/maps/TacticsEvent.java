/**
 *  TacticsEvent.java
 *  Created on Feb 12, 2014 2:42:46 AM for project tactics-desktop
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.tactics.maps;

import java.util.ArrayList;
import java.util.List;

import net.wombatrpgs.mgne.ai.AStarPathfinder;
import net.wombatrpgs.mgne.maps.Loc;
import net.wombatrpgs.mgne.maps.events.MapEvent;
import net.wombatrpgs.mgneschema.maps.data.OrthoDir;
import net.wombatrpgs.tactics.rpg.GameUnit;

/**
 * A TacticsEvent is an event on the map that has a link to a relevant unit in
 * the tactics RPG. That's it. It shouldn't take on the role of the old
 * CharacterEvent. Instead, it should just serve to link the physical to the
 * RPG. This thing is created anew every time a new battle starts by passing
 * it a game unit to take. If there's a unit on the map that's supposed to be
 * the "hero" or whatever, it should get taken out before battle.
 */
public class TacticsEvent extends MapEvent {
	
	protected GameUnit unit;

	/**
	 * Constructs a new TacticsEvent given a GameUnit. Really shouldn't be
	 * called by anything but a GameUnit when it gets created.
	 * @param	unit			The unit this event is for
	 */
	public TacticsEvent(GameUnit unit) {
		super(unit.extractEventMDO());
		this.unit = unit;
	}
	
	/** @return The game unit this event represents */
	public GameUnit getUnit() { return unit; }

	/**
	 * Calculates everywhere this unit could step next turn.
	 * @return					A list of viable step locations
	 */
	public List<Loc> getMoveRange() {
		int move = unit.stats().getMove();
		List<Loc> availableSquares = new ArrayList<Loc>();
		AStarPathfinder pather = new AStarPathfinder();
		pather.setMap(parent);
		pather.setStart(tileX, tileY);
		for (int x = tileX - move; x <= tileX + move; x += 1) {
			for (int y = tileY - move; y <= tileY + move; y += 1) {
				if (x < 0 || x >= parent.getWidth()) continue;
				if (y < 0 || y >= parent.getHeight()) continue;
				if (this.tileDistanceTo(x, y) > move) {
					continue;
				}
				pather.setTarget(x, y);
				List<OrthoDir> path = pather.getOrthoPath(this);
				if (path != null && path.size() <= move) {
					availableSquares.add(new Loc(x, y));
				}
			}
		}
		return availableSquares;
	}

}
