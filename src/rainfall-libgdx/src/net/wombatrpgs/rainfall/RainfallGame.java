package net.wombatrpgs.rainfall;

import net.wombatrpgs.rainfall.test.TestScreen;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.TimeUtils;

public class RainfallGame implements ApplicationListener {
	private OrthographicCamera camera;
	private Rectangle glViewport;
	
	@Override
	public void create() {		
		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();
		
		camera = new OrthographicCamera(w, h);
		camera.position.set(w/2, h/2, 0);
		glViewport = new Rectangle(0, 0, w, h);
		
		RGlobal.screens.push(new TestScreen());
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

		RGlobal.screens.render(camera);
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
