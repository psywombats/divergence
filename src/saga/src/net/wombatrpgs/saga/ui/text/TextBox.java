/**
 *  TextBox.java
 *  Created on Feb 2, 2013 3:47:40 AM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.saga.ui.text;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont.HAlignment;

import net.wombatrpgs.saga.core.SGlobal;
import net.wombatrpgs.saga.graphics.ScreenDrawable;
import net.wombatrpgs.saga.io.audio.SoundObject;
import net.wombatrpgs.saga.maps.MapThing;
import net.wombatrpgs.saga.screen.WindowSettings;
import net.wombatrpgs.saga.ui.Nineslice;
import net.wombatrpgs.sagaschema.audio.SoundMDO;
import net.wombatrpgs.sagaschema.ui.NinesliceMDO;
import net.wombatrpgs.sagaschema.ui.TextBoxMDO;
import net.wombatrpgs.sagaschema.ui.data.AnchorType;

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
public class TextBox extends ScreenDrawable {
	
	protected static final float FADE_IN_TIME = .5f;
	
	protected TextBoxMDO mdo;
	protected FontHolder font;
	
	protected List<String> lines;
	protected List<String> visibleLines;
	
	protected Nineslice backer;
	protected TextBoxFormat bodyFormat, nameFormat;
	protected SoundObject typeSfx;
	protected float sinceChar;
	protected int visibleChars;
	protected int totalLength;
	protected int boxHeight;
	protected boolean waiting;
	
	/**
	 * Creates a new text box from data. Does not deal with the loading of its
	 * font's assets.
	 * @param 	mdo				The MDO to create from
	 * @param 	font			The font to use in rendering, can change
	 */
	public TextBox(TextBoxMDO mdo, FontHolder font) {
		super(1);
		this.mdo = mdo;
		this.font = font;
		this.visibleChars = 0;
		this.sinceChar = 0;
		this.bodyFormat = new TextBoxFormat();
		this.nameFormat = new TextBoxFormat();
		this.visibleLines = new ArrayList<String>();
		this.waiting = false;
		
		if (MapThing.mdoHasProperty(mdo.typeSfx)) {
			typeSfx = new SoundObject(SGlobal.data.getEntryFor(mdo.typeSfx, SoundMDO.class));
			assets.add(typeSfx);
		}
		if (MapThing.mdoHasProperty(mdo.nineslice)) {
			backer = new Nineslice(SGlobal.data.getEntryFor(mdo.nineslice, NinesliceMDO.class));
			assets.add(backer);
		}
		assets.add(font);
	}
	
	/**
	 * @see net.wombatrpgs.saga.maps.MapThing#render
	 * (com.badlogic.gdx.graphics.OrthographicCamera)
	 */
	@Override
	public void render(OrthographicCamera camera) {
		super.render(camera);
		
		// backer first
		if (backer != null) {
			int atY = 0;
			if (mdo.anchor == AnchorType.BOTTOM) {
				atY = SGlobal.window.getHeight() - boxHeight;
			}
			backer.renderAt(getBatch(), 0, atY);
		}
		
		// now for the font
		for (int i = 0; i < visibleLines.size(); i++) {
			font.draw(getBatch(), bodyFormat,
					visibleLines.get(i), (int) (font.getLineHeight() * -i));
		}
		font.setAlpha(1);
	}

	/**
	 * @see net.wombatrpgs.saga.maps.MapThing#postProcessing
	 * (com.badlogic.gdx.assets.AssetManager, int)
	 */
	@Override
	public void postProcessing(AssetManager manager, int pass) {
		super.postProcessing(manager, pass);
		WindowSettings win = SGlobal.window;
		
		boxHeight = (int) (font.getLineHeight() * mdo.lines);
		boxHeight += mdo.marginHeight * 2;
		backer.resizeTo(win.getWidth(), boxHeight);
		
		bodyFormat.x = mdo.marginWidth;
		if (mdo.anchor != AnchorType.BOTTOM) {
			bodyFormat.y = boxHeight - mdo.marginHeight;
		} else {
			bodyFormat.y = win.getHeight() - mdo.marginHeight;
		}
		bodyFormat.align = HAlignment.LEFT;
		bodyFormat.width = win.getWidth() - mdo.marginWidth * 2;
		bodyFormat.height = win.getHeight() - mdo.marginHeight * 2;
	}

	/**
	 * @see net.wombatrpgs.saga.maps.objects.Picture#update(float)
	 */
	@Override
	public void update(float elapsed) {
		super.update(elapsed);
		
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
								if (typeSfx != null) typeSfx.play();
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
	 * Pretty text display. This is the primary input to the textbox. It will
	 * take a string, break it at the nearest word, and display it on the page.
	 * Note that this does not cover cases where the text is longer than the
	 * box can hold; that should be handled in CommandSpeakAll, because the only
	 * time multiple text boxes should be used to display one string is in
	 * cutscenes.
	 * @param	text			The hunk of text to display	
	 */
	public void show(String text) {
		// it turns out that using the built-in libgdx text display is the
		// easiest way to do this, just have to make sure the bounds of the
		// text box are set properly
		setText(text);
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
	 * Sets the internal text to be displayed at this textbox. Nothing fancy.
	 * This only displays one line so you probably don't want to use this.
	 * @param 	text			The text to be displayed
	 */
	protected void setText(String text) {
		this.lines = new ArrayList<String>();
		lines.add(text);
	}

}
