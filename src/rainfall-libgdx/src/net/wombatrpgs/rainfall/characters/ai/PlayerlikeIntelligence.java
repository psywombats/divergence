/**
 *  PlayerlikeIntelligence.java
 *  Created on Jun 22, 2013 12:36:22 PM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.rainfall.characters.ai;

import net.wombatrpgs.rainfall.characters.enemies.EnemyEvent;
import net.wombatrpgs.rainfall.characters.moveset.MoveType;
import net.wombatrpgs.rainfall.characters.moveset.MovesetAct;
import net.wombatrpgs.rainfall.core.RGlobal;
import net.wombatrpgs.rainfallschema.characters.enemies.ai.PlayerlikeIntelligenceMDO;
import net.wombatrpgs.rainfallschema.maps.data.Direction;

/**
 * Intelligence for ABS-participants. Moves them like a human would, to some
 * extent. It's an alternative to the doofus Blockbound enemies.
 */
public class PlayerlikeIntelligence extends Intelligence {
	
	protected PlayerlikeIntelligenceMDO mdo;

	/**
	 * Inherited constructor.
	 * @param	actor			The enemy that will be controlled
	 */
	public PlayerlikeIntelligence(PlayerlikeIntelligenceMDO mdo, EnemyEvent actor) {
		super(mdo, actor);
		this.mdo = mdo;
	}

	/**
	 * @see net.wombatrpgs.rainfall.characters.ai.Intelligence#act()
	 */
	@Override
	public void act() {
		if (actor.distanceTo(RGlobal.hero) < mdo.nearRange) {
			retreat();
			actor.faceToward(RGlobal.hero);
			return;
		}
		if (maybeAttack()) {
			return;
		}
		if (actor.distanceTo(RGlobal.hero) > mdo.visionRadius) {
			idle();
			return;
		}
		if (actor.distanceTo(RGlobal.hero) > mdo.farRange) {
			approach();
			actor.faceToward(RGlobal.hero);
			return;
		}
		actor.halt();
	}
	
	/**
	 * What to do when you're not in sight range.
	 */
	private void idle() {
		return;
	}
	
	/**
	 * Move away from the hero, however we know how.
	 */
	private void retreat() {
		MovesetAct escapeAct = null;
		for (MovesetAct move : actor.getMoves()) {
			if (move.getType() == MoveType.MOVEMENT) {
				escapeAct = move;
			}
		}
		if (escapeAct == null) {
			float dx = actor.getX() - RGlobal.hero.getX();
			float dy = actor.getY() - RGlobal.hero.getY();
			actor.targetLocation(actor.getX() + dx, actor.getY() + dy);
		} else {
			actor.faceAway(RGlobal.hero);
			escapeAct.act(actor.getLevel(), actor);
		}
	}
	
	/**
	 * Move towards the hero, however we know how.
	 */
	private void approach() {
		actor.targetLocation(RGlobal.hero.getX(), RGlobal.hero.getY());
	}
	
	/**
	 * Attack based on a heuristic related to aggression and range.
	 * @return					True if an attack fired
	 */
	private boolean maybeAttack() {
		float dist = actor.distanceTo(RGlobal.hero);
		Direction dir = actor.directionTo(RGlobal.hero);
		for (MovesetAct attack : actor.getMoves()) {
			int range;
			if (dir == Direction.LEFT || dir == Direction.RIGHT) {
				range = attack.getRangeX();
			} else {
				range = attack.getRangeY();
			}
			if (range > dist && mdo.aggression > RGlobal.rand.nextInt(20)) {
				actor.faceToward(RGlobal.hero);
				attack.act(actor.getLevel(), actor);
				return true;
			}
		}
		return false;
	}

}
