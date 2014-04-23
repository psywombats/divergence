/**
 *  CombatItem.java
 *  Created on Apr 12, 2014 2:49:18 AM for project saga-desktop
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.saga.rpg;

import net.wombatrpgs.mgne.core.AssetQueuer;
import net.wombatrpgs.mgne.core.MGlobal;
import net.wombatrpgs.saga.rpg.Intent.IntentListener;
import net.wombatrpgs.saga.rpg.warheads.AbilEffect;
import net.wombatrpgs.saga.rpg.warheads.AbilEffectFactory;
import net.wombatrpgs.saga.ui.ItemSelector;
import net.wombatrpgs.sagaschema.rpg.abil.CombatItemMDO;

/**
 * Represents the combat item MDO. This could be an item or ability, but either
 * way, it can be equipped to a character and potentially have a use in combat.
 */
// TODO: saga: add the combat aspects of this class
public class CombatItem extends AssetQueuer {
	
	protected CombatItemMDO mdo;
	
	protected Chara owner;	// null for party
	protected AbilEffect effect;
	protected String name;
	protected int uses;
	
	/**
	 * Creates a new combat item from data.
	 * @param	mdo				The data to create from
	 */
	public CombatItem(CombatItemMDO mdo) {
		this.mdo = mdo;
		uses = mdo.uses;
		name = MGlobal.charConverter.convert(mdo.abilityName);
		effect = AbilEffectFactory.create(mdo.warhead.key, this);
		assets.add(effect);
	}
	
	/**
	 * Creates a new combat item from a key to data.
	 * @param	key				The key to the data to create from
	 */
	public CombatItem(String key) {
		this(MGlobal.data.getEntryFor(key, CombatItemMDO.class));
	}
	
	/** @return The ability name of this item */
	public String getName() { return name; }
	
	/** @return True if this item has unlimited uses */
	public boolean isUnlimited() { return mdo.uses == 0; }
	
	/** @return The number of uses remaining on this item */
	public int getUses() { return uses; }
	
	/** @param owner The new owner of this item, or null for party */
	public void setOwner(Chara owner) { this.owner = owner; }
	
	/**
	 * Called when this item is used from the map menu.
	 * @param	selector		The menu that invoked this item
	 */
	public void onMapUse(ItemSelector selector) {
		// TODO: saga: onmapuse
	}
	
	/**
	 * Check if this item can be used on the world map.
	 * @return					True if this item can be used on the map
	 */
	public boolean isMapUsable() {
		return effect.isMapUsable();
	}
	
	/**
	 * Check if this item can be used in battle (a true combat item!)
	 * @return					True if this item can be used in battle
	 */
	public boolean isBattleUsable() {
		return effect.isBattleUsable();
	}
	
	/**
	 * Construct an intent from player input and warhead. This is called by the
	 * battle when this item is selected for use, and later passed back when
	 * the item is used in battle playback. The intent might already have some
	 * preselected options and the player is editing it, or it might be raw.
	 * @param	intent			The currently recorded intent state, never null
	 * @param	listener		The listener to give intent to when finished
	 */
	public void modifyIntent(Intent intent, IntentListener listener) {
		effect.modifyIntent(intent, listener);
	}

}
