/**
 *  AbilEffect.java
 *  Created on Oct 18, 2013 4:40:25 AM for project mrogue-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mrogue.rpg.abil;

import java.util.List;

import net.wombatrpgs.mrogue.maps.Level;
import net.wombatrpgs.mrogue.rpg.CharacterEvent;
import net.wombatrpgs.mrogue.rpg.GameUnit;
import net.wombatrpgs.mrogue.rpg.travel.Step;
import net.wombatrpgs.mrogue.rpg.travel.StepWait;
import net.wombatrpgs.mrogueschema.characters.data.AbilityEffectMDO;

/**
 * This is the core thing behind an ability.
 */
public abstract class AbilEffect {
	
	protected AbilityEffectMDO mdo;
	protected Ability abil;
	protected CharacterEvent actor;
	protected Level parent;
	
	/**
	 * Creates a new effect from data for an ability. Also sets some helpful
	 * fields, for convenience. This will probably be the source of an
	 * irritating bug somewhere down the road. FAKEEDIT: caught it early! No
	 * longer does this, it's done somewhere else.
	 * @param	mdo				The mdo to generate from
	 * @param	abil			The parent ability
	 */
	public AbilEffect(AbilityEffectMDO mdo, Ability abil) {
		this.mdo = mdo;
		this.abil = abil;
	}
	
	/**
	 * Returns the physical animation of this effect. By default, returns
	 * nothing. This is where those cool fire effects should go!
	 * @return					The step animation of this effect
	 */
	public Step getStep() {
		return new StepWait(abil.getActor());
	}
	
	/**
	 * Wrapper for an internal abstract method. Performs the universal
	 * abileffect functionality, which should be minimal. The fancy stuff
	 * belongs in the Ability owner.
	 * @param	targets			The dopes to affect
	 */
	final public void act(List<GameUnit> targets) {
		this.actor = abil.getActor();
		this.parent = actor.getParent();
		internalAct(targets);
	}
	
	/**
	 * Actually perform the ability on some targets.
	 * @param	targets			The dopes to affect
	 */
	protected abstract void internalAct(List<GameUnit> targets);

}
