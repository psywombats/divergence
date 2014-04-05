/**
 *  MapRenderable.java
 *  Created on Apr 4, 2014 11:46:26 PM for project mgne
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mgne.graphics;

import net.wombatrpgs.mgne.maps.events.MapEvent;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * A screen graphic that drawn locally when given a map object.
 */
public abstract class MapGraphic extends ScreenGraphic {
	
	/**
	 * Moves this graphic to the specified position relative to the map and then
	 * renders it there.
	 * @param	x				The x-coord to render at (in virtual px)
	 * @param	y				The y-coord to render at (in virtual px)
	 */
	public void mapRender(float x, float y) {
		
	}
	
	public void mapRender(MapEvent event, float offX, float offY) {
		
	}
	
	public void mapRender(MapEvent event) {
		
	}
	
	/**
	 * Fetches the appropriate batch for rendering things on the map.
	 * @return					The parent's view batch
	 */
	protected SpriteBatch getMapBatch() {
		return parent.getViewBatch();
	}

}
