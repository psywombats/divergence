package net.wombatrpgs.rainfall;

import net.wombatrpgs.mgne.global.Global;
import net.wombatrpgs.rainfallschema.MapLoadTestMDO;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.TileMapRendererLoader.TileMapParameter;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.tiled.TileMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.TimeUtils;

public class RainfallGame implements ApplicationListener {
	private OrthographicCamera camera;
//	private SpriteBatch batch;
//	private Texture texture;
//	private Sprite sprite;
	private AssetManager assetManager = new AssetManager();
	private TileMapRenderer mapRenderer;
	private String mapName;
	private Rectangle glViewport;
	
	@Override
	public void create() {		
		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();
		
		camera = new OrthographicCamera(w, h);
		camera.position.set(w/2, h/2, 0);
		glViewport = new Rectangle(0, 0, w, h);
		
//		batch = new SpriteBatch();
//		
//		texture = new Texture(Gdx.files.internal("data/libgdx.png"));
//		texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
//		
//		TextureRegion region = new TextureRegion(texture, 0, 0, 512, 275);
//		
//		sprite = new Sprite(region);
//		sprite.setSize(0.9f, 0.9f * sprite.getHeight() / sprite.getWidth());
//		sprite.setOrigin(sprite.getWidth()/2, sprite.getHeight()/2);
//		sprite.setPosition(-sprite.getWidth()/2, -sprite.getHeight()/2);
		
		MapLoadTestMDO mdo = (MapLoadTestMDO) Global.data.getEntryByKey("map_test");
		System.out.println("We're trying to load a " + mdo.map);
	
		TileMapParameter tileMapParameter = new TileMapParameter("res/maps/", 8, 8);
		mapName = "res/maps/" + mdo.map;
		assetManager.load("res/maps/" + mdo.map, TileMapRenderer.class, tileMapParameter);
	}

	@Override
	public void dispose() {
//		batch.dispose();
//		texture.dispose();
		assetManager.unload(mapName);
		assetManager.dispose();
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

		if (assetManager.update()) {
			if (mapRenderer == null) {
				mapRenderer = assetManager.get(mapName, TileMapRenderer.class);
			}
			mapRenderer.render(camera);
		}
		
//		batch.setProjectionMatrix(camera.combined);
//		batch.begin();
//		sprite.draw(batch);
//		batch.end();
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
