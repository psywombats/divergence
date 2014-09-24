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
import net.wombatrpgs.mgne.maps.layers.TiledGridLayer;
import net.wombatrpgs.mgne.screen.WindowSettings;

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
	
	protected enum DeployState {
		DEPLOYED,
		DEPLOYING,
		RETRACTING,
		RETRACTED,
	};
	
	protected EventCeiling event;
	protected DeployState state;
	protected Polygon polygon;
	protected transient Cell roof, empty;
	protected int currentRadius;
	protected int lastVisible;
	protected float sinceStart;

	/**
	 * Creates a ceiling layer based on a ceiling event.
	 * @param	event			The ceiling event creating the ceiling layer
	 * @param	polygon			The polygon defining this layer
	 */
	public CeilingLayer(EventCeiling event, Polygon polygon) {
		super((LoadedLevel) extractParent(event), generateLayer(event));
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
				if (Math.abs(radius) >= getVisionRadius()) {
					if (state == DeployState.RETRACTING) {
						instantRetract();
					} else if (state == DeployState.DEPLOYING) {
						instantDeploy();
					}
					resumeHero();
				} else {
					int visible = setRadius(radius);
					if (visible == lastVisible) {
						lastVisible = visible;
						update(TILE_DEPLOY_TIME);
					}
					lastVisible = visible;
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
	 * @see net.wombatrpgs.mgne.maps.layers.TiledGridLayer#isTilePassable(int, int)
	 */
	@Override
	public boolean isTilePassable(int tileX, int tileY) {
		return true;
	}

	/**
	 * @see net.wombatrpgs.mgne.maps.layers.GridLayer#getZ()
	 */
	@Override
	public float getZ() {
		return 1f;
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
	protected static Level extractParent(EventCeiling event) {
		return event.getParent();
	}
	
	/**
	 * Generates the ceiling layer from a defining event.
	 * @param	event			The event to generate from
	 * @return					The ceiling represented by that event
	 */
	protected static TiledMapTileLayer generateLayer(EventCeiling event) {
		Level parent = event.getParent();
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
		WindowSettings win = MGlobal.window;
		Level map = event.getParent();
		int horiz = (int) Math.ceil((float) (win.getViewportWidth() / map.getTileWidth()) / 2f);
		int vert = (int) Math.ceil((float) (win.getViewportHeight() / map.getTileHeight()) / 2f);
		return (horiz > vert) ? horiz : vert;
	}
	
	/**
	 * Populates the transient cell members.
	 */
	protected void generateCells() {
		empty = null;	// as in, do not display
		roof = new Cell();
		Integer id = event.getTileID();
		String tilesetName = event.getTilesetString();
		if (id == null || tilesetName == null) {
			MGlobal.reporter.err("No id or tileset name on roof event: " + event);
			return;
		}
		TiledMapTileSet tileset = getTilesetByName(parent.getMap(), tilesetName);
		roof.setTile(tileset.getTile(relativeToAbsoluteTileID(tilesetName, id)));
	}
	
	/**
	 * Writes ceiling tiles on the layer where appropriate, in a circular
	 * pattern around the hero where intersecting with the definining ceiling
	 * polygon. Positive radii correspond to a deployed roof, over the defined
	 * room, where negative radii are a retracted roof, not over the room but
	 * over the rest of the map instead. A radius of 0 would be no roof tiles.
	 * @param	radius			The radius to draw tiles out to
	 * @return					The number of visible ceiling tiles
	 */
	protected int setRadius(int radius) {
		int set = 0;
		currentRadius = radius;
		Level map = event.getParent();
		int absRadius = Math.abs(radius);
		Avatar hero = MGlobal.getHero();
		int heroCol = hero.getTileX();
		int heroRow = hero.getTileY();
		int vision = getVisionRadius();
		for (int col = 0; col < layer.getWidth(); col += 1) {
			for (int row = 0; row < layer.getHeight(); row += 1) {
				int x = col * map.getTileWidth() + map.getTileWidth() / 2;
				int y = row * map.getTileHeight() + map.getTileHeight() / 2;
				boolean inRange = Math.abs(col - heroCol) < absRadius &&
						Math.abs(row - heroRow) < absRadius;
				if (inRange ^ polygon.contains(x, y) ^ (radius >= 0)) {
					boolean wasSet = attemptSetAt(col, row);
					if (wasSet && radius < vision) set += 1;
				} else {
					layer.setCell(col, row, empty);
				}
			}
		}
		return set;
	}
	
	/**
	 * Sets a tile to covered if it meets the roof conditions.
	 * @param	col			The column to set at
	 * @param	row			The row to set at
	 * @return				True if the tile was set, false otherwise
	 */
	protected boolean attemptSetAt(int col, int row) {
		if (!parent.isBlankTile(col, row)) {
			layer.setCell(col, row, roof);
			return true;
		} else {
			layer.setCell(col, row, empty);
			return false;
		}
	}

}
