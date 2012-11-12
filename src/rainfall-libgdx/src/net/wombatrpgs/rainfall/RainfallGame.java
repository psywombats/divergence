package net.wombatrpgs.rainfall;

import net.wombatrpgs.rainfall.graphics.Anim;
import net.wombatrpgs.rainfallschema.graphics.AnimationMDO;
import net.wombatrpgs.rainfallschema.test.MapLoadTestMDO;
import net.wombatrpgs.rainfallschema.test.SpriteRenderTestMDO;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.loaders.TileMapRendererLoader.TileMapParameter;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.tiled.TileMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.TimeUtils;

public class RainfallGame implements ApplicationListener {
	private OrthographicCamera camera;
	private TileMapRenderer mapRenderer;
	private String mapName;
	private Rectangle glViewport;
	private Anim anim;
	private AnimationMDO animMDO;
	
	@Override
	public void create() {		
		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();
		
		camera = new OrthographicCamera(w, h);
		camera.position.set(w/2, h/2, 0);
		glViewport = new Rectangle(0, 0, w, h);
		
		SpriteRenderTestMDO testMDO = (SpriteRenderTestMDO) RGlobal.data.getEntryByKey("anim_test");
		animMDO = (AnimationMDO) RGlobal.data.getEntryByKey(testMDO.anim);
		RGlobal.reporter.inform("We're trying to load from " + RGlobal.SPRITES_DIR + animMDO.file);	
		RGlobal.assetManager.load(RGlobal.SPRITES_DIR + animMDO.file, Texture.class);
		
		MapLoadTestMDO mapMDO = (MapLoadTestMDO) RGlobal.data.getEntryByKey("map_test");
		TileMapParameter tileMapParameter = new TileMapParameter(RGlobal.MAPS_DIR, 8, 8);
		mapName = RGlobal.MAPS_DIR + mapMDO.map;
		RGlobal.reporter.inform("We're trying to load from " + mapName);
		RGlobal.assetManager.load(mapName, TileMapRenderer.class, tileMapParameter);

	}

	@Override
	public void dispose() {
		
	}

	@Override
	public void render() {		
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		// camera
		GL10 gl = Gdx.graphics.getGL10();
		gl.glViewport((int) glViewport.x, (int) glViewport.y,
				(int) glViewport.width, (int) glViewport.height);
		camera.update();
		camera.apply(gl);
		if (TimeUtils.millis() % 50 == 0) {
			System.out.println("FPS: " + (1/Gdx.graphics.getDeltaTime()));
		}

		if (RGlobal.assetManager.update()) {
			if (mapRenderer == null) {
				mapRenderer = RGlobal.assetManager.get(mapName, TileMapRenderer.class);
			}
			if (anim == null) {
				anim = new Anim(animMDO);
			}
			mapRenderer.render(camera);
			anim.render();
		}
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}
}
