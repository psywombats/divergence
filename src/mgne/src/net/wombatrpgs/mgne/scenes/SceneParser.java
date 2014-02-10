/**
 *  SceneParser.java
 *  Created on Feb 3, 2013 8:43:01 PM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mgne.scenes;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.luaj.vm2.LuaValue;

import com.badlogic.gdx.assets.AssetManager;

import net.wombatrpgs.mgne.core.Constants;
import net.wombatrpgs.mgne.core.FinishListener;
import net.wombatrpgs.mgne.core.Queueable;
import net.wombatrpgs.mgne.core.SGlobal;
import net.wombatrpgs.mgne.core.Updateable;
import net.wombatrpgs.mgne.io.CommandListener;
import net.wombatrpgs.mgne.io.CommandMap;
import net.wombatrpgs.mgne.io.command.CMapScene;
import net.wombatrpgs.mgne.maps.Level;
import net.wombatrpgs.mgne.screen.Screen;
import net.wombatrpgs.mgneschema.io.data.InputCommand;

/**
 * This thing takes a scene and then hijacks its parent level into doing its
 * bidding.
 * As of 2014-01-24, it plays back a series of Lua commands.
 */
public class SceneParser implements	Updateable,
									Queueable,
									CommandListener {
	
	protected Screen parent;
	protected List<FinishListener> listeners;
	protected CommandMap commandMap;
	protected String filename;
	
	protected List<SceneCommand> commands;
	protected Iterator<SceneCommand> runningCommands;
	protected SceneCommand currentCommand;
	
	protected boolean running;
	
	/**
	 * Creates a scene parser with no commands. It's expected that somewhere
	 * along the way it will be filled in with some commands. This is useful for
	 * generating dialog on the fly.
	 */
	public SceneParser() {
		this.running = false;
		this.listeners = new ArrayList<FinishListener>();
	}
	
	/**
	 * Creates a new scene parser for a given file. No autoplay. Assumes no
	 * repeat.
	 * @param	fileName		The filename to load, relative to scenes dir
	 */
	public SceneParser(String filename) {
		this();
		this.filename = Constants.SCENES_DIR + filename;
	}
	
	/** @return The screen we're running on */
	public Screen getScreen() { return parent; }
	
	/**
	 * Load the file if we're using one, otherwise we're anonymous and assume
	 * all the commands have been manually added.
	 * @see net.wombatrpgs.mgne.maps.MapThing#queueRequiredAssets
	 * (com.badlogic.gdx.assets.AssetManager)
	 */
	@Override
	public void queueRequiredAssets(AssetManager manager) {
		if (filename != null) {
			manager.load(filename, LuaValue.class);
		}
	}

	/**
	 * @see net.wombatrpgs.mgne.maps.MapThing#postProcessing
	 * (com.badlogic.gdx.assets.AssetManager, int)
	 */
	@Override
	public void postProcessing(AssetManager manager, int pass) {
		if (pass == 0) {
			LuaValue script = manager.get(filename, LuaValue.class);
			commands = SceneLib.parseScene(script);
			for (SceneCommand command : commands) {
				command.queueRequiredAssets(manager);
			}
		} else {
			for (SceneCommand command : commands) {
				command.postProcessing(manager, pass - 1);
			}
		}
	}

	/**
	 * @see net.wombatrpgs.mgne.maps.MapThing#update(float)
	 */
	@Override
	public void update(float elapsed) {
		if (running) {
			if (currentCommand.isFinished()) {
				nextCommand();
			} else {
				currentCommand.update(elapsed);
			}
		}
	}

	/**
	 * @see net.wombatrpgs.mgne.io.CommandListener#onCommand
	 * (net.wombatrpgs.mgneschema.io.data.InputCommand)
	 */
	@Override
	public boolean onCommand(InputCommand command) {
		if (!running || currentCommand == null) {
			SGlobal.reporter.warn("Received a command to invalid parser " + this);
			return false;
		}
		return currentCommand.onCommand(command);
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		if (filename != null) {
			return filename;
		} else {
			return "anon scene";
		}
	}
	
	/** @return Trus if this parser is in the process of running */
	public boolean isRunning() { return this.running; }
	
	/** @return The level currently active. Same as the static call */
	public Level getLevel() { return SGlobal.levelManager.getActive(); }

	/**
	 * Runs the scene assuming it should be run in the current context. The only
	 * check is to make sure it isn't already running. This always runs on the
	 * current screen.
	 */
	public void run() {
		if (running) {
			SGlobal.reporter.warn("Trying to run a running scene: " + this);
			return;
		}
		if (commands.size() == 0) {
			SGlobal.reporter.warn("Tried to run an empty scene: " + this);
		}
		
		SGlobal.reporter.inform("Now running a scene: " + this);
		commandMap = new CMapScene();
		parent = SGlobal.screens.peek();
		parent.addUChild(this);
		parent.pushCommandContext(commandMap);
		parent.registerCommandListener(this);
		runningCommands = commands.iterator();
		nextCommand();
		running = true;
	}
	
	/**
	 * Adds a listener to the parser. The parser will be notified when the
	 * parser finished executing the scene. The listener will then be removed,
	 * so watch out.
	 * @param 	listener		The listener to add
	 */
	public void addListener(FinishListener listener) {
		listeners.add(listener);
	}
	
	/**
	 * Removes a listener to a parser. It will no longer be notified.
	 * @param 	listener		The existing listener to remove
	 */
	public void removeListener(FinishListener listener) {
		if (listeners.contains(listener)) {
			listeners.remove(listener);
		} else {
			SGlobal.reporter.warn("Tried to remove a non-listener: " + listener);
		}
	}
	
	/**
	 * Called when this parser finishes execution.
	 */
	protected void terminate() {
		SGlobal.reporter.inform("Terminated a scene: " + this);
		parent.removeCommandContext(commandMap);
		parent.unregisterCommandListener(this);
		running = false;
		for (FinishListener listener : listeners) {
			listener.onFinish();
		}
		listeners.clear();
	}
	
	/**
	 * Moves on to start processing the next command.
	 */
	protected void nextCommand() {
		if (runningCommands.hasNext()) {
			currentCommand = runningCommands.next();
			currentCommand.run(this);
		} else {
			terminate();
		}
	}

}
