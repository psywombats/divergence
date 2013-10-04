/**
 *  IntentPathfindToHero.java
 *  Created on Mar 14, 2013 9:26:54 AM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mrogue.characters.ai.actions;

import java.util.List;

import net.wombatrpgs.mrogue.characters.CharacterEvent;
import net.wombatrpgs.mrogue.characters.ai.AStarPathfinder;
import net.wombatrpgs.mrogue.characters.ai.BehaviorList;
import net.wombatrpgs.mrogue.characters.ai.IntentAct;
import net.wombatrpgs.mrogue.core.MGlobal;
import net.wombatrpgs.mrogueschema.maps.data.Direction;

/**
 * Uses A* to hunt down hero. Calculates occasionally.
 */
public class IntentPathfindToHero extends IntentAct {
	
	AStarPathfinder finder;

	public IntentPathfindToHero(BehaviorList parent, CharacterEvent actor) {
		super(parent, actor);
		this.finder = new AStarPathfinder(actor.getLevel(), 0, 0, 0, 0, 0);
	}

	@Override
	public void act() {
		if (!actor.isTracking() && MGlobal.hero.getLevel() != null) {
			int tileX = Math.round(actor.getX()/32.f);
			int tileY = Math.round(actor.getY()/32.f);
			float targetTileX = tileX * 32;
			float targetTileY = tileY * 32;
			if (actor.getLevel().isTilePassable(actor, tileX, tileY) &&
					(Math.abs(targetTileX-actor.getX()) > .1 || 
					Math.abs(targetTileY-actor.getY()) > .1)) {
				actor.targetLocation(targetTileX, targetTileY);
			} else if (MGlobal.hero.getLevel() != null) {
				actor.setX(targetTileX);
				actor.setY(targetTileY);
				finder.setStart(actor.getTileX(), actor.getTileY());
				finder.setTarget(MGlobal.hero.getTileX(), MGlobal.hero.getTileY());
				List<Direction> steps = finder.getPath(actor);
				if (steps != null && steps.size() != 0) {
					Direction first = steps.get(0);
					int t;
					for (t = 0; t < steps.size() && steps.get(t) == first; t++);
					float toX = (t * first.getVector().x + actor.getTileX());
					float toY = (t * first.getVector().y + actor.getTileY());
					toX *= actor.getLevel().getTileWidth();
					toY *= actor.getLevel().getTileHeight();
					actor.targetLocation(toX, toY);
				}
			}
		}
	}

}
