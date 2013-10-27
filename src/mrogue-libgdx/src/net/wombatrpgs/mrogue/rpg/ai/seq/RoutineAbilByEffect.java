/**
 *  RoutineTeleport.java
 *  Created on Oct 27, 2013 7:25:57 AM for project mrogue-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mrogue.rpg.ai.seq;

import net.wombatrpgs.mrogue.rpg.CharacterEvent;
import net.wombatrpgs.mrogue.rpg.abil.AbilEffect;
import net.wombatrpgs.mrogue.rpg.act.ActAbilByEffect;
import net.wombatrpgs.mrogue.rpg.ai.BTAction;
import net.wombatrpgs.mrogue.rpg.ai.BTCondition;
import net.wombatrpgs.mrogue.rpg.ai.BTSequence;

/**
 * Takes an action of a certain type if the actor can use any ability of that
 * type.
 */
public class RoutineAbilByEffect extends BTSequence {
	
	protected Class<? extends AbilEffect> type;
	protected ActAbilByEffect act;

	/**
	 * Creates a teleportation routine for the given actor.
	 * @param	actor			The actor that will be teleporting
	 */
	public RoutineAbilByEffect(CharacterEvent actor, Class<? extends AbilEffect> type) {
		super(actor);
		this.type = type;
		act = new ActAbilByEffect(actor, type);
		
		addChild(new BTCondition(actor) {
			@Override protected boolean isMet() {
				return act.canUse();
			}
		});
		addChild(new BTAction(actor, act, true));
	}

}
