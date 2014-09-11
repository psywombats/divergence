/**
 *  MgnGame.java
 *  Created on Feb 10, 2014 12:17:05 AM for project mgne
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mgne.core;

import java.util.ArrayList;
import java.util.List;

import org.luaj.vm2.lib.TwoArgFunction;

import net.wombatrpgs.mgne.graphics.GraphicsSettings;
import net.wombatrpgs.mgne.maps.Level;
import net.wombatrpgs.mgne.maps.events.EventFactory;
import net.wombatrpgs.mgne.screen.Screen;
import net.wombatrpgs.mgne.screen.instances.ScreenGame;
import net.wombatrpgs.mgneschema.settings.IntroSettingsMDO;

/**
 * The MgnGame corresponds to all the game-specific stuff a game might want to
 * define. Create a subclass called SagaGame or something, I don't care. This
 * should probably be turned into an interface that returns a data blob that
 * controls things like game-specific settings, but the database exists to
 * handle all of the things like game name, resolution, etc, that most
 * conventional engines pass up in their application class. Instead, this should
 * only require the asbolute raw-est stuff needed to make the game work from a
 * source level.
 * 
 * I'm still not sure what to do about hooks. Anything that followed the old
 * super-janky factory isClassAssignableFrom antipattern should definitely get
 * hooked in somewhere. Otherwise, let's hope the game can be mostly scripts and
 * database entries!
 */
public abstract class MgnGame {
	
	/**
	 * Create and then return the first scene of the game. This defaults to the
	 * engine-provided GameScreen class if left alone. Whatever it returns is
	 * probably responsible for stuff like creating the hero, so it might as
	 * well just extend GameScreen anyway. Actually, it's probably the title
	 * screen, in which case, override this thing wholesale.
	 * @return					The first screen the engine will display
	 */
	public Screen makeStarterScreen() {		
		Screen screen = makeLevelScreen();
		readyLevelScreen(screen);
		return screen;
	}
	
	/**
	 * Create and return a level screen of the game. The screen should not
	 * require much loading and should not expect an active level in the
	 * level manager, but by the time the level is pushed to the stack, a level
	 * will be active. The screen should come pre-processed.
	 * @param	firstLevel		The first level to display
	 * @return					The level display screen, showing that level
	 */
	public Screen makeLevelScreen() {
		ScreenGame screen = new ScreenGame();
		MGlobal.assets.loadAsset(screen, "level screen");
		return screen;
	}
	
	/**
	 * Create and then return the memory object used to manage game saves. This
	 * defaults to the engine-provided Memory that stores everything in MGlobal,
	 * but games will most likely need to store some additional game-specific
	 * info from their own globals.
	 * @return					The game memory manager object
	 */
	public Memory makeMemory() {
		return new Memory();
	}
	
	/**
	 * Create and then return the constants object for stored constant lookup.
	 * This defaults to engine constants, but overriding Constants can be used
	 * to add more values.
	 * @return					The constants object for this game
	 */
	public Constants makeConstants() {
		return new Constants();
	}
	
	/**
	 * Create and then return the graphics settings object used to manage the
	 * shaders and sprite batches. This defaults to the engine graphics settings
	 * but can be overridden to do things like pass arguments to the default
	 * batch shader.
	 * @return					The graphics object for this game
	 */
	public GraphicsSettings makeGraphics() {
		return new GraphicsSettings();
	}
	
	/**
	 * Create and then return the event factory used to turn event data on maps
	 * into java objects. The default handles NPC-ish and teleporty events, but
	 * not things like random encounter areas and other game-specific junk.
	 * @return					The event factory for this game
	 */
	public EventFactory makeEventFactory() {
		return new EventFactory();
	}
	
	/**
	 * Called when the data is loaded. This is guaranteed to be called once all
	 * mdos are in memory but before any of the make creation methods.
	 */
	public void onDataLoaded() {
		// noop
	}
	
	/**
	 * Called when the game is created. Globals etc should get initialized here.
	 */
	public void onCreate() {
		// noop
	}
	
	/**
	 * Returns the list of the classes of lua lib that the game requires. These
	 * should be the ones defined by the game; mgn defaults are already
	 * included. By default, returns an empty list.
	 * @return					A list of all lua libs required by the game
	 */
	public List<Class<? extends TwoArgFunction>> getLuaLibs() {
		return new ArrayList<Class<? extends TwoArgFunction>>();
	}
	
	/**
	 * Sprite synch toggle. This will cause face animations to synch together
	 * when called, which is usually a good thing for RPGs but bad for more 
	 * actiony games where animations have more frames.
	 * @return					True to synchronize face animation anims
	 */
	public boolean synchronizeSprites() {
		return true;
	}
	
	/**
	 * Preps a screen that will display the levels. This is mainly meant as
	 * a ready-to-go helper for setting up the hero, level, and hero location
	 * from MDO. Should be usable in production.
	 * @param	levelScreen		The screen to prep for level display
	 */
	public void readyLevelScreen(Screen levelScreen) {
		IntroSettingsMDO mdo = MGlobal.data.getEntryFor(Constants.KEY_INTRO, IntroSettingsMDO.class);
		MGlobal.levelManager.setScreen(levelScreen);
		
		String mapName;
		if (MGlobal.args.get("map") != null) {
			mapName = MGlobal.args.get("map");
		} else {
			mapName = mdo.map;
		}
		Level level = MGlobal.levelManager.getLevel(mapName);
		MGlobal.assets.loadAsset(level, "first level");
		Avatar hero = new Avatar();
		
		if (MGlobal.args.get("x") == null) {
			hero.setTileX(mdo.mapX);
			hero.setTileY(mdo.mapY);
		} else {
			hero.setTileX(Integer.valueOf(MGlobal.args.get("x")));
			hero.setTileY(Integer.valueOf(MGlobal.args.get("y")));
		}
		
		MGlobal.levelManager.setNewActiveSet(hero, level);
		MGlobal.assets.loadAsset(hero, "hero");
	}
	
}
