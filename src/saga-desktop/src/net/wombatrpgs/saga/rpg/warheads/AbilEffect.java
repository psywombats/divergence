/**
 *  ItemEffect.java
 *  Created on Apr 23, 2014 3:04:20 AM for project saga-desktop
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.saga.rpg.warheads;

import java.util.Arrays;

import net.wombatrpgs.mgne.core.AssetQueuer;
import net.wombatrpgs.saga.rpg.CombatItem;
import net.wombatrpgs.saga.rpg.Intent;
import net.wombatrpgs.saga.rpg.Intent.IntentListener;
import net.wombatrpgs.sagaschema.rpg.abil.data.AbilEffectMDO;

/**
 * An effect brought about by a combat item. Superclass for a variety of
 * different warheads. Successor to the MRogue AbilEffect.
 */
public abstract class AbilEffect extends AssetQueuer {
	
	protected CombatItem item;
	protected AbilEffectMDO mdo;
	
	/**
	 * Constructs a combat effect for a combat item. This should be called by
	 * subclass constructors that are called from the factory.
	 * @param	mdo				The MDO this was create from, probably empty
	 * @param	item			The item to construct for
	 */
	public AbilEffect(AbilEffectMDO mdo, CombatItem item) {
		this.mdo = mdo;
		this.item = item;
	}
	
	/** @return item The owning item of this effect */
	public CombatItem getItem() { return item; }
	
	/**
	 * Check if this effect is applicable on the world map.
	 * @return					True if  can be used on the map
	 */
	public abstract boolean isMapUsable();
	
	/**
	 * Check if can be used in battle.
	 * @return					True if this item can be used in battle
	 */
	public abstract boolean isBattleUsable();
	
	/**
	 * Construct an intent from the player's input. This should use the battle
	 * to request user input, set the fields of a new input, then pass it to
	 * the listener. Could be a new intent or an edit. If the item isn't
	 * actually battle-usable, MGlobal an error or something.
	 * @param	intent			The current intent state, never null
	 * @param	listener		The listener to call when done
	 */
	public abstract void modifyIntent(Intent intent, IntentListener listener);
	
	/**
	 * Construct an intent for an enemy action. Just the target is needed,
	 * usually. MGlobal an error if not battle useable.
	 * @param	intent			The intent to select target for
	 */
	public abstract void modifyEnemyIntent(Intent intent);
	
	/**
	 * Called when a round involving this ability begins. Usually does nothing,
	 * but can sometimes apply pre-round effects like shielding.
	 * @param	intent			The intent that will be resolved later this turn
	 */
	public void onRoundStart(Intent intent) {
		// default is nothing
	}
	
	/**
	 * Carry out an intent in battle. Preconditions handled elsewhere.
	 * @param	intent			The intent to resolve
	 */
	public abstract void resolve(Intent intent);
	
	/**
	 * Array containment test.
	 * @param	flags			The set of flags to check
	 * @param	flag			The flag to check among them
	 * @return					True if the flag is present, false otherwise
	 */
	protected static boolean hasFlag(Object[] flags, Object flag) {
		return Arrays.asList(flags).contains(flag);
	}

}
