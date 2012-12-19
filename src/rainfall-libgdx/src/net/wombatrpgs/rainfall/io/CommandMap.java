/**
 *  CommandMap.java
 *  Created on Nov 23, 2012 3:47:12 AM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.rainfall.io;

import java.util.ArrayList;
import java.util.List;

import net.wombatrpgs.mgne.global.Global;
import net.wombatrpgs.rainfallschema.io.data.InputCommand;

/**
 * Okay, this is a little weird, but this maps virtual button presses to command
 * intentions. Something like if you press the virtual A-button, this interprets
 * that as a confirm. However, in most games this only exists as a single blob
 * and never changes. This is here so that multiple maps can be plug-and-played
 * for testing purposes. A default implementation should be kicking around
 * somewhere.<br><br>
 * 
 * Also of note: these should be specific to a context, so the command map for
 * menus shouldn't be the same as gameplay, etc.
 */
public abstract class CommandMap implements ButtonListener {
	
	private List<CommandListener> listeners;
	
	/**
	 * Creates and initializes a new command map.
	 */
	public CommandMap() {
		listeners = new ArrayList<CommandListener>();
	}
	
	/**
	 * Adds a new listener to listen for commands from the player.
	 * @param 	listener		The listener to register
	 */
	public final void registerListener(CommandListener listener) {
		listeners.add(listener);
	}
	
	/**
	 * Unsubscribes a give listener from commands from the player.
	 * @param 	listener		The listener to unregister
	 */
	public final void unregisterListener(CommandListener listener) {
		if (listeners.contains(listener)) {
			listeners.remove(listener);
		} else {
			Global.reporter.warn("Listener " + listener + " was not actually " +
					"in the listeners list for " + this);
		}
	}
	
	/**
	 * Signal to the listeners that a command has been indicated by the player.
	 * @param 	command			The command that was indicated
	 */
	protected final void signal(InputCommand command) {
		for (CommandListener listener : listeners) {
			listener.onCommand(command);
		}
	}
	
}
