/**
 *  CommandMap.java
 *  Created on Nov 23, 2012 3:47:12 AM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mrogue.io;

import java.util.ArrayList;
import java.util.List;

import net.wombatrpgs.mrogue.core.MGlobal;
import net.wombatrpgs.mrogueschema.io.data.InputCommand;

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
	private List<CommandListener> toRemove;
	private List<CommandListener> toAdd;
	
	/**
	 * Creates and initializes a new command map.
	 */
	public CommandMap() {
		listeners = new ArrayList<CommandListener>();
		toRemove = new ArrayList<CommandListener>();
		toAdd = new ArrayList<CommandListener>();
	}
	
	/** @return A list of all our listeners */
	public List<CommandListener> getListener() { return listeners; }
	
	/**
	 * Adds a new listener to listen for commands from the player.
	 * @param 	listener		The listener to register
	 */
	public final void registerListener(CommandListener listener) {
		toAdd.add(listener);
	}
	
	/**
	 * Adds a new listener to listen for commands from the player. The real.
	 * @param 	listener		The listener to register
	 */
	private final void internalRegisterListener(CommandListener listener) {
		listeners.add(listener);
	}
	
	/**
	 * Unsubscribes a give listener from commands from the player.
	 * @param 	listener		The listener to unregister
	 */
	public final void unregisterListener(CommandListener listener) {
		toRemove.add(listener);
	}
	
	/**
	 * Unsubscribes a give listener from commands from the player. The real
	 * version.
	 * @param 	listener		The listener to unregister
	 */
	private final void internalUnregisterListener(CommandListener listener) {
		if (listeners.contains(listener)) {
			listeners.remove(listener);
		} else {
			MGlobal.reporter.warn("Listener " + listener + " was not actually " +
					"in the listeners list for " + this);
		}
	}
	
	/**
	 * Signal to the listeners that a command has been indicated by the player.
	 * @param 	command			The command that was indicated
	 */
	protected final void signal(InputCommand command) {
		// there were concurrent mod problems here
		for (CommandListener listener : listeners) {
			listener.onCommand(command);
		}
		for (CommandListener listener : toRemove) {
			internalUnregisterListener(listener);
		}
		for (CommandListener listener : toAdd) {
			internalRegisterListener(listener);
		}
		toAdd.clear();
		toRemove.clear();
	}
	
}
