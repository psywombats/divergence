/**
 *  ConditionSightLine.java
 *  Created on Feb 28, 2013 5:41:46 PM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.rainfall.characters.ai.conditions;

import net.wombatrpgs.rainfall.characters.CharacterEvent;
import net.wombatrpgs.rainfall.characters.ai.Condition;
import net.wombatrpgs.rainfall.core.RGlobal;

public class ConditionSightLine extends Condition {

	public ConditionSightLine(CharacterEvent actor) {
		super(actor);
	}

	@Override
	public boolean isMet() {
		float deltaX = actor.getX() - RGlobal.hero.getX();
		float deltaY = actor.getY() - RGlobal.hero.getY();
		return	Math.abs(deltaX) < actor.getLevel().getTileWidth()/4 ||
				Math.abs(deltaY) < actor.getLevel().getTileHeight()/4;
	}

}
