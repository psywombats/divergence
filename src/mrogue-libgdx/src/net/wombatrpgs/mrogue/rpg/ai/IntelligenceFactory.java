/**
 *  IntelligenceFactory.java
 *  Created on Oct 10, 2013 3:44:20 PM for project mrogue-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mrogue.rpg.ai;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import net.wombatrpgs.mrogue.core.MGlobal;
import net.wombatrpgs.mrogue.rpg.CharacterEvent;
import net.wombatrpgs.mrogue.rpg.act.ActStepHero;
import net.wombatrpgs.mrogue.rpg.act.ActWait;
import net.wombatrpgs.mrogue.rpg.act.ActWander;
import net.wombatrpgs.mrogue.rpg.ai.seq.RoutineMeleeEnemies;
import net.wombatrpgs.mrogue.rpg.ai.seq.RoutineOffenseAbility;
import net.wombatrpgs.mrogueschema.characters.ai.BehaviorListMDO;
import net.wombatrpgs.mrogueschema.characters.ai.BossIntelligenceMDO;
import net.wombatrpgs.mrogueschema.characters.ai.data.ActionMDO;
import net.wombatrpgs.mrogueschema.characters.ai.data.IntelligenceMDO;
import net.wombatrpgs.mrogueschema.characters.ai.intent.ActionAttackEnemiesMDO;
import net.wombatrpgs.mrogueschema.characters.ai.intent.ActionHeroStepMDO;
import net.wombatrpgs.mrogueschema.characters.ai.intent.ActionWanderMDO;
import net.wombatrpgs.mrogueschema.characters.ai.intent.IntentMDO;

/**
 * Constructs behavior trees from a variety of MDOs. This is different from
 * regular factories in that it isn't just for instanceof. MDOs don't have a 
 * 1-to-1 correspondance to in-game objects because their structure is a little
 * too cumbersome to input directly. Instead, it should infer the real structure
 * from the MDO and set up from there.
 */
public class IntelligenceFactory {
	
	/**
	 * Creates an intelligence given the key to some data.
	 * @param	mdoKey			The key of the data to build from
	 * @param	actor			The actor to create for
	 * @return					A BTNode based on that data
	 */
	public static BTNode createIntelligence(String mdoKey, CharacterEvent actor) {
		return createIntelligence(MGlobal.data.getEntryFor(mdoKey, IntelligenceMDO.class), actor);
	}
	
	/**
	 * Creates an intelligence given some intelligence data.
	 * @param	mdo				The data to create an intelligence from
	 * @param	actor			The actor to create intelligence for
	 * @return					A BTNode based on that data
	 */
	public static BTNode createIntelligence(IntelligenceMDO mdo, CharacterEvent actor) {
		if (BossIntelligenceMDO.class.isAssignableFrom(mdo.getClass())) {
			return new BossAI(actor, (BossIntelligenceMDO) mdo);
		} else if (BehaviorListMDO.class.isAssignableFrom(mdo.getClass())) {
			return createBehaviorList((BehaviorListMDO) mdo, actor);
		} else {
			MGlobal.reporter.warn("Invalid intelligence mdo: " + mdo);
			return null;
		}
	}

	/**
	 * Creates a behavior list from data. A behavior list is just a selector
	 * that iterates over very simple sequences, usually in the form condition->
	 * action.
	 * @param	mdo				The data to create the behavior from
	 * @param	actor			The actor to create intelligence for
	 * @return					A BTNode representing a behavior list
	 */
	protected static BTNode createBehaviorList(BehaviorListMDO mdo, CharacterEvent actor) {
		List<IntentMDO> intents = new ArrayList<IntentMDO>(Arrays.asList(mdo.intents));
		Collections.sort(intents, new Comparator<IntentMDO>() {
			@Override public int compare(IntentMDO mdo1, IntentMDO mdo2) {
				return mdo2.priority - mdo1.priority;
			}
		});
		BTSelector selector = new BTSelector(actor);
		selector.addChild(new RoutineOffenseAbility(actor, .3f));
		for (IntentMDO intent : intents) {
			BTSequence seq = new BTSequence(actor);
			ActionMDO actionMDO = MGlobal.data.getEntryFor(intent.action, ActionMDO.class);
			seq.addChild(createNode(actionMDO, actor));
			selector.addChild(seq);
		}
		selector.addChild(new BTAction(actor, new ActWait(), true));
		return selector;
	}
	
	/**
	 * Creates a behavior node for an indicated intent. Intents are usually
	 * things enemies would like to do if they could, so they usually have
	 * a condition->action sequence structure, but sometimes non-sequenced
	 * ones will come up. This is just going to be a massive instanceof
	 * shitstorm, so, hold on tight folks!
	 * @param	mdo				The data to create a node from
	 * @param	actor			The actor who will perform this action
	 * @return					A node representing that data
	 */
	protected static BTNode createNode(ActionMDO mdo, CharacterEvent actor) {
		if (ActionHeroStepMDO.class.isAssignableFrom(mdo.getClass())) {
			BTSequence seq = new BTSequence(actor);
			seq.addChild(new BTCondition(actor) {
				@Override protected boolean isMet() {
					return actor.inLoS(MGlobal.hero);
				}
			});
			seq.addChild(new BTAction(actor, new ActStepHero(actor), true));
			return seq;
		} else if (ActionWanderMDO.class.isAssignableFrom(mdo.getClass())) {
			return new BTAction(actor, new ActWander(actor), true);
		} else if (ActionAttackEnemiesMDO.class.isAssignableFrom(mdo.getClass())) {
			return new RoutineMeleeEnemies(actor);
		} else {
			MGlobal.reporter.warn("Unknown intent mdo: " + mdo);
			return null;
		}
	}

}
