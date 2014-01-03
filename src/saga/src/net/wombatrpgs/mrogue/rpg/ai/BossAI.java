/**
 *  BossAI.java
 *  Created on Oct 27, 2013 7:05:24 AM for project mrogue-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mrogue.rpg.ai;

import net.wombatrpgs.mrogue.rpg.Boss;
import net.wombatrpgs.mrogue.rpg.CharacterEvent;
import net.wombatrpgs.mrogue.rpg.abil.AbilTeleport;
import net.wombatrpgs.mrogue.rpg.act.ActStepHero;
import net.wombatrpgs.mrogue.rpg.act.ActWait;
import net.wombatrpgs.mrogue.rpg.act.ActWander;
import net.wombatrpgs.mrogue.rpg.ai.seq.RoutineChanceAbility;
import net.wombatrpgs.mrogue.rpg.ai.seq.RoutineOffenseAbility;
import net.wombatrpgs.mrogueschema.characters.ai.BossIntelligenceMDO;

/**
 * Creates boss-like intelligence!! Oh yeah!!!
 */
public class BossAI extends BTSelector {
	
	protected BossIntelligenceMDO mdo;
	protected Boss boss;

	/**
	 * Creates boss AI for a boss.
	 * @param	actor				The boss to generate for
	 */
	public BossAI(CharacterEvent actor, BossIntelligenceMDO mdo) {
		super(actor);
		this.mdo = mdo;
		boss = (Boss) actor;		// you gotta be a boss to use dis AI!!
		
		BTSequence idleSeq = new BTSequence(actor);
		idleSeq.addChild(new BTCondition(actor) {
			@Override protected boolean isMet() {
				return !boss.hasBeenSighted();
			}
		});
		idleSeq.addChild(new BTAction(actor, new ActWait(), true));
		
		BTSelector fightRou = new BTSelector(actor);
		fightRou.addChild(new RoutineChanceAbility(actor, AbilTeleport.class, .1f));
		fightRou.addChild(new RoutineOffenseAbility(actor, .5f));
		fightRou.addChild(new BTAction(actor, new ActStepHero(actor), true));
		fightRou.addChild(new BTAction(actor, new ActWander(actor), true));
		
		addChild(idleSeq);
		addChild(fightRou);
	}

}
