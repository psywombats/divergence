/**
 *  ConditionFactory.java
 *  Created on Jan 29, 2013 11:12:53 PM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mrogue.characters.ai;

import net.wombatrpgs.mrogue.characters.CharacterEvent;
import net.wombatrpgs.mrogue.characters.ai.actions.IntentChargeForward;
import net.wombatrpgs.mrogue.characters.ai.actions.IntentChase;
import net.wombatrpgs.mrogue.characters.ai.actions.IntentFaceHero;
import net.wombatrpgs.mrogue.characters.ai.actions.IntentHalt;
import net.wombatrpgs.mrogue.characters.ai.actions.IntentNothing;
import net.wombatrpgs.mrogue.characters.ai.actions.IntentPace;
import net.wombatrpgs.mrogue.characters.ai.actions.IntentPathfindToHero;
import net.wombatrpgs.mrogue.characters.ai.actions.IntentWander;
import net.wombatrpgs.mrogue.characters.ai.actions.IntentWanderOrganic;
import net.wombatrpgs.mrogue.characters.ai.conditions.ConditionDefault;
import net.wombatrpgs.mrogue.characters.ai.conditions.ConditionHeroSpotted;
import net.wombatrpgs.mrogue.characters.ai.conditions.ConditionSightCone;
import net.wombatrpgs.mrogue.characters.ai.conditions.ConditionSightLine;
import net.wombatrpgs.mrogue.core.MGlobal;
import net.wombatrpgs.mrogueschema.characters.ai.intent.IntentMDO;

/**
 * A thing that makes intent things. You give it an intent MDO, it generates the
 * appropriate conditions and actions.
 */
public class IntentFactory {
	
	/**
	 * Generates a condition from an intent data. This can be as simple as
	 * generating a preset or could involve subclasses.
	 * @param	actor			The character that's going to be doing the act
	 * @param 	mdo				The data to generate the condition from
	 * @return					The condition specified in the MDO
	 */
	public static Condition makeCondition(CharacterEvent actor, IntentMDO mdo) {
		// do some if-else assignable instanceofs here
		switch (mdo.condition) {
		case HERO_IN_VISIONLINE:
			return new ConditionSightLine(actor);
		case HERO_IN_VISIONCONE:
			return new ConditionSightCone(actor);
		case HERO_IN_RANGE:
			return new ConditionHeroSpotted(actor);
		case DEFAULT:
			return new ConditionDefault(actor);
		default:
			MGlobal.reporter.warn("Unsupported condition: " + mdo.condition);
			return new ConditionDefault(actor);
		}
	}
	
	/**
	 * Generates a condition from an intent data. Same deal as the above.
	 * @param 	actor			The character performing the action
	 * @param 	mdo				The data to make the action from
	 * @return					An action made from that data
	 */
	public static IntentAct makeAction(BehaviorList intel, CharacterEvent actor, IntentMDO mdo) {
		// do the if-else
		switch (mdo.action) {
		case PATHFIND_TO_HERO:
			return new IntentPathfindToHero(intel, actor);
		case CHARGE_FORWARD:
			return new IntentChargeForward(intel, actor);
		case FACE_HERO:
			return new IntentFaceHero(intel, actor);
		case WANDER_ORGANICALLY:
			return new IntentWanderOrganic(intel, actor);
		case WANDER_RANDOMLY:
			return new IntentWander(intel, actor);
		case PACE_MENACINGLY:
			return new IntentPace(intel, actor);
		case CHARGE_HERO:
			return new IntentChase(intel, actor);
		case SIT_STILL:
			return new IntentHalt(intel, actor);
		case DO_NOTHING:
			return new IntentNothing(intel, actor);
		default:
			MGlobal.reporter.warn("Unsupported action: " + mdo.action);
			return new IntentNothing(intel, actor);
		}
	}

}
