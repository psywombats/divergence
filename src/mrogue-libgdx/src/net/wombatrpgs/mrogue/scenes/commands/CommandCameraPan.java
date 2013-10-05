/**
 *  CommandCameraPan.java
 *  Created on Feb 5, 2013 8:38:33 PM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mrogue.scenes.commands;

import net.wombatrpgs.mrogue.core.MGlobal;
import net.wombatrpgs.mrogue.maps.Positionable;
import net.wombatrpgs.mrogue.maps.events.MapEvent;
import net.wombatrpgs.mrogue.scenes.SceneCommand;
import net.wombatrpgs.mrogue.scenes.SceneParser;
import net.wombatrpgs.mrogue.screen.TrackerCam;

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
	 * @see net.wombatrpgs.mrogue.scenes.SceneCommand#run()
	 */
	@Override
	public boolean run() {
		if (!runOnce) {
			TrackerCam cam = MGlobal.screens.getCamera();
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
	 * @see net.wombatrpgs.mrogue.scenes.SceneCommand#reset()
	 */
	@Override
	public void reset() {
		super.reset();
		runOnce = false;
	}

}
