/**
 *  TiledGridLayer.java
 *  Created on Aug 21, 2014 12:02:38 AM for project mgne
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mgne.maps.layers;

import java.util.ArrayList;
import java.util.List;

import net.wombatrpgs.mgne.core.Constants;
import net.wombatrpgs.mgne.core.MGlobal;
import net.wombatrpgs.mgne.maps.LoadedLevel;
import net.wombatrpgs.mgne.ui.Graphic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

/**
 * Any layer (loaded or generated) based on a Tiled grid layer.
 */
public class TiledGridLayer extends GridLayer {
	
	protected LoadedLevel parent;
	protected List<Glower> glowers;
	protected ShapeRenderer shaper;
	protected transient TiledMapTileLayer layer;

	/**
	 * Creates a new object layer with a parent level and Tiled grid.
	 * @param 	parent			The parent level of the layer
	 * @param 	layer			The underlying layer of this abstraction
	 */
	public TiledGridLayer(LoadedLevel parent, TiledMapTileLayer layer) {
		super(parent, Float.valueOf((String) layer.getProperties().get(Constants.PROPERTY_Z)));
		this.parent = parent;
		this.layer = layer;
		glowers = new ArrayList<TiledGridLayer.Glower>();
		if (getProperty(Constants.PROPERTY_Z) == null) {
			MGlobal.reporter.warn("Layer with no Z exists on map " + parent);
		}
		
		for (int x = 0; x < layer.getWidth(); x += 1) {
			for (int y = 0; y < layer.getHeight(); y += 1) {
				Cell c = layer.getCell(x, y);
				if (c!= null && c.getTile() != null && c.getTile().getProperties().containsKey("glow")) {
					Graphic graphic = new Graphic("res/sprites/",
							c.getTile().getProperties().get("glow").toString());
					assets.add(graphic);
					Glower glow = new Glower(x, y, graphic);
					glowers.add(glow);
				}
			}
		}
		
		shaper = new ShapeRenderer();
	}
	
	/**
	 * Converts from the tileset/id format to global tile id format.
	 * @param	map				The Tiled map that's the parent of this layer
	 * @param	tilesetName		The name of the tileset with the tile
	 * @param	relativeTileID	The ID of the tile within that tileset
	 * @return					The global ID of that tile (gid)
	 */
	public static int relativeToAbsoluteTileID(TiledMap map, String tilesetName, int relativeTileID) {
		TiledMapTileSet tileset = getTilesetByName(map, tilesetName);
		String key = Constants.PROPERTY_FIRST_GID;
		int offset = Integer.valueOf(tileset.getProperties().get(key).toString());
		return relativeTileID + offset;
	}

	/**
	 * Ignores the batch for the most part. Sorry.
	 * @see net.wombatrpgs.mgne.graphics.interfaces.Renderable#render
	 * (com.badlogic.gdx.graphics.g2d.SpriteBatch)
	 */
	@Override
	public void render(SpriteBatch batch) {
		OrthogonalTiledMapRenderer renderer = parent.getRenderer();
		renderer.setView(parent.getScreen().getCamera());
		renderer.getSpriteBatch().begin();
		renderer.renderTileLayer(layer);
		renderer.getSpriteBatch().end();
		
		Gdx.gl.glEnable(GL20.GL_BLEND);
	    Gdx.gl.glBlendFunc(GL20.GL_SRC_COLOR, GL20.GL_ONE_MINUS_SRC_ALPHA);
//		if (layer.getProperties().get("alt") != null) {
//
//			shaper.setProjectionMatrix(parent.getScreen().getCamera().combined);
//			shaper.begin(ShapeType.Filled);
//			shaper.setColor(0, 0, 0, .5f);
//			shaper.rect(0, 0, parent.getWidthPixels(), parent.getHeightPixels());
//			shaper.end();
//		}
		
//		for (Glower glow : glowers) {
//			glow.graphic.renderAt(parent.getScreen().getViewBatch(),
//					glow.x * parent.getTileWidth() + parent.getTileWidth()/2,
//					glow.y * parent.getTileHeight() + parent.getTileHeight()/2);
//		}
	}

	/**
	 * @see net.wombatrpgs.mgne.maps.layers.Layer#isTilePassable(int, int)
	 */
	@Override
	public boolean isTilePassable(int tileX, int tileY) {
		if (z >= 1) return true;
		if (getTileID(tileX, tileY) == 0) {
			// there is no tile at this location
			return z > 0;
		} else {
			return (getTileProperty(tileX, tileY, Constants.PROPERTY_IMPASSABLE) == null);
		}
	}

	/**
	 * @see net.wombatrpgs.mgne.maps.layers.GridLayer#hasPropertyAt
	 * (int, int, java.lang.String)
	 */
	@Override
	public boolean hasPropertyAt(int tileX, int tileY, String property) {
		if (getTileID(tileX, tileY) == 0) {
			// there is no tile at this location
			return false;
		} else {
			return (getTileProperty(tileX, tileY, property) != null);
		}
	}

	/**
	 * @see net.wombatrpgs.mgne.maps.layers.Layer#hasTileAt(int, int)
	 */
	@Override
	public boolean hasTileAt(int tileX, int tileY) {
		return getTileID(tileX, tileY) != 0;
	}

	/**
	 * @see net.wombatrpgs.mgne.maps.layers.GridLayer#getTerrainAt(int, int)
	 */
	@Override
	public int getTerrainAt(int tileX, int tileY) {
		return getTileID(tileX, tileY);
	}
	
	/**
	 * @see net.wombatrpgs.mgne.graphics.interfaces.Disposable#dispose()
	 */
	@Override
	public void dispose() {
		// the layer will be disposed as part of the map, maybe?
		shaper.dispose();
	}

	/**
	 * Finds the tileset with the given name in the list of tilesets.
	 * @param	map				The map to check on
	 * @param	tilesetName		The name of the tileset to fetch
	 * @return					That tileset
	 */
	protected static TiledMapTileSet getTilesetByName(TiledMap map, String tilesetName) {
		for (TiledMapTileSet tileset : map.getTileSets()) {
			if (tileset.getName().equals(tilesetName)) {
				return tileset;
			}
		}
		MGlobal.reporter.warn("No tileset found with name " + tilesetName);
		return null;
	}
	
	/**
	 * @see net.wombatrpgs.mgne.maps.layers.GridLayer#getProperty(java.lang.String)
	 */
	@Override
	public String getProperty(String key) {
		Object val = layer.getProperties().get(key);
		return (val == null) ? null : val.toString();
	}

	/**
	 * Extracts the property from a tile at a given cell.
	 * @param 	tileX			The x-coord of the tile to get from (in tiles)
	 * @param 	tileY			The y-coord of the tile to get from (in tiles)
	 * @param 	key				The key of the property to extract
	 * @return					The tile's property at that cell
	 */
	protected String getTileProperty(int tileX, int tileY, String key) {
		Cell cell = layer.getCell(tileX, tileY);
		if (cell == null) return null;
		TiledMapTile tile = cell.getTile();
		Object val = tile.getProperties().get(key);
		return (val == null) ? null : val.toString();
	}
	
	/**
	 * Extracts the tile ID of a given cell.
	 * @param 	tileX			The x-coord of the tile to get from (in tiles)
	 * @param 	tileY			The y-coord of the tile to get from (in tiles)
	 * @return					The tile's ID at that cell
	 */
	protected int getTileID(int tileX, int tileY) {
		Cell cell = layer.getCell(tileX, tileY);
		return (cell == null) ? 0 : cell.getTile().getId();
	}
	
	/**
	 * Converts from the tileset/id format to global tile id format. Assumes
	 * that the current map is the one to use.
	 * @param	tilesetName		The name of the tileset with the tile
	 * @param	relativeTileID	The ID of the tile within that tileset
	 * @return					The global ID of that tile (gid)
	 */
	protected int relativeToAbsoluteTileID(String tilesetName, int relativeTileID) {
		return relativeToAbsoluteTileID(parent.getMap(), tilesetName, relativeTileID);
	}
	
	private class Glower {
		public int x, y;
		public Graphic graphic;
		public Glower(int x, int y, Graphic g) {
			this.x = x;
			this.y = y;
			this.graphic = g;
		}
	}
}
