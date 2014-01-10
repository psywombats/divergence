/**
 *  CommandCameraPan.java
 *  Created on Feb 5, 2013 8:38:33 PM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.saga.scenes.commands;

import net.wombatrpgs.saga.core.SGlobal;
import net.wombatrpgs.saga.maps.Positionable;
import net.wombatrpgs.saga.maps.events.MapEvent;
import net.wombatrpgs.saga.scenes.SceneCommand;
import net.wombatrpgs.saga.scenes.SceneParser;
import net.wombatrpgs.saga.screen.TrackerCam;

/**
 * Moves that mysterious camera. As of 2013-03-07 does not reset the camera.
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
			}
		};
		subCommand = new CommandMove(getParent(), 
				line.substring(line.indexOf(' ') + 1),
				target);
	}

	/**
	 * @see net.wombatrpgs.saga.scenes.SceneCommand#run()
	 */
	@Override
	public boolean run() {
		if (!runOnce) {
			TrackerCam cam = SGlobal.screens.getCamera();
			oldTarget = cam.getTarget();
			target.setX((int) cam.position.x);
			target.setY((int) cam.position.y);
			cam.track(target);
			parent.getLevel().addEvent(target);
			runOnce = true;
		}
		if (subCommand.isFinished() && !finished) {
//			TrackerCam cam = RGlobal.screens.getCamera();
//			cam.track(oldTarget);
			parent.getLevel().removeEvent(target);
			finished = true;
		}
		return subCommand.run();
	}

	/**
	 * @see net.wombatrpgs.saga.scenes.SceneCommand#reset()
	 */
	@Override
	public void reset() {
		super.reset();
		runOnce = false;
	}

}
