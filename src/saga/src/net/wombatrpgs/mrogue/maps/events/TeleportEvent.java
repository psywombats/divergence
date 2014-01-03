/**
 *  TeleportEvent.java
 *  Created on Dec 24, 2012 2:00:17 AM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mrogue.maps.events;

import net.wombatrpgs.mrogue.core.FinishListener;
import net.wombatrpgs.mrogue.core.MGlobal;
import net.wombatrpgs.mrogue.maps.Level;
import net.wombatrpgs.mrogue.maps.Loc;
import net.wombatrpgs.mrogue.rpg.CharacterEvent;

/**
 * Constructs a teleportation device! (or event, just depends on your
 * perspective...)
 * MR: Contains a link to some map data. If that map doesn't exist, generate it.
 * This is meant to be the payload of any staircase map event, and they should
 * extend it.
 */
public class TeleportEvent extends MapEvent {
	
	protected String mapKey;
	
	protected boolean triggered;
	protected MapEvent victim;

	/**
	 * Creates a new teleport for the supplied parent level using coordinates
	 * inferred from the tiled object. Called from the superclass's factory
	 * method.
	 * @param 	parent			The level we want to teleport from
	 * @param	mapKey			The MDO key of the map we want to teleport to
	 * @param	dir				Whether the stairs go up or down
	 */
	public TeleportEvent(Level parent, String mapKey) {
		super(parent);
		this.mapKey = mapKey;
		
		triggered = false;
		victim = null;
	}

	/**
	 * @see net.wombatrpgs.mrogue.maps.events.MapEvent#update(float)
	 */
	@Override
	public void update(float elapsed) {
		super.update(elapsed);
		if (triggered && !parent.isMoving()) {
			teleport(victim);
			triggered = false;
		}
	}

	/**
	 * @see net.wombatrpgs.mrogue.maps.events.MapEvent#collideWith
	 * (net.wombatrpgs.mrogue.rpg.CharacterEvent)
	 */
	@Override
	public void collideWith(CharacterEvent character) {
		if (!triggered && character == MGlobal.hero) {
			triggered = true;
			victim = character;
		}
	}

	/**
	 * @see net.wombatrpgs.mrogue.maps.events.MapEvent#isPassable()
	 */
	@Override
	public boolean isPassable() {
		return true;
	}
	
	/**
	 * Teleports the hero from one map to another.
	 * @param	other			The event that triggered this (hero)	
	 */
	protected void teleport(MapEvent other) {
		if (other != MGlobal.hero) return;
		MGlobal.levelManager.getTele().getPre().addListener(new FinishListener() {
			@Override
			public void onFinish() {
				Level newMap = MGlobal.levelManager.getLevel(mapKey);
				Loc to = newMap.getTeleInLoc(parent.getKey());
				MGlobal.levelManager.getTele().teleport(newMap, to.x, to.y);
				MGlobal.levelManager.getTele().getPost().run();
			}
		});
		MGlobal.levelManager.getTele().getPre().run();
	}
	
}
