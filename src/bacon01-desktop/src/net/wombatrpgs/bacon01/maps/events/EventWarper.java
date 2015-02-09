/**
 *  EventWarper.java
 *  Created on Jan 23, 2015 1:17:51 AM for project bacon01-desktop
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.bacon01.maps.events;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import net.wombatrpgs.bacon01.maps.BaconLevel;
import net.wombatrpgs.mgne.core.MAssets;
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
	protected float period;
	protected float r1, r2;
	protected float origWidth;
	protected float startX, startY;

	public EventWarper(EventMDO mdo, TiledMapObject object) {
		super(mdo);
		
		light = new AnimationStrip(MGlobal.data.getEntryFor("anim_light", AnimationMDO.class));
		assets.add(light);
		
		period = Float.valueOf(object.getString("period"));
		r1 = Float.valueOf(object.getString("radius1"));
		r2 = Float.valueOf(object.getString("radius2"));
		
		totalElapsed = 0;
	}

	/**
	 * @see net.wombatrpgs.mgne.maps.events.MapEvent#onAddedToMap(net.wombatrpgs.mgne.maps.Level)
	 */
	@Override
	public void onAddedToMap(Level map) {
		super.onAddedToMap(map);
		this.level = (BaconLevel) map;
		startX = x;
		startY = y;
	}

	/**
	 * @see net.wombatrpgs.mgne.maps.events.MapEvent#postProcessing(net.wombatrpgs.mgne.core.MAssets, int)
	 */
	@Override
	public void postProcessing(MAssets manager, int pass) {
		super.postProcessing(manager, pass);
		origWidth = light.getWidth();
	}

	/**
	 * @see net.wombatrpgs.mgne.maps.events.MapEvent#render(com.badlogic.gdx.graphics.g2d.SpriteBatch)
	 */
	@Override
	public void render(SpriteBatch batch) {
		super.render(batch);
		
		level.getLightBuffer().begin();
		int screenX = getCenterX() + (getAppearance() == null ? 16 : 0);
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
		float ratio = (float) Math.cos(totalElapsed / period * Math.PI*2);
		ratio = 1f - (ratio/2f + .5f);
		float newRadius = (float) (r1 + ratio * (r2 - r1));
		light.setScale(2 * newRadius / origWidth);
	}

	/**
	 * @see net.wombatrpgs.mgne.maps.events.MapEvent#isMovable()
	 */
	@Override
	public boolean isMovable() {
		return true;
	}

	/**
	 * @see net.wombatrpgs.mgne.maps.MapThing#onMapFocusLost(net.wombatrpgs.mgne.maps.Level)
	 */
	@Override
	public void onMapFocusLost(Level map) {
		super.onMapFocusLost(map);
		x = startX;
		y = startY;
	}

}
