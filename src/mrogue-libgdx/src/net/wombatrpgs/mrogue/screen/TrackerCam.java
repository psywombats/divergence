/**
 *  TrackerCam.java
 *  Created on Feb 5, 2013 7:19:24 PM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mrogue.screen;

import net.wombatrpgs.mrogue.core.MGlobal;
import net.wombatrpgs.mrogue.core.Updateable;
import net.wombatrpgs.mrogue.maps.Level;
import net.wombatrpgs.mrogue.maps.Positionable;

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
	protected Level constrainedMap;
	protected float speed; // in px/s

	/**
	 * Same as the superclass constructor.
	 * @param 	viewportWidth	The width of the viewport for this cam
	 * @param 	viewportHeight	The height of the viewport for this cam
	 */
	public TrackerCam(float viewportWidth, float viewportHeight) {
		super(viewportWidth, viewportHeight);
		setToOrtho(false, 
				viewportWidth,
				viewportHeight);
		zoom = MGlobal.window.getZoom();
	}
	
	/**
	 * @see net.wombatrpgs.mrogue.core.Updateable#update(float)
	 */
	@Override
	public void update(float elapsed) {
		if (target != null) {
			position.x = Math.round(target.getX()/zoom)*zoom;// * ratioX;
			position.y = Math.round(target.getY()/zoom)*zoom;// * ratioY;
			if (constrainedMap != null) {
				int halfWidth = MGlobal.window.getViewportWidth() / 2;
				int halfHeight = MGlobal.window.getViewportHeight() / 2;
				boolean tooLeft = position.x < halfWidth;
				boolean tooRight = position.x > constrainedMap.getWidthPixels() - halfWidth;
				boolean tooUp = position.y < halfHeight;
				boolean tooDown = position.y > constrainedMap.getHeightPixels() - halfHeight;
				if (tooLeft) {
					position.x = halfWidth;
				} else if (tooRight) {
					position.x = constrainedMap.getWidthPixels() - halfWidth;
				}
				if (tooUp) {
					position.y = halfHeight;
				} else if (tooDown) {
					position.y = constrainedMap.getHeightPixels() - halfHeight;
				}
				tooLeft = position.x < halfWidth;
				tooRight = position.x > constrainedMap.getWidthPixels() - halfWidth;
				tooUp = position.y < halfHeight;
				tooDown = position.y > constrainedMap.getHeightPixels() - halfHeight;
				if (tooLeft || tooRight) {
					position.x = constrainedMap.getWidthPixels() / 2;
				} else if (tooUp || tooDown) {
					position.y = constrainedMap.getHeightPixels() / 2;
				}
			}
		}
		super.update();
	}
	
	/** @param speed The speed at which this camera pans to its destination */
	public void setPanSpeed(float speed) { this.speed = speed; }
	
	/** @return The speed at which this camera pans to its destination */
	public float getPanSpeed() { return this.speed; }
	
	/** @return What the camera is currently tracking */
	public Positionable getTarget() { return this.target; }

	/**
	 * Sets the camera to follow positionable object. Setting to null locks.
	 * @param 	target			The location to follow, or null for lock
	 */
	public void track(Positionable target) {
		this.target = target;
	}
	
	/**
	 * Sets the camera to restrain itself to the bounds of a map. Can also pass
	 * in null to clear the map.
	 * @param 	map				The map to contrain the camera to, or null
	 */
	public void constrainMaps(Level map) {
		this.constrainedMap = map;
	}

}
