/**
 *  ItemEffect.java
 *  Created on Apr 23, 2014 3:04:20 AM for project saga-desktop
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.saga.rpg.warheads;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.wombatrpgs.mgne.core.AssetQueuer;
import net.wombatrpgs.mgne.core.MGlobal;
import net.wombatrpgs.saga.rpg.CombatItem;
import net.wombatrpgs.saga.rpg.Intent;
import net.wombatrpgs.saga.rpg.Intent.IntentListener;
import net.wombatrpgs.saga.rpg.chara.Chara;
import net.wombatrpgs.saga.rpg.chara.Party;
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
	 * Assigns random targets to the intent as if confused. It will already have
	 * been cleared.
	 * @param	intent			The intent to modify.
	 */
	public abstract void assignRandomTargets(Intent intent);
	
	/**
	 * Carry out an intent in battle. Preconditions handled elsewhere.
	 * @param	intent			The intent to resolve
	 */
	public abstract void resolve(Intent intent);
	
	/**
	 * Called when a round involving this ability begins. Usually does nothing,
	 * but can sometimes apply pre-round effects like shielding.
	 * @param	intent			The intent that will be resolved later this turn
	 */
	public void onRoundStart(Intent intent) {
		// default is nothing
	}
	
	/**
	 * Array containment test.
	 * @param	flags			The set of flags to check
	 * @param	flag			The flag to check among them
	 * @return					True if the flag is present, false otherwise
	 */
	protected static boolean hasFlag(Object[] flags, Object flag) {
		return Arrays.asList(flags).contains(flag);
	}
	
	/**
	 * Randomly adds one party or another to the intent target list.
	 * @param	intent			The intent to add to
	 */
	protected void assignRandomParty(Intent intent) {
		Party enemy = intent.getBattle().getEnemy();
		Party player = intent.getBattle().getPlayer();
		if (MGlobal.rand.nextBoolean()) {
			intent.addTargets(player.getAll());
		} else {
			intent.addTargets(enemy.getAll());
		}
	}
	
	/**
	 * Randomly adds a group (player or enemy) to the intent target list.
	 * @param	intent			The intent to add to
	 */
	protected void assignRandomGroup(Intent intent) {
		List<Party> parties = new ArrayList<Party>();
		parties.add(intent.getBattle().getEnemy());
		parties.add(intent.getBattle().getPlayer());
		List<List<Chara>> groups = new ArrayList<List<Chara>>();
		for (Party party : parties) {
			for (int i = 0; i < party.groupCount(); i += 1) {
				if (party == intent.getBattle().getEnemy()) {
					if (intent.getBattle().isEnemyAlive(i)) {
						groups.add(party.getGroup(i));
					}
				}
				if (party == intent.getBattle().getPlayer()) {
					if (intent.getBattle().isPlayerAlive(i)) {
						groups.add(party.getGroup(i));
					}
				}
			}
		}
		int index = MGlobal.rand.nextInt(groups.size());
		intent.addTargets(groups.get(index));
	}
	
	/**
	 * Randomly adds a single living target to the intent target list.
	 * @param	intent			The intent to add to
	 */
	protected void assignRandomTarget(Intent intent) {
		List<Chara> charas = new ArrayList<Chara>();
		Party enemy = intent.getBattle().getEnemy();
		Party player = intent.getBattle().getPlayer();
		charas.addAll(player.getAll());
		charas.addAll(enemy.getAll());
		Chara target;
		do {
			target = charas.get(MGlobal.rand.nextInt(charas.size()));
		} while (target.isDead());
		intent.addTargets(target);
	}

}
