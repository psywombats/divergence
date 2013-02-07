/**
 *  FourDir.java
 *  Created on Nov 12, 2012 11:18:45 AM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.rainfall.graphics;

import net.wombatrpgs.rainfall.core.RGlobal;
import net.wombatrpgs.rainfall.maps.events.MapEvent;
import net.wombatrpgs.rainfallschema.graphics.AnimationMDO;
import net.wombatrpgs.rainfallschema.graphics.FourDirMDO;
import net.wombatrpgs.rainfallschema.maps.data.Direction;

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
		super(mdo, parent, Direction.values().length);
		this.mdo = mdo;
		sliceAnimations();
	}

	/**
	 * @see net.wombatrpgs.rainfall.graphics.FacesAnimation#currentDirOrdinal()
	 */
	@Override
	protected int currentDirOrdinal() {
		return currentDir.ordinal();
	}

	/**
	 * @see net.wombatrpgs.rainfall.graphics.FacesAnimation#sliceAnimations()
	 */
	@Override
	protected void sliceAnimations() {
		animations[Direction.DOWN.ordinal()] = new AnimationStrip(
				RGlobal.data.getEntryFor(mdo.downAnim, AnimationMDO.class), parent);
		animations[Direction.UP.ordinal()] = new AnimationStrip(
				RGlobal.data.getEntryFor(mdo.upAnim, AnimationMDO.class), parent);
		animations[Direction.LEFT.ordinal()] = new AnimationStrip(
				RGlobal.data.getEntryFor(mdo.leftAnim, AnimationMDO.class), parent);
		animations[Direction.RIGHT.ordinal()] = new AnimationStrip(
				RGlobal.data.getEntryFor(mdo.rightAnim, AnimationMDO.class), parent);
	}
	
}
