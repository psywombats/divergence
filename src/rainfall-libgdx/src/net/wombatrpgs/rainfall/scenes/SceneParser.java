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
	
	protected SceneMDO mdo;
	protected List<SceneCommand> commands;
	protected List<MapEvent> controlledEvents;
	protected CommandMap oldMap;
	protected String filename;
	protected boolean executed, enabled;
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
	 * play. Assumes you will add it to the scene yourself before playing.
	 * @param 	mdo				The data to construct from
	 */
	public SceneParser(SceneMDO mdo) {
		this.mdo = mdo;
		this.executed = false;
		this.enabled = false;
		this.filename = Constants.SCENES_DIR + mdo.file;
		this.controlledEvents = new ArrayList<MapEvent>();
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
		if (enabled) {
			for (SceneCommand command : commands) {
				if (!command.run()) return;
			}
			terminate();
		}
	}
	
	/** @return True if this parser has been run */
	public boolean hasExecuted() { return this.executed; }
	
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
		if (!enabled && (!executed || mdo.repeat == TriggerRepeatType.RUN_EVERY_TIME)) {
			if (executed) reset();
			enabled = true;
			forceRun();
		}
	}
	
	/**
	 * Just run the scene, regardless of context.
	 */
	public void forceRun() {
		parent.setPause(true);
		timeSinceStart = 0;
		oldMap = RGlobal.screens.getLevelScreen().getCommandContext();
		RGlobal.screens.getLevelScreen().setCommandContext(new SceneCommandMap());
	}
	
	/** 
	 * Resets the scene so that it can be run again. Does this by setting
	 * execution to false and reseting its children.
	 */
	public void reset() { 
		this.executed = false;
		for (SceneCommand command : commands) {
			command.reset();
		}
	}
	
	/**
	 * Called when this parser finishes execution.
	 */
	protected void terminate() {
		RGlobal.screens.getLevelScreen().setCommandContext(oldMap);
		parent.setPause(false);
		parent.removeObject(this);
		enabled = false;
		executed = true;
		RGlobal.hero.halt();
	}

}
