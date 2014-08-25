/**
 *  CeilingLayer.java
 *  Created on Aug 21, 2014 12:09:50 AM for project mgne
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.saga.maps;

import net.wombatrpgs.mgne.core.Avatar;
import net.wombatrpgs.mgne.core.Constants;
import net.wombatrpgs.mgne.core.MAssets;
import net.wombatrpgs.mgne.core.MGlobal;
import net.wombatrpgs.mgne.core.interfaces.Updateable;
import net.wombatrpgs.mgne.maps.Level;
import net.wombatrpgs.mgne.maps.LoadedLevel;
import net.wombatrpgs.mgne.maps.TiledMapObject;
import net.wombatrpgs.mgne.maps.layers.TiledGridLayer;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.badlogic.gdx.math.Polygon;

/**
 * A generated Tiled layer that acts as a retractable ceiling.
 */
public class CeilingLayer extends TiledGridLayer implements Updateable {
	
	protected static final float TILE_DEPLOY_TIME = .075f;	// in s
	protected static final String KEY_ROOF_ID = "roofID";
	protected static final String KEY_ROOF_TILESET = "roofTileset";
	protected static final String KEY_FIRST_GID = "firstgid";
	protected static final int MASK_CLEAR = 0xE0000000;
	
	protected enum DeployState {
		DEPLOYED,
		DEPLOYING,
		RETRACTING,
		RETRACTED,
	};
	
	protected TiledMapObject event;
	protected DeployState state;
	protected Polygon polygon;
	protected transient Cell roof, empty;
	protected int currentRadius;
	protected float sinceStart;

	/**
	 * Creates a ceiling layer based on a ceiling event.
	 * @param	event			The ceiling event creating the ceiling layer
	 * @param	polygon			The polygon defining this layer
	 */
	public CeilingLayer(TiledMapObject event, Polygon polygon) {
		super(extractParent(event), generateLayer(event));
		this.event = event;
		this.polygon = polygon;
		generateCells();
		state = DeployState.DEPLOYED;
	}
	
	/**
	 * @see net.wombatrpgs.mgne.core.interfaces.Updateable#update(float)
	 */
	@Override
	public void update(float elapsed) {
		if (state == DeployState.RETRACTING || state == DeployState.DEPLOYING) {
			sinceStart += elapsed;
			int radius = (int) Math.ceil((1f/TILE_DEPLOY_TIME) * sinceStart);
			if (radius == 0) {
				radius += .1f;
			}
			if (state == DeployState.RETRACTING) {
				radius *= -1;
			}
			if (radius != currentRadius) {
				setRadius(radius);
				if (Math.abs(radius) >= getVisionRadius()) {
					if (state == DeployState.RETRACTING) {
						instantRetract();
					} else if (state == DeployState.DEPLOYING) {
						instantDeploy();
					}
					resumeHero();
				}
			}
		}
	}

	/**
	 * We only override this because our transient grid layer will have
	 * disappeared when this layer is reloaded from memory. The idea is that
	 * this is only called after deserializion.
	 * @see net.wombatrpgs.mgne.core.AssetQueuer#postProcessing
	 * (net.wombatrpgs.mgne.core.MAssets, int)
	 */
	@Override
	public void postProcessing(MAssets manager, int pass) {
		super.postProcessing(manager, pass);
		this.layer = generateLayer(event);
		generateCells();
	}

	/**
	 * @see net.wombatrpgs.mgne.maps.layers.TiledGridLayer#render(com.badlogic.gdx.graphics.g2d.SpriteBatch)
	 */
	@Override
	public void render(SpriteBatch batch) {
		// TODO Auto-generated method stub
		super.render(batch);
	}

	/**
	 * @see net.wombatrpgs.mgne.maps.layers.TiledGridLayer#isTilePassable(int, int)
	 */
	@Override
	public boolean isTilePassable(int tileX, int tileY) {
		return true;
	}

	/**
	 * Removes the ceiling so that the non-covered areas are covered by ceiling
	 * tile, but the player can see into this room.
	 */
	public void retract() {
		sinceStart = 0;
		state = DeployState.RETRACTING;
		suspendHero();
	}
	
	/**
	 * Deploys the ceiling so that non-covered areas are clear and the room
	 * covered by this ceiling is covered by ceiling tile.
	 */
	public void deploy() {
		sinceStart = 0;
		state = DeployState.DEPLOYING;
		suspendHero();
	}
	
	/**
	 * Performs a retraction without a smooth transition.
	 */
	public void instantRetract() {
		setRadius(-10000);
		state = DeployState.RETRACTED;
	}
	
	/**
	 * Performs a deployment without a smooth transition.
	 */
	public void instantDeploy() {
		setRadius(10000);
		state = DeployState.DEPLOYED;
	}
	
	/**
	 * Extracts the loaded level parent of a ceiling event.
	 * @param	event			The event to extract from
	 * @return					The loaded level that event is from
	 */
	protected static LoadedLevel extractParent(TiledMapObject event) {
		return event.getLevel();
	}
	
	/**
	 * Generates the ceiling layer from a defining event.
	 * @param	event			The event to generate from
	 * @return					The ceiling represented by that event
	 */
	protected static TiledMapTileLayer generateLayer(TiledMapObject event) {
		LoadedLevel parent = event.getLevel();
		TiledMapTileLayer layer = new TiledMapTileLayer(
				parent.getWidth(), parent.getHeight(),
				parent.getTileWidth(), parent.getTileHeight());
		layer.getProperties().put(Constants.PROPERTY_Z, "2");
		return layer;
	}
	
	/**
	 * Prevent that idiot hero from moving during animation.
	 */
	protected void suspendHero() {
		MGlobal.getHero().pause(true);
	}
	
	/**
	 * Passes control back off to the hero.
	 */
	protected void resumeHero() {
		MGlobal.getHero().pause(false);
	}
	
	/**
	 * Calculates the maximum number of visible tiles away.
	 * @return					The maximum number of tiles visible from center
	 */
	protected int getVisionRadius() {
		Level map = event.getLevel();
		int horiz = (int) Math.ceil(map.getWidthPixels() / ((float) map.getTileWidth() * 2f)) + 1;
		int vert = (int) Math.ceil(map.getHeightPixels() / ((float) map.getTileHeight() * 2f)) + 1;
		return (horiz > vert) ? horiz : vert;
	}
	
	/**
	 * Populates the transient cell members.
	 */
	protected void generateCells() {
		empty = null;	// as in, do not display
		roof = new Cell();
		Integer id = Integer.valueOf(event.getString(KEY_ROOF_ID));
		String tilesetName = event.getString(KEY_ROOF_TILESET);
		if (id == null || tilesetName == null) {
			MGlobal.reporter.err("No id or tileset name on roof event: " + event);
			return;
		}
		for (TiledMapTileSet tileset : event.getLevel().getMap().getTileSets()) {
			if (tileset.getName().equals(tilesetName)) {
				int offset = Integer.valueOf(tileset.getProperties().get(KEY_FIRST_GID).toString());
				roof.setTile(tileset.getTile(id + offset));
				break;
			}
		}
	}
	
	/**
	 * Writes ceiling tiles on the layer where appropriate, in a circular
	 * pattern around the hero where intersecting with the definining ceiling
	 * polygon. Positive radii correspond to a deployed roof, over the defined
	 * room, where negative radii are a retracted roof, not over the room but
	 * over the rest of the map instead. A radius of 0 would be no roof tiles.
	 * @param	radius			The radius to draw tiles out to
	 */
	protected void setRadius(int radius) {
		currentRadius = radius;
		Level map = event.getLevel();
		int absRadius = Math.abs(radius);
		if (radius >= getVisionRadius()) {
			for (int col = 0; col < layer.getWidth(); col += 1) {
				for (int row = 0; row < layer.getHeight(); row += 1) {
					int x = col * map.getTileWidth() + map.getTileWidth() / 2;
					int y = row * map.getTileHeight() + map.getTileHeight() / 2;
					if (polygon.contains(x, y) ^ (radius < 0)) {
						layer.setCell(col, row, roof);
					} else {
						layer.setCell(col, row, empty);
					}
				}
			}
		} else {
			Avatar hero = MGlobal.getHero();
			int heroCol = hero.getTileX();
			int heroRow = hero.getTileY();
			for (int col = 0; col < layer.getWidth(); col += 1) {
				for (int row = 0; row < layer.getHeight(); row += 1) {
					int x = col * map.getTileWidth() + map.getTileWidth() / 2;
					int y = row * map.getTileHeight() + map.getTileHeight() / 2;
					boolean inRange = Math.abs(col - heroCol) < absRadius &&
							Math.abs(row - heroRow) < absRadius;
					if (inRange ^ polygon.contains(x, y) ^ (radius >= 0)) {
						layer.setCell(col, row, roof);
					} else {
						layer.setCell(col, row, empty);
					}
				}
			}
		}
	}

}
