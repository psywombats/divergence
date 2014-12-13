/**
 *  MemoryIndex.java
 *  Created on Jul 21, 2014 5:45:33 PM for project saga-desktop
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.saga.core;

import net.wombatrpgs.mgne.core.Constants;
import net.wombatrpgs.mgne.core.MGlobal;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

/**
 * Serialized object to keep track of save files.
 */
public class MemoryIndex {
	
	protected static final String INDEX_NAME = "descriptors.json";
	protected static final int SAVE_SLOT_COUNT= 5;
	
	// data to save/load
	protected SaveDescriptor descriptors[];
	protected int lastSavedIndex;
	
	/**
	 * Creates a new index. Probably only should be used if one doesn't exist.
	 */
	public MemoryIndex() {
		descriptors = new SaveDescriptor[SAVE_SLOT_COUNT];
	}
	
	/** @return The maximum number of save slots */
	public int maxSavesCount() { return SAVE_SLOT_COUNT; }
	
	/** @return The index of the last saved file slot */
	public int getLastSavedIndex() { return lastSavedIndex; }
	
	/**
	 * Counts the number of non-null saves existing in the save slots.
	 * @return					The number of saves existing, from 0 to max save
	 */
	public int existingSavesCount() {
		int count = 0;
		for (int i = 0; i < SAVE_SLOT_COUNT; i += 1) {
			if (descriptors[i] != null) {
				count += 1;
			}
		}
		return count;
	}
	
	/**
	 * Loads the serialized memory index, or creates a new one if none exists.
	 * @return					The index of loaded memory
	 */
	public static MemoryIndex loadIndex() {
		String fileName = Constants.SAVES_DIR + INDEX_NAME;
		FileHandle handle = Gdx.files.internal(fileName);
		if (handle.exists()) {
			Kryo kryo = MGlobal.memory.getKryo();
			MGlobal.reporter.inform("Loading index memory from " + fileName);
			Input input = new Input(MGlobal.files.getInputStream(fileName));
			return kryo.readObject(input, MemoryIndex.class);
		} else {
			MGlobal.reporter.inform("Index memory does not exist at " + fileName);
			return new MemoryIndex();
		}
	}
	
	/**
	 * Fetches the descriptor in the designated slot.
	 * @param	slot			The slot to retrieve from
	 * @return					The descriptor for that slot
	 */
	public SaveDescriptor getSave(int slot) {
		return descriptors[slot];
	}
	
	/**
	 * Adds a save descriptor matching the currently loaded info to the given
	 * slot. If any save exists there, it is erased first. This will load any
	 * assets needed by the descriptor.
	 * @param	slot			The slot to store descriptor in
	 */
	public void addSave(int slot) {
		SaveDescriptor save = SaveDescriptor.generateDescriptor();
		lastSavedIndex = slot;
		descriptors[slot] = save;
	}
	
	/**
	 * Saves this index to its designated location.
	 */
	public void save() {
		String fileName = Constants.SAVES_DIR + INDEX_NAME;
		Kryo kryo = MGlobal.memory.getKryo();
		Output output = new Output(MGlobal.files.getOuputStream(fileName));
		MGlobal.reporter.inform("Writing saves index to " + fileName);
		kryo.writeObject(output, this);
		output.close();
	}

}
