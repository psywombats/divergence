/**
 *  TwoDir.java
 *  Created on Jan 24, 2013 8:28:44 PM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.rainfall.graphics;

import net.wombatrpgs.rainfall.collisions.Hitbox;
import net.wombatrpgs.rainfall.core.RGlobal;
import net.wombatrpgs.rainfall.maps.Direction;
import net.wombatrpgs.rainfall.maps.events.MapEvent;
import net.wombatrpgs.rainfallschema.graphics.AnimationMDO;
import net.wombatrpgs.rainfallschema.graphics.TwoDirMDO;

import com.badlogic.gdx.graphics.OrthographicCamera;

/**
 * The same thing as a FourDir, but with a left/right facing only.
 */
public class TwoDir extends FacesAnimation {
	
	protected TwoDirMDO mdo;
	protected int effectiveIndex;
	
	protected static final int LEFT_INDEX = 0;
	protected static final int RIGHT_INDEX = 1;

	/**
	 * Constructs and splices a 2dir
	 * @param 	mdo				The MDO with relevant data
	 * @param 	parent			The parent this 4dir is tied to
	 */
	public TwoDir(TwoDirMDO mdo, MapEvent parent) {
		super(mdo, parent, 2);
		this.mdo = mdo;
		sliceAnimations();
	}

	/**
	 * @see net.wombatrpgs.rainfall.graphics.FacesAnimation#setFacing
	 * (net.wombatrpgs.rainfall.maps.Direction)
	 */
	@Override
	public void setFacing(Direction dir) {
		super.setFacing(dir);
		if (dir == Direction.LEFT) {
			effectiveIndex = LEFT_INDEX;
		} else if (dir == Direction.RIGHT) {
			effectiveIndex = RIGHT_INDEX;
		}
	}

	/**
	 * @see net.wombatrpgs.rainfall.graphics.Renderable#render
	 * (com.badlogic.gdx.graphics.OrthographicCamera)
	 */
	@Override
	public void coreRender(OrthographicCamera camera) {
		animations[effectiveIndex].render(camera);
	}

	/**
	 * @see net.wombatrpgs.rainfall.graphics.FacesAnimation#getHitbox()
	 */
	@Override
	public Hitbox getHitbox() {
		return animations[effectiveIndex].getHitbox();
	}

	/**
	 * @see net.wombatrpgs.rainfall.graphics.FacesAnimation#sliceAnimations()
	 */
	@Override
	protected void sliceAnimations() {
		animations[LEFT_INDEX] = new AnimationStrip(
				RGlobal.data.getEntryFor(mdo.leftAnim, AnimationMDO.class), parent);
		animations[RIGHT_INDEX] = new AnimationStrip(
				RGlobal.data.getEntryFor(mdo.rightAnim, AnimationMDO.class), parent);
	}

}
