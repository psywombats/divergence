/**
 *  Memory.java
 *  Created on Jan 22, 2014 8:36:57 PM for project saga
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mgne.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import net.wombatrpgs.mgne.io.Keymap;

/**
 * I don't know, all of the switches and variables for the game? It's meant to
 * be saved along with the hero and level to keep track of things. Like how RM
 * stored its save games: party progress, plus all the F9 stuff. Well we'll
 * store the current stuff too!
 * 
 * This kind of neeeds to be fleshed out and made to persist.
 */
public class Memory implements Serializable {

	private static final long serialVersionUID = 1L;
	
	/** Actual game-global memory */
	protected Map<String, Boolean> switches;
	
	/** Stuff that only gets set during a write */
	protected Keymap keymap;
	protected Random rand;
	
	
	/**
	 * Creates a new memory holder! This is great! It should also probably only
	 * be called from SGlobal.
	 */
	public Memory() {
		switches = new HashMap<String, Boolean>();
	}
	
	/**
	 * Sets a certain switch on or off.
	 * @param	name			The name of the switch to toggle
	 * @param	value			The value to toggle to (on=true, off=false)
	 */
	public void setSwitch(String name, boolean value) {
		switches.put(name, value);
	}
	
	/**
	 * Determines if a certain switch is on or off. If the switch hasn't been
	 * touched yet, it's assumed that the switch is off.
	 * @param	name			The name of the switch to check
	 * @return					The value of the switch (on=true, off=false)
	 */
	public boolean getSwitch(String name) {
		Boolean val = switches.get(name);
		return (val == null) ? false : val;
	}
	
	/**
	 * Writes this save data to a file.
	 * @param	out				The save file (assumed to exist)
	 */
	public void save(File savefile) {
		try {
			FileOutputStream stream = new FileOutputStream(savefile);
			ObjectOutputStream writer = new ObjectOutputStream(stream);
			
			// store all objects in memory in this object
			storeFields();
			
			writer.writeObject(this);
			writer.close();
			stream.close();
		} catch (IOException e) {
			MGlobal.reporter.err(e);
		}
	}
	
	/**
	 * Loads memory from a file. Has a bunch of side effects on the global
	 * object as it overwrites saved global values.
	 * @param	savefile		The save file (assumed to exist)
	 */
	public static void load(File savefile) {
		try {
			FileInputStream stream = new FileInputStream(savefile);
			ObjectInputStream reader = new ObjectInputStream(stream);
			MGlobal.memory = (Memory) reader.readObject();
			
			// write all stored objects to global etc
			MGlobal.memory.unloadFields();
			
			reader.close();
			stream.close();
		} catch (IOException e) {
			MGlobal.reporter.err(e);
		} catch (ClassNotFoundException e) {
			MGlobal.reporter.err(e);
		}
	}
	
	/**
	 * Performs the messy part of copying stuff from global into the save.
	 */
	protected void storeFields() {
		keymap = MGlobal.keymap;
		rand = MGlobal.rand;
	}

	/**
	 * The other messy method, copies from this save into global.
	 */
	protected void unloadFields() {
		MGlobal.keymap = keymap;
		MGlobal.rand = rand;
	}
}
