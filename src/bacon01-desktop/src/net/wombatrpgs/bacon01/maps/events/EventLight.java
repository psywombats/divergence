/**
 *  Light.java
 *  Created on Jan 15, 2015 1:03:38 AM for project bacon01-desktop
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.bacon01.maps.events;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;

import net.wombatrpgs.bacon01.maps.BaconLevel;
import net.wombatrpgs.baconschema.events.EventLightMDO;
import net.wombatrpgs.mgne.core.MAssets;
import net.wombatrpgs.mgne.maps.Level;
import net.wombatrpgs.mgne.maps.TiledMapObject;
import net.wombatrpgs.mgne.maps.events.MapEvent;
import net.wombatrpgs.mgne.physics.ShadowResult;
import net.wombatrpgs.mgne.ui.Graphic;
import net.wombatrpgs.mgns.core.Annotations.Path;

/**
 * Hack for light test.
 */
@Path("map/")
public class EventLight extends MapEvent {
	
	protected static String SHADER_MDO = "shader_lighttest";
	protected static String PROPERTY_RADIUS = "radius";
	protected static String PROPERTY_SIZE = "size";
	
	protected EventLightMDO mdo;
	
	protected FrameBuffer buffer;
	protected BaconLevel level;
	protected SpriteBatch effectBatch, copyBatch;
	protected Graphic sprite, penumbraSprite;
	protected ShapeRenderer shaper;
	protected int radius, size;

	public EventLight(EventLightMDO mdo, TiledMapObject object) {
		super(mdo);
		sprite = new Graphic("res/sprites/", "textures/light.png"); // more hardcode hack
		penumbraSprite = new Graphic("res/sprites/", "textures/penumbra.png");
		
		this.radius = object.getInt(PROPERTY_RADIUS);
		this.size = object.getInt(PROPERTY_SIZE);
		
		sprite.setTextureWidth(radius);
		sprite.setTextureHeight(radius);
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
			buffer = new FrameBuffer(Format.RGBA8888,
					getParent().getScreen().getWidth(),
					getParent().getScreen().getHeight(),
					false);
			copyBatch = new SpriteBatch();
			copyBatch.setProjectionMatrix(matrix);
		}
	}

	/**
	 * @see net.wombatrpgs.mgne.maps.events.MapEvent#render(com.badlogic.gdx.graphics.g2d.SpriteBatch)
	 */
	@Override
	public void render(SpriteBatch batch) {
		
		buffer.begin();
		Gdx.gl.glClearColor(0f, 0f, 0f, 0f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		
		effectBatch.setColor(Color.WHITE);
		sprite.renderAt(effectBatch, x, y);
		
		for (MapEvent event : level.getEventLayer().getAll()) {
			
			ShadowResult shadow = event.getHitbox().shadowcast(
					x,
					y,
					size);
			if (shadow == null) {
				continue;
			}
			
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
		}
		
		level.getLightBuffer().begin();
		copyBatch.begin();
		copyBatch.enableBlending();
		copyBatch.setBlendFunction(GL20.GL_SRC_COLOR, GL20.GL_ONE);
		copyBatch.draw(buffer.getColorBufferTexture(),
				0, 0,
				0, 0,
				level.getScreen().getWidth(), level.getScreen().getHeight(),
				1, 1,
				0,
				0, 0,
				level.getScreen().getWidth(), level.getScreen().getHeight(),
				false, true);
		copyBatch.end();
		level.getLightBuffer().end();
		
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
		buffer.dispose();
		copyBatch.dispose();
	}

}
