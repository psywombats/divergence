/**
 *  CommandCameraPan.java
 *  Created on Feb 5, 2013 8:38:33 PM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.rainfall.scenes.commands;

import net.wombatrpgs.rainfall.core.RGlobal;
import net.wombatrpgs.rainfall.maps.Positionable;
import net.wombatrpgs.rainfall.maps.events.MapEvent;
import net.wombatrpgs.rainfall.scenes.SceneCommand;
import net.wombatrpgs.rainfall.scenes.SceneParser;
import net.wombatrpgs.rainfall.screen.TrackerCam;

/**
 * Moves that mysterious camera.
 */
public class CommandCameraPan extends SceneCommand {
	
	protected MapEvent target;
	protected CommandMove subCommand;
	protected Positionable oldTarget;
	protected boolean runOnce;

	/**
	 * Creates a new pan from code.
	 * @param 	parent			Our executor parent
	 * @param 	line			The line that inspired this pan
	 */
	public CommandCameraPan(SceneParser parent, String line) {
		super(parent, line);
		runOnce = false;
		target = new MapEvent() {
			@Override
			protected void zeroCoords() {
				super.zeroCoords();
				this.acceleration = 10000; // big
				this.decceleration = 10000; // big;
			}
			@Override public boolean isCollisionEnabled() { return false; }
		};
		subCommand = new CommandMove(getParent(), 
				line.substring(line.indexOf(' ') + 1),
				target);
	}

	/**
	 * @see net.wombatrpgs.rainfall.scenes.SceneCommand#run()
	 */
	@Override
	public boolean run() {
		if (!runOnce) {
			TrackerCam cam = RGlobal.screens.getCamera();
			oldTarget = cam.getTarget();
			target.setX((int) cam.position.x);
			target.setY((int) cam.position.y);
			target.setMaxVelocity(cam.getPanSpeed());
			cam.track(target);
			parent.getLevel().addEvent(target, 0);
			runOnce = true;
		}
		if (subCommand.isFinished() && !finished) {
			TrackerCam cam = RGlobal.screens.getCamera();
			cam.track(oldTarget);
			parent.getLevel().removeEvent(target);
			finished = true;
		}
		return subCommand.run();
	}

	/**
	 * @see net.wombatrpgs.rainfall.scenes.SceneCommand#reset()
	 */
	@Override
	public void reset() {
		super.reset();
		runOnce = false;
	}

}
