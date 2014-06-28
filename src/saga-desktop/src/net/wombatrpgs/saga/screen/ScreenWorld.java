/**
 *  WorldScreen.java
 *  Created on Mar 28, 2014 5:14:00 PM for project saga-desktop
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.saga.screen;

import net.wombatrpgs.mgne.core.Avatar;
import net.wombatrpgs.mgne.core.MAssets;
import net.wombatrpgs.mgne.core.MGlobal;
import net.wombatrpgs.mgne.io.command.CMapGame;
import net.wombatrpgs.mgne.maps.Level;
import net.wombatrpgs.mgne.screen.HeroSource;
import net.wombatrpgs.mgneschema.io.data.InputCommand;
import net.wombatrpgs.mgneschema.settings.IntroSettingsMDO;

/**
 * Shows up when you wander the overworld.
 */
public class ScreenWorld extends SagaScreen implements HeroSource {
	
	protected Avatar hero;
	protected Level map;
	
	/**
	 * Creates a new world map screen.
	 */
	public ScreenWorld() {
		super();
		MGlobal.levelManager.setScreen(this);
		MGlobal.levelManager.setHeroTracker(this);
		
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
	 * @see net.wombatrpgs.mgne.screen.Screen#postProcessing
	 * (net.wombatrpgs.mgne.core.MAssets, int)
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
	 * @see net.wombatrpgs.mgne.screen.instances.ScreenGame#onCommand
	 * (net.wombatrpgs.mgneschema.io.data.InputCommand)
	 */
	@Override
	public boolean onCommand(InputCommand command) {
		if (super.onCommand(command)) return true;
		switch (command) {
		case WORLD_PAUSE:
			ScreenPause menu = new ScreenPause();
			MGlobal.assets.loadAsset(menu, "main menu");
			MGlobal.screens.push(menu);
			return true;
		default:
			return hero.onCommand(command);
		}
	}

	/**
	 * @see net.wombatrpgs.mgne.screen.HeroSource#getHero()
	 */
	@Override
	public Avatar getHero() {
		return hero;
	}

}
