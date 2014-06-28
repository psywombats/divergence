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
import net.wombatrpgs.mgne.maps.events.EventFactory;
import net.wombatrpgs.mgne.screen.Screen;
import net.wombatrpgs.mgne.screen.instances.ScreenGame;

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
	 * screen... which is then responsible for all that, etc.
	 * @return					The first screen the engine will display
	 */
	public Screen makeStarterScreen() {
		return new ScreenGame();
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

}
