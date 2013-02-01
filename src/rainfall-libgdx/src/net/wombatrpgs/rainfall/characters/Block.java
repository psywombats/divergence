/**
 *  Block.java
 *  Created on Dec 29, 2012 1:13:08 AM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.rainfall.characters;

import net.wombatrpgs.rainfall.collisions.CollisionResult;
import net.wombatrpgs.rainfall.core.RGlobal;
import net.wombatrpgs.rainfall.graphics.AnimationStrip;
import net.wombatrpgs.rainfall.maps.Level;
import net.wombatrpgs.rainfall.maps.events.MapEvent;
import net.wombatrpgs.rainfallschema.characters.CharacterEventMDO;
import net.wombatrpgs.rainfallschema.characters.hero.moveset.SummonMDO;

/**
 * That big blocky thing that falls from the sky.
 */
public class Block extends CharacterEvent {
	
	protected SummonMDO mdo;
	protected AnimationStrip anim;
	protected boolean summonInProgress;
	
	/**
	 * Creates the block. Must be updated with the map each time the hero
	 * makes a transition.
	 * @param	mdo			The MDO that spawned this abomination
	 */
	public Block(SummonMDO mdo) {
		super(RGlobal.data.getEntryFor(mdo.blockEvent, CharacterEventMDO.class));
		// TODO: move this somewhere sane
		appearance.queueRequiredAssets(RGlobal.assetManager);
		RGlobal.assetManager.finishLoading();
		appearance.postProcessing(RGlobal.assetManager);
		setCollisionsEnabled(true);
		summonInProgress = false;
	}
	
	/** @param summoning True if summoning is in progress */
	public void setSummoning(boolean summoning) { this.summonInProgress = summoning; }
	
	/** @return True if summoning is in progress */
	public boolean isSummoning() { return this.summonInProgress; }
	
	/**
	 * Changes the map the block is currently located on. Call every time the
	 * block is summoned.
	 * @param	map				The new level for the block to be on
	 */
	public void changeMap(Level map) {
		if (this.parent != null) {
			parent.teleportOff();
		}
		map.addEvent(this, getX(), getY());
	}

	/**
	 * @see net.wombatrpgs.rainfall.characters.CharacterEvent#onCollide
	 * (net.wombatrpgs.rainfall.maps.MapEvent, net.wombatrpgs.rainfall.collisions.CollisionResult)
	 */
	@Override
	public boolean onCollide(MapEvent other, CollisionResult result) {
		if (other == RGlobal.hero) {
			return true;
		} else {
			return super.onCollide(other, result);
		}
	}
	
}
