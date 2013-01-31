/**
 *  Enemy.java
 *  Created on Jan 23, 2013 9:14:38 PM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.rainfall.characters.enemies;

import net.wombatrpgs.rainfall.characters.CharacterEvent;
import net.wombatrpgs.rainfall.characters.ai.Intelligence;
import net.wombatrpgs.rainfall.collisions.CollisionResult;
import net.wombatrpgs.rainfall.core.RGlobal;
import net.wombatrpgs.rainfall.maps.Level;
import net.wombatrpgs.rainfall.maps.events.MapEvent;
import net.wombatrpgs.rainfallschema.characters.enemies.EnemyEventMDO;
import net.wombatrpgs.rainfallschema.characters.enemies.VulnerabilityMDO;
import net.wombatrpgs.rainfallschema.characters.enemies.ai.IntelligenceMDO;

/**
 * The one and only class for those pesky badniks that hunt down the valiant
 * hero and hinder his quest to save the earth.
 */
public class Enemy extends CharacterEvent {
	
	protected EnemyEventMDO mdo;
	protected Intelligence ai;
	protected Vulnerability vuln;
	
	/**
	 * Creates a new enemy on a map from a database entry.
	 * @param 	mdo				The MDO with data to create from
	 * @param 	parent			The parent map of the object
	 * @param	x				The initial x-coord (in tiles)
	 * @param	y				The intitila y-coord (in tiles)
	 */
	public Enemy(EnemyEventMDO mdo, Level parent, float x, float y) {
		super(mdo, parent, x, y);
		this.mdo = mdo;
		IntelligenceMDO aiMDO = RGlobal.data.getEntryFor(
				mdo.intelligence, IntelligenceMDO.class);
		ai = new Intelligence(aiMDO, this);
		VulnerabilityMDO vulnMDO = RGlobal.data.getEntryFor(
				mdo.vulnerability, VulnerabilityMDO.class);
		vuln = new Vulnerability(vulnMDO);
	}

	/**
	 * @see net.wombatrpgs.rainfall.characters.CharacterEvent#update(float)
	 */
	@Override
	public void update(float elapsed) {
		ai.act();
		super.update(elapsed);
	}

	/**
	 * @see net.wombatrpgs.rainfall.characters.CharacterEvent#onCharacterCollide
	 * (net.wombatrpgs.rainfall.characters.CharacterEvent,
	 * net.wombatrpgs.rainfall.collisions.CollisionResult)
	 */
	@Override
	public boolean onCharacterCollide(CharacterEvent other, CollisionResult result) {
		if (other == RGlobal.block) {
			if (RGlobal.block.isMoving()) {
				if (vuln.killableByPush()) {
					selfDestruct(RGlobal.block);
					return true;
				} else {
					// check if we've been pinned
				}
			} else {
				// maybe the block has been summoned on top of us?
				if (RGlobal.block.isSummoning()) {
					if (vuln.killableBySummon()) {
						selfDestruct(RGlobal.block);
					}
				}
			}
		}
		return super.onCharacterCollide(other, result); // ie false
	}
	
	/**
	 * @see net.wombatrpgs.rainfall.maps.events.MapEvent#supportsBlockLanding()
	 */
	@Override
	public boolean supportsBlockLanding() {
		return vuln.killableBySummon();
	}

	/**
	 * Kills self in a spectacular manner. Another object is supplied so that
	 * gibs can scatter correctly. If this enemy just imploded randomly, then
	 * pass in itself and the distribution will be random.
	 * @param 	cause			The event that caused this enemy's death
	 */
	public void selfDestruct(MapEvent cause) {
		parent.removeEvent(this);
	}
	
}
