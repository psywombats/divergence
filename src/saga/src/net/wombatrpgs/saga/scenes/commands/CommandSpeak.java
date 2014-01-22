/**
 *  CommandSpeak.java
 *  Created on Feb 3, 2013 8:58:38 PM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.saga.scenes.commands;

import java.util.List;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import net.wombatrpgs.saga.core.SGlobal;
import net.wombatrpgs.saga.graphics.Graphic;
import net.wombatrpgs.saga.maps.objects.Picture;
import net.wombatrpgs.saga.maps.objects.TimerListener;
import net.wombatrpgs.saga.maps.objects.TimerObject;
import net.wombatrpgs.saga.scenes.SceneCommand;
import net.wombatrpgs.saga.scenes.SceneParser;

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
	
	protected List<String> lines;
	protected Graphic faceGraphic;
	protected Picture facePic;
	protected boolean running;
	protected boolean system;
	
	public CommandSpeak(SceneParser parent, String speakerKey, List<String> lines) {
		super(parent, "[subcommand]");
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
	 * @see net.wombatrpgs.saga.scenes.SceneCommand#run()
	 */
	@Override
	public boolean run() {
		if (!running) {
			running = true;
			SGlobal.ui.getBox().setLines(lines);
			SGlobal.ui.getBox().reset();

			// TODO: figure out why the text box default batch is broken
			if (faceGraphic != null) {
				facePic = new Picture(faceGraphic,
						(SGlobal.window.getWidth() - faceGraphic.getWidth()) / 2 - FACE_OFFSET,
						(SGlobal.window.getHeight() - faceGraphic.getHeight()) / 2, 0);
				facePic.setColor(new Color(1, 1, 1, 0));
				SGlobal.screens.peek().addObject(facePic);
				facePic.tweenTo(new Color(1, 1, 1, 1), FADE_TIME);
				SGlobal.ui.getBox().setBatch(facePic.batch);
			} else {
				SGlobal.ui.getBox().setBatch(new SpriteBatch());
			}
			SGlobal.ui.getBox().setColor(new Color(1, 1, 1, 0));
			SGlobal.screens.peek().addObject(SGlobal.ui.getBox());
			SGlobal.ui.getBox().tweenTo(new Color(1, 1, 1, 1), FADE_TIME);
			block(this);
			return false;
		}
		return !blocking;
	}

	/**
	 * @see net.wombatrpgs.saga.scenes.SceneCommand#queueRequiredAssets
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
	 * @see net.wombatrpgs.saga.scenes.SceneCommand#postProcessing
	 * (com.badlogic.gdx.assets.AssetManager, int)
	 */
	@Override
	public void postProcessing(AssetManager manager, int pass) {
		if (faceGraphic != null) {
			faceGraphic.postProcessing(manager, pass);
		}
	}

	/**
	 * @see net.wombatrpgs.saga.scenes.commands.UnblockedListener#onUnblock()
	 */
	@Override
	public void onUnblock() {
		if (SGlobal.ui.getBox().isFinished()) {
			if (facePic != null) {
				facePic.tweenTo(new Color(1, 1, 1, 0), FADE_TIME);
			}
			SGlobal.ui.getBox().tweenTo(new Color(1, 1, 1, 0), FADE_TIME);
			final CommandSpeak speak = this;
			new TimerObject(FADE_TIME, getScreen(), new TimerListener() {
				@Override public void onTimerZero(TimerObject source) {
					speak.zero();
				}
			});
		} else {
			SGlobal.ui.getBox().hurryUp();
			block(this);
		}
	}

	/**
	 * @see net.wombatrpgs.saga.scenes.SceneCommand#reset()
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
		SGlobal.screens.peek().removeObject(SGlobal.ui.getBox());
		if (facePic != null) {
			SGlobal.screens.peek().removeObject(facePic);
		}
	}

}
