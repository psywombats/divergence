/**
 *  CommandSpeakAll.java
 *  Created on Feb 21, 2013 5:01:19 PM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mrogue.scenes.commands;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.assets.AssetManager;

import net.wombatrpgs.mrogue.scenes.SceneCommand;
import net.wombatrpgs.mrogue.scenes.SceneParser;

/**
 * Exists to turn a multi-line dialogue blurb into a bunch of CommandSpeak.
 */
public class CommandSpeakAll extends SceneCommand {
	
	public static final int CHARS_PER_LINE = 54;
	public static final int LINES_PER_BOX = 3;
	
	protected List<CommandSpeak> subCommands;

	public CommandSpeakAll(SceneParser parent, String line) {
		super(parent, line);
		String speakerKey = line.substring(0, line.indexOf(':'));
		String allText = line.substring(line.indexOf(':') + 2);
		subCommands = new ArrayList<CommandSpeak>();
		while (allText.length() > 0) {
			List<String> lines = new ArrayList<String>();
			for (int i = 0; i < LINES_PER_BOX && allText.length() > 0; i++) {
				if (allText.length() < CHARS_PER_LINE) {
					lines.add(allText);
					allText = "";
				} else {
					String full = allText.substring(0, CHARS_PER_LINE);
					lines.add(full.substring(0, full.lastIndexOf(' ')));
					allText = allText.substring(CHARS_PER_LINE);
					allText = full.substring(full.lastIndexOf(' ') + 1) + allText;
				}

			}
			subCommands.add(new CommandSpeak(parent, speakerKey, lines));
		}
	}

	/**
	 * @see net.wombatrpgs.mrogue.scenes.SceneCommand#run()
	 */
	@Override
	public boolean run() {
		for (CommandSpeak command : subCommands) {
			if (!command.run()) return false;
		}
		return true;
	}

	/**
	 * @see net.wombatrpgs.mrogue.scenes.SceneCommand#queueRequiredAssets
	 * (com.badlogic.gdx.assets.AssetManager)
	 */
	@Override
	public void queueRequiredAssets(AssetManager manager) {
		super.queueRequiredAssets(manager);
		for (CommandSpeak command : subCommands) {
			command.queueRequiredAssets(manager);
		}
	}

	/**
	 * @see net.wombatrpgs.mrogue.scenes.SceneCommand#postProcessing
	 * (com.badlogic.gdx.assets.AssetManager, int)
	 */
	@Override
	public void postProcessing(AssetManager manager, int pass) {
		super.postProcessing(manager, pass);
		for (CommandSpeak command : subCommands) {
			command.postProcessing(manager, pass);
		}
	}

	/**
	 * @see net.wombatrpgs.mrogue.scenes.SceneCommand#reset()
	 */
	@Override
	public void reset() {
		super.reset();
		for (CommandSpeak command : subCommands) {
			command.reset();
		}
	}

}
