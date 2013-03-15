/**
 *  IntentPathfindToHero.java
 *  Created on Mar 14, 2013 9:26:54 AM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.rainfall.characters.ai.actions;

import java.util.List;

import net.wombatrpgs.rainfall.characters.CharacterEvent;
import net.wombatrpgs.rainfall.characters.ai.AStarPathfinder;
import net.wombatrpgs.rainfall.characters.ai.Intelligence;
import net.wombatrpgs.rainfall.characters.ai.IntentAct;
import net.wombatrpgs.rainfall.core.RGlobal;
import net.wombatrpgs.rainfallschema.maps.data.Direction;

/**
 * Uses A* to hunt down hero. Calculates occasionally.
 */
public class IntentPathfindToHero extends IntentAct {
	
	AStarPathfinder finder;

	public IntentPathfindToHero(Intelligence parent, CharacterEvent actor) {
		super(parent, actor);
		this.finder = new AStarPathfinder(actor.getLevel(), 0, 0, 0, 0, 0);
	}

	@Override
	public void act() {
		if (!actor.isTracking() && RGlobal.hero.getLevel() != null) {
			finder.setStart(actor.getTileX(), actor.getTileY(), actor.getZ());
			finder.setTarget(RGlobal.hero.getTileX(), RGlobal.hero.getTileY());
			List<Direction> steps = finder.getPath();
			if (steps != null && steps.size() != 0) {
				float toX = (steps.get(0).getVector().x + actor.getTileX());
				float toY = (steps.get(0).getVector().y + actor.getTileY());
				toX *= actor.getLevel().getTileWidth();
				toY *= actor.getLevel().getTileHeight();
				actor.targetLocation(toX, toY);
			}
		}
	}

}
