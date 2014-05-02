/**
 *  Status.java
 *  Created on Apr 30, 2014 2:31:08 AM for project saga-desktop
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.saga.rpg.chara;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

import net.wombatrpgs.mgne.core.MGlobal;
import net.wombatrpgs.saga.core.SConstants;
import net.wombatrpgs.saga.rpg.Battle;
import net.wombatrpgs.sagaschema.rpg.StatusMDO;
import net.wombatrpgs.sagaschema.rpg.chara.data.Resistable;
import net.wombatrpgs.sagaschema.rpg.data.LethalityType;
import net.wombatrpgs.sagaschema.rpg.data.RecoverType;
import net.wombatrpgs.sagaschema.rpg.stats.Flag;
import net.wombatrpgs.sagaschema.rpg.stats.Stat;

/**
 * Status condtion.
 */
public class Status implements Resistable {
	
	protected StatusMDO mdo;
	
	protected static Map<String, Status> instances;
	
	/**
	 * Retrieves the status singleton object for the provided key. Creates the
	 * singleton if it doesn't already exist.
	 * @param	key				The key to the data for the status effect
	 * @return					The designated status singleton
	 */
	public static Status get(String key) {
		if (key == null)  {
			return null;
		}
		if (instances == null) {
			instances = new HashMap<String, Status>();
		}
		Status result = instances.get(key);
		if (result == null) {
			result = new Status(key);
			instances.put(key, result);
		}
		return result;
	}
	
	/** @see net.wombatrpgs.sagaschema.rpg.chara.data.Resistable#getResistFlags() */
	@Override public EnumSet<Flag> getResistFlags() { return EnumSet.of(mdo.resistFlag); }

	/** @see net.wombatrpgs.sagaschema.rpg.chara.data.Resistable#getWeakFlags() */
	@Override public EnumSet<Flag> getWeakFlags() { return EnumSet.noneOf(Flag.class); }
	
	/** @see net.wombatrpgs.sagaschema.rpg.chara.data.Resistable#isNegateable() */
	@Override public boolean isNegateable() { return true; }
	
	/** @return The short display name for this status */
	public String getTag() { return mdo.tag; }
	
	/** @return The full display name for this status */
	public String getName() { return mdo.fullName; }
	
	/** @return The relative weight of this status, the higher the better */
	public int getPriority() { return mdo.priority; }
	
	/** @return The recovery type of this status */
	public RecoverType getRecover() { return mdo.recover; }
	
	/** @return True if this status effect is the same as death */
	public boolean isDeadly() { return mdo.lethality == LethalityType.DEADLY; }

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return mdo.key;
	}
	
	/**
	 * Checks if this status is represented in a list of keys to statuses.
	 * @param	statusKeys		The list of keys to statusMDO to check against
	 * @return					True if this status is present, false otherwise
	 */
	public boolean isContainedIn(String[] statusList) {
		return Arrays.asList(statusList).contains(mdo.key);
	}
	
	/**
	 * Checks if this status halves the given stat.
	 * @param	stat			The stat to check
	 * @return					True if the stat is halved by this status
	 */
	public boolean reduces(Stat stat) {
		return Arrays.asList(mdo.halvedStats).contains(stat);
	}
	
	/**
	 * Tests if this character should act randomly in battle, selecting an
	 * arbitrary move and target. Rolls the RNG. Prints an informative message
	 * to explain why the user is acting.
	 * @param	battle			The battle this check takes place in
	 * @param	chara			The chara making this check
	 * @param	absolute		True if only 100% confusion rate should be used
	 * @param	silent			True to suppress the printout
	 * @return					True if the character should act randomly
	 */
	public boolean actsRandomly(Battle battle, Chara chara, boolean absolute, boolean silent) {
		int roll = absolute ? 99 : MGlobal.rand.nextInt(100);
		if (roll < mdo.randomChance) {
			if (!silent) {
				battle.println(chara.getName() + mdo.inflictString + ".");
			}
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Tests if this character should not do anything in battle. Rolls the RNG.
	 * Prints an informative message to explain why user is not acting.
	 * @param	battle			The battle this check takes place in
	 * @param	chara			The chara making this check
	 * @param	absolute		True if only 100% inaction rate should be used
	 * @param	silent			True to suppress the printout
	 * @return					True if the actor should not act
	 */
	public boolean preventsAct(Battle battle, Chara chara, boolean absolute, boolean silent) {
		int roll = absolute ? 99 : MGlobal.rand.nextInt(100);
		if (roll < mdo.preventChance) {
			if (!silent) {
				battle.println(chara.getName() + mdo.inflictString + ".");
			}
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Tests if this character should heal from this status during a battle
	 * round. Will remove the status if the result is positive. Rolls the RNG.
	 * Prints an informative message about the heal.
	 * @param	battle			The battle this check takes place in
	 * @param	chara			The chara making this check
	 * @return					True if the status was cured
	 */
	public boolean checkHeal(Battle battle, Chara chara) {
		if (MGlobal.rand.nextInt(100) < mdo.recoverChance) {
			battle.println("");
			heal(battle, chara);
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Inflicts this status condition upon a character. If battle is non-null,
	 * will print a message to that effect in the battle.
	 * @param	battle			The battle for context, or null if at map
	 * @param	chara			The chara to inflict upon
	 */
	public void inflict(Battle battle, Chara chara) {
		if (battle != null) {
			String tab = SConstants.TAB;
			battle.println(tab + chara.getName() + mdo.inflictString + ".");
		}
		chara.setStatus(this);
	}
	
	/**
	 * Returns the character to normal condition. If battle is non-null, will
	 * print a message to that effect in the battle
	 * @param	battle			The battle for context, or null if at map
	 * @param	chara			The chara to cure
	 */
	public void heal(Battle battle, Chara chara) {
		if (battle != null) {
			battle.println(chara.getName() + mdo.healString + ".");
		}
		chara.setStatus(null);
	}
	
	/**
	 * Called when a battle ends and this status is still on some sap. This
	 * should probably just heal them if needed. No message.
	 * @param	battle			The battle that just ended
	 * @param	chara			The chara that is afflicted
	 */
	public void onBattleEnd(Battle battle, Chara chara) {
		if (mdo.recover == RecoverType.RECOVER_AFTER_BATTLE) {
			chara.setStatus(null);
		}
	}

	/**
	 * Internal constructor from data.
	 * @param	mdo				The data to create from
	 */
	protected Status(StatusMDO mdo) {
		this.mdo = mdo;
	}
	
	/**
	 * Internal constructor from data key.
	 * @param	key				The database key to the data to create from
	 */
	protected Status(String key) {
		this(MGlobal.data.getEntryFor(key, StatusMDO.class));
	}

}
