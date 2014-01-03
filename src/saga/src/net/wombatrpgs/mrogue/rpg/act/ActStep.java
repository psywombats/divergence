/**
 *  ActMove.java
 *  Created on Oct 5, 2013 10:00:48 PM for project mrogue-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mrogue.rpg.act;

import net.wombatrpgs.mrogue.maps.events.MapEvent;
import net.wombatrpgs.mrogue.rpg.CharacterEvent;
import net.wombatrpgs.mrogueschema.maps.data.EightDir;

/**
 * Step in the appropriate direction, attacking whatever's there if necessary.
 * It's fine to create one of these and then use it for all directions.
 */
public class ActStep extends Action {
	
	protected EightDir dir;
	
	/**
	 * Creates a new step action with no direction. Direction must be set before
	 * using this act.
	 * @param	actor			The thing that will be moving
	 */
	public ActStep(CharacterEvent actor) {
		super(actor);
	}

	/**
	 * Creates a new step action in the specified direction.
	 * @param	actor			The chara that will be stepping
	 * @param	dir				The direction to step in
	 */
	public ActStep(CharacterEvent actor, EightDir dir) {
		this(actor);
		this.dir = dir;
	}
	
	/** @param dir The new direction to step in */
	public void setDirection(EightDir dir) { this.dir = dir; }

	/**
	 * @see net.wombatrpgs.mrogue.rpg.act.Action#act()
	 */
	@Override
	public void act() {
		int targetX = (int) (actor.getTileX() + dir.getVector().x);
		int targetY = (int) (actor.getTileY() + dir.getVector().y);
		actor.attemptStep(targetX, targetY);
	}

	/**
	 * @see net.wombatrpgs.mrogue.rpg.act.Action#baseCost()
	 */
	@Override
	protected int baseCost() {
		int targetX = (int) (actor.getX() + dir.getVector().x);
		int targetY = (int) (actor.getY() + dir.getVector().y);
		for (MapEvent event : (actor.getParent().getEventsAt(targetX, targetY))) {
			if (!event.isPassable()) {
				return actor.getStats().attackEP;
			}
		}
		return actor.getStats().walkEP;
	}

}
