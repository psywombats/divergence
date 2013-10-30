/**
 *  TestScreen.java
 *  Created on Nov 24, 2012 4:14:43 AM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mrogue.screen.instances;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import net.wombatrpgs.mrogue.core.MGlobal;
import net.wombatrpgs.mrogue.io.command.CMapGame;
import net.wombatrpgs.mrogue.maps.Level;
import net.wombatrpgs.mrogue.screen.Screen;
import net.wombatrpgs.mrogue.ui.text.FontHolder;
import net.wombatrpgs.mrogue.ui.text.TextBox;
import net.wombatrpgs.mrogueschema.io.data.InputCommand;
import net.wombatrpgs.mrogueschema.test.MapLoadTestMDO;
import net.wombatrpgs.mrogueschema.test.TextBoxTestMDO;
import net.wombatrpgs.mrogueschema.test.data.TestState;
import net.wombatrpgs.mrogueschema.ui.FontMDO;
import net.wombatrpgs.mrogueschema.ui.TextBoxMDO;

/**
 * TESTING 1 2 3 TESTING DO YOU HEAR ME TESTINGGGGGGGG
 */
// TODO: screens should probably appear as a database entry
public class TestScreen extends Screen {

	protected BitmapFont defaultFont;
	protected FontHolder font;
	protected TextBox box;
	
	public TestScreen() {
		super();
		MapLoadTestMDO mapTestMDO = MGlobal.data.getEntryFor("map_test", MapLoadTestMDO.class);
		Level map = MGlobal.levelManager.getLevel(mapTestMDO.map);
		addObject(map);
		
		TextBoxTestMDO testMDO = MGlobal.data.getEntryFor("test_textbox", TextBoxTestMDO.class);
		if (testMDO != null && testMDO.enabled == TestState.ENABLED) {
			FontMDO fontMDO = MGlobal.data.getEntryFor(testMDO.font, FontMDO.class);
			font = new FontHolder(fontMDO);
			TextBoxMDO textMDO = MGlobal.data.getEntryFor(testMDO.box, TextBoxMDO.class);
			box = new TextBox(textMDO, font);
			box.setText(testMDO.text);
		}
		
		pushCommandContext(new CMapGame());
		defaultFont = new BitmapFont();
		batch = new SpriteBatch();
		cam.track(MGlobal.hero);
		
		init();
	}

	/**
	 * @see net.wombatrpgs.mrogue.io.CommandListener#onCommand
	 * (net.wombatrpgs.mrogueschema.io.data.InputCommand)
	 */
	@Override
	public boolean onCommand(InputCommand command) {
		//RGlobal.reporter.inform("Command received: " + command);
		
		/* We no longer do this because it's very shaky to convey button presses
		 * 1:1 to the hero. If one is missed, or one is canceled, things just
		 * turn into a giant mess. So instead, we'll expect that the hero is
		 * polling us for actions.
		 */
//		// start move
//		if (command.equals(InputCommand.MOVE_START_DOWN)) {
//			RGlobal.hero.startMove(Direction.DOWN);
//		} else if (command.equals(InputCommand.MOVE_START_LEFT)) {
//			RGlobal.hero.startMove(Direction.LEFT);
//		} else if (command.equals(InputCommand.MOVE_START_RIGHT)) {
//			RGlobal.hero.startMove(Direction.RIGHT);
//		} else if (command.equals(InputCommand.MOVE_START_UP)) {
//			RGlobal.hero.startMove(Direction.UP);
//		}
//		
//		// end move
//		if (command.equals(InputCommand.MOVE_STOP_DOWN)) {
//			RGlobal.hero.stopMove(Direction.DOWN);
//		} else if (command.equals(InputCommand.MOVE_STOP_LEFT)) {
//			RGlobal.hero.stopMove(Direction.LEFT);
//		} else if (command.equals(InputCommand.MOVE_STOP_RIGHT)) {
//			RGlobal.hero.stopMove(Direction.RIGHT);
//		} else if (command.equals(InputCommand.MOVE_STOP_UP)) {
//			RGlobal.hero.stopMove(Direction.UP);
//		}
		
		return false;
	}

	/**
	 * @see net.wombatrpgs.mrogue.screen.Screen#render()
	 */
	@Override
	public void render() {
		super.render();
		batch.begin();
		defaultFont.draw(batch, "FPS: " + Gdx.graphics.getFramesPerSecond(), 8, 16);
		batch.end();
		
		// this is actually legal wtf, used to be to-do here
		getViewBatch().begin();
		if (box != null) box.render(cam);
		getViewBatch().end();
	}

	/**
	 * @see net.wombatrpgs.mrogue.screen.Screen#queueRequiredAssets
	 * (com.badlogic.gdx.assets.AssetManager)
	 */
	@Override
	public void queueRequiredAssets(AssetManager manager) {
		if (font != null) {
			font.queueRequiredAssets(manager);
		}
		if (box != null) {
			box.queueRequiredAssets(manager);
		}
	}

	/**
	 * @see net.wombatrpgs.rainfall.core.GameScreen#postProcessing
	 * (com.badlogic.gdx.assets.AssetManager, int pass)
	 */
	@Override
	public void postProcessing(AssetManager manager, int pass) {
		if (pass > 0) return;
		if (font != null) {
			font.postProcessing(manager, pass);
		}
		if (box != null) {
			box.postProcessing(manager, pass);
		}
	}

}
