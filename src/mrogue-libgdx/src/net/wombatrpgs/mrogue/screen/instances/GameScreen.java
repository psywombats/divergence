/**
 *  GameScreen.java
 *  Created on Feb 22, 2013 4:13:09 AM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mrogue.screen.instances;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

import net.wombatrpgs.mrogue.characters.Hero;
import net.wombatrpgs.mrogue.core.Constants;
import net.wombatrpgs.mrogue.core.MGlobal;
import net.wombatrpgs.mrogue.io.TestCommandMap;
import net.wombatrpgs.mrogue.maps.Level;
import net.wombatrpgs.mrogue.maps.Loc;
import net.wombatrpgs.mrogue.scenes.SceneParser;
import net.wombatrpgs.mrogue.screen.Screen;
import net.wombatrpgs.mrogueschema.cutscene.SceneMDO;
import net.wombatrpgs.mrogueschema.io.data.InputCommand;
import net.wombatrpgs.mrogueschema.settings.IntroSettingsMDO;
import net.wombatrpgs.mrogueschema.test.FramerateTestMDO;
import net.wombatrpgs.mrogueschema.test.ShaderTestMDO;
import net.wombatrpgs.mrogueschema.test.data.TestState;

/**
 * This is the default screen that appears when the game is first loaded. Once
 * on this screen, the opening scene is played.
 */
// TODO: there's some sloppy fullscreen shit here that should be generalized
public class GameScreen extends Screen {
	
	protected SceneParser introParser;
	protected Level map;
	protected BitmapFont defaultFont;
	
	// tests
	protected FramerateTestMDO fpsMDO;
	protected ShaderTestMDO shaderMDO;
	
	protected ShaderProgram testShader;
	
	/**
	 * Constructs the introduction scene. This consists of simply setting up the
	 * parser and map.
	 */
	public GameScreen() {
		IntroSettingsMDO introMDO=MGlobal.data.getEntryFor("default_intro", IntroSettingsMDO.class);
		SceneMDO sceneMDO = MGlobal.data.getEntryFor(introMDO.scene, SceneMDO.class);
		MGlobal.levelManager.setScreen(this);
		map = MGlobal.levelManager.getLevel(introMDO.map);
		introParser = new SceneParser(sceneMDO);
		this.canvas = map;
		this.commandContext = new TestCommandMap();
		defaultFont = new BitmapFont();
		batch = new SpriteBatch();
		
		fpsMDO = MGlobal.data.getEntryFor("test_fps", FramerateTestMDO.class);
		
		shaderMDO = MGlobal.data.getEntryFor("test_shader", ShaderTestMDO.class);
		if (shaderMDO.enabled == TestState.ENABLED) {
			testShader = new ShaderProgram(
					MGlobal.loader.getText(Constants.SHADERS_DIR + shaderMDO.vertexFile),
					MGlobal.loader.getText(Constants.SHADERS_DIR + shaderMDO.fragmentFile));
			batch.setShader(testShader);
			mapShader = (testShader);
		}
		
		MGlobal.hero = new Hero(map, 0, 0);
		assets.add(MGlobal.hero);
		
		addScreenObject(MGlobal.ui.getNarrator());
		addScreenObject(MGlobal.ui.getHud());
		
		init();
	}
	
	/**
	 * @see net.wombatrpgs.mrogue.io.CommandListener#onCommand
	 * (net.wombatrpgs.mrogueschema.io.data.InputCommand)
	 */
	@Override
	public void onCommand(InputCommand command) {
		//RGlobal.reporter.inform("Command received: " + command);
		switch (command) {
		case INTENT_EXIT:
			Gdx.app.exit();
			break;
		case INTENT_FULLSCREEN:
			Gdx.graphics.setDisplayMode(
					MGlobal.window.getResolutionWidth(), 
					MGlobal.window.getResolutionHeight(), 
					!Gdx.graphics.isFullscreen());
			break;
		default:
			MGlobal.hero.onCommand(command);
		}
		
	}

	/**
	 * @see net.wombatrpgs.mrogue.screen.Screen#queueRequiredAssets
	 * (com.badlogic.gdx.assets.AssetManager)
	 */
	@Override
	public void queueRequiredAssets(AssetManager manager) {
		super.queueRequiredAssets(manager);
		introParser.queueRequiredAssets(manager);
		// the map should be done already
	}

	/**
	 * @see net.wombatrpgs.mrogue.screen.Screen#postProcessing
	 * (com.badlogic.gdx.assets.AssetManager, int)
	 */
	@Override
	public void postProcessing(AssetManager manager, int pass) {
		super.postProcessing(manager, pass);
		introParser.postProcessing(manager, pass);
		
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
	 * @see net.wombatrpgs.mrogue.screen.Screen#render()
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
	 * @see net.wombatrpgs.mrogue.screen.Screen#update(float)
	 */
	@Override
	public void update(float elapsed) {
		super.update(elapsed);
		if (!introParser.isRunning() && !introParser.hasExecuted()) {
			introParser.run(map);
		}
	}

}
