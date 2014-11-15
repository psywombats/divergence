/**
 *  OneDir.java
 *  Created on Mar 2, 2013 5:49:45 AM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mgne.graphics;

import net.wombatrpgs.mgneschema.graphics.AnimationMDO;

/**
 * snickersnickersnicker A very thin wrapper for an animation strip. Looks like
 * there's so much crap in FacesAnimation that AnimationStrip can't extend this
 * directly.
 */
public class OneDir extends FacesAnimation {
	
	protected AnimationMDO mdo;

	/**
	 * Goes through the normal rigamarole of settings up an animation.
	 * @param 	mdo				The data to create from
	 */
	public OneDir(AnimationMDO mdo) {
		super(1);
		this.mdo = mdo;
		sliceAnimations();
	}

	/**
	 * Always render the down dir.
	 * @see net.wombatrpgs.mgne.graphics.FacesAnimation#currentDirOrdinal()
	 */
	@Override
	protected int currentDirOrdinal() {
		return 0;
	}

	/**
	 * hehe
	 * @see net.wombatrpgs.mgne.graphics.FacesAnimation#sliceAnimations()
	 */
	@Override
	protected void sliceAnimations() {
		animations[0] = new AnimationStrip(mdo);
	}

}
