/**
 *  EffectEnemyTarget.java
 *  Created on Apr 28, 2014 6:27:43 PM for project saga-desktop
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
import net.wombatrpgs.saga.rpg.Party;
import net.wombatrpgs.saga.rpg.Intent.IntentListener;
import net.wombatrpgs.sagaschema.rpg.abil.data.warheads.EffectEnemyTargetMDO;

/**
 * Superclass for abilities that target the enemy.
 */
public abstract class EffectEnemyTarget extends AbilEffect {
	
	protected EffectEnemyTargetMDO mdo;

	/**
	 * Inherited constructor.
	 * @param	mdo				The data to create from
	 * @param	item			The item to create for
	 */
	public EffectEnemyTarget(EffectEnemyTargetMDO mdo, CombatItem item) {
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
	 * @see net.wombatrpgs.saga.rpg.warheads.AbilEffect#assignRandomTargets
	 * (net.wombatrpgs.saga.rpg.Intent)
	 */
	@Override
	public void assignRandomTargets(Intent intent) {
		switch (mdo.projector) {
		case ALL_ENEMY:
			assignRandomParty(intent);
			break;
		case GROUP_ENEMY:
			assignRandomGroup(intent);
			break;
		case SINGLE_ENEMY:
			assignRandomTarget(intent);
			break;
		}
	}

	/**
	 * @see net.wombatrpgs.saga.rpg.warheads.AbilEffect#resolve
	 * (net.wombatrpgs.saga.rpg.Intent)
	 */
	@Override
	public final void resolve(Intent intent) {
		List<Chara> targets = new ArrayList<Chara>();
		Battle battle = intent.getBattle();
		Chara user = intent.getActor();
		String username = user.getName();
		Chara front = intent.getTargets().get(0);
		String frontname = front.getName();
		String itemname = intent.getItem().getName();
		
		// Acquire targets, print attack messages if needed
		switch (mdo.projector) {
		case SINGLE_ENEMY:
			if (!intent.isRecursive()) {
				battle.println(username + " attacks " + frontname + " by " + itemname + ".");
			}
			targets.add(front);
			break;
		case GROUP_ENEMY:
			if (!intent.isRecursive()) {
				battle.println(username + " attacks " + frontname + " by " + itemname + ".");
			}
			targets.addAll(intent.getTargets());
			break;
		case ALL_ENEMY:
			if (!intent.isRecursive()) {
				battle.println(username + " attacks by " + itemname + ".");
			}
			targets.addAll(intent.getTargets());
			break;
		}
		
		// Calculate parameters and then affect targets
		int power = calcPower(battle, intent.getActor());
		float roll = MGlobal.rand.nextFloat();
		for (Chara victim : targets) {
			if (hits(battle, user, victim, power, roll)) {
				onAffect(battle, intent, user, victim, power);
			}
		}
		
		// All done!
		onResolveComplete(battle, user);
	}
	
	/**
	 * Called when this effect happens to a victim. Things like hit chance,
	 * effect immunities, etc, should already be taken care of. The inflict
	 * power is calculated once per use from calcPower.
	 * @param	battle			The battle this effect occurs in
	 * @param	intent			The intent of the effect triggerer
	 * @param	user			The chara using this ability
	 * @param	victim			The chara to affect
	 * @param	power			The inflict power or damage power this usage
	 */
	protected abstract void onAffect(Battle battle, Intent intent, Chara user, Chara victim, int power);
	
	/**
	 * Calculates the power output of this attack by a user. Does not deal
	 * damage. Is affected by RNG. This is called once per attack usage. The
	 * outputted power could be inflict power or actual damage in HP. The result
	 * is later interpreted by onAffect.
	 * @param battle TODO
	 * @param	user			The character using this ability
	 * @return					The damage or inflict power of this effect
	 */
	protected abstract int calcPower(Battle battle, Chara user);
	
	/**
	 * Determines if this attack should hit a hypothetical target. Does not
	 * actually deal damage. Is not affected by RNG. Called once per victim.
	 * If the attack misses, it might be nice to print out an informative text.
	 * @param	battle			The battle context of this check
	 * @param	user			The character using the ability
	 * @param	target			The target to check against
	 * @param	power			The power of the attack, could be inflict chance
	 * @param	roll			The "chance to miss" roll of the user
	 * @return					True if the attack should hit, false for miss
	 */
	protected abstract boolean hits(Battle battle, Chara user, Chara target, int power, float roll);
	
	/**
	 * Called when attack resolution is complete. The effect can use this space
	 * to apply side effects or do cleanup. Default is nothing.
	 * @param	battle			The battle in which resolution is complete
	 * @param	user			The chara that used this ability
	 */
	protected void onResolveComplete(Battle battle, Chara user) {
		// default is nothing
	}

}
