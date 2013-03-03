/**
 *  IntentPaceWalls.java
 *  Created on Mar 3, 2013 6:37:21 AM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.rainfall.characters.ai.actions;

import net.wombatrpgs.rainfall.characters.CharacterEvent;
import net.wombatrpgs.rainfall.characters.ai.Intelligence;
import net.wombatrpgs.rainfall.characters.ai.IntentAct;
import net.wombatrpgs.rainfall.maps.layers.GridLayer;
import net.wombatrpgs.rainfallschema.maps.data.Direction;

/**
 * Walk left/right until walls hit.
 */
public class IntentPaceWalls extends IntentAct {
	
	protected static final int SENSOR_RANGE = 3;

	public IntentPaceWalls(Intelligence parent, CharacterEvent actor) {
		super(parent, actor);
	}

	/**
	 * @see net.wombatrpgs.rainfall.characters.ai.IntentAct#act()
	 */
	@Override
	public void act() {
		if (actor.getVX() == 0) actor.targetDirection(Direction.RIGHT);
		int nudge;
		if (actor.getVX() > 0) nudge = SENSOR_RANGE + actor.getHitbox().getWidth();
		else nudge = -SENSOR_RANGE;
		int x = actor.getHitbox().getX() + nudge;
		int y = actor.getHitbox().getY() + actor.getHitbox().getHeight()/2;
		int tileX = (int) Math.floor(x / actor.getLevel().getTileWidth());
		int tileY = (int) Math.floor(y / actor.getLevel().getTileHeight());
		for (GridLayer layer : actor.getLevel().getGridLayers()) {
			if (Math.floor(layer.getZ()) == Math.floor(actor.getLevel().getZ(actor))) {
				if (!layer.isPassable(tileX, tileY)) {
					if (actor.getVX() > 0) actor.targetDirection(Direction.LEFT);
					if (actor.getVX() < 0) actor.targetDirection(Direction.RIGHT);
				}
			}
		}
		
	}

}
