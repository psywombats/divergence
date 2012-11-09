/**
 *  LWJGLFronted.java
 *  Created on Nov 8, 2012 6:21:16 PM for project MGNEngine
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.rainfall.io.lwjgl;

import net.wombatrpgs.mgne.global.Global;
import net.wombatrpgs.rainfall.game.RainfallGame;
import net.wombatrpgs.rainfallschema.WindowDataMDO;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

/**
 * The default LWJGL implementation of the MGN frontend. Could be replaced with
 * an android front later, I guess.
 */
public class LWJGLFrontend {
	
	/** All of our game data */
	protected RainfallGame game;
	
	public LWJGLFrontend(RainfallGame game) {
		this.game = game;
	}

	/**
	 * @see net.wombatrpgs.rainfall.io.Frontend#start(org.lwjgl.opengl.DisplayMode)
	 */
	public void start(WindowDataMDO data) {
		try {
			Display.setDisplayMode(new DisplayMode(data.defaultWidth, data.defaultHeight));
			Display.setTitle(data.windowName);
			Display.create();
			
			// init OpenGL
			
			while (!Display.isCloseRequested()) {
				// render OpenGL
				Display.update();
				game.update();
			}
			onWindowCloseRequested();

		} catch (LWJGLException e) {
			Global.reporter.err("Error initializing the window", e);
		}
	}

	/**
	 * @see net.wombatrpgs.rainfall.io.Frontend#onWindowCloseRequested()
	 */
	public void onWindowCloseRequested() {
		Display.destroy();
	}

}
