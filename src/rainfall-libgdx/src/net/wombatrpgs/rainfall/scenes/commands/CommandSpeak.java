/**
 *  CommandSpeak.java
 *  Created on Feb 3, 2013 8:58:38 PM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.rainfall.scenes.commands;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import net.wombatrpgs.rainfall.core.Constants;
import net.wombatrpgs.rainfall.core.RGlobal;
import net.wombatrpgs.rainfall.graphics.Graphic;
import net.wombatrpgs.rainfall.maps.Level;
import net.wombatrpgs.rainfall.maps.objects.Picture;
import net.wombatrpgs.rainfall.maps.objects.TimerListener;
import net.wombatrpgs.rainfall.maps.objects.TimerObject;
import net.wombatrpgs.rainfall.scenes.SceneCommand;
import net.wombatrpgs.rainfall.scenes.SceneParser;
import net.wombatrpgs.rainfallschema.cutscene.SpeakerMDO;

/**
 * An individual character speaks. As of 2013-02-14 this thing is a designated
 * single-line command and not meant to be used for multi-line speaks, or to
 * be instantiated by anything but CommandSpeakAll. It's also been repurpposed
 * to take a list of lines instead of a single one.
 */
public class CommandSpeak extends SceneCommand implements UnblockedListener {
	
	protected static final String NAME_SYSTEM = "SYSTEM";
	protected static final int FACE_OFFSET = 160; // px from center
	protected static final float FADE_TIME = .08f; // in s
	
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
			for (SpeakerMDO speakerMDO : RGlobal.data.getEntriesByClass(SpeakerMDO.class)) {
				speakers.put(speakerMDO.id, speakerMDO);
			}
		}
		this.lines = lines;
		if (speakerKey.equals(NAME_SYSTEM)) {
			system = true;
		} else {
			this.mdo = speakers.get(speakerKey);
			if (mdo == null) {
				RGlobal.reporter.warn("Speaker key not in database: " + speakerKey);
			} else if (mdo.file != null) {
				faceGraphic = new Graphic(Constants.UI_DIR + mdo.file);
			}
		}
	}

	/**
	 * @see net.wombatrpgs.rainfall.scenes.SceneCommand#run()
	 */
	@Override
	public boolean run() {
		if (!running) {
			running = true;
			RGlobal.ui.getBox().setLines(lines);
			if (system) {
				RGlobal.ui.getBox().setName("");
			} else {
				RGlobal.ui.getBox().setName(mdo.name);
			}
			// TODO: figure out why the text box default batch is broken
			if (faceGraphic != null) {
				facePic = new Picture(faceGraphic,
						(RGlobal.window.getWidth() - faceGraphic.getWidth()) / 2 - FACE_OFFSET,
						(RGlobal.window.getHeight() - faceGraphic.getHeight()) / 2, 0);
				facePic.setColor(new Color(1, 1, 1, 0));
				RGlobal.screens.peek().addPicture(facePic);
				facePic.tweenTo(new Color(1, 1, 1, 1), FADE_TIME);
				RGlobal.ui.getBox().setBatch(facePic.batch);
			} else {
				RGlobal.ui.getBox().setBatch(new SpriteBatch());
			}
			RGlobal.ui.getBox().setColor(new Color(1, 1, 1, 0));
			RGlobal.screens.peek().addPicture(RGlobal.ui.getBox());
			RGlobal.ui.getBox().tweenTo(new Color(1, 1, 1, 1), FADE_TIME);
			block(this);
			return false;
		}
		return !blocking;
	}

	/**
	 * @see net.wombatrpgs.rainfall.scenes.SceneCommand#queueRequiredAssets
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
	 * @see net.wombatrpgs.rainfall.scenes.SceneCommand#postProcessing
	 * (com.badlogic.gdx.assets.AssetManager, int)
	 */
	@Override
	public void postProcessing(AssetManager manager, int pass) {
		if (faceGraphic != null) {
			faceGraphic.postProcessing(manager, pass);
		}
	}

	/**
	 * @see net.wombatrpgs.rainfall.scenes.commands.UnblockedListener#onUnblock()
	 */
	@Override
	public void onUnblock() {
		if (RGlobal.ui.getBox().isFinished()) {
			if (facePic != null) {
				facePic.tweenTo(new Color(1, 1, 1, 0), FADE_TIME);
				RGlobal.ui.getBox().tweenTo(new Color(1, 1, 1, 0), FADE_TIME);
			}
			final CommandSpeak speak = this;
			final TimerListener listener = new TimerListener() {
				@Override public void onTimerZero(TimerObject source) {
					speak.zero();
				}
			};
			new TimerObject(FADE_TIME, getParent(), listener) {
				@Override public void onMapFocusLost(Level map) {
					super.onMapFocusLost(map);
					speak.zero();
					map.removeObject(this);
				}
			};
		} else {
			RGlobal.ui.getBox().displayAll();
			block(this);
		}
	}

	/**
	 * @see net.wombatrpgs.rainfall.scenes.SceneCommand#reset()
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
		RGlobal.screens.peek().removePicture(RGlobal.ui.getBox());
		if (facePic != null) {
			RGlobal.screens.peek().removePicture(facePic);
		}
	}

}
