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

import com.badlogic.gdx.graphics.g2d.BitmapFont.HAlignment;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import net.wombatrpgs.mgne.core.MAssets;
import net.wombatrpgs.mgne.core.MGlobal;
import net.wombatrpgs.mgne.core.interfaces.FinishListener;
import net.wombatrpgs.mgne.graphics.ScreenGraphic;
import net.wombatrpgs.mgne.maps.MapThing;
import net.wombatrpgs.mgne.screen.Screen;
import net.wombatrpgs.mgne.screen.WindowSettings;
import net.wombatrpgs.mgne.ui.Nineslice;
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
	protected TextFormat bodyFormat, nameFormat;
	protected float sinceChar;
	protected int totalLength;
	protected int visibleChars;
	protected int boxWidth, boxHeight;
	protected boolean waiting;
	protected boolean allVisible;
	
	protected FinishListener outListener;
	protected float expandTime, elapsedExpand;
	protected boolean expandingIn, expandingOut;
	protected int expandBackerHeight;
	
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
		this.bodyFormat = new TextFormat();
		this.nameFormat = new TextFormat();
		this.currentLines = new ArrayList<String>();
		this.visibleLines = new ArrayList<String>();
		this.words = new ArrayList<String>();
		this.waiting = false;
		this.fadingOut = false;
		
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
	 * @see net.wombatrpgs.mgne.graphics.ScreenGraphic#coreRender
	 * (com.badlogic.gdx.graphics.g2d.SpriteBatch)
	 */
	@Override
	public void coreRender(SpriteBatch batch) {
		// backer first
		if (backer != null) {
			int atY = 0;
			if (mdo.anchor == BoxAnchorType.BOTTOM) {
				atY = MGlobal.window.getViewportHeight() - boxHeight;
			}
			atY += (boxHeight - expandBackerHeight) / 2;
			atY += 1;
			backer.renderAt(getBatch(), 0, atY);
		}
		
		// now for the font
		if (!expandingIn && !expandingOut) {
			font.setAlpha(currentColor.a);
			for (int i = 0; i < visibleLines.size(); i++) {
				font.draw(getBatch(), bodyFormat,
						visibleLines.get(i), (int) (font.getLineHeight() * -i));
			}
			font.setAlpha(1);
		}
	}

	/**
	 * @see net.wombatrpgs.mgne.maps.MapThing#postProcessing
	 * (MAssets, int)
	 */
	@Override
	public void postProcessing(MAssets manager, int pass) {
		super.postProcessing(manager, pass);
		WindowSettings win = MGlobal.window;
		
		boxWidth = win.getViewportWidth();
		boxHeight = (int) (font.getLineHeight() * mdo.lines);
		boxHeight += mdo.marginTop + mdo.marginBottom;
		if (backer != null) {
			backer.resizeTo(boxWidth, boxHeight+2);
			expandBackerHeight = boxHeight+2;
		}
		
		bodyFormat.x = mdo.marginWidth;
		if (mdo.anchor != BoxAnchorType.BOTTOM) {
			bodyFormat.y = win.getViewportHeight() - mdo.marginTop;
		} else {
			bodyFormat.y = win.getViewportHeight() - mdo.marginTop;
		}
		bodyFormat.align = HAlignment.LEFT;
		bodyFormat.width = win.getViewportWidth() - mdo.marginWidth * 2;
		bodyFormat.height = boxHeight - mdo.marginTop - mdo.marginBottom;
	}

	/**
	 * @see net.wombatrpgs.mgne.graphics.ScreenGraphic#fadeIn
	 * (net.wombatrpgs.mgne.screen.Screen, float)
	 */
	@Override
	public void fadeIn(Screen screen, float fadeTime) {
		backer.resizeTo(boxWidth, boxHeight+2);
		expandBackerHeight = boxHeight+2;
		reset();
		super.fadeIn(screen, fadeTime);
	}

	/**
	 * @see net.wombatrpgs.mgne.core.interfaces.Updateable#update(float)
	 */
	@Override
	public void update(float elapsed) {
		super.update(elapsed);
		
		if (expandingIn || expandingOut) {
			elapsedExpand += elapsed;
			float r = elapsedExpand / expandTime;
			if (r > 1) {
				if (expandingOut) {
					parent.removeChild(this);
					outListener.onFinish();
				}
				expandingIn = false;
				expandingOut = false;
				if (backer != null) {
					expandBackerHeight = boxHeight+2;
					backer.resizeTo(boxWidth, boxHeight+2);
				}
			} else {
				if (expandingOut) r = 1f - r;
				if (backer != null) {
					int heightGain = (boxHeight+2) - backer.getBorderHeight()*2;
					int backerHeight = (int) (backer.getBorderHeight()*2 + heightGain*r);
					backerHeight -= backerHeight % 4;
					if (backerHeight != expandBackerHeight) {
						expandBackerHeight = backerHeight;
						backer.resizeTo(boxWidth, backerHeight);
					}
				}
			}
		}
		
		if (currentLines.size() == 0) return;
		if (waiting) return;
		if (expandingIn || expandingOut) return;
		if (allVisible) {
			if (waitOnNewline()) {
				return;
			} else if (words.size() > 0) {
				advanceLines(1);
			}
		}
		
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
								if (MapThing.mdoHasProperty(mdo.typeRefKey)) {
									MGlobal.audio.playSFX(mdo.typeRefKey);
								}
							} else if (special == 'n') {
								newLine = line.substring(0, line.indexOf("\\n"));
								currentLines.set(atLine, newLine);
								visibleLines.set(atLine, newLine);
								totalLength -= 2;
								if (waitOnNewline()) {
									waiting = true;
									sinceChar = 0;
									break;
								} else {
									// advanceLines(1);
								}
							}
						} else if (Character.isLetter(lastChar) || Character.isDigit(lastChar)) {
							if (!playedType) {
								if (MapThing.mdoHasProperty(mdo.typeRefKey)) {
									MGlobal.audio.playSFX(mdo.typeRefKey);
								}
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
	 * This will scroll the textbox rather than creating multiple boxes like a
	 * cutscene. This does not add the textbox to the current screen.
	 * @param	text			The hunk of text to display	
	 */
	public void showText(String text) {
		reset();
		Collections.addAll(words, text.split("\\s+"));
		advanceLines(mdo.lines);
	}
	
	/**
	 * Speeds up this box's movement, either by instantly displaying all
	 * characters in the box or by unsetting its most recent wait.
	 */
	public void hurryUp() {
		if (expandingIn || expandingOut) {
			return;
		}
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
	 * Animates this textbox expanding in, similar to a fadein.
	 * @param	screen			The screen the box will expand onto
	 * @param	expandTime		The time it will take to expand, in seconds
	 */
	public void expandIn(Screen screen, float expandTime) {
		this.parent = screen;
		this.expandTime = expandTime;
		reset();
		expandingIn = true;
		elapsedExpand = 0;
		expandBackerHeight = -1;
		update(0);
		if (!screen.containsChild(this)) {
			screen.addChild(this);
		}
	}
	
	/**
	 * Animates this textbox expanding out (closing) like a fadeout.
	 * @param	expandTime		The time it will take to expand, in seconds
	 */
	public void expandOut(float expandTime) {
		expandOut(expandTime, null);
	}
	
	/**
	 * Animates this textbox expanding out (closing) like a fadeout.
	 * @param	expandTime		The time it will take to expand, in seconds
	 * @param	outListener		The listener for on complete, or null
	 */
	public void expandOut(float expandTime, FinishListener outListener) {
		this.expandTime = expandTime;
		this.outListener = outListener;
		expandingIn = false;
		expandingOut = true;
		elapsedExpand = 0;
	}
	
	/**
	 * Resets this text box. Called internally when new text is set.
	 */
	protected void reset() {
		sinceChar = 0;
		visibleChars = 0;
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
				if (test.equals("\n")) {
					words.remove(0);
					break;
				}
				lastGood = test;
				words.remove(0);
				if (words.size() == 0) {
					break;
				}
				test = test + " " + words.get(0);
				if (words.get(0).equals("\n") && !waitOnNewline()) {
					lastGood = test.substring(0, test.indexOf("\n"));
					words.remove(0);
					break;
				}
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
	
	/**
	 * Does this text box pause when it encounters a new line? Defaults to yes.
	 * @return					True if should pause on newline, false otherwise
	 */
	protected boolean waitOnNewline() {
		return true;
	}

}
