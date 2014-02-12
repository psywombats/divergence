/**
 *  TacticsEvent.java
 *  Created on Feb 12, 2014 2:42:46 AM for project tactics-desktop
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.tactics.maps;

import net.wombatrpgs.mgne.maps.events.MapEvent;
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

}
