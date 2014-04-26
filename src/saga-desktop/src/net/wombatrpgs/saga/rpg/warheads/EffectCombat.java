/**
 *  EffectCombat.java
 *  Created on Apr 25, 2014 11:30:32 AM for project saga-desktop
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.saga.rpg.warheads;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.wombatrpgs.mgne.core.MGlobal;
import net.wombatrpgs.saga.core.SConstants;
import net.wombatrpgs.saga.rpg.Battle;
import net.wombatrpgs.saga.rpg.Chara;
import net.wombatrpgs.saga.rpg.CombatItem;
import net.wombatrpgs.saga.rpg.Intent;
import net.wombatrpgs.saga.rpg.Party;
import net.wombatrpgs.saga.rpg.Intent.IntentListener;
import net.wombatrpgs.saga.rpg.warheads.EffectDefend.DefendResult;
import net.wombatrpgs.saga.rpg.warheads.EffectDefend.PostDefend;
import net.wombatrpgs.sagaschema.rpg.abil.data.OffenseFlag;
import net.wombatrpgs.sagaschema.rpg.abil.data.warheads.EffectCombatMDO;
import net.wombatrpgs.sagaschema.rpg.chara.data.Race;
import net.wombatrpgs.sagaschema.rpg.stats.Flag;
import net.wombatrpgs.sagaschema.rpg.stats.Stat;

/**
 * Superclass for some common combat ability effects.
 */
public abstract class EffectCombat extends AbilEffect {
	
	protected EffectCombatMDO mdo;

	/**
	 * Inherited constructor.
	 * @param	mdo				The data to create from
	 * @param	item			The item to create for
	 */
	public EffectCombat(EffectCombatMDO mdo, CombatItem item) {
		super(mdo, item);
		this.mdo = mdo;
	}

	/** @see net.wombatrpgs.saga.rpg.warheads.AbilEffect#isMapUsable() */
	@Override public boolean isMapUsable() { return false; }

	/** @see net.wombatrpgs.saga.rpg.warheads.AbilEffect#isBattleUsable() */
	@Override public boolean isBattleUsable() { return true; }

	/**
	 * @see net.wombatrpgs.saga.rpg.warheads.AbilEffect#modifyIntent
	 * (net.wombatrpgs.saga.rpg.Intent, net.wombatrpgs.saga.rpg.Intent.IntentListener)
	 */
	@Override
	public void modifyIntent(final Intent intent, final IntentListener listener) {
		switch (mdo.projector) {
		case SINGLE_ENEMY:
			intent.clearTargets();
			Chara selected = null;
			if (intent.getTargets().size() > 0) {
				selected = intent.getTargets().get(0);
			}
			intent.getBattle().selectSingleEnemy(selected, intent.genDefaultListener(listener));
			break;
		case GROUP_ENEMY:
			int group = intent.inferEnemy();
			intent.clearTargets();
			intent.getBattle().selectEnemyGroup(group, intent.genDefaultListener(listener));
			break;
		case ALL_ENEMY:
			intent.clearTargets();
			intent.addTargets(intent.getBattle().getEnemy().getAll());
			listener.onIntent(intent);
			break;
		}
	}

	/**
	 * @see net.wombatrpgs.saga.rpg.warheads.AbilEffect#modifyEnemyIntent
	 * (net.wombatrpgs.saga.rpg.Intent)
	 */
	@Override
	public void modifyEnemyIntent(Intent intent) {
		Party player = intent.getBattle().getPlayer();
		switch (mdo.projector) {
		case SINGLE_ENEMY: case GROUP_ENEMY:
			List<Integer> candidates = new ArrayList<Integer>();
			for (int i = 0; i < player.groupCount(); i += 1) {
				if (player.getFront(i).isAlive()) {
					candidates.add(i);
				}
			}
			int index = candidates.get(MGlobal.rand.nextInt(candidates.size()));
			intent.addTargets(player.getFront(index));
			break;
		case ALL_ENEMY:
			intent.addTargets(player.getAll());
			break;
		}
	}

	/**
	 * @see net.wombatrpgs.saga.rpg.warheads.AbilEffect#resolve
	 * (net.wombatrpgs.saga.rpg.Intent)
	 */
	@Override
	public void resolve(Intent intent) {
		List<Chara> targets = new ArrayList<Chara>();
		Battle battle = intent.getBattle();
		Chara user = intent.getActor();
		String username = user.getName();
		Chara front = intent.getTargets().get(0);
		String frontname = front.getName();
		String itemname = intent.getItem().getName();
		String tab = SConstants.TAB;
		if (!intent.isRecursive()) {
			switch (mdo.projector) {
			case SINGLE_ENEMY:
				battle.println(username + " attacks " + frontname + " by " + itemname + ".");
				targets.add(front);
				break;
			case GROUP_ENEMY:
				battle.println(username + " attacks " + frontname + " by " + itemname + ".");
				targets.addAll(intent.getTargets());
				break;
			case ALL_ENEMY:
				battle.println(username + " attacks by " + itemname + ".");
				targets.addAll(intent.getTargets());
				break;
			}
		}
		int power = calcPower(intent.getActor());
		float roll = MGlobal.rand.nextFloat();
		for (Chara victim : targets) {
			// TODO: battle: animations in here somewhere
			String victimname = victim.getName();
			if (effect(OffenseFlag.ONLY_AFFECT_UNDEAD)) {
				if (!user.is(Flag.UNDEAD)) {
					battle.println(tab + "Nothing happens.");
					continue;
				}
			}
			if (effect(OffenseFlag.ONLY_AFFECT_HUMANS)) {
				if (user.getRace() != Race.HUMAN) {
					battle.println(tab + "Nothing happens.");
					continue;
				}
			}
			if (hits(battle, intent.getActor(), victim, roll)) {
				
				// Collect list of all effects triggered by this attack
				List<PostDefend> callbacks = new ArrayList<PostDefend>();
				boolean countered = false;
				for (EffectDefend defense : battle.getDefenses(victim)) {
					DefendResult result = defense.onAttack(victim, intent, mdo.damType);
					if (result.callback != null) {
						callbacks.add(result.callback);
					}
					if (result.countered) {
						countered = true;
						break;
					}
				}
				
				if (countered) {
					// This attack has been blocked
					// nothing?
				} else if (weak(victim) && effect(OffenseFlag.CRITICAL_ON_WEAKNESS)) {
					// This attack insta-killed the victim
					victim.damage(victim.get(Stat.MHP));
					battle.println("");
					battle.println("");
					battle.println(tab + tab + "CRITICAL HIT!");
					battle.println("");
					battle.println(frontname + " is dead.");
					battle.checkDeath(victim, true);
				} else {
					// This attack may have damaged the victim
					int damage = calcDamage(power, victim);
					if (resists(victim) && !effect(OffenseFlag.IGNORE_RESISTANCES)) {
						if (mdo.damType.isNegateable()) {
							battle.println(tab + frontname + " is resistant to " + itemname + ".");
							damage = 0;
						} else {
							damage /= 2;
						}
					}
					if (damage > 0) {
						battle.println(tab + victimname + " takes " + damage + " damage.");
						victim.damage(damage);
						battle.checkDeath(victim, false);
						if (effect(OffenseFlag.DRAIN_LIFE) && !victim.is(Flag.UNDEAD)) {
							int healed = user.heal(damage);
							if (healed > 0) {
								battle.println(tab + username + " recovers " + damage + " HP.");
							}
						}
					} else {
						battle.println(tab + victimname + " takes no damage.");
					}
					if (effect(OffenseFlag.STUNS_ON_HIT) && victim.isAlive()) {
						if (battle.cancelAction(victim)) {
							battle.println(tab + "A stunning hit!");
						}
					}
				}
				
				// Battle damage calculations have been resolved, now callbacks
				for (PostDefend callback : callbacks) {
					callback.onTriggerResolve(victim, intent);
				}
				
			} else {
				
				// We missed... why?
				// TODO: battle: something is buggy with shield+counter?
				List<EffectDefend> defenses = battle.getDefenses(victim);
				if (defenses.size() == 0) {
					battle.println(tab + username + " misses " + victimname + ".");
				} else {
					Collections.sort(defenses);
					EffectDefend blocker = defenses.get(0);
					CombatItem shield = blocker.getItem();
					String shieldname = shield.getName();
					battle.println(tab + victimname + " deflects by " + shieldname + ".");
				}
			}
		}
		if (effect(OffenseFlag.KILLS_USER)) {
			user.damage(user.get(Stat.HP));
			battle.checkDeath(user, false);
		}
	}
	
	/**
	 * Calculates the damage output of this attack by a user. Does not deal
	 * damage. Is affected by RNG.
	 * @param	user			The character using this ability
	 * @return					The damage of the attack, in HP
	 */
	protected abstract int calcPower(Chara user);
	
	/**
	 * Calculates the damage this attack would do against a hypothetical target.
	 * Does not actually deal damage. Is not affected by RNG.
	 * @param	user			The character using the ability
	 * @param	target			The target to check against
	 * @return					An appropriate amount of damage to deal, in HP
	 */
	protected abstract int calcDamage(int power, Chara target);
	
	/**
	 * Determines if this attack should hit a hypothetical target. Does not
	 * actually deal damage. Is not affected by RNG.
	 * @param	battle			The battle context of this check
	 * @param	user			The character using the ability
	 * @param	target			The target to check against
	 * @param	roll			The "chance to miss" roll of the user
	 * @return					True if the attack should hit, false for miss
	 */
	protected abstract boolean hits(Battle battle, Chara user, Chara target, float roll);
	
	/**
	 * Checks if the effect flag is present.
	 * @param	flag			The flag to check
	 * @return					True if the flag is present, false otherwise
	 */
	protected boolean effect(OffenseFlag flag) {
		return hasFlag(mdo.sideEffects, flag);
	}
	
	/**
	 * Checks if the target resists this effect.
	 * @param	target			The target to check
	 * @return					True if the target is resistant, else false
	 */
	protected boolean resists(Chara target) {
		return target.resists(mdo.damType);
	}
	
	/**
	 * Checks if the target is weak to this effect.
	 * @param	target			The target to check
	 * @return					True if the target is weak, else false
	 */
	protected boolean weak(Chara target) {
		return target.isWeakTo(mdo.damType);
	}
	
	/**
	 * Checks the shield value of a character.
	 * @param	battle			The battle context of this call
	 * @param	chara			The character to check
	 * @return					The shield value dodge boost for that character
	 */
	protected int shielding(Battle battle, Chara chara) {
		int shielding = 0;
		for (EffectDefend defense : battle.getDefenses(chara)) {
			shielding += defense.getShielding();
		}
		return shielding;
	}

}
