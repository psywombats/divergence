/**
 *  CommandSpeak.java
 *  Created on Feb 3, 2013 8:58:38 PM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.rainfall.scenes.commands;

import java.util.HashMap;
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
import net.wombatrpgs.rainfall.ui.text.FontHolder;
import net.wombatrpgs.rainfall.ui.text.TextBox;
import net.wombatrpgs.rainfallschema.cutscene.SpeakerMDO;
import net.wombatrpgs.rainfallschema.settings.UISettingsMDO;
import net.wombatrpgs.rainfallschema.ui.FontMDO;
import net.wombatrpgs.rainfallschema.ui.TextBoxMDO;

/**
 * An individual character speaks.
 */
public class CommandSpeak extends SceneCommand implements UnblockedListener {
	
	protected static final int FACE_OFFSET = 180; // px from center
	protected static final String MDO_KEY = "ui_settings";
	
	protected static Map<String, SpeakerMDO> speakers;
	protected static TextBoxMDO boxMDO;
	protected static FontHolder font;
	
	protected String speech;
	protected SpeakerMDO mdo;
	protected Graphic faceGraphic;
	protected Picture facePic;
	protected TextBox box;
	protected boolean firstSpeak;

	public CommandSpeak(SceneParser parent, String line) {
		super(parent, line);
		// TODO: move the text box / font init
		if (speakers == null) {
			speakers = new HashMap<String, SpeakerMDO>();
			for (SpeakerMDO speakerMDO : RGlobal.data.getEntriesByClass(SpeakerMDO.class)) {
				speakers.put(speakerMDO.id, speakerMDO);
			}
		}
		if (font == null || boxMDO == null) {
			UISettingsMDO mdo = RGlobal.data.getEntryFor(MDO_KEY, UISettingsMDO.class);
			if (mdo == null) {
				RGlobal.reporter.warn("No default UI settings found");
			} else {
				boxMDO = RGlobal.data.getEntryFor(mdo.box, TextBoxMDO.class);
				FontMDO fontMDO = RGlobal.data.getEntryFor(mdo.font, FontMDO.class);
				font = new FontHolder(fontMDO);
			}
			firstSpeak = true;
		} else {
			firstSpeak = false;
		}
		box = new TextBox(boxMDO, font);
		String speakerKey = line.substring(0, line.indexOf(':'));
		speech = line.substring(line.indexOf(':') + 2);
		this.mdo = speakers.get(speakerKey);
		if (mdo == null) {
			RGlobal.reporter.warn("Speaker key not in database: " + speakerKey);
		}
		if (mdo.file != null) {
			faceGraphic = new Graphic(Constants.PORTRAITS_DIR + mdo.file);
		}
	}

	/**
	 * @see net.wombatrpgs.rainfall.scenes.SceneCommand#run()
	 */
	@Override
	public boolean run() {
		if (!running) {
			running = true;
			box.setText(speech);
			if (faceGraphic != null) {
				facePic = new Picture(faceGraphic,
						(Gdx.graphics.getWidth() - faceGraphic.getWidth()) / 2 - FACE_OFFSET,
						(Gdx.graphics.getHeight() - faceGraphic.getHeight()) / 2, 0) {
							@Override
							public void update(float elapsed) {
								super.update(elapsed);
								box.update(elapsed);
							}
							@Override
							public void render(OrthographicCamera camera) {
								super.render(camera);
								box.render(camera);
							}
							
				};
				parent.getLevel().addPicture(facePic);
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
		box.queueRequiredAssets(manager);
		if (faceGraphic != null) {
			faceGraphic.queueRequiredAssets(manager);
		}
		if (firstSpeak) {
			font.queueRequiredAssets(manager);
		}
	}

	/**
	 * @see net.wombatrpgs.rainfall.scenes.SceneCommand#postProcessing
	 * (com.badlogic.gdx.assets.AssetManager, int)
	 */
	@Override
	public void postProcessing(AssetManager manager, int pass) {
		box.postProcessing(manager, pass);
		if (faceGraphic != null) {
			faceGraphic.postProcessing(manager, pass);
		}
		if (firstSpeak) {
			font.postProcessing(manager, pass);
		}
	}

	/**
	 * @see net.wombatrpgs.rainfall.scenes.commands.UnblockedListener#onUnblock()
	 */
	@Override
	public void onUnblock() {
		if (facePic != null) {
			parent.getLevel().removePicture(facePic);
		}
	}

}
