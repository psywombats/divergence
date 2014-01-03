/**
 *  TextBox.java
 *  Created on Feb 2, 2013 3:47:40 AM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mrogue.ui.text;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont.HAlignment;

import net.wombatrpgs.mrogue.core.MGlobal;
import net.wombatrpgs.mrogue.graphics.Graphic;
import net.wombatrpgs.mrogue.io.audio.SoundObject;
import net.wombatrpgs.mrogue.maps.MapThing;
import net.wombatrpgs.mrogue.maps.objects.Picture;
import net.wombatrpgs.mrogue.screen.WindowSettings;
import net.wombatrpgs.mrogueschema.audio.SoundMDO;
import net.wombatrpgs.mrogueschema.ui.AnchorType;
import net.wombatrpgs.mrogueschema.ui.TextBoxMDO;

/**
 * A box that appears on-screen and does its dirty business. Hmph. Actually it
 * stores some data from its MDO, but dynamically updates its strings. In the
 * future it will do some other cool stuff. These things only display on the
 * map the hero is on. Otherwise it really wouldn't make sense, would it?
 */
public class TextBox extends Picture {
	
	protected static final float FADE_IN_TIME = .5f;
	
	protected List<String> lines;
	protected List<String> visibleLines, mutatedLines;
	protected TextBoxMDO mdo;
	protected FontHolder font;
	protected TextBoxFormat bodyFormat, nameFormat;
	protected Graphic backer, backer2;
	protected SoundObject typeSfx;
	protected String name;
	protected float sinceChar;
	protected int visibleChars;
	protected int totalLength;
	protected boolean waiting;
	
	/**
	 * Creates a new text box from data. Does not deal with the loading of its
	 * font's assets.
	 * @param 	mdo				The MDO to create from
	 * @param 	font			The font to use in rendering, can change
	 */
	public TextBox(TextBoxMDO mdo, FontHolder font) {
		super(mdo.image, 1);
		this.mdo = mdo;
		this.font = font;
		this.name = "";
		this.visibleChars = 0;
		this.sinceChar = 0;
		this.bodyFormat = new TextBoxFormat();
		this.nameFormat = new TextBoxFormat();
		this.visibleLines = new ArrayList<String>();
		this.mutatedLines = new ArrayList<String>();
		this.backer = appearance;
		this.waiting = false;
		
		if (MapThing.mdoHasProperty(mdo.image2)) {
			this.backer2 = new Graphic(mdo.image2);
			assets.add(backer2);
		}
		if (MapThing.mdoHasProperty(mdo.typeSfx)) {
			typeSfx = new SoundObject(MGlobal.data.getEntryFor(mdo.typeSfx, SoundMDO.class));
			assets.add(typeSfx);
		}
	}
	
	/**
	 * @see net.wombatrpgs.mrogue.maps.MapThing#render
	 * (com.badlogic.gdx.graphics.OrthographicCamera)
	 */
	@Override
	public void render(OrthographicCamera camera) {
		if (!MGlobal.won2) {
			super.render(camera);
		}
		font.setAlpha(currentColor.a);
		for (int i = 0; i < mutatedLines.size(); i++) {
			font.draw(getBatch(), bodyFormat,
					mutatedLines.get(i), (int) (font.getLineHeight() * -i));
		}
		font.draw(getBatch(), nameFormat, name, 0);
		font.setAlpha(1);
	}

	/**
	 * @see net.wombatrpgs.mrogue.maps.MapThing#queueRequiredAssets
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
	 * @see net.wombatrpgs.mrogue.maps.MapThing#postProcessing
	 * (com.badlogic.gdx.assets.AssetManager, int)
	 */
	@Override
	public void postProcessing(AssetManager manager, int pass) {
		super.postProcessing(manager, pass);
		if (backer2 != null) {
			backer2.postProcessing(manager, pass);
		}
		WindowSettings win = MGlobal.window;
		if (mdo.anchor == AnchorType.ANCHOR) {
			bodyFormat.x = win.getWidth()/2 - backer.getWidth()/2 + mdo.x1;
			bodyFormat.y = win.getHeight()/2 + backer.getHeight()/2 - mdo.y1;
			bodyFormat.align = HAlignment.LEFT;
			bodyFormat.width = backer.getWidth() - mdo.x1 - mdo.x2;
			bodyFormat.height = backer.getHeight() - mdo.y1 - mdo.y2;
		} else {
			bodyFormat.x = mdo.x1;
			bodyFormat.y = backer.getHeight() - mdo.y1;
			bodyFormat.align = HAlignment.LEFT;
			bodyFormat.height = mdo.y2 - mdo.y1;
			bodyFormat.width = mdo.x2 - mdo.x1;
		}
		nameFormat.x = mdo.nameX;
		nameFormat.y = backer.getHeight() - mdo.nameY;
		nameFormat.align = HAlignment.LEFT;
		nameFormat.height = mdo.y2 - mdo.y1;
		nameFormat.width = mdo.x2 - mdo.x1;
	}

	/**
	 * @see net.wombatrpgs.mrogue.maps.objects.Picture#update(float)
	 */
	@Override
	public void update(float elapsed) {
		super.update(elapsed);
		
		if (mdo.anchor == AnchorType.ANCHOR) {
			setX(MGlobal.window.getWidth()/2 - backer.getWidth()/2);
			setY(MGlobal.window.getHeight()/2 - backer.getHeight()/2);
		}
		
		boolean mutate = false;
		for (String line : visibleLines) {
			if (line.contains("*") || line.contains("$")) {
				mutate = true;
				break;
			}
		}
		if (mutate) {
			mutatedLines = mutate(visibleLines);
		} else {
			mutatedLines = visibleLines;
		}
		
		if (waiting) return;
		
		sinceChar += elapsed;
		boolean playedType = false;
		for (; sinceChar > 1f/mdo.typeSpeed; sinceChar -= 1f/mdo.typeSpeed) {
			visibleChars += 1;
			if (visibleChars > totalLength) {
				visibleLines = lines;
				return;
			}
			int at = visibleChars;
			int atLine = 0;
			for (atLine = 0; atLine < lines.size(); atLine++) {
				String line = lines.get(atLine);
				if (line.length() < at) {
					visibleLines.set(atLine, line);
					at -= line.length();
				} else {
					String newLine = line.substring(0, at);
					if (newLine.length() > 1) {
						char lastChar = newLine.charAt(newLine.length() - 1);
						if (lastChar == '\\') {
							char special = line.charAt(at);
							if (special == '\\') {
								typeSfx.play();
							} else if (special == 'n') {
								waiting = true;
								lines.set(atLine, line.substring(0, line.indexOf("\\n")));
								sinceChar = 0;
								break;
							}
						} else if (Character.isLetter(lastChar) || Character.isDigit(lastChar)) {
							if (!playedType) {
								typeSfx.play();
								playedType = true;
							}
						}
					}
					visibleLines.set(atLine, newLine);
					break;
				}
			}
			if (atLine == lines.size()) {
				visibleChars = totalLength;
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
		this.sinceChar = 0;
		for (String line : lines) {
			totalLength += line.length();
		}
		sinceChar = 0;
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
		if (mdo.anchor == AnchorType.OFFSET) {
			setX(mdo.graphicX);
			setY(MGlobal.window.getHeight() - mdo.graphicY - appearance.getHeight());
		}
	}
	
	/**
	 * Speeds up this box's movement, either by tnstantly displaying all
	 * characters in the box or by unsetting its most recent wait.
	 */
	public void hurryUp() {
		if (waiting) {
			waiting = false;
			sinceChar = 0;
		} else {
			sinceChar = Float.MAX_VALUE;
		}
	}
	
	/**
	 * Checks if this text box has finished autotyping chars.
	 * @return				True if this text box is done, false otherwise
	 */
	public boolean isFinished() {
		return visibleChars >= totalLength;
	}
	
	/**
	 * Resets this text box.
	 */
	public void reset() {
		sinceChar = 0;
		visibleChars = 0;
	}
	
	/**
	 * Given a bunch of strings, runs a randomizing algorithm over them.
	 * @param	input			The strings to mutate
	 * @return					The result of the mutation
	 */
	protected List<String> mutate(List<String> input) {
		List<String> output = new ArrayList<String>();
		for (String in : input) {
			char out[] = new char[in.length()];
			int fillAt = 0;
			for (int i = 0; i < in.length(); i += 1) {
				out[fillAt] = in.charAt(i);
				if (out[fillAt] == '*') {
					out[fillAt] = (char)(MGlobal.rand.nextInt(26) + 'a');
				}
				if (out[fillAt] == '$') {
					if (i < in.length()-1) {
						i += 1;
						if (MGlobal.rand.nextFloat() < .01) {
							out[fillAt] = (char)(MGlobal.rand.nextInt(26) + 'a');
						} else {
							out[fillAt] = in.charAt(i); 
						}
					} else {
						out[fillAt] = ' ';
					}
				}
				fillAt += 1;
			}
			output.add(new String(out, 0, fillAt));
		}
		return output;
	}

}
