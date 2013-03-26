/**
 *  FontHolder.java
 *  Created on Feb 2, 2013 2:39:25 AM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.rainfall.ui.text;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import net.wombatrpgs.rainfall.core.Constants;
import net.wombatrpgs.rainfall.core.Queueable;
import net.wombatrpgs.rainfall.core.RGlobal;
import net.wombatrpgs.rainfallschema.ui.FontMDO;

/**
 * Creates and initializes a font for use in cutscenes.
 */
public class FontHolder implements Queueable {
	
	protected FontMDO mdo;
	protected BitmapFont font;
	
	public FontHolder(FontMDO mdo) {
		this.mdo = mdo;
	}

	/**
	 * @see net.wombatrpgs.rainfall.core.Queueable#queueRequiredAssets
	 * (com.badlogic.gdx.assets.AssetManager)
	 */
	@Override
	public void queueRequiredAssets(AssetManager manager) {
		RGlobal.assetManager.load(Constants.FONTS_DIR + mdo.file, BitmapFont.class);
	}

	/**
	 * @see net.wombatrpgs.rainfall.core.Queueable#postProcessing
	 * (com.badlogic.gdx.assets.AssetManager, int)
	 */
	@Override
	public void postProcessing(AssetManager manager, int pass) {
		String filename = Constants.FONTS_DIR+mdo.file;
		if (RGlobal.assetManager.isLoaded(filename)) {
			font = RGlobal.assetManager.get(filename, BitmapFont.class);
		} else {
			RGlobal.reporter.warn("Did not load font: " + filename);
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
	public void draw(SpriteBatch batch, TextBoxFormat format, String text, int offY) {
		batch.begin();
		TextBounds bounds = font.drawWrapped(batch, text, 
				format.x, format.y + offY, 
				format.width, format.align);
		if (bounds.height > format.height) {
			RGlobal.reporter.warn("A string was oversized: \"" + text + "\"");
		}
		batch.end();
	}
	
	/**
	 * Gets the distance between two lines of text, usually used in a text box.
	 * @return					The distance between two lines of text (in px)
	 */
	public float getLineHeight() {
		return font.getLineHeight();
	}
}
