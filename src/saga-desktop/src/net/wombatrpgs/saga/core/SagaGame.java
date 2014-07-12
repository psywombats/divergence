/**
 *  SagaGame.java
 *  Created on Feb 10, 2014 12:29:13 AM for project saga-desktop
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.saga.core;

import java.util.List;

import org.luaj.vm2.lib.TwoArgFunction;

import net.wombatrpgs.mgne.core.DebugLevel;
import net.wombatrpgs.mgne.core.MGlobal;
import net.wombatrpgs.mgne.core.Memory;
import net.wombatrpgs.mgne.core.MgnGame;
import net.wombatrpgs.mgne.maps.events.EventFactory;
import net.wombatrpgs.mgne.screen.Screen;
import net.wombatrpgs.saga.lua.SagaSceneLib;
import net.wombatrpgs.saga.maps.SagaEventFactory;
import net.wombatrpgs.saga.screen.SagaScreen;
import net.wombatrpgs.saga.screen.ScreenTextIntro;
import net.wombatrpgs.saga.screen.ScreenWorld;

/**
 * One day, this class will tell MGNE how to run Saga.
 */
public class SagaGame extends MgnGame {

	/**
	 * @see net.wombatrpgs.mgne.core.MgnGame#makeStarterScreen()
	 */
	@Override
	public Screen makeStarterScreen() {
		SagaScreen screen;
		if (MGlobal.debug != DebugLevel.RELEASE) {
			screen = new ScreenWorld();
		} else {
			screen = new ScreenTextIntro();
		}
		MGlobal.assets.loadAsset(screen, "starter screen");
		return screen;
	}

	/**
	 * @see net.wombatrpgs.mgne.core.MgnGame#makeMemory()
	 */
	@Override
	public Memory makeMemory() {
		return new SagaMemory();
	}

	/**
	 * @see net.wombatrpgs.mgne.core.MgnGame#makeEventFactory()
	 */
	@Override
	public EventFactory makeEventFactory() {
		return new SagaEventFactory();
	}

	/**
	 * @see net.wombatrpgs.mgne.core.MgnGame#onCreate()
	 */
	@Override
	public void onCreate() {
		super.onCreate();
		SGlobal.globalInit();
	}

	/**
	 * @see net.wombatrpgs.mgne.core.MgnGame#getLuaLibs()
	 */
	@Override
	public List<Class<? extends TwoArgFunction>> getLuaLibs() {
		List<Class<? extends TwoArgFunction>> libs = super.getLuaLibs();
		libs.add(SagaSceneLib.class);
		return libs;
	}
	
}
