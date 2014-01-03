/**
 *  ItemGenerator.java
 *  Created on Oct 20, 2013 7:21:29 AM for project mrogue-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mrogue.maps;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.assets.AssetManager;

import net.wombatrpgs.mrogue.core.MGlobal;
import net.wombatrpgs.mrogue.core.Queueable;
import net.wombatrpgs.mrogue.rpg.item.Item;
import net.wombatrpgs.mrogue.rpg.item.ItemEvent;
import net.wombatrpgs.mrogue.rpg.item.ItemFactory;
import net.wombatrpgs.mrogueschema.items.ItemGeneratorMDO;

/**
 * Generates items for a level.
 */
public class ItemGenerator implements Queueable {
	
	protected ItemGeneratorMDO mdo;
	protected Level parent;
	protected List<Item> loaderDummies;
	
	/**
	 * Creates a new item generator given parent, data.
	 * @param	parent			The parent level to generate for
	 * @param	mdo				The data to generate from
	 */
	public ItemGenerator(Level parent, ItemGeneratorMDO mdo) {
		this.parent = parent;
		this.mdo = mdo;
		loaderDummies = new ArrayList<Item>();
		for (String key : mdo.items) {
			loaderDummies.add(ItemFactory.createItem(key));
		}
	}

	/**
	 * @see net.wombatrpgs.mrogue.core.Queueable#queueRequiredAssets
	 * (com.badlogic.gdx.assets.AssetManager)
	 */
	@Override
	public void queueRequiredAssets(AssetManager manager) {
		for (Item dummy : loaderDummies) {
			dummy.queueRequiredAssets(manager);
		}
	}

	/**
	 * @see net.wombatrpgs.mrogue.core.Queueable#postProcessing
	 * (com.badlogic.gdx.assets.AssetManager, int)
	 */
	@Override
	public void postProcessing(AssetManager manager, int pass) {
		for (Item dummy : loaderDummies) {
			dummy.postProcessing(manager, pass);
		}
	}
	
	/**
	 * Magics a suitable item out of thin air!!
	 * @return					The magically generated item
	 */
	public Item createItem() {
		String key = mdo.items[MGlobal.rand.nextInt(mdo.items.length)];
		Item i = ItemFactory.createItem(key);
		if (MGlobal.rand.nextFloat() < i.getRarity()) {
			return createItem();
		} else {
			i.postProcessing(MGlobal.assetManager, 0);
		}
		return i;
	}
	
	/**
	 * Creates an item event for you.
	 * @return					The generated item
	 */
	public ItemEvent createEvent() {
		Item item = createItem();
		ItemEvent event = new ItemEvent(parent, item, 0, 0);
		return event;
	}
	
	/**
	 * Spawns an item somewhere on the floor of the map.	
	 */
	public void spawnItem() {
		ItemEvent event = createEvent();
		parent.addEvent(event);
		for (int i = 0; i < 100; i += 1) {
			if (!parent.isTilePassable(event, event.getTileX(), event.getTileY())) {
				event.setTileX(MGlobal.rand.nextInt(parent.getWidth()-2) + 1);
				event.setTileY(MGlobal.rand.nextInt(parent.getHeight()-2) + 1);
			} else {
				return;
			}
		}
		MGlobal.reporter.warn("Waited too long to spawn item on " + parent);
	}
	
	/**
	 * Spawns the first wave of treasure on the floor.
	 */
	public void spawnOnCreate() {
		for (int i = 0; i < mdo.firstTreasure; i += 1) spawnItem();
	}

}
