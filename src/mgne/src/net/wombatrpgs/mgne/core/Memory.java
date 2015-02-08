/**
 *  Memory.java
 *  Created on Jan 22, 2014 8:36:57 PM for project saga
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mgne.core;

import java.io.InputStream;
import java.io.OutputStream;

import net.wombatrpgs.mgne.core.lua.Lua;
import net.wombatrpgs.mgne.io.json.PerfectPrinter;
import net.wombatrpgs.mgne.maps.Level;
import net.wombatrpgs.mgne.maps.events.Avatar;
import net.wombatrpgs.mgne.maps.events.AvatarMemory;
import net.wombatrpgs.mgne.rpg.SwitchMap;
import net.wombatrpgs.mgne.screen.Screen;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

/**
 * I don't know, all of the switches and variables for the game? It's meant to
 * be saved along with the hero and level to keep track of things. Like how RM
 * stored its save games: party progress, plus all the F9 stuff. Well we'll
 * store the current stuff too!
 * 
 * It now runs off JSON. Don't touch its public properties please, they're there
 * so it's a bean rather than a something-jackson-can't-save.
 */
public class Memory {
	
	/** Live memory */
	public SwitchMap switches;
	
	/** Stuff to be serialized */
	public String levelKey;
	public AvatarMemory heroMemory;
	
	/**
	 * Creates a new memory holder! This is great! It should also probably only
	 * be called from MGlobal.
	 */
	public Memory() {
		switches = new SwitchMap();
	}
	
	/**
	 * Formats the save file name for a given save name.
	 * @param	saveName		The human name of the save file
	 * @return					The appropriate file path to that save file
	 */
	public static String saveToPath(String saveName) {
		return Constants.SAVES_DIR + saveName + Constants.SAVES_SUFFIX;
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
	 * Turns a switch on.
	 * @param	name			The name of the switch to turn on
	 */
	public final void setSwitch(String name) {
		setSwitch(name, true);
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
	 * @param	fileName		The name of the file to write to
	 */
	public void save(String fileName) {
		MGlobal.reporter.inform("Saving to " + fileName);
		OutputStream output = MGlobal.files.getOuputStream(fileName);
		
		// store all objects in memory in this object
		storeFields();
		
		ObjectMapper mapper = new ObjectMapper();
		ObjectWriter writer = mapper.writer(new PerfectPrinter());
		try {
			writer.writeValue(output, this);
			output.close();
		} catch (Exception e) {
			MGlobal.reporter.err("Error saving", e);
			return;
		}
		
		MGlobal.reporter.inform("Save complete.");
	}
	
	/**
	 * Loads memory from a file. Has a bunch of side effects on the global
	 * object as it overwrites saved global values.
	 * @param	fileName		The name of the file to read from
	 */
	public void load(String fileName) {
		MGlobal.reporter.inform("Loading from " + fileName);
		MGlobal.levelManager.reset();
		InputStream input = MGlobal.files.getInputStream(fileName);
		ObjectMapper mapper = new ObjectMapper();
		Memory saved = null;
		
		try {
			saved = mapper.readValue(input, getClass());
			input.close();
		} catch (Exception e) {
			MGlobal.reporter.err("Error loading", e);
			return;
		}
		
		// write all stored objects to global etc
		saved.unloadFields();
		
		// load required assets
		loadAssets();
		
		MGlobal.reporter.inform("Load complete.");
	}
	
	/**
	 * Performs the loading process and sets the screen to the level screen.
	 * @param	fileName		The name of the file to read from
	 */
	public void loadAndSetScreen(String fileName) {
		Screen gameScreen = MGlobal.game.makeLevelScreen();
		MGlobal.levelManager.setScreen(gameScreen);
		load(fileName);
		MGlobal.assets.loadAsset(gameScreen, "game screen");
	}
	
	/**
	 * Performs the messy part of copying stuff from global into the save.
	 */
	protected void storeFields() {
		levelKey = MGlobal.levelManager.getActive().getKeyName();
		heroMemory = new AvatarMemory(MGlobal.getHero());
	}

	/**
	 * The other messy method, copies from this save into global.
	 */
	protected void unloadFields() {
		
		MGlobal.memory = this;
		
		// this is needed to prevent lua calls from becoming stale?
		MGlobal.lua = new Lua();
		
		// put the hero on the new map
		Level level = MGlobal.levelManager.getLevel(levelKey);
		Avatar hero = new Avatar(heroMemory);
		hero.parent = level;
		level.addEvent(hero, hero.getTileX(), hero.getTileY());
		hero.setTileLocation(
				hero.getTileX(),
				level.getHeight() - hero.getTileY() - 1);
		MGlobal.levelManager.setNewActiveSet(hero, level);
		hero.onUnloaded();
	}
	
	/**
	 * Loads all the assets for stuff that got serialized.
	 */
	protected void loadAssets() {
		MGlobal.assets.loadAsset(MGlobal.screens, "loaded screens");
		MGlobal.assets.loadAsset(MGlobal.getHero(), "avatar");
	}
}
