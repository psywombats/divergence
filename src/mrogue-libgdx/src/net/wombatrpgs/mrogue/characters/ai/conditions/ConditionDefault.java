/**
 *  ConditionDefault.java
 *  Created on Jan 29, 2013 11:34:09 PM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mrogue.characters.ai.conditions;

import net.wombatrpgs.mrogue.characters.CharacterEvent;
import net.wombatrpgs.mrogue.characters.ai.Condition;

/**
 * Always true.
 */
public class ConditionDefault extends Condition {
	
	public ConditionDefault(CharacterEvent actor) {
		super(actor);
	}

	@Override
	public boolean isMet() {
		return true;
	}

}
