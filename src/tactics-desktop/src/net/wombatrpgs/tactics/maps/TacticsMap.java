/**
 *  TacticsMap.java
 *  Created on Feb 12, 2014 3:09:32 AM for project tactics-desktop
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.tactics.maps;

import java.util.List;

import com.badlogic.gdx.graphics.OrthographicCamera;

import net.wombatrpgs.mgne.maps.Level;
import net.wombatrpgs.mgne.maps.Loc;
import net.wombatrpgs.mgne.screen.Screen;
import net.wombatrpgs.mgne.screen.ScreenObject;
import net.wombatrpgs.tactics.core.TGlobal;

/**
 * A map that gets created from a normal map for a tactics battle.
 */
public class TacticsMap extends ScreenObject {
	
	protected Level map;
	
	/**
	 * Creates a new tactics map based on an existing map. Does not populate it
	 * with units or anything like that. Presumably the hero is currently on the
	 * level.
	 * @param	map				The map the battle will take place on
	 */
	public TacticsMap(Level map) {
		this.map = map;
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
		map.render(camera);
	}

	/**
	 * Renders any number of square overlays. Has any number of uses.
	 * @param	squares			The locs to display squares over
	 */
	public void renderSquares(List<Loc> squares) {
		for (Loc loc : squares) {
			TGlobal.ui.getHighlight().renderAt(
					map.getBatch(),
					loc.x * map.getTileWidth(),
					loc.y * map.getTileHeight());
		}
	}

}
