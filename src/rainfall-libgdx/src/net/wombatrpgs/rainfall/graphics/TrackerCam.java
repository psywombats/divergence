/**
 *  TrackerCam.java
 *  Created on Feb 5, 2013 7:19:24 PM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.rainfall.graphics;

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
	}
	
	/**
	 * @see net.wombatrpgs.rainfall.core.Updateable#update(float)
	 */
	@Override
	public void update(float elapsed) {
		if (target != null) {
			position.x = target.getX();
			position.y = target.getY();
			update();
		}
	}
	
	/** @param speed The speed at which this camera pans to its destination */
	public void setPanSpeed(float speed) { this.speed = speed; }
	
	/** @return The speed at which this camera pans to its destination */
	public float getPanSpeed() { return this.speed; }
	
	/** @return What the camera is currently tracking */
	public Positionable getTarget() { return this.target; }

	/**
	 * Invokes some OpenGL black magic to get the camera viewport set up. As of
	 * 2012-02-05 this was pretty much eliminated due to a switch to GL20.
	 */
	public void init() {
//		GL20 gl = Gdx.graphics.getGL20();
//		glViewport = new Rectangle(
//				0, 0, 
//				RGlobal.window.defaultWidth, RGlobal.window.defaultHeight);
//		gl.glViewport((int) glViewport.x, (int) glViewport.y,
//				(int) glViewport.width, (int) glViewport.height);
//		apply(gl);
		update();
	}

	/**
	 * Sets the camera to follow positionable object.
	 * @param 	target			The location to follow
	 */
	public void track(Positionable target) {
		this.target = target;
	}

}
