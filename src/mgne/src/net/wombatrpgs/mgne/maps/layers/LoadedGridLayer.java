/**
 *  LoadedGridLayer.java
 *  Created on Jan 7, 2014 6:20:59 PM for project saga
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mgne.maps.layers;

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import net.wombatrpgs.mgne.maps.LoadedLevel;

/**
 * A grid layer that was loaded from a Tiled map. Has a couple internal methods
 * for initialization, but apart from that, it should be mostly the same as a
 * normal grid layer.
 */
public class LoadedGridLayer extends TiledGridLayer {

	/**
	 * It seems like this is identical to the TiledGridLayer...
	 * @param	parent			The parent level
	 * @param	layer			The layer of this abstraction
	 */
	public LoadedGridLayer(LoadedLevel parent, TiledMapTileLayer layer) {
		super(parent, layer);
	}

}
