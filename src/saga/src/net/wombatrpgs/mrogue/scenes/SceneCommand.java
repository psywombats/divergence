/**
 *  SceneCommand.java
 *  Created on Feb 3, 2013 8:51:29 PM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mrogue.scenes;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.assets.AssetManager;

import net.wombatrpgs.mrogue.core.MGlobal;
import net.wombatrpgs.mrogue.core.Queueable;
import net.wombatrpgs.mrogue.io.CommandListener;
import net.wombatrpgs.mrogue.scenes.commands.UnblockedListener;
import net.wombatrpgs.mrogue.screen.Screen;
import net.wombatrpgs.mrogueschema.io.data.InputCommand;

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
	
	protected List<Queueable> assets;
	
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
		assets = new ArrayList<Queueable>();
	}
	
	/**
	 * @see net.wombatrpgs.mrogue.core.Queueable#queueRequiredAssets
	 * (com.badlogic.gdx.assets.AssetManager)
	 */
	@Override
	public void queueRequiredAssets(AssetManager manager) {
		for (Queueable asset : assets) {
			asset.queueRequiredAssets(manager);
		}
	}

	/**
	 * @see net.wombatrpgs.mrogue.core.Queueable#postProcessing
	 * (com.badlogic.gdx.assets.AssetManager, int)
	 */
	@Override
	public void postProcessing(AssetManager manager, int pass) {
		for (Queueable asset : assets) {
			asset.postProcessing(manager, pass);
		}
	}

	/**
	 * @see net.wombatrpgs.mrogue.io.CommandListener#onCommand
	 * (net.wombatrpgs.mrogueschema.io.data.InputCommand)
	 */
	@Override
	public boolean onCommand(InputCommand command) {
		if (blocking && command == InputCommand.INTENT_CONFIRM) {
			blocking = false;
			MGlobal.screens.peek().unregisterCommandListener(this);
			UnblockedListener oldListener = listener;
			listener = null;
			oldListener.onUnblock();
			return true;
		} else {
			return false;
		}
	}
	
	/** @return The parent parser that will execute this command */
	public SceneParser getParent() { return this.parent; }
	
	/** @return The screen this command is running on */
	public Screen getScreen() { return parent.getScreen(); }
	
	/** @return True if this command is done running */
	public boolean isFinished() { return this.finished; }

	/**
	 * Let this command be executed! Sometimes commands can wait by returning
	 * false.
	 * @return					True if we're done running and should continue
	 */
	public abstract boolean run();
	
	/**
	 * Reset this command so it can be run again. The default resets all
	 * state variables, and children should override this and reset their
	 * state variables.
	 */
	public void reset() {
		finished = false;
		blocking = false;
	}
	
	/**
	 * Waits for the user to acknowledge before progressing.
	 */
	public void block(UnblockedListener listener) {
		this.listener = listener;
		MGlobal.screens.peek().registerCommandListener(this);
		blocking = true;
	}

}
