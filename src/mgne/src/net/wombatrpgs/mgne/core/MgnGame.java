/**
 *  MgnGame.java
 *  Created on Feb 10, 2014 12:17:05 AM for project mgne
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mgne.core;

import net.wombatrpgs.mgne.screen.Screen;
import net.wombatrpgs.mgne.screen.instances.GameScreen;

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
		return new GameScreen();
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
	 * Called when the game is created. Globals etc should get initialized here.
	 */
	public void onCreate() {
		// noop
	}

}
