/**
 *  Light.java
 *  Created on Jan 15, 2015 1:03:38 AM for project bacon01-desktop
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.bacon01.maps.events;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.math.Matrix4;

import net.wombatrpgs.bacon01.maps.BaconLevel;
import net.wombatrpgs.mgne.core.MAssets;
import net.wombatrpgs.mgne.maps.Level;
import net.wombatrpgs.mgne.maps.events.MapEvent;
import net.wombatrpgs.mgne.ui.Graphic;
import net.wombatrpgs.sagaschema.events.EventLightMDO;

/**
 * Hack for light test.
 */
public class EventLight extends MapEvent {
	
	protected static String SHADER_MDO = "shader_lighttest";
	
	protected EventLightMDO mdo;
	
	protected BaconLevel level;
	protected SpriteBatch effectBatch;
	protected Graphic sprite;

	public EventLight(EventLightMDO mdo) {
		super(mdo);
		sprite = new Graphic("res/sprites/", "textures/light.png"); // more hardcode hack
		sprite.setTextureWidth(512);
		sprite.setTextureHeight(512);
		assets.add(sprite);
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
	 * @see net.wombatrpgs.mgne.maps.events.MapEvent#postProcessing(net.wombatrpgs.mgne.core.MAssets, int)
	 */
	@Override
	public void postProcessing(MAssets manager, int pass) {
		super.postProcessing(manager, pass);
		if (pass == 0) {
			effectBatch = new SpriteBatch();
			Matrix4 matrix = new Matrix4();
			matrix.setToOrtho2D(0, 0, level.getScreen().getWidth(), level.getScreen().getHeight());
			effectBatch.setProjectionMatrix(matrix);
		}
	}

	/**
	 * @see net.wombatrpgs.mgne.maps.events.MapEvent#render(com.badlogic.gdx.graphics.g2d.SpriteBatch)
	 */
	@Override
	public void render(SpriteBatch batch) {
		FrameBuffer buffer = level.getLightBuffer();
		buffer.begin();
		sprite.renderAt(effectBatch, x, y);
		level.getScreen().resumeNormalBuffer();
	}

	/**
	 * @see net.wombatrpgs.mgne.maps.events.MapEvent#update(float)
	 */
	@Override
	public void update(float elapsed) {
		super.update(elapsed);
		effectBatch.setProjectionMatrix(level.getScreen().getCamera().combined);
	}

}
