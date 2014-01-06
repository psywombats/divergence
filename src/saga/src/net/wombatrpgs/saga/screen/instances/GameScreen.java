/**
 *  GameScreen.java
 *  Created on Feb 22, 2013 4:13:09 AM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.saga.screen.instances;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

import net.wombatrpgs.saga.core.Constants;
import net.wombatrpgs.saga.core.MGlobal;
import net.wombatrpgs.saga.io.command.CMapGame;
import net.wombatrpgs.saga.maps.Level;
import net.wombatrpgs.saga.maps.Loc;
import net.wombatrpgs.saga.maps.events.Cursor;
import net.wombatrpgs.saga.rpg.Hero;
import net.wombatrpgs.saga.scenes.SceneParser;
import net.wombatrpgs.saga.screen.Screen;
import net.wombatrpgs.sagaschema.io.data.InputCommand;
import net.wombatrpgs.sagaschema.settings.IntroSettingsMDO;
import net.wombatrpgs.sagaschema.test.FramerateTestMDO;
import net.wombatrpgs.sagaschema.test.ShaderTestMDO;
import net.wombatrpgs.sagaschema.test.data.TestState;

/**
 * This is the default screen that appears when the game is first loaded. Once
 * on this screen, the opening scene is played.
 */
public class GameScreen extends Screen {
	
	protected Level map;
	protected SceneParser introParser, tutorialParser;
	protected Cursor cursor;
	
	protected boolean stasisMode;
	
	// tests
	protected FramerateTestMDO fpsMDO;
	protected ShaderTestMDO shaderMDO;
	protected ShaderProgram testShader;
	
	/**
	 * Constructs the introduction scene. This consists of simply setting up the
	 * parser and map.
	 */
	public GameScreen() {
		super();
		MGlobal.levelManager.setScreen(this);
		
		IntroSettingsMDO introMDO=MGlobal.data.getEntryFor("default_intro", IntroSettingsMDO.class);
		map = MGlobal.levelManager.getLevel(introMDO.map);
		MGlobal.levelManager.setActive(map);
		if (map.getBGM() != null) {
			MGlobal.screens.playMusic(map.getBGM(), false);
		}
		introParser = MGlobal.levelManager.getCutscene(introMDO.scene);
		assets.add(introParser);
		tutorialParser = MGlobal.levelManager.getCutscene(introMDO.tutorialScene);
		assets.add(tutorialParser);
		
		addObject(map);
		pushCommandContext(new CMapGame());
		
		fpsMDO = MGlobal.data.getEntryFor("test_fps", FramerateTestMDO.class);
		
		shaderMDO = MGlobal.data.getEntryFor("test_shader", ShaderTestMDO.class);
		if (shaderMDO.enabled == TestState.ENABLED) {
			testShader = new ShaderProgram(
					MGlobal.loader.getText(Constants.SHADERS_DIR + shaderMDO.vertexFile),
					MGlobal.loader.getText(Constants.SHADERS_DIR + shaderMDO.fragmentFile));
			batch.setShader(testShader);
			mapShader = (testShader);
		}
		
		cursor = new Cursor();
		assets.add(cursor);
		
		addObject(MGlobal.ui.getNarrator());
		
		// all this stuff is crap
		MGlobal.hero = new Hero(MGlobal.levelManager.getActive());
		assets.add(MGlobal.hero);
		
		// will be called later
		// normally
		init();
	}
	
	/**
	 * @see net.wombatrpgs.saga.io.CommandListener#onCommand
	 * (net.wombatrpgs.sagaschema.io.data.InputCommand)
	 */
	@Override
	public boolean onCommand(InputCommand command) {
		if (super.onCommand(command)) return true;
		switch (command) {
		case INTENT_LOOK:
			cursor.activate(false);
			return true;
		default:
			return MGlobal.hero.onCommand(command);
		}
		
	}

	/**
	 * @see net.wombatrpgs.saga.screen.Screen#postProcessing
	 * (com.badlogic.gdx.assets.AssetManager, int)
	 */
	@Override
	public void postProcessing(AssetManager manager, int pass) {
		super.postProcessing(manager, pass);
		
		if (pass == 0) {
			while (!map.isTilePassable(MGlobal.hero, MGlobal.hero.getTileX(), MGlobal.hero.getTileY())) {
				MGlobal.hero.setTileX(MGlobal.rand.nextInt(map.getWidth()));
				MGlobal.hero.setTileY(MGlobal.rand.nextInt(map.getHeight()));
			}
			map.addEvent(MGlobal.hero);
			map.setTeleInLoc("hero", new Loc(MGlobal.hero.getTileX(), MGlobal.hero.getTileY()));
			MGlobal.hero.setX(MGlobal.hero.getTileX()*map.getTileWidth());
			MGlobal.hero.setY(MGlobal.hero.getTileY()*map.getTileHeight());
			getCamera().track(MGlobal.hero);
			getCamera().update(0);
			MGlobal.hero.refreshVisibilityMap();
		}
	}
	
	/**
	 * @see net.wombatrpgs.saga.screen.Screen#render()
	 */
	@Override
	public void render() {
		super.render();
		batch.begin();
		if (fpsMDO.enabled == TestState.ENABLED) {
			float wr = MGlobal.window.getZoom();
			float wh = MGlobal.window.getZoom();
			defaultFont.draw(batch, "FPS: " + Gdx.graphics.getFramesPerSecond(),
					cam.position.x - MGlobal.window.getWidth()/2*wh + 8,
					cam.position.y + MGlobal.window.getHeight()/2*wr - 8);
		}
		batch.end();
	}

	/**
	 * @see net.wombatrpgs.saga.screen.Screen#update(float)
	 */
	@Override
	public void update(float elapsed) {
		super.update(elapsed);
		if (!introParser.isRunning() && !introParser.hasExecuted()) {
			introParser.run();
		}
		if (introParser.hasExecuted() && !tutorialParser.isRunning() && !tutorialParser.hasExecuted()) {
			tutorialParser.run();
		}
	}

}
