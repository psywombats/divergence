/**
 *  Event.java
 *  Created on Nov 12, 2012 11:13:21 AM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.rainfall.maps;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;

import net.wombatrpgs.rainfall.RGlobal;
import net.wombatrpgs.rainfall.graphics.FourDir;
import net.wombatrpgs.rainfallschema.graphics.FourDirMDO;
import net.wombatrpgs.rainfallschema.maps.EventMDO;

/**
 * Any object static object on a Tiled map is an Event.
 */
public class Event extends MapObject {
	
	protected EventMDO mdo;
	protected FourDir appearance;

	/**
	 * Creates a new event with the specified data at the specified coords.
	 * @param	parent	The parent level of the event
	 * @param 	mdo		The data to create the event with
	 * @param 	x		The x-coord of the event (in pixels)
	 * @param 	y		The y-coord of the event (in pixels)
	 */
	public Event(Level parent, EventMDO mdo, int x, int y) {
		super(parent, x, y);
		this.mdo = mdo;
		if (mdo.appearance != null) {
			FourDirMDO dirMDO = (FourDirMDO) RGlobal.data.getEntryByKey(mdo.appearance);
			appearance = new FourDir(dirMDO, this);
		}
	}

	/**
	 * @see net.wombatrpgs.rainfall.graphics.Renderable#render
	 * (com.badlogic.gdx.graphics.OrthographicCamera)
	 */
	@Override
	public void render(OrthographicCamera camera) {
		if (appearance != null) {
			appearance.render(camera);
			camera.update();
		}
	}

	/**
	 * @see net.wombatrpgs.rainfall.maps.MapObject#queueRequiredAssets
	 * (com.badlogic.gdx.assets.AssetManager)
	 */
	@Override
	public void queueRequiredAssets(AssetManager manager) {
		appearance.queueRequiredAssets(manager);
	}

	/**
	 * @see net.wombatrpgs.rainfall.graphics.Renderable#postProcessing()
	 */
	@Override
	public void postProcessing(AssetManager manager) {
		appearance.postProcessing(manager);
	}

}
