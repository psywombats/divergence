/**
 *  EventWarper.java
 *  Created on Jan 23, 2015 1:17:51 AM for project bacon01-desktop
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.bacon01.maps.events;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import net.wombatrpgs.bacon01.maps.BaconLevel;
import net.wombatrpgs.mgne.core.MGlobal;
import net.wombatrpgs.mgne.graphics.AnimationStrip;
import net.wombatrpgs.mgne.maps.Level;
import net.wombatrpgs.mgne.maps.TiledMapObject;
import net.wombatrpgs.mgne.maps.events.MapEvent;
import net.wombatrpgs.mgneschema.graphics.AnimationMDO;
import net.wombatrpgs.mgneschema.maps.EventMDO;

/**
 * Warps reality!
 */
public class EventWarper extends MapEvent {
	
	protected BaconLevel level;
	
	protected AnimationStrip light;
	protected float totalElapsed;

	public EventWarper(EventMDO mdo, TiledMapObject object) {
		super(mdo);
		
		light = new AnimationStrip(MGlobal.data.getEntryFor("anim_light", AnimationMDO.class));
		light.setScale(4);
		assets.add(light);
		
		totalElapsed = 0;
	}

	/**
	 * @see net.wombatrpgs.mgne.maps.events.MapEvent#onAddedToMap(net.wombatrpgs.mgne.maps.Level)
	 */
	@Override
	public void onAddedToMap(Level map) {
		super.onAddedToMap(map);
		this.level = (BaconLevel) map;
	}

	/**
	 * @see net.wombatrpgs.mgne.maps.events.MapEvent#render(com.badlogic.gdx.graphics.g2d.SpriteBatch)
	 */
	@Override
	public void render(SpriteBatch batch) {
		super.render(batch);
		
		level.getLightBuffer().begin();
		int screenX = getCenterX();
		int screenY = getCenterY();
		screenX -= light.getWidth() / 2;
		screenY -= light.getHeight() / 2;
		light.renderAt(batch, screenX, screenY);
		level.getLightBuffer().end();
		parent.getScreen().resumeNormalBuffer();
	}

	/**
	 * @see net.wombatrpgs.mgne.maps.events.MapEvent#update(float)
	 */
	@Override
	public void update(float elapsed) {
		super.update(elapsed);
		
		light.update(elapsed);
		totalElapsed += elapsed;
		float scale = (float) (6f + Math.sin(totalElapsed / 3f) * 4f);
		light.setScale(scale);
	}

}
