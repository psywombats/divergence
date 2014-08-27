/**
 *  MemoryIndex.java
 *  Created on Jul 21, 2014 5:45:33 PM for project saga-desktop
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.saga.core;

import java.util.ArrayList;
import java.util.List;

import net.wombatrpgs.mgne.core.AssetQueuer;
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
public class MemoryIndex extends AssetQueuer {
	
	protected static final String indexName = "descriptors.kryo";
	
	// data to save/load
	protected List<SaveDescriptor> descriptors;
	protected SaveDescriptor lastLoaded;
	
	/**
	 * Creates a new index. Probably only should be used if one doesn't exist.
	 */
	public MemoryIndex() {
		descriptors = new ArrayList<SaveDescriptor>();
	}
	
	/** @return The total number of save files */
	public int getSaveCount() { return descriptors.size(); }
	
	/**
	 * Loads the serialized memory index, or creates a new one if none exists.
	 * @return
	 */
	public static MemoryIndex loadIndex() {
		String fileName = Constants.SAVES_DIR + indexName;
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
		return descriptors.get(slot);
	}
	
	/**
	 * Adds a save descriptor matching the currently loaded info to the given
	 * slot. If any save exists there, it is erased first. This will load any
	 * assets needed by the descriptor.
	 * @param	slot			The slot to store descriptor in
	 */
	public void addSave(int slot) {
		SaveDescriptor save = SaveDescriptor.generateDescriptor();
		assets.add(save);
		MGlobal.assets.loadAsset(save, "new save descriptor");
		if (slot >= descriptors.size()) {
			descriptors.add(save);
		} else {
			descriptors.remove(slot);
			descriptors.add(slot, save);
		}
	}
	
	/**
	 * Saves this index to its designated location.
	 */
	public void save() {
		String fileName = Constants.SAVES_DIR + indexName;
		Kryo kryo = MGlobal.memory.getKryo();
		Output output = new Output(MGlobal.files.getOuputStream(fileName));
		MGlobal.reporter.inform("Writing saves index to " + fileName);
		kryo.writeObject(output, this);
		output.close();
	}

}
