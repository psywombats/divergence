/**
 *  Memory.java
 *  Created on Jan 22, 2014 8:36:57 PM for project saga
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mgne.core;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import net.wombatrpgs.mgne.io.Keymap;
import net.wombatrpgs.mgne.maps.LoadedLevel;
import net.wombatrpgs.mgne.maps.MapThing;
import net.wombatrpgs.mgne.maps.Positionable;
import net.wombatrpgs.mgne.maps.layers.EventLayer;
import net.wombatrpgs.mgne.screen.Screen;
import net.wombatrpgs.mgne.screen.ScreenStack;
import net.wombatrpgs.mgne.screen.TrackerCam;
import net.wombatrpgs.mgneschema.maps.LoadedMapMDO;

import com.badlogic.gdx.graphics.Color;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

/**
 * I don't know, all of the switches and variables for the game? It's meant to
 * be saved along with the hero and level to keep track of things. Like how RM
 * stored its save games: party progress, plus all the F9 stuff. Well we'll
 * store the current stuff too!
 * 
 * This kind of neeeds to be fleshed out and made to persist.
 */
public class Memory {
	
	/** Fields for the saving process */
	protected transient Kryo kryo;
	
	/** Live memory */
	protected Map<String, Boolean> switches;
	
	/** Stuff to be serialized */
	protected Random rand;
	protected ScreenStack screens;
	protected Keymap keymap;
	
	
	/**
	 * Creates a new memory holder! This is great! It should also probably only
	 * be called from MGlobal.
	 */
	public Memory() {
		switches = new HashMap<String, Boolean>();
		
		kryo = new Kryo();
		
		// Now we need to register the custom serializers. This is mostly for
		// data classes (texture, sound, etc) and immutables

		// this one from libgdx wiki
		kryo.register(Color.class, new Serializer<Color>() {
			@Override public Color read(Kryo kryo, Input input, Class<Color> type) {
				Color color = new Color();
				Color.rgba8888ToColor(color, input.readInt());
				return color;
			}
			@Override public void write(Kryo kryo, Output output, Color color) {
				output.writeInt(Color.rgba8888(color));
			}
		});
		
		// but these are mine
		kryo.register(TrackerCam.class, new Serializer<TrackerCam>() {
			@Override public void write(Kryo kryo, Output output, TrackerCam object) {
				output.writeFloat(object.viewportWidth);
				output.writeFloat(object.viewportHeight);
				output.writeFloat(object.getPanSpeed());
				kryo.writeClassAndObject(output, object.getTarget());
			}
			@Override public TrackerCam read(Kryo kryo, Input input, Class<TrackerCam> type) {
				float w = input.readFloat();
				float h = input.readFloat();
				float s = input.readFloat();
				Positionable target = (Positionable) kryo.readClassAndObject(input);
				return new TrackerCam(w, h, target, s);
			}
		});
		kryo.register(LoadedLevel.class, new Serializer<LoadedLevel>() {
			@Override
			public void write(Kryo kryo, Output output, LoadedLevel object) {
				output.writeString(object.getKey());
				kryo.writeClassAndObject(output, object.getScreen());
				kryo.writeClassAndObject(output, object.getContents());
				kryo.writeClassAndObject(output, object.getEventLayer());
			}
			@Override
			public LoadedLevel read(Kryo kryo, Input input, Class<LoadedLevel> type) {
				String key = input.readString();
				Screen scr = (Screen) kryo.readClassAndObject(input);
				@SuppressWarnings("unchecked") // guaranteed by protocol
				List<MapThing> contents = (List<MapThing>) kryo.readClassAndObject(input);
				EventLayer events = (EventLayer) kryo.readClassAndObject(input);
				LoadedLevel level = new LoadedLevel(
						MGlobal.data.getEntryFor(key, LoadedMapMDO.class),
						scr, contents, events);
				return level;
			}
		});
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
	 * Formats the save file name for a given save name.
	 * @param	saveName		The human name of the save file
	 * @return					The appropriate file path to that save file
	 */
	public static String saveToPath(String saveName) {
		return Constants.SAVES_DIR + saveName + Constants.SAVES_SUFFIX;
	}
	
	/**
	 * Writes this save data to a file.
	 * @param	fileName		The name of the file to write to
	 */
	public void save(String fileName) {
		MGlobal.reporter.inform("Saving to " + fileName);
		Output output = new Output(MGlobal.files.getOuputStream(fileName));
		
		// store all objects in memory in this object
		storeFields();
		
		kryo.writeObject(output, this);
		output.close();
		MGlobal.reporter.inform("Save complete.");
	}
	
	/**
	 * Loads memory from a file. Has a bunch of side effects on the global
	 * object as it overwrites saved global values.
	 * @param	savefile		The name of the file to read from
	 */
	public void load(String fileName) {
		MGlobal.reporter.inform("Loading from " + fileName);
		Input input = new Input(MGlobal.files.getInputStream(fileName));
		MGlobal.memory = kryo.readObject(input, Memory.class);
		
		// write all stored objects to global etc
		MGlobal.memory.unloadFields();
		
		// load required assets
		MGlobal.assets.loadAsset(MGlobal.screens, "loaded screens");
		
		input.close();
		MGlobal.memory.kryo = kryo;
		MGlobal.reporter.inform("Load complete.");
	}
	
	/**
	 * Performs the messy part of copying stuff from global into the save.
	 */
	protected void storeFields() {
		rand = MGlobal.rand;
		screens = MGlobal.screens;
		keymap = MGlobal.keymap;
	}

	/**
	 * The other messy method, copies from this save into global.
	 */
	protected void unloadFields() {
		
		// rand is copied directly
		MGlobal.rand = rand;
		
		// the old screen stack is disposed and we copy the new one over
		MGlobal.screens.dispose();
		MGlobal.screens = screens;
		
		// everything listening to the current keymap will listen to the stored
		keymap.absorbListeners(MGlobal.keymap);
		MGlobal.keymap = keymap;
	}
}
