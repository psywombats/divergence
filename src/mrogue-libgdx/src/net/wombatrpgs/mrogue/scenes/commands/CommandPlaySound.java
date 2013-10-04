/**
 *  CommandPlaySound.java
 *  Created on Mar 28, 2013 7:54:17 PM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mrogue.scenes.commands;

import com.badlogic.gdx.assets.AssetManager;

import net.wombatrpgs.mrogue.core.MGlobal;
import net.wombatrpgs.mrogue.io.audio.SoundObject;
import net.wombatrpgs.mrogue.scenes.SceneCommand;
import net.wombatrpgs.mrogue.scenes.SceneParser;
import net.wombatrpgs.mrogueschema.audio.SoundMDO;

/**
 *
 */
public class CommandPlaySound extends SceneCommand {
	
	protected SoundObject sfx;

	/**
	 * Inherited constructor.
	 * @param 	parent			The parent parser
	 * @param 	line			The line of code spawned from
	 */
	public CommandPlaySound(SceneParser parent, String line) {
		super(parent, line);
		String arg = line.substring(line.indexOf(' ' )+1, line.indexOf(']'));
		sfx = new SoundObject(MGlobal.data.getEntryFor(arg, SoundMDO.class));
	}

	/**
	 * @see net.wombatrpgs.mrogue.scenes.SceneCommand#run()
	 */
	@Override
	public boolean run() {
		if (!finished) sfx.play();
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
		sfx.queueRequiredAssets(manager);
	}

	/**
	 * @see net.wombatrpgs.mrogue.scenes.SceneCommand#postProcessing
	 * (com.badlogic.gdx.assets.AssetManager, int)
	 */
	@Override
	public void postProcessing(AssetManager manager, int pass) {
		super.postProcessing(manager, pass);
		sfx.postProcessing(manager, pass);
	}

}
