/**
 *  SMemory.java
 *  Created on Apr 4, 2014 8:00:28 PM for project saga-desktop
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.bacon01.core;

import java.util.List;

import net.wombatrpgs.bacon01.rpg.Inventory;
import net.wombatrpgs.bacon01.rpg.ItemMemory;
import net.wombatrpgs.mgne.core.MGlobal;
import net.wombatrpgs.mgne.core.Memory;

/**
 * Saga memory for storing specialized saga data in mgne.
 */
public class BMemory extends Memory {
	
	public static String FILE_NAME = "save.json";
	
	// fields to store/unload
	public int saveSlot;
	
	// serialzied items
	public List<ItemMemory> items;

	/**
	 * @see net.wombatrpgs.mgne.core.Memory#storeFields()
	 */
	@Override
	protected void storeFields() {
		super.storeFields();
		this.saveSlot = BGlobal.saveSlot;
		this.items = BGlobal.items.toMemory();
	}

	/**
	 * @see net.wombatrpgs.mgne.core.Memory#unloadFields()
	 */
	@Override
	protected void unloadFields() {
		BGlobal.saveSlot = this.saveSlot;
		BGlobal.items = new Inventory(this.items);
		super.unloadFields();
	}

	/**
	 * @see net.wombatrpgs.mgne.core.Memory#loadAssets()
	 */
	@Override
	protected void loadAssets() {
		super.loadAssets();
		MGlobal.assets.loadAsset(BGlobal.items, "items");
	}

}
