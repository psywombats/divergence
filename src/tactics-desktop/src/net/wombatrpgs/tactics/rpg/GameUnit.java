/**
 *  GameUnit.java
 *  Created on Feb 12, 2014 2:39:47 AM for project tactics-desktop
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.tactics.rpg;

import net.wombatrpgs.mgneschema.maps.EventMDO;
import net.wombatrpgs.tactics.maps.TacticsEvent;
import net.wombatrpgs.tactics.maps.TacticsMap;
import net.wombatrpgs.tacticsschema.rpg.GameUnitMDO;

/**
 * A unit in the tactics RPG part of the game. This includes a link to the
 * unit's physical incarnation, but does not encapsulate it.
 */
public class GameUnit {
	
	protected GameUnitMDO mdo;
	
	protected TacticsEvent event;
	protected TacticsMap map;
	
	/**
	 * Creates a game unit from data. Does nothing about placing it on the map
	 * or creating its physical version. Probably shouldn't be called.
	 * @param	mdo				The data to create unit from
	 */
	protected GameUnit(GameUnitMDO mdo) {
		this.mdo = mdo;
	}
	
	/**
	 * Creates a game unit and spawns it on the map at the given location. This
	 * will create a new doll for the unit.
	 * @param	mdo				The data to create the unit from
	 * @param	map				The map to place unit on
	 * @param	tileX			The location to place unit at (in tiles)
	 * @param	tileY			The location to place unit at (in tiles)
	 */
	public GameUnit(GameUnitMDO mdo, TacticsMap map, int tileX, int tileY) {
		this(mdo);
		this.map = map;
		this.event = new TacticsEvent(this);
		this.event.setTileLocation(tileX, tileY);
	}
	
	/**
	 * Returns an eventMDO containing information about constructing a doll for
	 * this game unit. Safe to call/construct more than once.
	 * @return					An MDO with event information from this unit.
	 */
	public EventMDO extractEventMDO() {
		EventMDO dollMDO = new EventMDO();
		dollMDO.appearance = mdo.appearance;
		dollMDO.name = mdo.name;
		return dollMDO;
	}

}
