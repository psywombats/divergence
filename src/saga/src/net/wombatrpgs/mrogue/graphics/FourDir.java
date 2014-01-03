/**
 *  FourDir.java
 *  Created on Nov 12, 2012 11:18:45 AM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mrogue.graphics;

import net.wombatrpgs.mrogue.core.MGlobal;
import net.wombatrpgs.mrogue.maps.events.MapEvent;
import net.wombatrpgs.mrogueschema.graphics.AnimationMDO;
import net.wombatrpgs.mrogueschema.graphics.FourDirMDO;
import net.wombatrpgs.mrogueschema.maps.data.OrthoDir;

/**
 * A holder for four different animations that make up a character's up, right,
 * left, and down.
 */
public class FourDir extends FacesAnimation {
	
	protected FourDirMDO mdo;
	
	/**
	 * Constructs and splices a 4dir
	 * @param 	mdo				The MDO with relevant data
	 * @param 	parent			The parent this 4dir is tied to
	 */
	public FourDir(FourDirMDO mdo, MapEvent parent) {
		super(parent, OrthoDir.values().length);
		this.mdo = mdo;
		sliceAnimations();
		
		// this is so that the character starts moving right away
		for (int i = 0; i < 4; i++) {
			animations[i].setBump(Math.max(0, (1.f / (float) animations[i].getFPS())) - .05f);
		}
	}

	/**
	 * @see net.wombatrpgs.mrogue.graphics.FacesAnimation#currentDirOrdinal()
	 */
	@Override
	protected int currentDirOrdinal() {
		return currentDir.ordinal();
	}

	/**
	 * @see net.wombatrpgs.mrogue.graphics.FacesAnimation#sliceAnimations()
	 */
	@Override
	protected void sliceAnimations() {
		animations[OrthoDir.SOUTH.ordinal()] = new AnimationStrip(
				MGlobal.data.getEntryFor(mdo.downAnim, AnimationMDO.class), parent);
		animations[OrthoDir.NORTH.ordinal()] = new AnimationStrip(
				MGlobal.data.getEntryFor(mdo.upAnim, AnimationMDO.class), parent);
		animations[OrthoDir.WEST.ordinal()] = new AnimationStrip(
				MGlobal.data.getEntryFor(mdo.leftAnim, AnimationMDO.class), parent);
		animations[OrthoDir.EAST.ordinal()] = new AnimationStrip(
				MGlobal.data.getEntryFor(mdo.rightAnim, AnimationMDO.class), parent);
	}
	
}
