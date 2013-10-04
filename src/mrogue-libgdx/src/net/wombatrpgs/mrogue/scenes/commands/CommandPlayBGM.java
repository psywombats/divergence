/**
 *  CommandPlayBGM.java
 *  Created on Mar 31, 2013 4:11:15 AM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mrogue.scenes.commands;

import net.wombatrpgs.mrogue.core.MGlobal;
import net.wombatrpgs.mrogue.io.audio.MusicObject;
import net.wombatrpgs.mrogue.scenes.SceneCommand;
import net.wombatrpgs.mrogue.scenes.SceneParser;
import net.wombatrpgs.mrogueschema.audio.MusicMDO;

import com.badlogic.gdx.assets.AssetManager;

/**
 * Plays a piece of music.
 */
public class CommandPlayBGM extends SceneCommand {
	
	protected static final String ARG_NO_BGM = "none";
	
	protected MusicObject bgm;

	/**
	 * Inherited constructor.
	 * @param 	parent			The parent parser
	 * @param 	line			The line of code spawned from
	 */
	public CommandPlayBGM(SceneParser parent, String line) {
		super(parent, line);
		String arg = line.substring(line.indexOf(' ' )+1, line.indexOf(']'));
		if (!arg.equals(ARG_NO_BGM)) {
			bgm = new MusicObject(MGlobal.data.getEntryFor(arg, MusicMDO.class));
		}
	}

	/**
	 * @see net.wombatrpgs.mrogue.scenes.SceneCommand#run()
	 */
	@Override
	public boolean run() {
		if (!finished) {
			if (bgm != null) {
				bgm.play();
				parent.getLevel().setBGM(bgm);
			} else {
				parent.getLevel().getBGM().stop();
			}
		}
		finished = true;
		return true;
	}

	/**
	 * @see net.wombatrpgs.mrogue.scenes.SceneCommand#queueRequiredAssets
	 * (com.badlogic.gdx.assets.AssetManager)
	 */
	@Override
	public void queueRequiredAssets(AssetManager manager) {
		super.queueRequiredAssets(manager);
		if (bgm != null) bgm.queueRequiredAssets(manager);
	}

	/**
	 * @see net.wombatrpgs.mrogue.scenes.SceneCommand#postProcessing
	 * (com.badlogic.gdx.assets.AssetManager, int)
	 */
	@Override
	public void postProcessing(AssetManager manager, int pass) {
		super.postProcessing(manager, pass);
		if (bgm != null) bgm.postProcessing(manager, pass);
	}
}
