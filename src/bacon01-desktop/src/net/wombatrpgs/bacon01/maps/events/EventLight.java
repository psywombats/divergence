/**
 *  Light.java
 *  Created on Jan 15, 2015 1:03:38 AM for project bacon01-desktop
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.bacon01.maps.events;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;

import net.wombatrpgs.bacon01.maps.BaconLevel;
import net.wombatrpgs.mgne.core.MAssets;
import net.wombatrpgs.mgne.core.MGlobal;
import net.wombatrpgs.mgne.maps.Level;
import net.wombatrpgs.mgne.maps.events.MapEvent;
import net.wombatrpgs.mgne.physics.ShadowResult;
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
	protected Graphic sprite, penumbraSprite;
	protected ShapeRenderer shaper;

	public EventLight(EventLightMDO mdo) {
		super(mdo);
		sprite = new Graphic("res/sprites/", "textures/light.png"); // more hardcode hack
		penumbraSprite = new Graphic("res/sprites/", "textures/penumbra.png");
		sprite.setTextureWidth(512);
		sprite.setTextureHeight(512);
		assets.add(sprite);
		assets.add(penumbraSprite);
		
		shaper = new ShapeRenderer();
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
			shaper.setProjectionMatrix(matrix);
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
		
		ShadowResult shadow = MGlobal.getHero().getAppearance().getHitbox().shadowcast(
				x + level.getTileWidth() / 2,
				y + level.getTileHeight() / 2,
				8);
		
		float[] vertices = shadow.umbraVertices;
		shaper.setColor(Color.BLACK);
		shaper.begin(ShapeType.Filled);
		for (int i = 2; i < vertices.length-3; i += 2) {
			shaper.triangle(
					vertices[0], vertices[1],
					vertices[i], vertices[i+1],
					vertices[i+2], vertices[i+3]);
		}
		shaper.end();
		
//		float[] vertices = shadow.fin1.toVertices();
//		shaper.setColor(Color.BLACK);
//		shaper.begin(ShapeType.Filled);
//		for (int i = 2; i < vertices.length-3; i += 2) {
//			shaper.triangle(
//					vertices[0], vertices[1],
//					vertices[i], vertices[i+1],
//					vertices[i+2], vertices[i+3]);
//		}
//		shaper.end();
		
		float[] triVertices = createTriangle(
				penumbraSprite.getGraphic(),
				shadow.fin1.root,
				shadow.fin1.umbra,
				shadow.fin1.penumbra,
				Color.BLACK);
		float[] triVertices2 = createTriangle(
				penumbraSprite.getGraphic(),
				shadow.fin2.root,
				shadow.fin2.umbra,
				shadow.fin2.penumbra,
				Color.BLACK);
		effectBatch.begin();
		effectBatch.draw(penumbraSprite.getTexture(), triVertices, 0, triVertices.length);
		effectBatch.draw(penumbraSprite.getTexture(), triVertices2, 0, triVertices2.length);
		effectBatch.end();
		
		MGlobal.getHero().update(0);
		MGlobal.getHero().render(effectBatch);
		
		level.getScreen().resumeNormalBuffer();
	}

	/**
	 * @see net.wombatrpgs.mgne.maps.events.MapEvent#update(float)
	 */
	@Override
	public void update(float elapsed) {
		super.update(elapsed);
		level.getScreen().getCamera().update(0);
		effectBatch.setProjectionMatrix(level.getScreen().getCamera().combined);
		shaper.setProjectionMatrix(level.getScreen().getCamera().combined);
	}
	
	protected float[] createTriangle(TextureRegion region, Vector2 p1, Vector2 p2, Vector2 p3, Color color) {
		float c = color.toFloatBits();
		float u = region.getU();
		float v = region.getV();
		float u2 = region.getU2();
		float v2 = region.getV2();
		return new float[] {
			p1.x, p1.y, c, u, v,
			p2.x, p2.y, c, u, v2,
			p3.x, p3.y, c, u2, v,
			p1.x, p1.y, c, u, v
		};
	} 

	/**
	 * @see net.wombatrpgs.mgne.maps.MapThing#dispose()
	 */
	@Override
	public void dispose() {
		super.dispose();
		effectBatch.dispose();
		shaper.dispose();
	}

}
