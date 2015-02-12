/**
 *  EventGuide.java
 *  Created on Feb 10, 2015 5:08:39 AM for project bacon01-desktop
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.bacon01.maps.events;

import com.badlogic.gdx.graphics.Color;
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
 *
 */
public class EventGuide extends MapEvent {
	
	protected AnimationStrip light;
	protected float total;
	protected float fade, fade2;

	public EventGuide(EventMDO mdo, TiledMapObject object) {
		super(mdo, object);
		
		light = new AnimationStrip(MGlobal.data.getEntryFor("anim_light", AnimationMDO.class));
		assets.add(light);
	}
	
	/**
	 * @see net.wombatrpgs.mgne.maps.events.MapEvent#update(float)
	 */
	@Override
	public void update(float elapsed) {
		super.update(elapsed);
		total += elapsed;
		if (MGlobal.memory.getSwitch("finale_fade")) {
			fade += elapsed;
		}
		if (MGlobal.memory.getSwitch("finale_fade2")) {
			fade2 += elapsed;
		}
		
		light.setScale((float) (.6 + Math.sin(total)/4f));
	}

	/**
	 * @see net.wombatrpgs.mgne.maps.events.MapEvent#render(com.badlogic.gdx.graphics.g2d.SpriteBatch)
	 */
	@Override
	public void render(SpriteBatch batch) {
		// aura
		if (isHidden()) {
			return;
		}
		BaconLevel level = (BaconLevel) getParent();
		level.getLightBuffer().begin();
		float c = 1f - fade;
		c -= fade2;
		if (c < 0) c = 0;
		batch.setColor(new Color(1, 1, 1, c));
		int screenX = getCenterX() + 16;
		int screenY = getCenterY() - 24;
		screenX -= light.getWidth() / 2;
		screenY -= light.getHeight() / 2;
		light.renderAt(batch, screenX, screenY);
		level.getLightBuffer().end();
		parent.getScreen().resumeNormalBuffer();
		batch.setColor(new Color(1, 1, 1, 1));
	}
	
	public void trueRender(SpriteBatch batch) {
		float c = 1f - fade;
		if (c < 0) c = 0;
		batch.setColor(new Color(1, 1, 1, c));
		super.render(batch);
		batch.setColor(new Color(1, 1, 1, 1));
	}

	/**
	 * @see net.wombatrpgs.mgne.maps.events.MapEvent#onAddedToMap(net.wombatrpgs.mgne.maps.Level)
	 */
	@Override
	public void onAddedToMap(Level map) {
		super.onAddedToMap(map);
		halt();
		appearance.stopMoving();
		x -= 8;
	}

}
