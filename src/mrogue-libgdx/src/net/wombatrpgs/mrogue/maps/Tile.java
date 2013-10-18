/**
 *  TileType.java
 *  Created on Oct 4, 2013 12:57:29 AM for project mrogue-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mrogue.maps;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import net.wombatrpgs.mrogue.core.Constants;
import net.wombatrpgs.mrogue.core.MGlobal;
import net.wombatrpgs.mrogue.core.Queueable;
import net.wombatrpgs.mrogueschema.maps.data.TileMDO;
import net.wombatrpgs.mrogueschema.maps.data.TileType;

/**
 * A flyweight-style tile. The idea is that a gridlayer can just keep track of a
 * 2D array of IDs, and then look up tiles by ID rather than having a new
 * instance of a tile get created for every single slot in a grid layer. This
 * should cover things like passability, appearance, etc.
 */
public class Tile implements Queueable {
	
	protected TileMDO mdo;
	protected TileType type;
	protected TextureRegion appearance;
	protected String filename;
	
	/**
	 * Creates a tile type from data.
	 * @param	mdo				The data to create tile type from
	 * @param	type			The inferred type data about this tile
	 */
	public Tile(TileMDO mdo, TileType type) {
		this.mdo = mdo;
		this.type = type;
		if (MapThing.mdoHasProperty(mdo.appearance)) {
			filename = Constants.TILES_DIR + mdo.appearance;
		}
	}
	
	/** @return True if characters can pass this tile */
	public boolean isPassable() { 
		return type.isPassable();
	}
	
	/** @return True if this tile is see-through */
	public boolean isTransparent() {
		return type.isTransparent();
	}
	
	/** @return The texture representing this tile */
	public TextureRegion getAppearance() {
		return appearance;
	}
	
	/** @return The mdo data of this tile type */
	public TileMDO getMDO() {
		return mdo;
	}

	/**
	 * @see net.wombatrpgs.mrogue.core.Queueable#queueRequiredAssets
	 * (com.badlogic.gdx.assets.AssetManager)
	 */
	@Override
	public void queueRequiredAssets(AssetManager manager) {
		if (MapThing.mdoHasProperty(mdo.appearance)) {
			manager.load(filename, Texture.class);
		}
	}

	/** @see net.wombatrpgs.mrogue.core.Queueable#postProcessing
	 * (com.badlogic.gdx.assets.AssetManager, int) */
	@Override
	public void postProcessing(AssetManager manager, int pass) {
		if (!MapThing.mdoHasProperty(mdo.appearance)) return;
		if (!MGlobal.assetManager.isLoaded(filename)) {
			MGlobal.reporter.err("No tile loaded for file:" + mdo);
			return;
		}
		Texture sheet = manager.get(filename, Texture.class);
		appearance = new TextureRegion(sheet);
	}
	
	/**
	 * Renders a copy of this tile at the given position. This method is baad
	 * because you shouldn't be rendering each tile every frame! The given
	 * coords are for the upper left of the tile.
	 * @param	camera			The camera to render with
	 * @param	batch			The sprite batch to draw with
	 * @param	x				The x-coord (in pixels) to start rendering at
	 * @param	y				The y-coord (in pixels) to start rendering at
	 */
	public void renderLocal(OrthographicCamera camera, SpriteBatch batch, float x, float y) {
		if (appearance == null) return;
		float atX = x;
		float atY = y;
		batch.draw(
				appearance,
				atX, 
				atY,
				appearance.getRegionWidth() / 2,
				appearance.getRegionHeight() / 2, 
				appearance.getRegionWidth(),
				appearance.getRegionHeight(), 
				1f,
				1f, 
				0);	
	}

}
