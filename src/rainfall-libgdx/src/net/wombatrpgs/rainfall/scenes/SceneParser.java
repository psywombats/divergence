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
import net.wombatrpgs.rainfall.maps.MapObject;
import net.wombatrpgs.rainfallschema.cutscene.SceneMDO;
import net.wombatrpgs.rainfallschema.cutscene.data.TriggerRepeatType;

/**
 * This thing takes a scene and then hijacks its parent level into doing its
 * bidding.
 */
public class SceneParser extends MapObject {
	
	protected SceneMDO mdo;
	protected List<SceneCommand> commands;
	protected String filename;
	protected boolean executed;
	
	/**
	 * Creates a new scene parser from data. Does not autoplay.
	 * @param 	mdo				The data to create from
	 */
	public SceneParser(SceneMDO mdo) {
		this.mdo = mdo;
		this.executed = false;
		this.filename = Constants.SCENES_DIR + mdo.file;
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
				command.queueRequiredAssets(manager);
				commands.add(command);
			}
		}
	}

	/**
	 * Runs the scene assuming it should be run in the current context. Right
	 * now the only context is if a scene has been played before.
	 */
	public void run() {
		if (!executed || mdo.repeat == TriggerRepeatType.RUN_EVERY_TIME) {
			forceRun();
		}
	}
	
	/**
	 * Just run the scene, regardless of context.
	 */
	public void forceRun() {
		executed = true;
		final SceneParser parser = this;
		parent.setPause(new MapObject() {
			@Override
			public void update(float elapsed) {
				super.update(elapsed);
				for (SceneCommand command : commands) {
					if (!command.run()) return;
				}
				parser.getLevel().setPause(null);
				parser.getLevel().removeObject(parser);
				RGlobal.hero.halt();
			}
		});
	}

}
