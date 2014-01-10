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
import net.wombatrpgs.saga.core.SGlobal;
import net.wombatrpgs.saga.io.command.CMapGame;
import net.wombatrpgs.saga.maps.Level;
import net.wombatrpgs.saga.maps.Loc;
import net.wombatrpgs.saga.rpg.Hero;
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
		SGlobal.levelManager.setScreen(this);
		
		IntroSettingsMDO introMDO=SGlobal.data.getEntryFor("default_intro", IntroSettingsMDO.class);
		map = SGlobal.levelManager.getLevel(introMDO.map);
		SGlobal.levelManager.setActive(map);
		if (map.getBGM() != null) {
			SGlobal.screens.playMusic(map.getBGM(), false);
		}
		
		addObject(map);
		pushCommandContext(new CMapGame());
		
		fpsMDO = SGlobal.data.getEntryFor("test_fps", FramerateTestMDO.class);
		
		shaderMDO = SGlobal.data.getEntryFor("test_shader", ShaderTestMDO.class);
		if (shaderMDO.enabled == TestState.ENABLED) {
			testShader = new ShaderProgram(
					SGlobal.loader.getText(Constants.SHADERS_DIR + shaderMDO.vertexFile),
					SGlobal.loader.getText(Constants.SHADERS_DIR + shaderMDO.fragmentFile));
			batch.setShader(testShader);
			mapShader = (testShader);
		}
		
		addObject(SGlobal.ui.getNarrator());
		
		// all this stuff is crap
		SGlobal.hero = new Hero(SGlobal.levelManager.getActive());
		assets.add(SGlobal.hero);
		
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
			// TODO: screen-based commands
		default:
			return SGlobal.hero.onCommand(command);
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
			while (!map.isTilePassable(SGlobal.hero, SGlobal.hero.getTileX(), SGlobal.hero.getTileY())) {
				SGlobal.hero.setTileX(SGlobal.rand.nextInt(map.getWidth()));
				SGlobal.hero.setTileY(SGlobal.rand.nextInt(map.getHeight()));
			}
			map.addEvent(SGlobal.hero);
			map.setTeleInLoc("hero", new Loc(SGlobal.hero.getTileX(), SGlobal.hero.getTileY()));
			SGlobal.hero.setX(SGlobal.hero.getTileX()*map.getTileWidth());
			SGlobal.hero.setY(SGlobal.hero.getTileY()*map.getTileHeight());
			getCamera().track(SGlobal.hero);
			getCamera().update(0);
			SGlobal.hero.refreshVisibilityMap();
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
			float wr = SGlobal.window.getZoom();
			float wh = SGlobal.window.getZoom();
			defaultFont.draw(batch, "FPS: " + Gdx.graphics.getFramesPerSecond(),
					cam.position.x - SGlobal.window.getWidth()/2*wh + 8,
					cam.position.y + SGlobal.window.getHeight()/2*wr - 8);
		}
		batch.end();
	}

}
