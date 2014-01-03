/**
 *  CommandSpeak.java
 *  Created on Feb 3, 2013 8:58:38 PM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mrogue.scenes.commands;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import net.wombatrpgs.mrogue.core.MGlobal;
import net.wombatrpgs.mrogue.graphics.Graphic;
import net.wombatrpgs.mrogue.maps.objects.Picture;
import net.wombatrpgs.mrogue.maps.objects.TimerListener;
import net.wombatrpgs.mrogue.maps.objects.TimerObject;
import net.wombatrpgs.mrogue.scenes.SceneCommand;
import net.wombatrpgs.mrogue.scenes.SceneParser;
import net.wombatrpgs.mrogueschema.cutscene.SpeakerMDO;

/**
 * An individual character speaks. As of 2013-02-14 this thing is a designated
 * single-line command and not meant to be used for multi-line speaks, or to
 * be instantiated by anything but CommandSpeakAll. It's also been repurpposed
 * to take a list of lines instead of a single one.
 */
public class CommandSpeak extends SceneCommand implements UnblockedListener {
	
	protected static final String NAME_SYSTEM = "SYSTEM";
	protected static final int FACE_OFFSET = 160; // px from center
	protected static final float FADE_TIME = .2f; // in s
	
	protected static Map<String, SpeakerMDO> speakers;
	
	protected List<String> lines;
	protected SpeakerMDO mdo;
	protected Graphic faceGraphic;
	protected Picture facePic;
	protected boolean running;
	protected boolean system;
	
	public CommandSpeak(SceneParser parent, String speakerKey, List<String> lines) {
		super(parent, "[subcommand]");
		if (speakers == null) {
			speakers = new HashMap<String, SpeakerMDO>();
			for (SpeakerMDO speakerMDO : MGlobal.data.getEntriesByClass(SpeakerMDO.class)) {
				speakers.put(speakerMDO.id, speakerMDO);
			}
		}
		this.lines = lines;
		if (speakerKey.equals(NAME_SYSTEM)) {
			system = true;
		} else {
//			this.mdo = speakers.get(speakerKey);
//			if (mdo == null) {
//				MGlobal.reporter.warn("Speaker key not in database: " + speakerKey);
//			} else if (mdo.file != null) {
//				faceGraphic = new Graphic(Constants.UI_DIR + mdo.file);
//			}
		}
	}

	/**
	 * @see net.wombatrpgs.mrogue.scenes.SceneCommand#run()
	 */
	@Override
	public boolean run() {
		if (!running) {
			running = true;
			MGlobal.ui.getBox().setLines(lines);
			MGlobal.ui.getBox().reset();
			if (system) {
				MGlobal.ui.getBox().setName("");
			} else {
				//MGlobal.ui.getBox().setName(mdo.name);
			}
			// TODO: figure out why the text box default batch is broken
			if (faceGraphic != null) {
				facePic = new Picture(faceGraphic,
						(MGlobal.window.getWidth() - faceGraphic.getWidth()) / 2 - FACE_OFFSET,
						(MGlobal.window.getHeight() - faceGraphic.getHeight()) / 2, 0);
				facePic.setColor(new Color(1, 1, 1, 0));
				MGlobal.screens.peek().addObject(facePic);
				facePic.tweenTo(new Color(1, 1, 1, 1), FADE_TIME);
				MGlobal.ui.getBox().setBatch(facePic.batch);
			} else {
				MGlobal.ui.getBox().setBatch(new SpriteBatch());
			}
			MGlobal.ui.getBox().setColor(new Color(1, 1, 1, 0));
			MGlobal.screens.peek().addObject(MGlobal.ui.getBox());
			MGlobal.ui.getBox().tweenTo(new Color(1, 1, 1, 1), FADE_TIME);
			block(this);
			return false;
		}
		return !blocking;
	}

	/**
	 * @see net.wombatrpgs.mrogue.scenes.SceneCommand#queueRequiredAssets
	 * (com.badlogic.gdx.assets.AssetManager)
	 */
	@Override
	public void queueRequiredAssets(AssetManager manager) {
		super.queueRequiredAssets(manager);
		if (faceGraphic != null) {
			faceGraphic.queueRequiredAssets(manager);
		}
	}

	/**
	 * @see net.wombatrpgs.mrogue.scenes.SceneCommand#postProcessing
	 * (com.badlogic.gdx.assets.AssetManager, int)
	 */
	@Override
	public void postProcessing(AssetManager manager, int pass) {
		if (faceGraphic != null) {
			faceGraphic.postProcessing(manager, pass);
		}
	}

	/**
	 * @see net.wombatrpgs.mrogue.scenes.commands.UnblockedListener#onUnblock()
	 */
	@Override
	public void onUnblock() {
		if (MGlobal.ui.getBox().isFinished()) {
			if (facePic != null) {
				facePic.tweenTo(new Color(1, 1, 1, 0), FADE_TIME);
			}
			MGlobal.ui.getBox().tweenTo(new Color(1, 1, 1, 0), FADE_TIME);
			final CommandSpeak speak = this;
			new TimerObject(FADE_TIME, getScreen(), new TimerListener() {
				@Override public void onTimerZero(TimerObject source) {
					speak.zero();
				}
			});
		} else {
			MGlobal.ui.getBox().hurryUp();
			block(this);
		}
	}

	/**
	 * @see net.wombatrpgs.mrogue.scenes.SceneCommand#reset()
	 */
	@Override
	public void reset() {
		super.reset();
		running = false;
	}
	
	/**
	 * Called when timer reaches zero-ish. For pic fadeout.
	 */
	protected void zero() {
		MGlobal.screens.peek().removeObject(MGlobal.ui.getBox());
		if (facePic != null) {
			MGlobal.screens.peek().removeObject(facePic);
		}
	}

}
