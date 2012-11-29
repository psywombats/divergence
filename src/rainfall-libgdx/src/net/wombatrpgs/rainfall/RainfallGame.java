package net.wombatrpgs.rainfall;

import net.wombatrpgs.rainfall.core.RGlobal;
import net.wombatrpgs.rainfall.io.FocusListener;
import net.wombatrpgs.rainfall.io.FocusReporter;
import net.wombatrpgs.rainfall.test.TestScreen;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Rectangle;

public class RainfallGame implements ApplicationListener, FocusListener {
	
	private FocusReporter focusReporter;
	private boolean paused;
	
	private OrthographicCamera camera;
	private Rectangle glViewport;
	
	/**
	 * Creates a new game. Requires a few setup tools that are platform
	 * dependant.
	 * @param 	focusReporter		The thing that tells if focus is lost
	 */
	public RainfallGame(FocusReporter focusReporter) {
		super();
		focusReporter.registerListener(this);
		this.focusReporter = focusReporter;
		paused = false;
	}
	
	@Override
	public void create() {		
		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();
		
		Gdx.graphics.setVSync(true);
		glViewport = new Rectangle(0, 0, w, h);
		
		Gdx.input.setInputProcessor(RGlobal.keymap);
		RGlobal.screens.push(new TestScreen());
	}

	@Override
	public void dispose() {
		RGlobal.assetManager.dispose();
	}

	@Override
	public void render() {		
		
		focusReporter.update();
		
		if (!paused) {
			Gdx.gl.glClearColor(0, 0, 0, 1);
			Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
			
			// camera
			if (camera == null) {
				createCamera();
			}
			camera.position.x = RGlobal.hero.getX();
			camera.position.y = RGlobal.hero.getY();
			camera.update();
			
			RGlobal.screens.render(camera);
		}
	}

	/**
	 * @see com.badlogic.gdx.ApplicationListener#resize(int, int)
	 */
	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * @see com.badlogic.gdx.ApplicationListener#pause()
	 */
	@Override
	public void pause() {
		paused = true;
		RGlobal.keymap.onPause();
	}

	/**
	 * @see com.badlogic.gdx.ApplicationListener#resume()
	 */
	@Override
	public void resume() {
		paused = false;
		RGlobal.keymap.onResume();
	}

	/**
	 * @see net.wombatrpgs.rainfall.io.FocusListener#onFocusLost()
	 */
	@Override
	public void onFocusLost() {
		pause();
	}

	/**
	 * @see net.wombatrpgs.rainfall.io.FocusListener#onFocusGained()
	 */
	@Override
	public void onFocusGained() {
		resume();
	}
	
	/**
	 * Camera creation, called once.
	 */
	private void createCamera() {
		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();
		
		camera = new OrthographicCamera(w, h);
		camera.update();
		GL10 gl = Gdx.graphics.getGL10();
		gl.glViewport((int) glViewport.x, (int) glViewport.y,
				(int) glViewport.width, (int) glViewport.height);
		camera.update();
		camera.apply(gl);
	}

}
