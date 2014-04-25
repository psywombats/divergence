/**
 *  EffectAttack.java
 *  Created on Apr 23, 2014 3:11:29 AM for project saga-desktop
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.saga.rpg.warheads;

import java.util.ArrayList;
import java.util.List;

import net.wombatrpgs.mgne.core.MGlobal;
import net.wombatrpgs.saga.rpg.Battle;
import net.wombatrpgs.saga.rpg.Chara;
import net.wombatrpgs.saga.rpg.CombatItem;
import net.wombatrpgs.saga.rpg.Intent;
import net.wombatrpgs.saga.rpg.Intent.IntentListener;
import net.wombatrpgs.saga.rpg.Party;
import net.wombatrpgs.sagaschema.rpg.abil.data.warheads.EffectAttackMDO;
import net.wombatrpgs.sagaschema.rpg.stats.Stat;

/**
 * Inflcict damage or status on the enemy.
 */
public class EffectAttack extends AbilEffect {
	
	protected EffectAttackMDO mdo;

	/**
	 * Creates a new effect.
	 * @param	mdo				The data to create effect from
	 * @param	item			The item to create effect for
	 */
	public EffectAttack(EffectAttackMDO mdo, CombatItem item) {
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
			Chara selected = null;
			if (intent.getTargets().size() > 0) {
				selected = intent.getTargets().get(0);
			}
			intent.getBattle().selectSingleEnemy(selected, intent.genDefaultListener(listener));
			break;
		case GROUP_ENEMY:
			int group = intent.inferSelectedGroup();
			intent.getBattle().selectEnemyGroup(group, intent.genDefaultListener(listener));
			break;
		case ALL_ENEMY:
			intent.clearTargets();
			intent.addTargets(intent.getBattle().getEnemy().getAll());
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
			Chara target = player.getFront(MGlobal.rand.nextInt(player.size()));
			intent.addTargets(target);
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
		Chara target = intent.getTargets().get(0);
		String targetname = target.getName();
		String itemname = intent.getItem().getName();
		switch (mdo.projector) {
		case SINGLE_ENEMY:
			battle.print(username + " attacks " + targetname + " by " + itemname + ".");
			targets.add(target);
			break;
		case GROUP_ENEMY:
			battle.print(username + " attacks " + targetname + " by " + itemname + ".");
			targets.addAll(intent.getTargets());
			break;
		case ALL_ENEMY:
			battle.println(username + " attacks by " + itemname + ".");
			targets.addAll(intent.getTargets());
			break;
		}
		int power = calcPower(intent.getActor());
		float roll = MGlobal.rand.nextFloat();
		for (Chara victim : targets) {
			// TODO: battle: animations in here somewhere
			String victimname = victim.getName();
			if (hits(intent.getActor(), victim, roll)) {
				int damage = calcDamage(power, victim);
				if (damage > 0) {
					battle.println(TAB + victimname + " takes " + damage + " damage.");
					boolean dead = victim.takeDamage(damage);
					if (dead) {
						battle.println(TAB + victimname + " is defeated.");
					}
				} else {
					battle.println(TAB + victimname + " takes no damage.");
				}
			} else {
				CombatItem shield = victim.getShield();
				if (shield == null) {
					battle.println(TAB + username + " misses " + victimname + ".");
				} else {
					String shieldname = shield.getName();
					battle.println(TAB + victimname + " deflects with " + shieldname + ".");
				}
			}
		}
	}
	
	/**
	 * Calculates the damage output of this attack by a user. Does not deal
	 * damage. Is affected by RNG.
	 * @param	user			The character using this ability
	 * @return					The damage of the attack, in HP
	 */
	protected int calcPower(Chara user) {
		// TODO: battle: weaknesses
		int temp = mdo.power;
		int power = 0;
		if (mdo.attackStat != null) {
			temp *= Math.ceil((float) user.get(mdo.attackStat) / 4f);
			power = user.get(mdo.attackStat);
		}
		power += (temp + MGlobal.rand.nextInt(temp));
		return power;
	}
	
	/**
	 * Calculates the damage this attack would do against a hypothetical target.
	 * Does not actually deal damage. Is not affected by RNG.
	 * @param	user			The character using the ability
	 * @param	target			The target to check against
	 * @return					An appropriate amount of damage to deal, in HP
	 */
	protected int calcDamage(int power, Chara target) {
		if (mdo.defendStat != null) {
			power -= target.get(mdo.defendStat);
		}
		return power;
	}
	
	/**
	 * Determines if this attack should hit a hypothetical target. Does not
	 * actually deal damage. Is not affected by RNG.
	 * @param	user			The character using the ability
	 * @param	target			The target to check against
	 * @param	roll			The "chance to miss" roll of the user
	 * @return					True if the attack should hit, false for miss
	 */
	protected boolean hits(Chara user, Chara target, float roll) {
		Stat agi = Stat.AGI;
		int temp = user.get(agi) - target.get(agi) - user.getShielding();
		float chance = (float) temp / 100f;
		return roll > chance;
	}

}
