/**
 *  EventCeiling.java
 *  Created on Aug 21, 2014 12:21:27 AM for project saga-desktop
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.saga.maps;

import com.badlogic.gdx.math.Polygon;

import net.wombatrpgs.mgne.core.Avatar;
import net.wombatrpgs.mgne.core.MGlobal;
import net.wombatrpgs.mgne.core.interfaces.FinishListener;
import net.wombatrpgs.mgne.maps.Level;
import net.wombatrpgs.mgne.maps.TiledMapObject;
import net.wombatrpgs.mgne.maps.events.MapEvent;
import net.wombatrpgs.mgneschema.maps.EventMDO;

/**
 * An event that spawns/unspawns the ceiling when the hero walks under it.
 */
public class EventCeiling extends MapEvent {
	
	protected Polygon polygon;
	protected CeilingLayer layer;
	protected FinishListener stepListener;
	protected boolean wasIn;
	
	/**
	 * Creates an ceiling event region from a tiled map object.
	 * @param	object			The object to create from
	 */
	public EventCeiling(TiledMapObject object) {
		super(object.generateMDO(EventMDO.class));
		polygon = object.getPolygon();
		layer = new CeilingLayer(object, polygon);
		assets.add(layer);
		stepListener = new FinishListener() {
			@Override public void onFinish() {
				Avatar hero = MGlobal.getHero();
				boolean nowIn = polygon.contains(hero.getX(), hero.getY());
				if (nowIn && !wasIn) {
					layer.retract();
				} else if (!nowIn && wasIn) {
					layer.deploy();
				}
				wasIn = nowIn;
			}
		};
	}

	/**
	 * @see net.wombatrpgs.mgne.maps.events.MapEvent#update(float)
	 */
	@Override
	public void update(float elapsed) {
		super.update(elapsed);
		layer.update(elapsed);
	}

	/**
	 * @see net.wombatrpgs.mgne.maps.events.MapEvent#onAddedToMap
	 * (net.wombatrpgs.mgne.maps.Level)
	 */
	@Override
	public void onAddedToMap(Level map) {
		super.onAddedToMap(map);
		map.addGridLayer(layer);
	}

	/**
	 * @see net.wombatrpgs.mgne.maps.events.MapEvent#onRemovedFromMap
	 * (net.wombatrpgs.mgne.maps.Level)
	 */
	@Override
	public void onRemovedFromMap(Level map) {
		super.onRemovedFromMap(map);
		// screw it, you're stuck with a non-functional ceiling
	}

	/**
	 * @see net.wombatrpgs.mgne.maps.MapThing#onMapFocusGained
	 * (net.wombatrpgs.mgne.maps.Level)
	 */
	@Override
	public void onMapFocusGained(Level map) {
		super.onMapFocusGained(map);
		quicksetState();
		MGlobal.getHero().addStepListener(stepListener);
	}

	/**
	 * @see net.wombatrpgs.mgne.maps.MapThing#onMapFocusLost
	 * (net.wombatrpgs.mgne.maps.Level)
	 */
	@Override
	public void onMapFocusLost(Level map) {
		super.onMapFocusLost(map);
		MGlobal.getHero().removeStepListener(stepListener);
	}
	
	/**
	 * Sets the retracted/unretracted status based on the hero's immediate
	 * position without any smoothing. Use when teleporting to the map.
	 */
	protected void quicksetState() {
		Avatar hero = MGlobal.getHero();
		if (polygon.contains(hero.getX(), hero.getY())) {
			layer.instantRetract();
			wasIn = true;
		} else {
			layer.instantDeploy();
			wasIn = false;
		}
	}

}
