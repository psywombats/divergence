/**
 *  TacticsMap.java
 *  Created on Feb 12, 2014 3:09:32 AM for project tactics-desktop
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.tactics.maps;

import java.util.List;

import com.badlogic.gdx.graphics.OrthographicCamera;

import net.wombatrpgs.mgne.core.MGlobal;
import net.wombatrpgs.mgne.maps.Level;
import net.wombatrpgs.mgne.maps.Loc;
import net.wombatrpgs.mgne.screen.Screen;
import net.wombatrpgs.mgne.screen.ScreenObject;
import net.wombatrpgs.tactics.core.TGlobal;
import net.wombatrpgs.tactics.rpg.GameUnit;
import net.wombatrpgs.tactics.ui.Cursor;

/**
 * A map that gets created from a normal map for a tactics battle.
 */
public class TacticsMap extends ScreenObject {
	
	protected Level map;
	
	protected GameUnit highlightedUnit;
	protected List<Loc> highlightedSquares;
	protected Cursor cursor;
	
	/**
	 * Creates a new tactics map based on an existing map. Does not populate it
	 * with units or anything like that. Presumably the hero is currently on the
	 * level.
	 * @param	map				The map the battle will take place on
	 */
	public TacticsMap(Level map) {
		this.map = map;
		cursor = TGlobal.ui.getCursor();
	}

	/**
	 * @see net.wombatrpgs.mgne.screen.ScreenObject#onAddedToScreen
	 * (net.wombatrpgs.mgne.screen.Screen)
	 */
	@Override
	public void onAddedToScreen(Screen screen) {
		super.onAddedToScreen(screen);
		screen.removeObject(map);
	}

	/**
	 * @see net.wombatrpgs.mgne.screen.ScreenObject#onRemovedFromScreen
	 * (net.wombatrpgs.mgne.screen.Screen)
	 */
	@Override
	public void onRemovedFromScreen(Screen screen) {
		screen.addObject(map);
		super.onRemovedFromScreen(screen);
	}

	/**
	 * @see net.wombatrpgs.mgne.screen.ScreenObject#update(float)
	 */
	@Override
	public void update(float elapsed) {
		super.update(elapsed);
		map.update(elapsed);
	}

	/**
	 * @see net.wombatrpgs.mgne.screen.ScreenObject#render
	 * (com.badlogic.gdx.graphics.OrthographicCamera)
	 */
	@Override
	public void render(OrthographicCamera camera) {
		super.render(camera);
		map.renderGrid(false);
		if (highlightedSquares != null) {
			renderSquares(highlightedSquares);
		}
		map.renderEvents();
		map.renderGrid(true);
	}
	
	/**
	 * Adds a doll to this map and the enclosing level.
	 * @param	doll			The tactics event to add
	 */
	public void addDoll(TacticsEvent doll) {
		map.addEvent(doll);
	}
	
	/**
	 * Removes a doll from this map and the enclosing level.
	 * @param	doll			The tactics event to remove
	 */
	public void removeDoll(TacticsEvent doll) {
		map.removeEvent(doll);
	}
	
	/**
	 * Spawns the cursor at a given location. Should be called by player units
	 * on the beginning of their turn.
	 * @param	tileX			The location to start cursor at (in tiles)
	 * @param	tileY			The location to start cursor at (in tiles)
	 */
	public void showCursor(int tileX, int tileY) {
		if (!map.contains(cursor)) {
			map.addEvent(cursor);
		}
		cursor.setTileLocation(tileX, tileY);
	}
	
	/**
	 * Purges the cursor from the map.
	 */
	public void hideCursor() {
		map.removeEvent(cursor);
	}
	
	/**
	 * Kicks the non-tactics version of the hero off the map and replaces it
	 * with the tactics version.
	 */
	public void swapHeroes() {
		TGlobal.party.getHero().spawnAt(
				MGlobal.getHero().getTileX(),
				MGlobal.getHero().getTileY());
		TGlobal.party.getHero().getEvent().setFacing(MGlobal.getHero().getFacing());
		map.removeEvent(MGlobal.getHero());
	}
	
	/**
	 * Shows that little blue highlight for where a unit can move.
	 * @param	unit			The unit to highlight, or null for clear
	 */
	public void highlightMovement(GameUnit unit) {
		highlightedUnit = unit;
		if (unit == null) {
			clearHighlight();
		} else {
			highlightedSquares = unit.getEvent().getMoveRange();
		}
	}
	
	/**
	 * Clears the movement highlights from the map.
	 */
	public void clearHighlight() {
		highlightedSquares = null;
	}

	/**
	 * Renders any number of square overlays. Has any number of uses.
	 * @param	squares			The locs to display squares over
	 */
	protected void renderSquares(List<Loc> squares) {
		for (Loc loc : squares) {
			TGlobal.ui.getHighlight().renderAt(
					map.getBatch(),
					loc.x * map.getTileWidth(),
					loc.y * map.getTileHeight());
		}
	}

}
