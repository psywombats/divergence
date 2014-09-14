/**
 *  EventEncounter.java
 *  Created on Sep 10, 2014 10:09:53 PM for project saga-desktop
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.saga.maps;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.math.Polygon;

import net.wombatrpgs.mgne.core.Avatar;
import net.wombatrpgs.mgne.core.MGlobal;
import net.wombatrpgs.mgne.core.interfaces.FinishListener;
import net.wombatrpgs.mgne.maps.Level;
import net.wombatrpgs.mgne.maps.TiledMapObject;
import net.wombatrpgs.mgne.maps.events.MapEvent;
import net.wombatrpgs.saga.rpg.battle.Battle;
import net.wombatrpgs.saga.rpg.chara.EnemyParty;
import net.wombatrpgs.sagaschema.rpg.encounter.EncounterMDO;
import net.wombatrpgs.sagaschema.rpg.encounter.EncounterSetMDO;
import net.wombatrpgs.sagaschema.rpg.encounter.data.EncounterSetMemberMDO;

/**
 * Abstract superclass for simple encounters and terrain-based encounters.
 */
public abstract class EventEncounter extends MapEvent {
	
	protected Polygon poly;
	protected FinishListener onStep;
	protected int lastX, lastY;
	
	/**
	 * Creates a new encounter event that acts as if it covers the entire map.
	 */
	public EventEncounter() {
		onStep = new FinishListener() {
			@Override public void onFinish() {
				onStep();
			}
		};
	}
	
	/**
	 * Creates a new encounter event. Registers a listener etc.
	 * @param	object			The tiled object defining this event's poly
	 */
	public EventEncounter(TiledMapObject object) {
		this();
		poly = object.getPolygon();
	}
	
	/**
	 * @see net.wombatrpgs.mgne.maps.MapThing#onMapFocusGained
	 * (net.wombatrpgs.mgne.maps.Level)
	 */
	@Override
	public void onMapFocusGained(Level map) {
		super.onMapFocusGained(map);
		MGlobal.getHero().addStepListener(onStep);
	}

	/**
	 * @see net.wombatrpgs.mgne.maps.MapThing#onMapFocusLost
	 * (net.wombatrpgs.mgne.maps.Level)
	 */
	@Override
	public void onMapFocusLost(Level map) {
		super.onMapFocusLost(map);
		MGlobal.getHero().removeStepListener(onStep);
	}
	
	/**
	 * Starts an encounter with a group included in this encounter set.
	 * @param	mdo				The MDO of the group to battle
	 */
	public void encounter(EncounterSetMDO mdo) {
		List<EncounterMDO> encounters = new ArrayList<EncounterMDO>();
		for (EncounterSetMemberMDO member : mdo.encounters) {
			EncounterMDO encMDO = MGlobal.data.getEntryFor(member.encounter, EncounterMDO.class);
			for (int i = 0; i < member.weight; i += 1) {
				encounters.add(encMDO);
			}
		}
		EncounterMDO chosen = encounters.get(MGlobal.rand.nextInt(encounters.size()));
		EnemyParty party = new EnemyParty(chosen);
		Battle battle = new Battle(party, true);
		MGlobal.assets.loadAsset(battle, "encounter " + chosen.key);
		battle.start();
	}
	
	/**
	 * Called each time the hero finishes a step.
	 */
	protected final void onStep() {
		Avatar hero = MGlobal.getHero();
		int terrainID = MGlobal.levelManager.getActive().getTerrainAt(
				MGlobal.getHero().getTileX(),
				MGlobal.getHero().getTileY());
		EncounterSetMDO mdo = getEncounterSetForTerrain(terrainID);
		if (MGlobal.rand.nextInt(mdo.steps) == 0 &&
				mdo != null &&
				(poly == null || poly.contains(hero.getX(), hero.getY())) &&
				!"off".equals(MGlobal.args.get("encounters"))) {
			encounter(mdo);
		}
	}
	
	/**
	 * Fetches the correct encounter set for the terrain supplied. Or return
	 * null for no encounter. Keep in mind the terrain ID is supplied by gid
	 * not relative to the tileset.
	 * @param	terrain			The terrain the hero is currently on (gid)
	 * @return					The encounters faces in that terrain
	 */
	protected abstract EncounterSetMDO getEncounterSetForTerrain(int terrain);

}
