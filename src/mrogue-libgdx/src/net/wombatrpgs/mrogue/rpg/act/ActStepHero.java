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

/**
 * Steps towards the hero and attacks them if necessary! :black101:
 */
@Path("characters/ai/")
public class ActStepHero extends ActStepChara {

	/**
	 * Constructs a step-to-hero for a character.
	 * @param	actor			The character that will be performing
	 */
	public ActStepHero(CharacterEvent actor) {
		super(actor, MGlobal.hero);
	}

	/**
	 * @see net.wombatrpgs.mrogue.rpg.act.ActStepChara#act()
	 */
	@Override
	public void act() {
		this.other = MGlobal.hero;
		super.act();
	}

}
