/**
 *  SceneCommand.java
 *  Created on Feb 3, 2013 8:51:29 PM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.rainfall.scenes;

import com.badlogic.gdx.assets.AssetManager;

import net.wombatrpgs.rainfall.core.Queueable;
import net.wombatrpgs.rainfall.core.RGlobal;
import net.wombatrpgs.rainfall.io.CommandListener;
import net.wombatrpgs.rainfall.io.CommandMap;
import net.wombatrpgs.rainfall.scenes.commands.UnblockedListener;
import net.wombatrpgs.rainfallschema.io.data.InputCommand;

/**
 * A command that is typically denoted by one line in a scene file. That line is
 * then turned into one of these, packed into an array, then run in order.
 */
public abstract class SceneCommand implements Queueable, CommandListener {
	
	protected SceneParser parent;
	protected String line;
	
	protected UnblockedListener listener;
	protected boolean blocking;
	protected boolean finished;
	
	/**
	 * Creates a new command from a line in scene data.
	 * @param	parent			The parser that will run this command
	 * @param 	line			The line to create the command for
	 */
	public SceneCommand(SceneParser parent, String line) {
		this.parent = parent;
		this.line = line;
		this.blocking = false;
		this.finished = false;
	}
	
	/**
	 * @see net.wombatrpgs.rainfall.core.Queueable#queueRequiredAssets
	 * (com.badlogic.gdx.assets.AssetManager)
	 */
	@Override
	public void queueRequiredAssets(AssetManager manager) {
		// default is nothing
	}

	/**
	 * @see net.wombatrpgs.rainfall.core.Queueable#postProcessing
	 * (com.badlogic.gdx.assets.AssetManager, int)
	 */
	@Override
	public void postProcessing(AssetManager manager, int pass) {
		// default is nothing
	}

	/**
	 * @see net.wombatrpgs.rainfall.io.CommandListener#onCommand
	 * (net.wombatrpgs.rainfallschema.io.data.InputCommand)
	 */
	@Override
	public void onCommand(InputCommand command) {
		if (blocking && command == InputCommand.INTENT_CONFIRM) {
			blocking = false;
			RGlobal.screens.getLevelScreen().getCommandContext().unregisterListener(this);
			listener.onUnblock();
			listener = null;
		}
	}
	
	/** @return The parent parser that will execute this command */
	public SceneParser getParent() { return this.parent; }

	/**
	 * Let this command be executed! Sometimes commands can wait by returning
	 * false.
	 * @return					True if we're done running and should continue
	 */
	public abstract boolean run();
	
	/**
	 * Waits for the user to acknowledge before progressing.
	 */
	public void block(UnblockedListener listener) {
		this.listener = listener;
		RGlobal.screens.getLevelScreen().getCommandContext().registerListener(this);
		blocking = true;
	}

}
