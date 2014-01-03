/**
 *  ActStepHero.java
 *  Created on Oct 10, 2013 6:06:12 PM for project mrogue-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mrogue.rpg.act;

import net.wombatrpgs.mgns.core.Annotations.Path;
import net.wombatrpgs.mrogue.core.MGlobal;
import net.wombatrpgs.mrogue.rpg.CharacterEvent;
import net.wombatrpgs.mrogueschema.maps.data.EightDir;

/**
 * Wanders aimlessly.
 */
@Path("characters/ai/")
public class ActWander extends Action {
	
	protected ActStep step;
	
	/**
	 * Constructs an act for a specified actor
	 * @param	chara			The actor that performs the action
	 */
	public ActWander(CharacterEvent chara) {
		super(chara);
		step = new ActStep(chara);
	}

	@Override
	public void act() {
		step.setDirection(EightDir.values()[MGlobal.rand.nextInt(EightDir.values().length)]);
		step.act();
	}

}
