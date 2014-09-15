/**
 *  ShopInventory.java
 *  Created on May 20, 2014 2:37:00 AM for project saga-desktop
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.saga.rpg.items;

import java.util.List;

import net.wombatrpgs.mgne.core.MGlobal;
import net.wombatrpgs.saga.core.SConstants;
import net.wombatrpgs.saga.core.SGlobal;
import net.wombatrpgs.sagaschema.rpg.abil.CombatItemMDO;

/**
 * An inventory held by the shop. Handles a collection of items and samples.
 */
public class ShopInventory extends Inventory {
	
	protected static final int MAX_SHOP_SIZE = 10;
	
	protected List<CombatItemMDO> mdos;
	
	/**
	 * Creates a new shop that carries the given items. Order of the items is
	 * important and will be the same as the display order.
	 * @param	mdos			The list of MDOs of the items to carry
	 */
	public ShopInventory(List<CombatItemMDO> mdos) {
		super(MAX_SHOP_SIZE);
		this.mdos = mdos;
		
		for (CombatItemMDO mdo : mdos) {
			add(new CombatItem(mdo));
		}
		if (mdos.size() > capacity) {
			MGlobal.reporter.warn("Too many items in shop: " + mdos);
		}
	}

	/**
	 * @see net.wombatrpgs.saga.rpg.items.Inventory#valueAt(int)
	 */
	@Override
	public int valueAt(int i) {
		return items[i].getCost(false);
	}
	
	/**
	 * Buys the item from the indicated slot and adds it to the player's
	 * inventory. Deducts the GP cost. Assumes the hero party has enough room
	 * for the item in the first place, and enough GP to buy.
	 * @param	i				The slot of the item to buy
	 */
	public void buyAt(int i) {
		CombatItem item = new CombatItem(mdos.get(i));
		SGlobal.heroes.addItem(item);
		SGlobal.heroes.addGP(-1 * item.getCost(false));
		MGlobal.sfx.play(SConstants.SFX_GET);
	}

}
