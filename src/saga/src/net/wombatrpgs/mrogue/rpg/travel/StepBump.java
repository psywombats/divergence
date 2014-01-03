/**
 *  BumpStep.java
 *  Created on Oct 5, 2013 4:52:10 AM for project mrogue-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mrogue.rpg.travel;

import net.wombatrpgs.mrogue.core.MGlobal;
import net.wombatrpgs.mrogue.rpg.CharacterEvent;
import net.wombatrpgs.mrogueschema.maps.data.EightDir;

/**
 * Bump into a wall or into a hostile enemy!!
 */
public class StepBump extends Step {
	
	/** Pixels we jut out for bumping */
	protected static final int BUMP_SIZE = 8;
	
	protected EightDir dir;
	protected float startX, startY;
	boolean started, halfway;

	/**
	 * Creates a new bumpstep in a particular direction for the actor.
	 * @param	actor			The dude doing the bumping
	 * @param	dir				The direction of the bump
	 */
	public StepBump(CharacterEvent actor, EightDir dir) {
		super(actor);
		this.dir = dir;
		started = false;
		halfway = false;
	}

	/**
	 * @see net.wombatrpgs.mrogue.rpg.travel.Step#update(float)
	 */
	@Override
	public void update(float elapsed) {
		super.update(elapsed);
		if (!started) {
			if (actor == MGlobal.hero) {
				MGlobal.screens.peek().getCamera().track(null);
			}
			started = true;
			startX = actor.getX();
			startY = actor.getY();
			actor.setFacing(dir.toOrtho(actor.getFacing()));
			float dx = dir.getVector().x * BUMP_SIZE;
			float dy = dir.getVector().y * BUMP_SIZE;
			float t = (allotted/2.f) - totalElapsed;
			actor.setVelocity(dx / t, dy / t);
			actor.targetLocation(actor.getX() + dx, actor.getY() + dy);
		}
		if (totalElapsed > allotted / 2.f && !halfway) {
			halfway = true;
			float dx = startX - actor.getX();
			float dy = startY - actor.getY();
			float t = (allotted) - totalElapsed;
			actor.setVelocity(dx / t, dy / t);
			actor.targetLocation(startX, startY);
		}
	}

	/**
	 * @see net.wombatrpgs.mrogue.rpg.travel.Step#onEnd()
	 */
	@Override
	public void onEnd() {
		super.onEnd();
		actor.setX(startX);
		actor.setY(startY);
		if (actor == MGlobal.hero) {
			MGlobal.screens.peek().getCamera().track(MGlobal.hero);
		}
	}

}
