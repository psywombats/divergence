/**
 *  MoveStep.java
 *  Created on Oct 5, 2013 4:15:38 AM for project MRogueSchema
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mrogue.rpg.travel;

import net.wombatrpgs.mrogue.rpg.CharacterEvent;

/**
 * The step for travel.
 */
public class StepMove extends Step {
	
	protected int tileX, tileY;

	/**
	 * Creates a new move step for the specified actor to a specified location.
	 * @param	actor			The thing that will be moving
	 * @param	tileX			The x-coord of the tile to move to (in tiles)
	 * @param	tileY			The y-coord of the tile to move to (in tiles)
	 */
	public StepMove(CharacterEvent actor, int tileX, int tileY) {
		super(actor);
		this.tileX = tileX;
		this.tileY = tileY;
	}

	/**
	 * @see net.wombatrpgs.mrogue.rpg.travel.Step#update(float)
	 */
	@Override
	public void update(float elapsed) {
		super.update(elapsed);
		if (!actor.isMoving()) {
			float targetX = tileX * actor.getParent().getTileWidth();
			float targetY = tileY * actor.getParent().getTileHeight();
			if (actor.getX() != targetX || actor.getY() != targetY) {
				float dx = targetX - actor.getX();
				float dy = targetY - actor.getY();
				float t = allotted - totalElapsed;
				actor.setVelocity(dx / t, dy / t);
				actor.targetLocation(targetX, targetY);
			}
		}
	}

	/**
	 * @see net.wombatrpgs.mrogue.rpg.travel.Step#onEnd()
	 */
	@Override
	public void onEnd() {
		super.onEnd();
		float targetX = tileX * actor.getParent().getTileWidth();
		float targetY = tileY * actor.getParent().getTileHeight();
		actor.setX(targetX);
		actor.setY(targetY);
		actor.halt();
	}

}
