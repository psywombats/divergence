/**
 *  TargetPosition.java
 *  Created on Jan 10, 2013 5:29:36 PM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.rainfall.collisions;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import net.wombatrpgs.rainfall.core.RGlobal;
import net.wombatrpgs.rainfall.maps.Positionable;

/**
 * A Positionable that can have its coords changed around easily enough.
 */
public class TargetPosition implements Positionable {
	
	protected int x;
	protected int y;
	
	/**
	 * Creates a new target position centered at the provided coords.
	 * @param 	x			The initial x-coord of the position
	 * @param 	y			The initial y-coord of the position
	 */
	public TargetPosition(int x, int y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * @see net.wombatrpgs.rainfall.maps.Positionable#getX()
	 */
	@Override
	public int getX() {
		return x;
	}

	/**
	 * @see net.wombatrpgs.rainfall.maps.Positionable#getY()
	 */
	@Override
	public int getY() {
		return y;
	}
	
	/** @param x The new x-coord of this target */
	public void setX(int x) { this.x = x; }
	
	/** @param x The new x-coord of this target */
	public void setY(int y) { this.y = y; }

	/**
	 * @see net.wombatrpgs.rainfall.maps.Positionable#getBatch()
	 */
	@Override
	public SpriteBatch getBatch() {
		RGlobal.reporter.warn("Getting the batch of a target position");
		return null;
	}

}
