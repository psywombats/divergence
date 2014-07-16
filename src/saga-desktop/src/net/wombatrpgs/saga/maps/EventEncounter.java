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
import net.wombatrpgs.mgne.maps.TiledMapObject;
import net.wombatrpgs.mgne.maps.events.MapEvent;
import net.wombatrpgs.saga.rpg.battle.Battle;
import net.wombatrpgs.saga.rpg.chara.EnemyParty;
import net.wombatrpgs.sagaschema.rpg.EncounterMDO;
import net.wombatrpgs.sagaschema.rpg.EncounterSetMDO;

/**
 * A block area where the player can fight some random encounters!
 */
public class EventEncounter extends MapEvent {
	
	protected EncounterSetMDO mdo;
	protected Polygon polygon;
	protected int lastX, lastY;
	
	/**
	 * Creates an encounter event region from a tiled map object.
	 * @param	object			The object to create from
	 */
	public EventEncounter(TiledMapObject object) {
		String key = object.getString(TiledMapObject.PROPERTY_MDO);
		mdo = MGlobal.data.getEntryFor(key, EncounterSetMDO.class);
		polygon = object.getPolygon();
	}

	/**
	 * @see net.wombatrpgs.mgne.maps.events.MapEvent#update(float)
	 */
	@Override
	public void update(float elapsed) {
		super.update(elapsed);
		final Avatar hero = MGlobal.getHero();
		if (polygon.contains(hero.getX(), hero.getY())) {
			hero.addTrackingListener(new FinishListener() {
				@Override public void onFinish() {
					if (MGlobal.rand.nextInt(mdo.steps) == 0 &&
							polygon.contains(hero.getX(), hero.getY())) {
						encounter();
					}
				}
			});
		}
	}
	
	/**
	 * Starts an encounter with a group included in this encounter set.
	 */
	public void encounter() {
		List<EncounterMDO> encounters = new ArrayList<EncounterMDO>();
		for (String key : mdo.encounters) {
			EncounterMDO mdo = MGlobal.data.getEntryFor(key, EncounterMDO.class);
			for (int i = 0; i < mdo.weight; i += 1) {
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
