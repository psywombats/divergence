/**
 *  ActLunge.java
 *  Created on Sep 30, 2013 2:48:40 PM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.rainfall.characters.moveset;

import net.wombatrpgs.rainfall.characters.CharacterEvent;
import net.wombatrpgs.rainfall.maps.Level;
import net.wombatrpgs.rainfallschema.characters.hero.moveset.LungeMDO;
import net.wombatrpgs.rainfallschema.maps.data.Direction;

/**
 * Snakebite-like attacks.
 */
public class ActLunge extends ActAttack {
	
	protected LungeMDO mdo;

	/**
	 * Creates a lunge attack from data.
	 * @param 	actor			The actor performing the attack
	 * @param 	mdo				The data to create from
	 */
	public ActLunge(CharacterEvent actor, LungeMDO mdo) {
		super(actor, mdo);
		this.mdo = mdo;
	}

	/**
	 * @see net.wombatrpgs.rainfall.characters.moveset.ActAttack#coreAct
	 * (net.wombatrpgs.rainfall.maps.Level, net.wombatrpgs.rainfall.characters.CharacterEvent)
	 */
	@Override
	public void coreAct(Level map, CharacterEvent actor) {
		super.coreAct(map, actor);
		
		// kick
		Direction dir = actor.getFacing();
		float newVX = actor.getVX() + dir.getVector().x * mdo.instantVelocity;
		float newVY = actor.getVY() + dir.getVector().y * mdo.instantVelocity;
		actor.setVelocity(newVX, newVY);
	}

	/**
	 * @see net.wombatrpgs.rainfall.characters.moveset.MovesetAct#getRangeX()
	 */
	@Override
	public int getRangeX() {
		return super.getRangeX() * 2;
	}

	/**
	 * @see net.wombatrpgs.rainfall.characters.moveset.MovesetAct#getRangeY()
	 */
	@Override
	public int getRangeY() {
		return super.getRangeY() * 2;
	}

}
