/**
 *  EventEncounter.java
 *  Created on Jun 26, 2014 3:05:10 PM for project saga-desktop
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
 * A block area where the player can fight some random encounters!
 */
public class EventEncounter extends MapEvent {
	
	protected EncounterSetMDO mdo;
	protected Polygon polygon;
	protected FinishListener onStep;
	protected int lastX, lastY;
	
	/**
	 * Creates an encounter event region from a tiled map object.
	 * @param	object			The object to create from
	 */
	public EventEncounter(TiledMapObject object) {
		String key = object.getString(TiledMapObject.PROPERTY_MDO);
		mdo = MGlobal.data.getEntryFor(key, EncounterSetMDO.class);
		polygon = object.getPolygon();
		onStep = new FinishListener() {
			@Override public void onFinish() {
				Avatar hero = MGlobal.getHero();
				if (MGlobal.rand.nextInt(mdo.steps) == 0 &&
						polygon.contains(hero.getX(), hero.getY())) {
					encounter();
				}
			}
		};
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
	 */
	public void encounter() {
		List<EncounterMDO> encounters = new ArrayList<EncounterMDO>();
		for (EncounterSetMemberMDO member : mdo.encounters) {
			EncounterMDO mdo = MGlobal.data.getEntryFor(member.encounter, EncounterMDO.class);
			for (int i = 0; i < member.weight; i += 1) {
				encounters.add(mdo);
			}
		}
		EncounterMDO chosen = encounters.get(MGlobal.rand.nextInt(encounters.size()));
		EnemyParty party = new EnemyParty(chosen);
		Battle battle = new Battle(party, true);
		MGlobal.assets.loadAsset(battle, "encounter " + chosen.key);
		battle.start();
	}

}
