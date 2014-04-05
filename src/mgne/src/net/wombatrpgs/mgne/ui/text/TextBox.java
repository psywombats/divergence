/**
 *  TextBox.java
 *  Created on Feb 2, 2013 3:47:40 AM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mgne.ui.text;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont.HAlignment;

import net.wombatrpgs.mgne.core.MGlobal;
import net.wombatrpgs.mgne.graphics.ScreenGraphic;
import net.wombatrpgs.mgne.io.audio.SoundObject;
import net.wombatrpgs.mgne.maps.MapThing;
import net.wombatrpgs.mgne.screen.Screen;
import net.wombatrpgs.mgne.screen.WindowSettings;
import net.wombatrpgs.mgne.ui.Nineslice;
import net.wombatrpgs.mgneschema.audio.SoundMDO;
import net.wombatrpgs.mgneschema.ui.NinesliceMDO;
import net.wombatrpgs.mgneschema.ui.TextBoxMDO;
import net.wombatrpgs.mgneschema.ui.data.BoxAnchorType;

/**
 * A box that appears on-screen and does its dirty business. Hmph. Actually it
 * stores some data from its MDO, but dynamically updates its strings. In the
 * future it will do some other cool stuff. These things only display on the
 * map the hero is on. Otherwise it really wouldn't make sense, would it?
 * 
 * Now that this thing's in the Saga project, it's nineslice-generated. At some
 * pointi it'd probably be a good idea to get in a MDO-inheritance deal where
 * picture-backed text boxes are also supported. It also seems to handle font
 * loading?
 */
public class TextBox extends ScreenGraphic {
	
	protected TextBoxMDO mdo;
	protected FontHolder font;
	
	protected List<String> words;
	protected List<String> currentLines;
	protected List<String> visibleLines;
	
	protected Screen parent;
	protected Nineslice backer;
	protected TextBoxFormat bodyFormat, nameFormat;
	protected SoundObject typeSfx;
	protected float sinceChar;
	protected int totalLength;
	protected int visibleChars;
	protected int boxWidth, boxHeight;
	protected boolean waiting;
	protected boolean allVisible;
	
	/**
	 * Creates a new text box from data. Does not deal with the loading of its
	 * font's assets.
	 * @param 	mdo				The MDO to create from
	 * @param 	font			The font to use in rendering, can change
	 */
	public TextBox(TextBoxMDO mdo, FontHolder font) {
		super(0, 0);
		this.mdo = mdo;
		this.font = font;
		this.visibleChars = 0;
		this.sinceChar = 0;
		this.bodyFormat = new TextBoxFormat();
		this.nameFormat = new TextBoxFormat();
		this.currentLines = new ArrayList<String>();
		this.visibleLines = new ArrayList<String>();
		this.words = new ArrayList<String>();
		this.waiting = false;
		this.fadingOut = false;
		
		if (MapThing.mdoHasProperty(mdo.typeSfx)) {
			typeSfx = new SoundObject(MGlobal.data.getEntryFor(mdo.typeSfx, SoundMDO.class));
			assets.add(typeSfx);
		}
		if (MapThing.mdoHasProperty(mdo.nineslice)) {
			backer = new Nineslice(MGlobal.data.getEntryFor(mdo.nineslice, NinesliceMDO.class));
			assets.add(backer);
		}
		assets.add(font);
	}
	
	/** @see net.wombatrpgs.mgne.graphics.ScreenGraphic#getWidth() */
	@Override public int getWidth() { return boxWidth; }

	/** @see net.wombatrpgs.mgne.graphics.ScreenGraphic#getHeight() */
	@Override public int getHeight() { return boxHeight; }
	
	/**
	 * @see net.wombatrpgs.mgne.maps.MapThing#render
	 * (com.badlogic.gdx.graphics.OrthographicCamera)
	 */
	@Override
	public void render(OrthographicCamera camera) {
		super.render(camera);
		
		// backer first
		if (backer != null) {
			int atY = 0;
			if (mdo.anchor == BoxAnchorType.BOTTOM) {
				atY = MGlobal.window.getViewportHeight() - boxHeight;
			}
			backer.renderAt(getBatch(), 0, atY);
		}
		
		// now for the font
		font.setAlpha(currentColor.a);
		for (int i = 0; i < visibleLines.size(); i++) {
			font.draw(getBatch(), bodyFormat,
					visibleLines.get(i), (int) (font.getLineHeight() * -i));
		}
		font.setAlpha(1);
	}

	/**
	 * @see net.wombatrpgs.mgne.maps.MapThing#postProcessing
	 * (com.badlogic.gdx.assets.AssetManager, int)
	 */
	@Override
	public void postProcessing(AssetManager manager, int pass) {
		super.postProcessing(manager, pass);
		WindowSettings win = MGlobal.window;
		
		boxWidth = win.getViewportWidth();
		boxHeight = (int) (font.getLineHeight() * mdo.lines);
		boxHeight += mdo.marginHeight * 2;
		backer.resizeTo(boxWidth, boxHeight);
		
		bodyFormat.x = mdo.marginWidth;
		if (mdo.anchor != BoxAnchorType.BOTTOM) {
			bodyFormat.y = boxHeight - mdo.marginHeight;
		} else {
			bodyFormat.y = win.getViewportHeight() - mdo.marginHeight;
		}
		bodyFormat.align = HAlignment.LEFT;
		bodyFormat.width = win.getViewportWidth() - mdo.marginWidth * 2;
		bodyFormat.height = boxHeight - mdo.marginHeight * 2;
	}

	/**
	 * @see net.wombatrpgs.mgne.graphics.ScreenGraphic#fadeIn
	 * (net.wombatrpgs.mgne.screen.Screen, float)
	 */
	@Override
	public void fadeIn(Screen screen, float fadeTime) {
		reset();
		super.fadeIn(screen, fadeTime);
	}

	/**
	 * @see net.wombatrpgs.mgne.core.interfaces.Updateable#update(float)
	 */
	@Override
	public void update(float elapsed) {
		super.update(elapsed);
		
		if (currentLines.size() == 0) return;
		if (waiting) return;
		if (allVisible) return;
		
		sinceChar += elapsed;
		boolean playedType = false;
		for (; sinceChar > 1f/mdo.typeSpeed; sinceChar -= 1f/mdo.typeSpeed) {
			visibleChars += 1;
			if (visibleChars > totalLength) {
				for (int i = 0; i < mdo.lines; i += 1) {
					Collections.copy(visibleLines, currentLines);
				}
				allVisible = true;
				return;
			}
			int at = visibleChars;
			int atLine = 0;
			for (atLine = 0; atLine < currentLines.size(); atLine++) {
				String line = currentLines.get(atLine);
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
								currentLines.set(atLine, line.substring(0, line.indexOf("\\n")));
								sinceChar = 0;
								break;
							}
						} else if (Character.isLetter(lastChar) || Character.isDigit(lastChar)) {
							if (!playedType) {
								if (typeSfx != null) typeSfx.play();
								playedType = true;
							}
						}
					}
					visibleLines.set(atLine, newLine);
					break;
				}
			}
			if (atLine == currentLines.size()) {
				visibleChars = totalLength;
			}
		}
	}
	
	/**
	 * Pretty text display. This is the primary input to the textbox. It will
	 * take a string, break it at the nearest word, and display it on the page.
	 * Note that this does not cover cases where the text is longer than the
	 * box can hold; that should be handled in CommandSpeakAll, because the only
	 * time multiple text boxes should be used to display one string is in
	 * cutscenes. This does not add the textbox to the current screen.
	 * @param	text			The hunk of text to display	
	 */
	public void setText(String text) {
		reset();
		Collections.addAll(words, text.split("\\s+"));
		advanceLines(mdo.lines);
	}
	
	/**
	 * Speeds up this box's movement, either by instantly displaying all
	 * characters in the box or by unsetting its most recent wait.
	 */
	public void hurryUp() {
		if (waiting) {
			waiting = false;
			sinceChar = 0;
		} else if (visibleChars < totalLength) {
			sinceChar = Float.MAX_VALUE;
		} else {
			advanceLines(1);
		}
	}
	
	/**
	 * Checks if this text box has finished autotyping chars.
	 * @return				True if this text box is done, false otherwise
	 */
	public boolean isFinished() {
		return allVisible && words.size() == 0;
	}
	
	/**
	 * Resets this text box. Called internally when new text is set.
	 */
	protected void reset() {
		sinceChar = 0;
		visibleChars = 0;
		fadingOut = false;
		allVisible = false;
		waiting = false;
		
		words.clear();
		currentLines.clear();
		visibleLines.clear();
		for (int i = 0; i < mdo.lines; i += 1) {
			currentLines.add("");
			visibleLines.add("");
		}
	}
	
	/**
	 * Moves the line display forward by moving text from remaining to the
	 * current lines category.
	 * @param	toAdvance			How many lines to move (usually 1)
	 */
	protected void advanceLines(int toAdvance) {
		
		// split the words into lines that fit the box
		int lineNo = 0;
		for (; lineNo < toAdvance && words.size() > 0; lineNo += 1) {
			String lastGood = "";
			String test = words.get(0);
			while (!font.isTooLong(bodyFormat, test)) {
				lastGood = test;
				words.remove(0);
				if (words.size() == 0) {
					break;
				}
				test = test + " " + words.get(0);
			}
			currentLines.add(lastGood);
			visibleLines.add("");
			totalLength -= currentLines.get(0).length();
			currentLines.remove(0);
			visibleLines.remove(0);
		}
		
		// fill in empty space if we're advancing past what we have
		for (; lineNo < toAdvance; lineNo += 1) {
			currentLines.add("");
			currentLines.remove(0);
		}
		
		// recalculate total chars to go
		for (String line : currentLines) {
			totalLength += line.length();
		}
		
		// recalculate visible characters
		visibleChars = 0;
		allVisible = false;
		sinceChar = 0;
		for (String line : visibleLines) {
			visibleChars += line.length();
		}
	}

}
