/**
 *  ItemFactory.java
 *  Created on Oct 20, 2013 6:57:13 AM for project mrogue-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mrogue.rpg.item;

import net.wombatrpgs.mrogue.core.MGlobal;
import net.wombatrpgs.mrogueschema.items.GeneratedPotionMDO;
import net.wombatrpgs.mrogueschema.items.GeneratedSpellbookMDO;
import net.wombatrpgs.mrogueschema.items.PotionMDO;
import net.wombatrpgs.mrogueschema.items.SpellbookMDO;
import net.wombatrpgs.mrogueschema.items.data.ItemMDO;

/**
 * Converts MDOs to items.
 */
public class ItemFactory {
	
	/**
	 * Creates a new item from a key to data.
	 * @param	key				The key of the data to generate from
	 * @return					The item generated
	 */
	public static Item createItem(String key) {
		return createItem(MGlobal.data.getEntryFor(key, ItemMDO.class));
	}
	
	/**
	 * Creates an item from data.
	 * @param	mdo				The data to generate from
	 * @return					The item generated
	 */
	public static Item createItem(ItemMDO mdo) {
		if (SpellbookMDO.class.isAssignableFrom(mdo.getClass())) {
			return new Spellbook((SpellbookMDO) mdo);
		} else if (GeneratedSpellbookMDO.class.isAssignableFrom(mdo.getClass())) {
			return GeneratedSpellbook.construct((GeneratedSpellbookMDO) mdo);
		} else if (GeneratedPotionMDO.class.isAssignableFrom(mdo.getClass())) {
			return new GeneratedPotion((GeneratedPotionMDO) mdo);
		} else if (PotionMDO.class.isAssignableFrom(mdo.getClass())) {
			return new Potion((PotionMDO) mdo);
		} else {
			MGlobal.reporter.err("Unknown item mdo class: " + mdo);
			return null;
		}
	}

}
