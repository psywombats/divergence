/**
 *  DesktopFocusReporter.java
 *  Created on Nov 26, 2012 2:41:30 AM for project rainfall-libgdx-desktop
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mrogue;

import org.lwjgl.opengl.Display;

import net.wombatrpgs.mrogue.io.FocusReporter;

/**
 * Desktop implementation of the focus reporter.
 */
public class DesktopFocusReporter extends FocusReporter {
	
	private boolean wasPaused;
	
	/**
	 * Starts the reporter running. Usually requires active listeners though.
	 */
	public DesktopFocusReporter() {
		wasPaused = false;
	}

	/**
	 * @see net.wombatrpgs.mrogue.io.FocusReporter#update()
	 */
	@Override
	public void update() {
		if (!Display.isActive() && !wasPaused) {
			this.reportFocusLost();
			wasPaused = true;
		}
		if (Display.isActive() && wasPaused) {
			this.reportFocusGained();
			wasPaused = false;
		}
	}

}
