/**
 *  Intent.java
 *  Created on Jan 29, 2013 10:55:34 PM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.rainfall.characters.ai;

import net.wombatrpgs.rainfall.characters.CharacterEvent;
import net.wombatrpgs.rainfallschema.characters.enemies.ai.intent.IntentMDO;

/**
 * A subcomponent of an intelligence that dictates an action and a condition.
 */
public class Intent implements Comparable<Intent> {
	
	protected IntentMDO mdo;
	protected Condition condition;
	protected IntentAct action;
	
	/**
	 * Creates an intent from data.
	 * @param 	mdo				The data for the new intent
	 * @param	actor			The actor that will wind up performing the act
	 */
	public Intent(IntentMDO mdo, CharacterEvent actor) {
		this.mdo = mdo;
		this.condition = IntentFactory.makeCondition(actor, mdo);
		this.action = IntentFactory.makeAction(actor, mdo);
	}
	
	/**
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(Intent other) {
		return other.mdo.priority - mdo.priority;
	}
	
	/**
	 * If condition evaluates to true, execute the acton.
	 * @return					True if action performed, false if condition
	 * 							was false and no action performed
	 */
	public boolean checkAndAct() {
		if (!condition.isMet()) return false;
		action.act();
		return true;
	}
}
