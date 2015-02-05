/**
 *  FontHolder.java
 *  Created on Feb 2, 2013 2:39:25 AM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mgne.ui.text;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import net.wombatrpgs.mgne.core.Constants;
import net.wombatrpgs.mgne.core.MAssets;
import net.wombatrpgs.mgne.core.MGlobal;
import net.wombatrpgs.mgne.core.interfaces.Queueable;
import net.wombatrpgs.mgneschema.ui.FontMDO;

/**
 * Creates and initializes a font for use in cutscenes.
 */
public class FontHolder implements Queueable {
	
	protected FontMDO mdo;
	protected transient BitmapFont font;
	
	/**
	 * Creates a new font holder from data.
	 * @param	mdo				The data to generate from
	 */
	public FontHolder(FontMDO mdo) {
		this.mdo = mdo;
	}
	
	/**
	 * Creates a new font holder from the key to some data.
	 * @param	mdoKey			The key to the data to generate from
	 */
	public FontHolder(String mdoKey) {
		this(MGlobal.data.getEntryFor(mdoKey, FontMDO.class));
	}

	/**
	 * @see net.wombatrpgs.mgne.core.interfaces.Queueable#queueRequiredAssets
	 * (MAssets)
	 */
	@Override
	public void queueRequiredAssets(MAssets manager) {
		manager.load(Constants.FONTS_DIR + mdo.file, BitmapFont.class);
	}

	/**
	 * @see net.wombatrpgs.mgne.core.interfaces.Queueable#postProcessing
	 * (MAssets, int)
	 */
	@Override
	public void postProcessing(MAssets manager, int pass) {
		String filename = Constants.FONTS_DIR+mdo.file;
		if (manager.isLoaded(filename)) {
			font = manager.get(filename, BitmapFont.class);
		} else {
			MGlobal.reporter.warn("Did not load font: " + filename);
		}
	}

	/**
	 * Tells this font to start drawing. This should be called from the render
	 * method of some object that wants to draw text. No camera is needed
	 * because text is drawn straight to the screen (I think?)
	 * @param	batch			The batch to draw in context of
	 * @param 	format			The formatting to use for the text box
	 * @param 	text			The actual text to draw
	 * @param	offY			Additional offset Y, to change lines (in px)
	 */
	public void draw(SpriteBatch batch, TextFormat format, String text, int offY) {
		draw(batch, format, text, 0, offY);
	}
	
	/**
	 * Draws some text with a horizontal offset as well.
	 * @param	batch			The batch to draw with
	 * @param	format			The text box format to draw with
	 * @param	text			The text to draw
	 * @param	offX			The x-offset from textbox format, in pixels
	 * @param	offY			The y-offset from textbox format, in pixels
	 */
	public void draw(SpriteBatch batch, TextFormat format, String text, int offX, int offY) {
		font.setColor(batch.getColor());
		batch.begin();
		TextBounds bounds = font.drawWrapped(batch,
				text, 
				format.x + offX,
				format.y + offY, 
				format.width,
				format.align);
		if (bounds.height > format.height + offY) {
			MGlobal.reporter.warn("A string was oversized: \"" + text + "\"");
		}
		batch.end();
	}
	
	/**
	 * Ugly drawing method. Really only used for debug purposes. Assume the
	 * batch is already drawing.
	 * @param	batch			The batch to draw in context of
	 * @param	text			The text to draw
	 * @param	x				The x to draw at (in px)
	 * @param	y				The y to draw at (in px)
	 */
	public void draw(SpriteBatch batch, String text, int x, int y) {
		font.draw(batch, text, x, y);
	}
	
	/**
	 * Another ugly drawing method to draw a single character. Assumes the batch
	 * is already drawing.
	 * @param	batch			The batch to draw in context of
	 * @param	c				The character to draw glyph of
	 * @param	x				The x to draw at (in px)
	 * @param	y				The y to draw at (in px)
	 */
	public void draw(SpriteBatch batch, char c, int x, int y) {
		font.draw(batch, String.valueOf(c), x, y);
	}
	
	/**
	 * Sets this font's alpha. 1 is full, 0 is transparent. Make sure to set
	 * this to something reasonable when you're done.
	 * @param	a				The new alpha value
	 */
	public void setAlpha(float a) {
		Color c = font.getColor();
		c.a = a;
		font.setColor(c);
	}
	
	/**
	 * Gets the distance between two lines of text, usually used in a text box.
	 * @return					The distance between two lines of text (in px)
	 */
	public float getLineHeight() {
		return font.getLineHeight();
	}
	
	/**
	 * Gets the width of a chunk of text if that text to be rendered.
	 * @param	text			The text to check
	 * @return					How wide that text would be, in px
	 */
	public float getWidth(String text) {
		return font.getBounds(text).width;
	}
	
	/**
	 * Gets the height of a chunk of text if that text to be rendered.
	 * @param	text			The text to check
	 * @return					How high that text would be, in px
	 */
	public float getHeight(String text) {
		return font.getBounds(text).height;
	}
	
	/**
	 * Gets the length of a single character in this font, assuming monspace.
	 * @return					The length of a single character, in px
	 */
	public float getCharWidth() {
		return font.getBounds("a").width;
	}
	
	/**
	 * Determines if a given string is too long to fit in this text box.
	 * @param	format			The text box format we are using
	 * @param	text			The text to check
	 * @return					True if this text would wrap, false otherwise
	 */
	public boolean isTooLong(TextFormat format, String text) {
		return getWidth(text) > format.width;
	}
}
