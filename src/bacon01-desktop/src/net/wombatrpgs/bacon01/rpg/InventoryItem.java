/**
 *  InventoryItem.java
 *  Created on Jan 26, 2015 9:36:51 PM for project bacon01-desktop
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.bacon01.rpg;

import net.wombatrpgs.bacon01.graphics.ItemGraphicDisplayer;
import net.wombatrpgs.baconschema.rpg.ItemMDO;
import net.wombatrpgs.mgne.core.AssetQueuer;
import net.wombatrpgs.mgne.core.MGlobal;
import net.wombatrpgs.mgne.core.interfaces.FinishListener;
import net.wombatrpgs.mgne.ui.Graphic;

/**
 * Stick one in your inventory.
 */
public class InventoryItem extends AssetQueuer {
	
	protected ItemMDO mdo;
	protected Graphic icon;
	protected int quantity;

	/**
	 * Creates a new inventory item from data. Has 1 quantity.
	 * @param	mdo				The data to create from
	 */
	public InventoryItem(ItemMDO mdo) {
		this.mdo = mdo;
		this.icon = new Graphic("res/ui/", mdo.icon);
		assets.add(icon);
		quantity = 1;
	}
	
	/**
	 * Create from serialized
	 * @param memory
	 */
	public InventoryItem(ItemMemory memory) {
		this(memory.itemKey);
		this.quantity = memory.quantity;
	}
	
	/**
	 * Creates a new inventory item from data key.
	 * @param	mdoKey			The key of the data to create from
	 */
	public InventoryItem(String mdoKey) {
		this(MGlobal.data.getEntryFor(mdoKey, ItemMDO.class));
	}
	
	/** @return The display name of the item */
	public String getName() { return mdo.name; }
	
	/** @return The internal UID of the item */
	public String getKey() { return mdo.key; }
	
	/** @param delta The amt to change quantity by */
	public void changeQuantity(int delta) { this.quantity += delta; }
	
	/** @return The number of items of this type in the inventory */
	public int getQuantity() { return quantity; }
	
	/** @return The visual representation of this icon */
	public Graphic getIcon() { return icon; }
	
	/** @return The item description, if we're going to be using one */
	public String getDescription() { return mdo.gameDesc; }
	
	public String getNotesKey() { return mdo.set; }
	
	/**
	 * This method is going to be a mess... could it be a giant switch statement
	 * based on type? That would be horrible.
	 */
	public void use(FinishListener listener) {
		MGlobal.reporter.inform("Used the " + getName());
		switch(mdo.itemType) {
		case GRAPHIC:
			ItemGraphicDisplayer graphic = new ItemGraphicDisplayer(this);
			MGlobal.assets.loadAsset(graphic, "item graphic displayer");
			graphic.show(listener);
			return;
		case RADIO:
			String message = MGlobal.levelManager.getActive().getProperty("radio");
			if (message == null) message = "...can't... ..interference... ...later...";
			MGlobal.ui.getBlockingBox().blockText(MGlobal.screens.peek(), message);
			listener.onFinish();
			return;
		case KEY:
			// nothing right now
			return;
		}
	}
}
