/**
 *  SagaScreen.java
 *  Created on May 15, 2014 1:34:09 AM for project saga-desktop
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.saga.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

import net.wombatrpgs.mgne.core.MGlobal;
import net.wombatrpgs.mgne.screen.Screen;
import net.wombatrpgs.mgne.screen.WindowSettings;
import net.wombatrpgs.saga.core.SGlobal;

/**
 * Saga-specific screen meant to apply some gameboy filters.
 */
public class SagaScreen extends Screen {

	/**
	 * @see net.wombatrpgs.mgne.screen.Screen#constructUIBatch()
	 */
	@Override
	protected SpriteBatch constructUIBatch() {
		return SGlobal.graphics.constructBackgroundBatch().getBatch();
	}

	/**
	 * @see net.wombatrpgs.mgne.screen.Screen#constructViewBatch()
	 */
	@Override
	protected SpriteBatch constructViewBatch() {
		return SGlobal.graphics.constructForegroundBatch().getBatch();
	}

	/**
	 * @see net.wombatrpgs.mgne.screen.Screen#constructMapShader()
	 */
	@Override
	protected ShaderProgram constructMapShader() {
		return SGlobal.graphics.constructBackgroundBatch().getShader();
	}

	/**
	 * @see net.wombatrpgs.mgne.screen.Screen#wipe()
	 */
	@Override
	protected void wipe() {
		float[] wipe = SGlobal.graphics.getBlack();
		WindowSettings window = MGlobal.window;
		Gdx.gl.glClearColor(wipe[0], wipe[1], wipe[2], 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		shapes.setColor(wipe[0], wipe[1], wipe[2], 1);
		shapes.begin(ShapeType.Filled);
		shapes.rect(0, 0, window.getWidth(), window.getHeight());
	}

}
