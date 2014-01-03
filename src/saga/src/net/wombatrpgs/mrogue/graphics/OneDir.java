/**
 *  OneDir.java
 *  Created on Mar 2, 2013 5:49:45 AM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mrogue.graphics;

import net.wombatrpgs.mrogue.maps.events.MapEvent;
import net.wombatrpgs.mrogueschema.graphics.AnimationMDO;

/**
 * snickersnickersnicker A very thin wrapper for an animation strip. Looks like
 * there's so much crap in FacesAnimation that AnimationStrip can't extend this
 * directly.
 */
public class OneDir extends FacesAnimation {
	
	protected AnimationMDO mdo;

	/**
	 * Goes through the normal rigamarole of settings up an animation.
	 * @param 	mdo				The animation strip this thing's holding
	 * @param 	parent			The parent map
	 */
	public OneDir(AnimationMDO mdo, MapEvent parent) {
		super(parent, 1);
		this.mdo = mdo;
		sliceAnimations();
	}

	/**
	 * Always render the down dir.
	 * @see net.wombatrpgs.mrogue.graphics.FacesAnimation#currentDirOrdinal()
	 */
	@Override
	protected int currentDirOrdinal() {
		return 0;
	}

	/**
	 * hehe
	 * @see net.wombatrpgs.mrogue.graphics.FacesAnimation#sliceAnimations()
	 */
	@Override
	protected void sliceAnimations() {
		animations[0] = new AnimationStrip(mdo, parent);
	}

}
