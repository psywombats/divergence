/**
 *  GameScreen.java
 *  Created on Feb 22, 2013 4:13:09 AM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mgne.screen.instances;

import net.wombatrpgs.mgne.core.Avatar;
import net.wombatrpgs.mgne.core.MAssets;
import net.wombatrpgs.mgne.core.MGlobal;
import net.wombatrpgs.mgne.io.command.CMapGame;
import net.wombatrpgs.mgne.maps.Level;
import net.wombatrpgs.mgne.screen.Screen;
import net.wombatrpgs.mgneschema.io.data.InputCommand;
import net.wombatrpgs.mgneschema.settings.IntroSettingsMDO;

/**
 * This is the default screen that appears when the game is first loaded. Once
 * on this screen, the opening scene is played.
 */
public class GameScreen extends Screen {
	
	protected Level map;
	protected Avatar hero;
	
	protected boolean stasisMode;
	
	/**
	 * Constructs the introduction scene. This consists of simply setting up the
	 * parser and map.
	 */
	public GameScreen() {
		super();
		MGlobal.levelManager.setScreen(this);
		
		IntroSettingsMDO introMDO=MGlobal.data.getEntryFor("default_intro", IntroSettingsMDO.class);
		map = MGlobal.levelManager.getLevel(introMDO.map);
		assets.add(map);
		MGlobal.levelManager.setActive(map);
		if (map.getBGM() != null) {
			MGlobal.screens.playMusic(map.getBGM(), false);
		}
		
		addChild(map);
		pushCommandContext(new CMapGame());
		
		hero = new Avatar();
		assets.add(hero);
	}
	
	/**
	 * @see net.wombatrpgs.mgne.io.CommandListener#onCommand
	 * (net.wombatrpgs.mgneschema.io.data.InputCommand)
	 */
	@Override
	public boolean onCommand(InputCommand command) {
		if (super.onCommand(command)) return true;
		switch (command) {
			// engine-wide commands go here
		default:
			return false;
		}
	}

	/**
	 * @see net.wombatrpgs.mgne.screen.Screen#postProcessing
	 * (MAssets, int)
	 */
	@Override
	public void postProcessing(MAssets manager, int pass) {
		super.postProcessing(manager, pass);
		
		if (pass == 0 && hero.getParent() == null) {
			hero.setTileX(2);
			hero.setTileY(2);
			while (!map.isTilePassable(hero.getTileX(), hero.getTileY())) {
				hero.setTileX(MGlobal.rand.nextInt(map.getWidth()));
				hero.setTileY(MGlobal.rand.nextInt(map.getHeight()));
			}
			map.addEvent(hero);
			hero.setX(hero.getTileX()*map.getTileWidth());
			hero.setY(hero.getTileY()*map.getTileHeight());
			getCamera().track(hero);
		}
		getCamera().update(0);
	}
	
	/**
	 * Fetches the player's avatar that's parading around on the map. This
	 * replaces one of those public static monstrosities that's been kicking
	 * around for forever.
	 * @return					The representation of the player on the map
	 */
	public Avatar getHero() {
		return hero;
	}

}
