/**
 *  SceneParser.java
 *  Created on Feb 3, 2013 8:43:01 PM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.rainfall.scenes;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.assets.AssetManager;

import net.wombatrpgs.rainfall.core.Constants;
import net.wombatrpgs.rainfall.core.RGlobal;
import net.wombatrpgs.rainfall.io.CommandMap;
import net.wombatrpgs.rainfall.maps.Level;
import net.wombatrpgs.rainfall.maps.MapObject;
import net.wombatrpgs.rainfall.maps.PauseLevel;
import net.wombatrpgs.rainfall.maps.events.MapEvent;
import net.wombatrpgs.rainfallschema.cutscene.SceneMDO;
import net.wombatrpgs.rainfallschema.cutscene.data.TriggerRepeatType;

/**
 * This thing takes a scene and then hijacks its parent level into doing its
 * bidding.
 */
public class SceneParser extends MapObject {
	
	protected static boolean runningGlobal = false;
	
	protected SceneMDO mdo;
	protected SceneParser childParser;
	protected List<SceneCommand> commands;
	protected List<MapEvent> controlledEvents;
	protected List<FinishListener> listeners;
	protected CommandMap oldMap;
	protected String filename;
	protected boolean executed, running;
	protected float timeSinceStart;
	
	/**
	 * Creates a new scene parser from data. Does not autoplay.
	 * @param 	mdo				The data to create from
	 * @param	parent			The level we'll be parsing on
	 */
	public SceneParser(SceneMDO mdo, Level parent) {
		this(mdo);
		parent.addObject(this);
	}
	
	/**
	 * Creates a new scene parser from data without a parent. Does not auto-
	 * play. Assumes you will add it to the map yourself before playing.
	 * @param 	mdo				The data to construct from
	 */
	public SceneParser(SceneMDO mdo) {
		this.mdo = mdo;
		this.executed = false;
		this.running = false;
		this.filename = Constants.SCENES_DIR + mdo.file;
		this.controlledEvents = new ArrayList<MapEvent>();
		this.listeners = new ArrayList<FinishListener>();
		this.timeSinceStart = 0;
		setPauseLevel(PauseLevel.PAUSE_RESISTANT);
	}
	
	/**
	 * @see net.wombatrpgs.rainfall.maps.MapObject#queueRequiredAssets
	 * (com.badlogic.gdx.assets.AssetManager)
	 */
	@Override
	public void queueRequiredAssets(AssetManager manager) {
		super.queueRequiredAssets(manager);
		manager.load(filename, SceneData.class);
	}

	/**
	 * @see net.wombatrpgs.rainfall.maps.MapObject#postProcessing
	 * (com.badlogic.gdx.assets.AssetManager, int)
	 */
	@Override
	public void postProcessing(AssetManager manager, int pass) {
		super.postProcessing(manager, pass);
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
	 * @see net.wombatrpgs.rainfall.maps.MapObject#update(float)
	 */
	@Override
	public void update(float elapsed) {
		super.update(elapsed);
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
		return filename;
	}

	/**
	 * ...should we really be overriding this?
	 * @see net.wombatrpgs.rainfall.maps.MapObject#reset()
	 */
	@Override
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
	 * Runs the scene assuming it should be run in the current context. Right
	 * now the only context is if a scene has been played before.
	 * @param	level			The level this command was executed on
	 */
	public void run(Level level) {
		if (!running && (!executed || mdo.repeat == TriggerRepeatType.RUN_EVERY_TIME)) {
			if (executed) reset();
			forceRun(level);
		}
	}
	
	/**
	 * Just run the scene, regardless of context.
	 */
	public void forceRun(Level level) {
		RGlobal.hero.cancelActions();
		if (running) {
			RGlobal.reporter.inform("Aborted a parser on " + parent + ": " + this);
			terminate();
		}
		if (parent != level) {
			if (parent != null && parent.contains(this)) {
				RGlobal.reporter.inform("Removed a parser on " + parent + ": " + this);
			}
			this.parent = level;
		}
		if (!level.contains(this)) {
			level.addObject(this);
		}
		RGlobal.reporter.inform("Now running a scene: " + this);
		running = true;
		parent.setPause(true);
		timeSinceStart = 0;
		oldMap = RGlobal.screens.peek().getCommandContext();
		RGlobal.screens.peek().setCommandContext(new SceneCommandMap());
		if (runningGlobal) {
			RGlobal.reporter.warn("Running two scenes at once? " + this);
		}
		runningGlobal = true;
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
			RGlobal.reporter.warn("Tried to remove a non-listener: " + listener);
		}
	}
	
	/**
	 * Called when this parser finishes execution.
	 */
	protected void terminate() {
		RGlobal.reporter.inform("Terminated a scene: " + this);
		RGlobal.screens.peek().setCommandContext(oldMap);
		parent.setPause(false);
		parent.removeObject(this);
		running = false;
		executed = true;
		if (RGlobal.hero != null) {
			RGlobal.hero.halt();
		}
		for (FinishListener listener : listeners) {
			listener.onFinish(parent);
		}
		listeners.clear();
		runningGlobal = false;
	}

}
