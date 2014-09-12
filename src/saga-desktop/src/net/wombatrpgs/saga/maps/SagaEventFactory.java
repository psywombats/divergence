/**
 *  SagaEventFactory.java
 *  Created on Jun 26, 2014 12:51:17 PM for project saga-desktop
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.saga.maps;

import com.badlogic.gdx.maps.tiled.TiledMap;

import net.wombatrpgs.mgne.core.MGlobal;
import net.wombatrpgs.mgne.maps.TiledMapObject;
import net.wombatrpgs.mgne.maps.events.EventFactory;
import net.wombatrpgs.mgne.maps.events.MapEvent;
import net.wombatrpgs.sagaschema.rpg.encounter.EncounterSetMDO;
import net.wombatrpgs.sagaschema.rpg.encounter.TerrainEncounterSetMDO;

/**
 * Saga-specific map events.
 */
public class SagaEventFactory extends EventFactory {
	
	protected static final String TYPE_ENCOUNTER = "Encounter";
	protected static final String TYPE_CEILING = "Ceiling";
	protected static final String TYPE_CHEST = "Chest";
	
	protected static final String PROPERTY_ENCOUNTER = "encounter";
	protected static final String PROPERTY_TERRAIN_ENCOUNTER = "terrainEncounter";

	/**
	 * @see net.wombatrpgs.mgne.maps.events.EventFactory#createEvent
	 * (net.wombatrpgs.mgne.maps.TiledMapObject)
	 */
	@Override
	protected MapEvent createEvent(TiledMapObject object) {
		String type = object.getString(TiledMapObject.PROPERTY_TYPE);
		if (TYPE_ENCOUNTER.equals(type)) {
			return new EventSimpleEncounter(object);
		} else if (TYPE_CEILING.equals(type)) {
			return new EventCeiling(object);
		} else if (TYPE_CHEST.equals(type)) {
			return new EventChest(object);
		}
		return super.createEvent(object);
	}

	/**
	 * @see net.wombatrpgs.mgne.maps.events.EventFactory#createFromMapProperty
	 * (com.badlogic.gdx.maps.tiled.TiledMap, java.lang.String, java.lang.String)
	 */
	@Override
	public MapEvent createFromMapProperty(TiledMap map, String key, String value) {
		MapEvent superResult = super.createFromMapProperty(map, key, value);
		if (superResult != null) return superResult;
		if (TYPE_ENCOUNTER.equals(key)) {
			EncounterSetMDO mdo = MGlobal.data.getEntryFor(value, EncounterSetMDO.class);
			return new EventSimpleEncounter(mdo);
		} else if (PROPERTY_TERRAIN_ENCOUNTER.equals(key)) {
			TerrainEncounterSetMDO mdo = MGlobal.data.getEntryFor(value, TerrainEncounterSetMDO.class);
			return new EventTerrainEncounter(mdo, map);
		}
		return null;
	}

}
