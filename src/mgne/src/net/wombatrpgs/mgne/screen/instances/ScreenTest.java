/**
 *  TestScreen.java
 *  Created on Nov 24, 2012 4:14:43 AM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mgne.screen.instances;

import com.badlogic.gdx.graphics.g2d.BitmapFont;

import net.wombatrpgs.mgne.core.MAssets;
import net.wombatrpgs.mgne.core.MGlobal;
import net.wombatrpgs.mgne.io.command.CMapGame;
import net.wombatrpgs.mgne.maps.Level;
import net.wombatrpgs.mgne.screen.Screen;
import net.wombatrpgs.mgne.ui.text.FontHolder;
import net.wombatrpgs.mgne.ui.text.TextBox;
import net.wombatrpgs.mgneschema.io.data.InputCommand;
import net.wombatrpgs.mgneschema.test.MapLoadTestMDO;
import net.wombatrpgs.mgneschema.test.TextBoxTestMDO;
import net.wombatrpgs.mgneschema.test.data.TestState;
import net.wombatrpgs.mgneschema.ui.FontMDO;
import net.wombatrpgs.mgneschema.ui.TextBoxMDO;

/**
 * TESTING 1 2 3 TESTING DO YOU HEAR ME TESTINGGGGGGGG
 */
public class ScreenTest extends Screen {

	protected BitmapFont defaultFont;
	protected FontHolder font;
	protected TextBox box;
	
	public ScreenTest() {
		super();
		MapLoadTestMDO mapTestMDO = MGlobal.data.getEntryFor("map_test", MapLoadTestMDO.class);
		Level map = MGlobal.levelManager.getLevel(mapTestMDO.map);
		addChild(map);
		
		TextBoxTestMDO testMDO = MGlobal.data.getEntryFor("test_textbox", TextBoxTestMDO.class);
		if (testMDO != null && testMDO.enabled == TestState.ENABLED) {
			FontMDO fontMDO = MGlobal.data.getEntryFor(testMDO.font, FontMDO.class);
			font = new FontHolder(fontMDO);
			TextBoxMDO textMDO = MGlobal.data.getEntryFor(testMDO.box, TextBoxMDO.class);
			box = new TextBox(textMDO, font);
			box.showText(testMDO.text);
		}
		
		pushCommandContext(new CMapGame());
		defaultFont = new BitmapFont();
		viewBatch = MGlobal.graphics.constructBatch();
		cam.track(MGlobal.getHero());
	}

	/**
	 * @see net.wombatrpgs.mgne.io.CommandListener#onCommand
	 * (net.wombatrpgs.mgneschema.io.data.InputCommand)
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
	 * @see net.wombatrpgs.mgne.screen.Screen#queueRequiredAssets
	 * (MAssets)
	 */
	@Override
	public void queueRequiredAssets(MAssets manager) {
		if (font != null) {
			font.queueRequiredAssets(manager);
		}
		if (box != null) {
			box.queueRequiredAssets(manager);
		}
	}

	/**
	 * @see net.wombatrpgs.mgne.screen.Screen#postProcessing
	 * (net.wombatrpgs.mgne.core.MAssets, int)
	 */
	@Override
	public void postProcessing(MAssets manager, int pass) {
		super.postProcessing(manager, pass);
		if (pass > 0) return;
		if (font != null) {
			font.postProcessing(manager, pass);
		}
		if (box != null) {
			box.postProcessing(manager, pass);
		}
	}
	
}
