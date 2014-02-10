/**
 *  SceneCommand.java
 *  Created on Feb 3, 2013 8:51:29 PM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mgne.scenes;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.assets.AssetManager;

import net.wombatrpgs.mgne.core.interfaces.Queueable;
import net.wombatrpgs.mgne.core.interfaces.Updateable;
import net.wombatrpgs.mgne.io.CommandListener;
import net.wombatrpgs.mgne.screen.Screen;
import net.wombatrpgs.mgneschema.io.data.InputCommand;

/**
 * A chunk of code to execute later, returned from a lua function. The idea is
 * that a bunch of these are placed in a queue when a script is evaluated and
 * then executed sequentially. This is to support blocking commands. Instead of
 * the parser being assigned at construction, it's assigned at runtime. Commands
 * can still queue assets like before. 
 * 
 * Blocking as in the press-button-to-go type is no longer support here because
 * really only the speak command needed that thing in the first place and it can
 * block itself. However, a collection of timing methods have been added.
 */
public abstract class SceneCommand implements	Queueable,
												Updateable,
												CommandListener {
	
	protected SceneParser parent;
	
	protected float timeToWait, timeSinceStart;
	protected boolean finished;
	protected boolean running;
	
	protected List<Queueable> assets;
	
	/**
	 * Creates a new command. Before being run it must be possessed by a parent
	 * and have its assets queued.
	 */
	public SceneCommand() {
		finished = false;
		assets = new ArrayList<Queueable>();
		timeToWait = 0;
		timeSinceStart = 0;
	}
	
	/** @return The parent parser that will execute this command */
	public SceneParser getParent() { return this.parent; }
	
	/** @return The screen this command is running on */
	public Screen getScreen() { return parent.getScreen(); }
	
	/** @return True if this command is done running */
	public boolean isFinished() { return this.finished; }
	
	/**
	 * @see net.wombatrpgs.mgne.core.interfaces.Queueable#queueRequiredAssets
	 * (com.badlogic.gdx.assets.AssetManager)
	 */
	@Override
	public void queueRequiredAssets(AssetManager manager) {
		for (Queueable asset : assets) {
			asset.queueRequiredAssets(manager);
		}
	}

	/**
	 * @see net.wombatrpgs.mgne.core.interfaces.Queueable#postProcessing
	 * (com.badlogic.gdx.assets.AssetManager, int)
	 */
	@Override
	public void postProcessing(AssetManager manager, int pass) {
		for (Queueable asset : assets) {
			asset.postProcessing(manager, pass);
		}
	}
	
	/**
	 * This will only be called if this command is actually running.
	 * @see net.wombatrpgs.mgne.core.interfaces.Updateable#update(float)
	 */
	@Override
	public void update(float elapsed) {
		timeSinceStart += elapsed;
		if (shouldFinish()) {
			finish();
		}
	}
	
	/**
	 * Scene commands are equipped to handle user input. Most of the time it's
	 * just ignored, but for text boxes, it could advance the text.
	 * @see net.wombatrpgs.mgne.io.CommandListener#onCommand
	 * (net.wombatrpgs.mgneschema.io.data.InputCommand)
	 */
	@Override
	public boolean onCommand(InputCommand command) {
		return false;
	}

	/**
	 * Call this when it's this scene's turn to run.
	 */
	public final void run(SceneParser parent) {
		this.parent = parent;
		finished = false;
		running = true;
		timeSinceStart = 0;
		internalRun();
	}
	
	/**
	 * What happens when this command has its turn in the scene and runs. All
	 * the running and finished flags are handled elsewhere. This is a good
	 * place to initialize fields.
	 */
	protected abstract void internalRun();
	
	/**
	 * Commands that require assets should take this opportunity to add all
	 * those assets to the assets list. The assets will probably be owned by the
	 * function that generated this command, but that's okay.
	 */
	protected void addToQueue() {
		// default is nothing
	}
	
	/**
	 * Called the very last time this command is run. Usually just tidies up the
	 * state variables. Override if things like clamping positions are needed.
	 */
	protected void finish() {
		finished = true;
	}
	
	/**
	 * Waits for a certain amount of time before progressing. A ton of commands
	 * use this, so why not?
	 * @param	time			The time to wait before reporting finished, in s
	 */
	protected final void waitFor(float time) {
		timeToWait = time;
	}
	
	/**
	 * End condition for the function. By default this just makes sure some
	 * amount of time has expired, but if a command is waiting for user input
	 * or something, then this should stall until that instead. Overrides should
	 * almost always call super. This is checked every update step.
	 * @return					True if command should terminate, else false
	 */
	protected boolean shouldFinish() {
		if (timeSinceStart < timeToWait) return false;
		return true;
	}

}
