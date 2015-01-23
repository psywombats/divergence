/**
 *  SagaGame.java
 *  Created on Feb 10, 2014 12:29:13 AM for project saga-desktop
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.bacon01.core;

import java.util.List;

import org.luaj.vm2.lib.TwoArgFunction;

import net.wombatrpgs.bacon01.maps.BaconLevel;
import net.wombatrpgs.bacon01.maps.events.BaconEventFactory;
import net.wombatrpgs.bacon01.screens.ScreenWorld;
import net.wombatrpgs.bacon01.ui.BaconUI;
import net.wombatrpgs.mgne.core.MGlobal;
import net.wombatrpgs.mgne.core.Memory;
import net.wombatrpgs.mgne.core.MgnGame;
import net.wombatrpgs.mgne.core.VersionInfo;
import net.wombatrpgs.mgne.graphics.GraphicsSettings;
import net.wombatrpgs.mgne.maps.LoadedLevel;
import net.wombatrpgs.mgne.maps.events.EventFactory;
import net.wombatrpgs.mgne.screen.Screen;
import net.wombatrpgs.mgne.ui.UISettings;
import net.wombatrpgs.mgneschema.maps.LoadedMapMDO;
import net.wombatrpgs.mgneschema.settings.UISettingsMDO;

/**
 * One day, this class will tell MGNE how to run BACON.
 */
public class BaconGame extends MgnGame {

	/**
	 * @see net.wombatrpgs.mgne.core.MgnGame#makeStarterScreen()
	 */
	@Override
	public Screen makeStarterScreen() {
		return super.makeStarterScreen();
	}

	/**
	 * @see net.wombatrpgs.mgne.core.MgnGame#makeLevelScreen()
	 */
	@Override
	public Screen makeLevelScreen() {
		return new ScreenWorld();
	}

	/**
	 * @see net.wombatrpgs.mgne.core.MgnGame#makeMemory()
	 */
	@Override
	public Memory makeMemory() {
		return new BMemory();
	}

	/**
	 * @see net.wombatrpgs.mgne.core.MgnGame#makeEventFactory()
	 */
	@Override
	public EventFactory makeEventFactory() {
		return new BaconEventFactory();
	}

	/**
	 * @see net.wombatrpgs.mgne.core.MgnGame#makeGraphics()
	 */
	@Override
	public GraphicsSettings makeGraphics() {
		return super.makeGraphics();
	}

	/**
	 * @see net.wombatrpgs.mgne.core.MgnGame#makeUI()
	 */
	@Override
	public UISettings makeUI() {
		return new BaconUI(MGlobal.data.getEntryFor(
				UISettings.DEFAULT_MDO_KEY, UISettingsMDO.class));
	}

	/**
	 * @see net.wombatrpgs.mgne.core.MgnGame#onDataLoaded()
	 */
	@Override
	public void onDataLoaded() {
		super.onDataLoaded();
	}

	/**
	 * @see net.wombatrpgs.mgne.core.MgnGame#onCreate()
	 */
	@Override
	public void onCreate() {
		super.onCreate();
		BGlobal.globalInit();
	}

	/**
	 * @see net.wombatrpgs.mgne.core.MgnGame#getLuaLibs()
	 */
	@Override
	public List<Class<? extends TwoArgFunction>> getLuaLibs() {
		List<Class<? extends TwoArgFunction>> libs = super.getLuaLibs();
		return libs;
	}

	/**
	 * @see net.wombatrpgs.mgne.core.MgnGame#getVersionInfo()
	 */
	@Override
	public VersionInfo getVersionInfo() {
		return new VersionInfo(BConstants.VERSION, BConstants.SAVE_BUILD, BConstants.GAME_NAME) {
			@Override public String getUserName() {
				return System.getProperty("user.name");
			}
		};
	}

	/**
	 * @see net.wombatrpgs.mgne.core.MgnGame#loadMap
	 * (net.wombatrpgs.mgneschema.maps.LoadedMapMDO, net.wombatrpgs.mgne.screen.Screen)
	 */
	@Override
	public LoadedLevel loadMap(LoadedMapMDO mdo, Screen levelScreen) {
		return new BaconLevel(mdo, levelScreen);
	}

	/**
	 * @see net.wombatrpgs.mgne.core.MgnGame#dispose()
	 */
	@Override
	public void dispose() {
		super.dispose();
		DebugThread.stopInstance();
	}
	
}
