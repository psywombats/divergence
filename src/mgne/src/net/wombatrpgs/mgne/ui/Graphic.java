/**
 *  Graphic.java
 *  Created on Feb 2, 2013 4:06:15 AM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mgne.ui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import net.wombatrpgs.mgne.core.Constants;
import net.wombatrpgs.mgne.core.MAssets;
import net.wombatrpgs.mgne.graphics.interfaces.PosRenderable;

/**
 * Single-frame graphics instance. Not meant to appear on its own, really.
 * Animations have their own version of this. This class is dumb.
 */
public class Graphic implements PosRenderable {
	
	protected transient Texture texture;
	protected transient TextureRegion appearance;
	protected String filename;
	protected int width, height;
	
	/**
	 * Creates a new graphic from file data. Width and height are assumed to be
	 * the same as the texture. Although this might not always be accurate, in
	 * 90% of cases it doesn't matter. The other 10%, use the setters or
	 * constructors.
	 * @param 	fileName		The name of the file (in UI) with the picture
	 */
	public Graphic(String fileName) {
		this(Constants.UI_DIR, fileName);
	}
	
	/**
	 * For creating a non-UI graphic.
	 * @param	dir				The dir to load from
	 * @param	filename		The file to load
	 */
	public Graphic(String dir, String filename) {
		this.filename = dir + filename;
		this.width = -1;
		this.height = -1;
	}

	/**
	 * @see net.wombatrpgs.mgne.core.interfaces.Queueable#queueRequiredAssets
	 * (MAssets)
	 */
	@Override
	public void queueRequiredAssets(MAssets manager) {
		manager.load(filename, Texture.class);
	}

	/**
	 * @see net.wombatrpgs.mgne.core.interfaces.Queueable#postProcessing
	 * (MAssets, int)
	 */
	@Override
	public void postProcessing(MAssets manager, int pass) {
		texture = manager.get(filename, Texture.class);
		if (width <= 0) width = texture.getWidth();
		if (height <= 0) height = texture.getHeight();
		appearance = new TextureRegion(texture, 0, 0, width, height);
	}

	/** @see net.wombatrpgs.mgne.graphics.interfaces.Boundable#getWidth() */
	@Override public int getWidth() { return width; }

	/** @see net.wombatrpgs.mgne.graphics.interfaces.Boundable#getHeight() */
	@Override public int getHeight() { return height; }

	/** @param width The width to set as the base texture width, in px */
	public void setTextureWidth(int width) { this.width = width; }
	
	/** @param height The height to set as the base texture height, in px */
	public void setTextureHeight(int height) { this.height = height; }
	
	/**
	 * Returns the renderable portion of this image, if you want to be a dick
	 * about it and render it yourself
	 * @return					The preloaded texture region appearance
	 */
	public TextureRegion getGraphic() {
		return appearance;
	}
	
	/**
	 * Returns the raw texture behind the graphic. This is only really useful
	 * if you want to do weird things that texture regions can't do, like use
	 * this thing as an alpha map.
	 * @return					The loaded texture
	 */
	public Texture getTexture() {
		return texture;
	}

	/**
	 * @see net.wombatrpgs.mgne.graphics.interfaces.PosRenderable#renderAt
	 * (com.badlogic.gdx.graphics.g2d.SpriteBatch, float, float)
	 */
	@Override
	public void renderAt(SpriteBatch batch, float x, float y) {
		batch.begin();
		batch.draw(appearance, x, y, getWidth(), getHeight());
		batch.end();
	}

}
