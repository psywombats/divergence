/**
 *  TrackerCam.java
 *  Created on Feb 5, 2013 7:19:24 PM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mgne.screen;

import net.wombatrpgs.mgne.core.MGlobal;
import net.wombatrpgs.mgne.core.interfaces.FinishListener;
import net.wombatrpgs.mgne.core.interfaces.Updateable;
import net.wombatrpgs.mgne.maps.Level;
import net.wombatrpgs.mgne.maps.Positionable;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Rectangle;

/**
 * A cool enhanced orthographic camera. It supports a few additional operations,
 * mainly tracking related, but also anything else the engine happens to need.
 */
public class TrackerCam extends OrthographicCamera implements Updateable {
	
	protected static final int DEFAULT_PAN_SPEED = 32; // px/s, should be somewhere else
	
	protected Rectangle glViewport;
	protected Positionable target, panTarget;
	protected FinishListener onPanFinish;
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
		speed = 1200;
	}
	
	/**
	 * @see net.wombatrpgs.mgne.core.interfaces.Updateable#update(float)
	 */
	@Override
	public void update(float elapsed) {
		if (target != null) {
			position.x = Math.round(target.getX()/zoom)*zoom;// * ratioX;
			position.y = Math.round(target.getY()/zoom)*zoom;// * ratioY;
			if (constrainedMap != null) {
				int halfWidth = MGlobal.window.getWidth() / 2;
				int halfHeight = MGlobal.window.getHeight() / 2;
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
		} else if (panTarget != null) {
			float dx = panTarget.getX() - position.x;
			float dy = panTarget.getY() - position.y;
			float a = (float) Math.atan2(dy, dx);
			float d = speed * elapsed;
			if (Math.sqrt(dx*dx + dy*dy) < d) {
				// we're there, or close enough
				position.x = panTarget.getX();
				position.y = panTarget.getY();
				target = panTarget;
				panTarget = null;
				if (onPanFinish != null) {
					onPanFinish.onFinish();
				}
			} else {
				position.x += d * Math.cos(a);
				position.y += d * Math.sin(a);
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
	 * This does not pan or anything like that.
	 * @param 	target			The location to follow, or null for lock
	 */
	public void track(Positionable target) {
		this.target = target;
	}
	
	/**
	 * Pans to some target and then when it gets there, locks on it. Pass in a
	 * listener if you want.
	 * @param	target			What we should pan to
	 * @param	onFinish		A function to call when we get there, or null
	 */
	public void panTo(Positionable target, FinishListener onFinish) {
		if (panTarget != null) {
			MGlobal.reporter.warn("Tried to pan to " + target + " but already "
					+ "panning to " + panTarget);
		}
		if (target == panTarget) {
			onFinish.onFinish();
		} else {
			this.target = null;
			this.panTarget = target;
			this.onPanFinish = onFinish;
		}
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
