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

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;

import net.wombatrpgs.rainfall.core.Constants;
import net.wombatrpgs.rainfall.core.RGlobal;
import net.wombatrpgs.rainfall.graphics.Graphic;
import net.wombatrpgs.rainfall.maps.objects.Picture;
import net.wombatrpgs.rainfall.scenes.SceneCommand;
import net.wombatrpgs.rainfall.scenes.SceneParser;
import net.wombatrpgs.rainfall.screen.ScreenShowable;
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
	
	protected static Map<String, SpeakerMDO> speakers;
	
	protected List<String> lines;
	protected SpeakerMDO mdo;
	protected Graphic faceGraphic;
	protected ScreenShowable facePic;
	protected boolean running;
	protected boolean system;
	
	CommandSpeak(SceneParser parent, String speakerKey, List<String> lines) {
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
				faceGraphic = new Graphic(Constants.PORTRAITS_DIR + mdo.file);
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
			if (faceGraphic != null) {
				facePic = new Picture(faceGraphic,
						(Gdx.graphics.getWidth() - faceGraphic.getWidth()) / 2 - FACE_OFFSET,
						(Gdx.graphics.getHeight() - faceGraphic.getHeight()) / 2, 0) {
							@Override
							public void update(float elapsed) {
								super.update(elapsed);
								RGlobal.ui.getBox().update(elapsed);
							}
							@Override
							public void render(OrthographicCamera camera) {
								super.render(camera);
								RGlobal.ui.getBox().render(camera);
							}
							
				};
				RGlobal.screens.peek().addPicture(facePic);
			} else {
				facePic = new ScreenShowable() {
					@Override public void update(float elapsed) { 
						RGlobal.ui.getBox().update(elapsed);
					}
					@Override public void queueRequiredAssets(AssetManager manager) { }
					@Override public void postProcessing(AssetManager manager, int pass) { }
					@Override public void render(OrthographicCamera camera) {
						RGlobal.ui.getBox().render(camera);
					}
					@Override public boolean ignoresTint() { return true; }
				};
				RGlobal.screens.peek().addPicture(facePic);
			}
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
		if (facePic != null) {
			RGlobal.screens.peek().removePicture(facePic);
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

}
