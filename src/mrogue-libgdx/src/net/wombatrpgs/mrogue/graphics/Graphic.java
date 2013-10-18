/**
 *  Graphic.java
 *  Created on Feb 2, 2013 4:06:15 AM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mrogue.graphics;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import net.wombatrpgs.mrogue.core.Constants;
import net.wombatrpgs.mrogue.core.Queueable;

/**
 * Single-frame graphics instance. Not meant to appear on its own, really.
 * Animations have their own version of this. This class is dumb.
 */
public class Graphic implements Queueable {
	
	protected Texture texture;
	protected TextureRegion appearance;
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
	 * @see net.wombatrpgs.mrogue.core.Queueable#queueRequiredAssets
	 * (com.badlogic.gdx.assets.AssetManager)
	 */
	@Override
	public void queueRequiredAssets(AssetManager manager) {
		manager.load(filename, Texture.class);
	}

	/**
	 * @see net.wombatrpgs.mrogue.core.Queueable#postProcessing
	 * (com.badlogic.gdx.assets.AssetManager, int)
	 */
	@Override
	public void postProcessing(AssetManager manager, int pass) {
		texture = manager.get(filename, Texture.class);
		if (width <= 0) width = texture.getWidth();
		if (height <= 0) height = texture.getHeight();
		appearance = new TextureRegion(texture, 0, 0, width, height);
	}
	
	/** @return The basic width of this graphic (in px) */
	public int getWidth() { return this.width; }
	
	/** @return The basic height of this graphic (in px) */
	public int getHeight() { return this.height; }
	
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
	 * Renders itself at a specific location.
	 * @param 	batch			The batch to render the graphic as part of
	 * @param 	x				The x-coord to render at (in px)
	 * @param 	y				The y-coord to render at (in px)
	 */
	public void renderAt(SpriteBatch batch, float x, float y) {
		renderAt(batch, x, y, 1, 1);
	}
	
	/**
	 * Renders itself at a specific location.
	 * @param 	batch			The batch to render the graphic as part of
	 * @param 	x				The x-coord to render at (in px)
	 * @param 	y				The y-coord to render at (in px)
	 * @param	scaleX			The scale for width, 1 is standard
	 * @param	scaleY			The scale for height, 1 is standard
	 */
	public void renderAt(SpriteBatch batch, float x, float y, float scaleX, float scaleY) {
		batch.begin();
		batch.draw(appearance, x, y, getWidth() * scaleX, getHeight() * scaleY);
		batch.end();
	}

}
