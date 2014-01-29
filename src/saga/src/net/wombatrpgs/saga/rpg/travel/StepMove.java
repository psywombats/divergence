/**
 *  MoveStep.java
 *  Created on Oct 5, 2013 4:15:38 AM for project MRogueSchema
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.saga.rpg.travel;

import net.wombatrpgs.saga.maps.events.MapEvent;

/**
 * The step for travel.
 */
public class StepMove extends Step {
	
	protected int tileX, tileY;
	protected float dirX, dirY;
	protected float targetX, targetY;

	/**
	 * Creates a new move step for the specified actor to a specified location.
	 * @param	actor			The thing that will be moving
	 * @param	tileX			The x-coord of the tile to move to (in tiles)
	 * @param	tileY			The y-coord of the tile to move to (in tiles)
	 */
	public StepMove(MapEvent actor, int tileX, int tileY) {
		super(actor);
		this.tileX = tileX;
		this.tileY = tileY;
		
		targetX = tileX * actor.getParent().getTileWidth();
		targetY = tileY * actor.getParent().getTileHeight();
		dirX = targetX - actor.getX();
		dirY = targetY - actor.getY();
	}

	/**
	 * @see net.wombatrpgs.saga.rpg.travel.Step#onStart()
	 */
	@Override
	public void onStart() {
		super.onStart();
		float dx = targetX - actor.getX();
		float dy = targetY - actor.getY();
		float t = allotted;
		float vx = dx / t;
		float vy = dy / t;
		if (Math.abs(vx-actor.getVX()) > 1 || Math.abs(vy-actor.getVY()) > 1) {
			actor.setVelocity(vx, vy);
		}
	}

	/**
	 * @see net.wombatrpgs.saga.rpg.travel.Step#onEnd()
	 */
	@Override
	public void onEnd() {
		super.onEnd();
		actor.setX(targetX);
		actor.setY(targetY);
		actor.halt();
	}

}
