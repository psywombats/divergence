/**
 *  TestScreen.java
 *  Created on Nov 24, 2012 4:14:43 AM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.rainfall.test;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import net.wombatrpgs.rainfall.core.GameScreen;
import net.wombatrpgs.rainfall.core.RGlobal;
import net.wombatrpgs.rainfall.io.TestCommandMap;
import net.wombatrpgs.rainfall.maps.Direction;
import net.wombatrpgs.rainfall.maps.Level;
import net.wombatrpgs.rainfallschema.io.data.InputCommand;
import net.wombatrpgs.rainfallschema.test.MapLoadTestMDO;

/**
 * TESTING 1 2 3 TESTING DO YOU HEAR ME TESTINGGGGGGGG
 */
public class TestScreen extends GameScreen {
	
	protected Level map;
	protected BitmapFont font;
	protected SpriteBatch batch;
	
	public TestScreen() {
		MapLoadTestMDO mapTestMDO = (MapLoadTestMDO) RGlobal.data.getEntryByKey("map_test");
		this.map = RGlobal.levelManager.getLevel(mapTestMDO.map);
		this.canvas = map;
		
		RGlobal.screens.registerLevelScreen(this);
		commandContext = new TestCommandMap();
		z = 0;
		font = new BitmapFont();
		batch = new SpriteBatch();
		
		init();
	}

	/**
	 * @see net.wombatrpgs.rainfall.io.CommandListener#onCommand
	 * (net.wombatrpgs.rainfallschema.io.data.InputCommand)
	 */
	@Override
	public void onCommand(InputCommand command) {
		//RGlobal.reporter.inform("Command received: " + command);
		
		// start move
		if (command.equals(InputCommand.MOVE_START_DOWN)) {
			RGlobal.hero.startMove(Direction.DOWN);
		} else if (command.equals(InputCommand.MOVE_START_LEFT)) {
			RGlobal.hero.startMove(Direction.LEFT);
		} else if (command.equals(InputCommand.MOVE_START_RIGHT)) {
			RGlobal.hero.startMove(Direction.RIGHT);
		} else if (command.equals(InputCommand.MOVE_START_UP)) {
			RGlobal.hero.startMove(Direction.UP);
		}
		
		// end move
		if (command.equals(InputCommand.MOVE_STOP_DOWN)) {
			RGlobal.hero.stopMove(Direction.DOWN);
		} else if (command.equals(InputCommand.MOVE_STOP_LEFT)) {
			RGlobal.hero.stopMove(Direction.LEFT);
		} else if (command.equals(InputCommand.MOVE_STOP_RIGHT)) {
			RGlobal.hero.stopMove(Direction.RIGHT);
		} else if (command.equals(InputCommand.MOVE_STOP_UP)) {
			RGlobal.hero.stopMove(Direction.UP);
		}
		
		RGlobal.hero.act(command, map);
	}

	/**
	 * @see net.wombatrpgs.rainfall.core.GameScreen#render
	 * (com.badlogic.gdx.graphics.OrthographicCamera)
	 */
	@Override
	public void render(OrthographicCamera camera) {
		super.render(camera);
		batch.begin();
		font.draw(batch, "FPS: " + Gdx.graphics.getFramesPerSecond(), 8, 16);
		batch.end();
	}

	/**
	 * @see net.wombatrpgs.rainfall.core.GameScreen#onFocusLost()
	 */
	@Override
	public void onFocusLost() {
		// tsilb
	}

	/**
	 * @see net.wombatrpgs.rainfall.core.GameScreen#onFocusGained()
	 */
	@Override
	public void onFocusGained() {
		// tslib
	}

}
