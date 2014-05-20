/**
 *  CombatItem.java
 *  Created on Apr 12, 2014 2:49:18 AM for project saga-desktop
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.saga.rpg.items;

import net.wombatrpgs.mgne.core.AssetQueuer;
import net.wombatrpgs.mgne.core.MGlobal;
import net.wombatrpgs.saga.rpg.Intent;
import net.wombatrpgs.saga.rpg.Intent.IntentListener;
import net.wombatrpgs.saga.rpg.stats.SagaStats;
import net.wombatrpgs.saga.rpg.warheads.AbilEffect;
import net.wombatrpgs.saga.rpg.warheads.AbilEffectFactory;
import net.wombatrpgs.saga.ui.ItemSelector;
import net.wombatrpgs.sagaschema.rpg.abil.CombatItemMDO;

/**
 * Represents the combat item MDO. This could be an item or ability, but either
 * way, it can be equipped to a character and potentially have a use in combat.
 */
public class CombatItem extends AssetQueuer {
	
	protected CombatItemMDO mdo;
	
	protected Inventory container;
	protected AbilEffect effect;
	protected String name;
	protected SagaStats stats, robostats;
	protected int uses;
	
	/**
	 * Creates a new combat item from data.
	 * @param	mdo				The data to create from
	 */
	public CombatItem(CombatItemMDO mdo) {
		this.mdo = mdo;
		uses = mdo.uses;
		name = MGlobal.charConverter.convert(mdo.abilityName);
		
		stats = new SagaStats();
		robostats = new SagaStats(mdo.robostats);
		
		if (mdo.warhead != null) {
			effect = AbilEffectFactory.create(mdo.warhead.key, this);
			assets.add(effect);
		}
	}
	
	/**
	 * Creates a combat item with a fixed effect. Does not queue the effect.
	 * @param	mdo				The data to create from
	 * @param	effect			The effect to create with
	 */
	public CombatItem(CombatItemMDO mdo, AbilEffect effect) {
		this(mdo);
		this.effect = effect;
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
	
	/** @return True if this item can be sold to a shop, false otherwise */
	public boolean isSellable() { return mdo.cost > 0; }
	
	/** @param container The new holder of this item */
	public void setContainer(Inventory container) { this.container = container; }
	
	/** @return The global stats this item imbues when equipped */
	public SagaStats getStatset() { return stats; }
	
	/** @return The robot-specific stat boosts this item imbues when equipped */
	public SagaStats getRobostats() { return robostats; }
	
	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return getName();
	}

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
	
	/**
	 * Construct an intent for an enemy. This is the AI method, anything that
	 * affects how the AI uses this specific item should be changed here. This
	 * differs from the player method because no callback is needed, AI decides
	 * immediately. Just the target needs to be selected usually.
	 * @param	intent			The intent to modify
	 */
	public void modifyEnemyIntent(Intent intent) {
		intent.setItem(this);
		effect.modifyEnemyIntent(intent);
	}
	
	/**
	 * Picks random targets for this item as if the owner were confused and
	 * attacking charas at random from both sides... hm, when could this be
	 * useful? Rather than return target list, add them to the intent. If no
	 * targets are available, do nothing.
	 * @param	intent			The intent to modify
	 */
	public void assignRandomTargets(Intent intent) {
		intent.setItem(this);
		intent.clearTargets();
		effect.assignRandomTargets(intent);
	}
	
	/**
	 * Called when a round begins in which this item is involved. This is used
	 * to do things that should occur at the round start, before the item is
	 * actually resolved. Most of the times it can do nothing.
	 * @param	intent			The intent that will be resolved later this turn
	 */
	public void onRoundStart(Intent intent) {
		effect.onRoundStart(intent);
	}
	
	/**
	 * Resolves a previous intent given in battle. Preconditions such as status
	 * of user are handled elsewhere. Just do what this item does.
	 * @param	intent			The intent to resolve
	 */
	public void resolve(Intent intent) {
		effect.resolve(intent);
		deductUse();
	}
	
	/**
	 * Halves the uses on this item, like if a robot equipped it.
	 */
	public void halveUses() {
		uses = Math.round((float) uses / 2f);
		checkDiscard();
	}
	
	/**
	 * Gives the item an extra use... like if someone reflected the attack.
	 */
	public void incrementUses() {
		uses += 1;
	}
	
	/**
	 * Calculates the price to buy the item at the current number of uses. Uses
	 * the standard uses/max * cost formula.
	 * @param	sellMode		True if the item is being sold back at 1/2 cost
	 * @return					The cost to buy the item, in GP
	 */
	public int getCost(boolean sellMode) {
		int cost = mdo.cost;
		if (mdo.uses > 0) {
			cost *= uses;
			cost /= mdo.uses;
		}
		if (sellMode) {
			cost /= 2;
		}
		return cost;
	}
	
	/**
	 * Calculates the price to buy the item at the current number of uses by
	 * calling the relevant inventory's price valuation. Will blow up if the
	 * item isn't owned by anything.
	 * @return					The cost to buy the item, in GP
	 */
	public int getCost() {
		return container.valueOf(this);
	}
	
	/**
	 * Called internally when the item is used. Simulates wear on the item and
	 * removes it if it breaks.
	 */
	protected void deductUse() {
		uses -= 1;
		checkDiscard();
	}
	
	/**
	 * Checks if this item should be discarded, and if so, discards it.
	 */
	protected void checkDiscard() {
		if (uses <= 0 && mdo.uses > 0) {
			container.drop(this);
		}
	}

}
