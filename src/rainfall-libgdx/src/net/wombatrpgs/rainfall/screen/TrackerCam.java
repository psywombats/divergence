/**
 *  TrackerCam.java
 *  Created on Feb 5, 2013 7:19:24 PM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.rainfall.screen;

import net.wombatrpgs.rainfall.core.RGlobal;
import net.wombatrpgs.rainfall.core.Updateable;
import net.wombatrpgs.rainfall.maps.Positionable;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Rectangle;

/**
 * A cool enhanced orthographic camera. It supports a few additional operations,
 * mainly tracking related, but also anything else the engine happens to need.
 */
public class TrackerCam extends OrthographicCamera implements Updateable {
	
	protected static final int DEFAULT_PAN_SPEED = 48; // px/s, should be somewhere else
	
	protected Rectangle glViewport;
	protected Positionable target;
	protected float speed; // in px/s

	/**
	 * Same as the superclass constructor.
	 * @param 	viewportWidth	The width of the viewport for this cam
	 * @param 	viewportHeight	The height of the viewport for this cam
	 */
	public TrackerCam(float viewportWidth, float viewportHeight) {
		super(viewportWidth, viewportHeight);
		setToOrtho(false, 
				RGlobal.window.width,
				RGlobal.window.height);
		this.zoom = 1;
	}
	
	/**
	 * @see net.wombatrpgs.rainfall.core.Updateable#update(float)
	 */
	@Override
	public void update(float elapsed) {
		float ratioX = RGlobal.window.resWidth / RGlobal.window.width;
		float ratioY = RGlobal.window.resHeight / RGlobal.window.height;
		if (target != null) {
			position.x = Math.round(target.getX()/ratioX)*ratioX;// * ratioX;
			position.y = Math.round(target.getY()/ratioY)*ratioY;// * ratioY;
		}
	}
	
	/** @param speed The speed at which this camera pans to its destination */
	public void setPanSpeed(float speed) { this.speed = speed; }
	
	/** @return The speed at which this camera pans to its destination */
	public float getPanSpeed() { return this.speed; }
	
	/** @return What the camera is currently tracking */
	public Positionable getTarget() { return this.target; }

	/**
	 * Sets the camera to follow positionable object.
	 * @param 	target			The location to follow
	 */
	public void track(Positionable target) {
		this.target = target;
	}

}
