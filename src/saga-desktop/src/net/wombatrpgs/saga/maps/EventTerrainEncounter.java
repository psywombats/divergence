/**
 *  EventTerrainEncounter.java
 *  Created on Sep 10, 2014 10:25:13 PM for project saga-desktop
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.saga.maps;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.maps.tiled.TiledMap;

import net.wombatrpgs.mgne.core.MGlobal;
import net.wombatrpgs.mgne.maps.layers.TiledGridLayer;
import net.wombatrpgs.sagaschema.rpg.encounter.EncounterSetMDO;
import net.wombatrpgs.sagaschema.rpg.encounter.TerrainEncounterSetMDO;
import net.wombatrpgs.sagaschema.rpg.encounter.data.TerrainEncounterSetMemberMDO;

/**
 * A more complex encounter that has multiple MDOs to fight depending on the
 * terrain where the encounter triggers. Right now this only supports
 * construction from MDO.
 */
public class EventTerrainEncounter extends EventEncounter {
	
	protected TerrainEncounterSetMDO mdo;
	protected Map<Integer, EncounterSetMDO> encounterTable;
	
	/**
	 * Creates a new terrain encounter event from data.
	 * @param	mdo				The data to create from
	 * @param	map			The level this encounter is loading on
	 */
	public EventTerrainEncounter(TerrainEncounterSetMDO mdo, TiledMap map) {
		this.mdo = mdo;
		encounterTable = new HashMap<Integer, EncounterSetMDO>();
		for (TerrainEncounterSetMemberMDO memberMDO : mdo.members) {
			String[] split = memberMDO.terrain.split("/");
			int id = Integer.valueOf(split[1]);
			id = TiledGridLayer.relativeToAbsoluteTileID(map, split[0], id);
			EncounterSetMDO setMDO = MGlobal.data.getEntryFor(
					memberMDO.encounters, EncounterSetMDO.class);
			encounterTable.put(id, setMDO);
		}
	}

	/**
	 * @see net.wombatrpgs.saga.maps.EventSimpleEncounter#getEncounterSetForTerrain(int)
	 */
	@Override
	protected EncounterSetMDO getEncounterSetForTerrain(int terrain) {
		return encounterTable.get(terrain);
	}

}
