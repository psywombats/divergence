/**
 *  Block.java
 *  Created on Dec 29, 2012 1:13:08 AM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.rainfall.characters;

import net.wombatrpgs.rainfall.core.RGlobal;
import net.wombatrpgs.rainfall.graphics.DirAnim;
import net.wombatrpgs.rainfall.maps.Level;
import net.wombatrpgs.rainfall.maps.events.CharacterEvent;
import net.wombatrpgs.rainfallschema.hero.moveset.SummonMDO;
import net.wombatrpgs.rainfallschema.maps.CharacterEventMDO;

/**
 * That big blocky thing that falls from the sky.
 */
public class Block extends CharacterEvent {
	
	protected SummonMDO mdo;
	protected DirAnim anim;
	
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
	}
	
	/**
	 * Changes the map the block is currently located on. Call every time the
	 * block is summoned.
	 * @param	map				The new level for the block to be on
	 */
	public void changeMap(Level map) {
		if (this.parent != null) {
			parent.teleportOff();
		}
		map.teleportOn(this, getX(), getY());
	}

}
