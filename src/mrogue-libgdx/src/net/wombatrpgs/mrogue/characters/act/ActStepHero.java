/**
 *  ActStepHero.java
 *  Created on Oct 10, 2013 6:06:12 PM for project mrogue-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mrogue.characters.act;

import net.wombatrpgs.mgns.core.Annotations.Path;
import net.wombatrpgs.mrogue.characters.CharacterEvent;
import net.wombatrpgs.mrogue.core.MGlobal;

/**
 * Steps towards the hero and attacks them if necessary! :black101:
 */
@Path("characters/ai/")
public class ActStepHero extends Action {
	
	protected ActStep step;
	
	/**
	 * Constructs an act for a specified actor
	 * @param	chara			The actor that performs the action
	 */
	public ActStepHero(CharacterEvent chara) {
		super(chara);
		step = new ActStep(chara);
	}

	@Override
	public void act() {
		step.setDirection(actor.directionTo(MGlobal.hero));
		step.act();
	}

}
