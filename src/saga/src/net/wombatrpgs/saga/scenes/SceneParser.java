/**
 *  SceneParser.java
 *  Created on Feb 3, 2013 8:43:01 PM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.saga.scenes;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.assets.AssetManager;

import net.wombatrpgs.saga.core.Constants;
import net.wombatrpgs.saga.core.FinishListener;
import net.wombatrpgs.saga.core.SGlobal;
import net.wombatrpgs.saga.core.Queueable;
import net.wombatrpgs.saga.core.Updateable;
import net.wombatrpgs.saga.io.CommandMap;
import net.wombatrpgs.saga.io.command.CMapScene;
import net.wombatrpgs.saga.maps.Level;
import net.wombatrpgs.saga.maps.events.MapEvent;
import net.wombatrpgs.saga.screen.Screen;

/**
 * This thing takes a scene and then hijacks its parent level into doing its
 * bidding.
 */
public class SceneParser implements	Updateable,
									Queueable {
	
	protected Screen parent;
	protected SceneParser childParser;
	protected List<SceneCommand> commands;
	protected List<MapEvent> controlledEvents;
	protected List<FinishListener> listeners;
	protected CommandMap ourMap;
	protected String filename;
	protected boolean executed, running;
	protected float timeSinceStart;
	
	/**
	 * Creates a scene parser with no commands. It's expected that somewhere
	 * along the way it will be filled in with some commands. This is useful for
	 * generating dialog on the fly.
	 */
	public SceneParser() {
		this.executed = false;
		this.running = false;
		this.controlledEvents = new ArrayList<MapEvent>();
		this.listeners = new ArrayList<FinishListener>();
		this.timeSinceStart = 0;
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
	 * @see net.wombatrpgs.saga.maps.MapThing#queueRequiredAssets
	 * (com.badlogic.gdx.assets.AssetManager)
	 */
	@Override
	public void queueRequiredAssets(AssetManager manager) {
		if (filename != null) {
			manager.load(filename, SceneData.class);
		}
	}

	/**
	 * @see net.wombatrpgs.saga.maps.MapThing#postProcessing
	 * (com.badlogic.gdx.assets.AssetManager, int)
	 */
	@Override
	public void postProcessing(AssetManager manager, int pass) {
		if (pass > 1) {
			return;
		} else if (pass == 1) {
			for (SceneCommand command : commands) {
				command.postProcessing(manager, pass-1);
			}
		} else if (pass == 0) {
			commands = new ArrayList<SceneCommand>();
			List<String> lines = manager.get(filename, SceneData.class).getLines();
			for (String line : lines) {
				SceneCommand command = CommandFactory.make(this, line);
				if (command != null) {
					command.queueRequiredAssets(manager);
					commands.add(command);
				}
			}
		}
	}

	/**
	 * @see net.wombatrpgs.saga.maps.MapThing#update(float)
	 */
	@Override
	public void update(float elapsed) {
		timeSinceStart += elapsed;
		if (running) {
			for (SceneCommand command : commands) {
				if (!command.run()) return;
			}
			if (childParser == null || childParser.hasExecuted()) {
				terminate();
			}
		}
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

	/**
	 * Resets the scene?
	 */
	public void reset() {
		this.executed = false;
		this.childParser = null;
		for (SceneCommand command : commands) {
			command.reset();
		}
	}

	/** @return True if this parser has been run */
	public boolean hasExecuted() { return this.executed; }
	
	/** @return Trus if this parser is in the process of running */
	public boolean isRunning() { return this.running; }
	
	/** @param parser The thing to wait until done */
	public void setChild(SceneParser parser) { this.childParser = parser; }
	
	/** @return The level currently active. Same as the static call */
	public Level getLevel() { return SGlobal.levelManager.getActive(); }
	
	/**
	 * Gets all map events that have been touched by movements by this parser.
	 * @return					The events controlled by the parser
	 */
	public List<MapEvent> getControlledEvents() {
		return controlledEvents;
	}
	
	/**
	 * Gets the time since this scene was last started.
	 * @return					Time since last restart
	 */
	public float getTimeSinceStart() { return this.timeSinceStart; }

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
		
		SGlobal.reporter.inform("Now running a scene: " + this);
		ourMap = new CMapScene();
		parent = SGlobal.screens.peek();
		parent.addUChild(this);
		parent.pushCommandContext(ourMap);
		running = true;
		timeSinceStart = 0;
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
		parent.removeCommandContext(ourMap);
		running = false;
		executed = true;
		if (SGlobal.getHero() != null) {
			SGlobal.getHero().halt();
		}
		for (FinishListener listener : listeners) {
			listener.onFinish();
		}
		listeners.clear();
	}

}
