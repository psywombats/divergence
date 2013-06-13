/**
 *  TextBox.java
 *  Created on Feb 2, 2013 3:47:40 AM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.rainfall.ui.text;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont.HAlignment;

import net.wombatrpgs.rainfall.core.RGlobal;
import net.wombatrpgs.rainfall.graphics.Graphic;
import net.wombatrpgs.rainfall.io.audio.SoundObject;
import net.wombatrpgs.rainfall.maps.MapThing;
import net.wombatrpgs.rainfall.maps.objects.Picture;
import net.wombatrpgs.rainfallschema.audio.SoundMDO;
import net.wombatrpgs.rainfallschema.graphics.GraphicMDO;
import net.wombatrpgs.rainfallschema.ui.TextBoxMDO;

/**
 * A box that appears on-screen and does its dirty business. Hmph. Actually it
 * stores some data from its MDO, but dynamically updates its strings. In the
 * future it will do some other cool stuff. These things only display on the
 * map the hero is on. Otherwise it really wouldn't make sense, would it?
 */
public class TextBox extends Picture {
	
	protected static final float FADE_IN_TIME = .5f;
	
	protected List<String> lines;
	protected List<String> visibleLines;
	protected TextBoxMDO mdo;
	protected FontHolder font;
	protected TextBoxFormat bodyFormat, nameFormat;
	protected Graphic backer, backer2;
	protected SoundObject typeSfx;
	protected String name;
	protected float elapsed;
	protected int oldChars;
	protected int totalLength;
	
	/**
	 * Creates a new text box from data. Does not deal with the loading of its
	 * font's assets.
	 * @param 	mdo				The MDO to create from
	 * @param 	font			The font to use in rendering, can change
	 */
	public TextBox(TextBoxMDO mdo, FontHolder font) {
		super(RGlobal.data.getEntryFor(mdo.image, GraphicMDO.class), 0);
		this.mdo = mdo;
		this.font = font;
		this.name = "";
		this.oldChars = 0;
		this.elapsed = 0;
		this.bodyFormat = new TextBoxFormat();
		this.nameFormat = new TextBoxFormat();
		this.visibleLines = new ArrayList<String>();
		this.backer = appearance;
		
		if (MapThing.mdoHasProperty(mdo.image2)) {
			GraphicMDO graphicMDO = RGlobal.data.getEntryFor(mdo.image2, GraphicMDO.class);
			this.backer2 = new Graphic(graphicMDO);
			assets.add(backer2);
		}
		if (MapThing.mdoHasProperty(mdo.typeSfx)) {
			typeSfx = new SoundObject(RGlobal.data.getEntryFor(mdo.typeSfx, SoundMDO.class));
			assets.add(typeSfx);
		}
	}
	
	/**
	 * @see net.wombatrpgs.rainfall.maps.MapThing#render
	 * (com.badlogic.gdx.graphics.OrthographicCamera)
	 */
	@Override
	public void render(OrthographicCamera camera) {
		super.render(camera);
		for (int i = 0; i < visibleLines.size(); i++) {
			font.draw(getBatch(), bodyFormat,
					visibleLines.get(i), (int) (font.getLineHeight() * -i));
		}
		font.draw(getBatch(), nameFormat, name, 0);
	}

	/**
	 * @see net.wombatrpgs.rainfall.maps.MapThing#queueRequiredAssets
	 * (com.badlogic.gdx.assets.AssetManager)
	 */
	@Override
	public void queueRequiredAssets(AssetManager manager) {
		super.queueRequiredAssets(manager);
		if (backer2 != null) {
			backer2.queueRequiredAssets(manager);
		}
	}

	/**
	 * @see net.wombatrpgs.rainfall.maps.MapThing#postProcessing
	 * (com.badlogic.gdx.assets.AssetManager, int)
	 */
	@Override
	public void postProcessing(AssetManager manager, int pass) {
		super.postProcessing(manager, pass);
		if (backer2 != null) {
			backer2.postProcessing(manager, pass);
		}
		bodyFormat.x = mdo.x1;
		bodyFormat.y = backer.getHeight() - mdo.y1;
		bodyFormat.align = HAlignment.LEFT;
		bodyFormat.height = mdo.y2 - mdo.y1;
		bodyFormat.width = mdo.x2 - mdo.x1;
		nameFormat.x = mdo.nameX;
		nameFormat.y = backer.getHeight() - mdo.nameY;
		nameFormat.align = HAlignment.LEFT;
		nameFormat.height = mdo.y2 - mdo.y1;
		nameFormat.width = mdo.x2 - mdo.x1;
	}

	/**
	 * @see net.wombatrpgs.rainfall.maps.objects.Picture#update(float)
	 */
	@Override
	public void update(float elapsed) {
		super.update(elapsed);
		
		this.elapsed += elapsed;
		int charsVisible = getVisibleCharCount();
		if (charsVisible > totalLength) {
			visibleLines = lines;
			return;
		}
		if (charsVisible != oldChars) {
			oldChars = charsVisible;
			int toGo = charsVisible;
			for (int atLine = 0; atLine < lines.size(); atLine++) {
				String line = lines.get(atLine);
				if (line.length() < toGo) {
					visibleLines.set(atLine, line);
					toGo -= line.length();
				} else {
					String newLine = line.substring(0, toGo);
					visibleLines.set(atLine, newLine);
					if (newLine.length() > 1) {
						char lastChar = newLine.charAt(newLine.length() - 1);
						if (Character.isLetter(lastChar) || Character.isDigit(lastChar)) {
							typeSfx.play();
						}
					}
					break;
				}
			}
		}
		
	}

	/**
	 * Sets the internal text to be displayed at this textbox. Nothing fancy.
	 * This only displays one line so you probably don't want to use this.
	 * @param 	text			The text to be displayed
	 */
	public void setText(String text) {
		this.lines = new ArrayList<String>();
		lines.add(text);
	}
	
	/**
	 * Sets the lines that are displayed by the text box. These should be
	 * pre-formatted. One string is displayed per line.
	 * @param 	lines			The lines to be displayed in the box.
	 */
	public void setLines(List<String> lines) {
		this.lines = lines;
		this.visibleLines = new ArrayList<String>();
		for (int i = 0; i < lines.size(); i++) {
			visibleLines.add("");
		}
		this.elapsed = 0;
		for (String line : lines) {
			totalLength += line.length();
		}
	}
	
	/**
	 * Sets the name tag for the text box, if the text box supports a name tag.
	 * @param 	name			The name of the character speaking
	 */
	public void setName(String name) {
		this.name = name;
		if (name.length() > 0) {
			this.appearance = (backer);
		} else {
			this.appearance = (backer2);
		}
		setX(mdo.graphicX);
		setY(RGlobal.window.getHeight() - mdo.graphicY - appearance.getHeight());
	}
	
	/**
	 * Instantly displays all characters in the box.
	 */
	public void displayAll() {
		elapsed = totalLength * mdo.typeSpeed;
	}
	
	/**
	 * Checks if this text box has finished autotyping chars.
	 * @return				True if this text box is done, false otherwise
	 */
	public boolean isFinished() {
		return getVisibleCharCount() >= totalLength;
	}
	
	/**
	 * Calculates number of characters that should be visible in the box.
	 * @return				The number of chars visible in this box
	 */
	private int getVisibleCharCount() {
		int speed = mdo.typeSpeed;
		return (int) Math.floor(speed * this.elapsed);
	}

}
